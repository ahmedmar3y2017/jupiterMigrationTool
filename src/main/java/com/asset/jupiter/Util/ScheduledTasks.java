package com.asset.jupiter.Util;


import com.asset.jupiter.JUPITER.Model.Entities.ActionListener;
import com.asset.jupiter.JUPITER.Model.Entities.ActionListenerPK;
import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.jupiterService.actionListenerService;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.Util.MailConfig.SendMail;
import com.asset.jupiter.Util.MailConfig.mail;
import com.asset.jupiter.Util.configurationService.config;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Util.excelModel.ExcelModel;
import com.asset.jupiter.Util.handlers.ExcelHandler;
import com.asset.jupiter.Web.JasperReports.SimpleReportExporter;
import com.asset.jupiter.Web.JasperReports.SimpleReportFiller;
import com.asset.jupiter.Web.JasperReports.reportMailModel;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Component
public class ScheduledTasks {
    @Autowired
    infoFileService infoFileService;

    @Autowired
    actionListenerService actionListenerService;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SimpleReportFiller simpleReportFiller;
    @Autowired
    SimpleReportExporter simpleExporter;
    @Autowired
    SendMail sendMail;
    @Autowired
    config config;


    // generate report And Send mail every 6 h
    //second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 0 0/6 * * *")
    // fixed 10 sec
   // @Scheduled(fixedRate = 2000l)
    public void getDbPathsReport() throws IOException {

        // get all infofiles With status Finished Or Inprocessing and TimeStampDate  = todayDate

        List<reportMailModel> reportMailModels = jdbcTemplate.query(
                "select u.PATH , u.STATUS , u.ADDING_DATE ,j.THREAD_NAME, j.TYPE ,j.COUNT_FOLDERS ,j.COUNT_DOCUMENTS" +
                        " from MG_INFO_FILES u , MG_JSON_FILES j " +
                        "where u.INFO_ID=j.INFO_FILE_ID AND " +
                        "TO_DATE(CAST(u.ADDING_DATE AS DATE), 'YYYY-MM-DD') = TO_DATE(sysdate, 'YYYY-MM-DD')" +
                        " AND " +
                        "u.STATUS " +
                        "in ('" + Defines.finished + "', '" + Defines.inProgress + "')",
                new ReportRowMapper());


        // if null -- > Dont do anything
        if (!reportMailModels.isEmpty()) {

            //  generate Report
            // report message
            String reportName = generateReport(reportMailModels,
                    "report2.jrxml",
                    "Dear " + config.getClient_name() +
                            " , Kindly check the below Report for JSON files status for today : " + new Date());

            sendReportWithMail(reportName);
        }

    }


    //    @Scheduled(fixedRate = 10000l)
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkJsonDBStatus() throws IOException {

        Set<String> threadNames = Thread.getAllStackTraces().keySet().stream().filter(thread -> {
            // return is Alive
            if (thread.isAlive())
                return true;

            return false;

        }).map(thread -> thread.getName()).collect(Collectors.toSet());

        infoFileService.findAllByStatus(Defines.inProgress).forEach(infoFile -> {

                    String path = infoFile.getPath();
                    long infoId = infoFile.getInfoId();
                    ActionListenerPK actionListenerPK = new ActionListenerPK(path, infoId);
                    ActionListener byId = actionListenerService.findById(actionListenerPK);

                    if (byId != null) {
                        if (!threadNames.contains(byId.getThreadName())) {
                            // update In Database To Finished And Contains Error
                            infoFile.setFinishDate(new Timestamp(new Date().getTime()));
                            infoFile.setFinishStatus(Defines.containErrors);
                            infoFile.setStatus(Defines.finished);
                            // change Db
                            infoFileService.updateInfoFile(infoFile);

                            // add to JsonFailure
                            // copy to contains error folder
                            // send mail with that happens
                        }

                    }

                }

        );


    }

    // send Mail With Jasper Report
    private void sendReportWithMail(String reportName) {


        // send Mail Report
//            sendMail.prepareAndSendTemplateReport();
        //  Send Mail
        try {
            sendMail.prepareAndSendTemplateReport(new mail("", "","Jupiter Migration Tool Service" ,
                            "Kindly check the below JSON status for today : " + new Date())
                    , reportName);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // generate Jasper report From info File And Json File
    private String generateReport(List<reportMailModel> reportMailModels, String reportName, String reportMessage) {

        // report parameters
        Map<String, Object> param = new HashMap<>();
        // path
        param.put("imagesDir", "image/");
        // message
        param.put("message", reportMessage);


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportDate(reportMailModels));

        simpleReportFiller.setReportFileName(reportName);
        simpleReportFiller.compileReport();
        simpleReportFiller.fillReport(dataSource, param);
        simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());
        Date date = new Date();
        long time = date.getTime();

        simpleExporter.exportToPdf(time + ".pdf", "Jupiter Migration Tool");
        simpleExporter.exportToXlsx(time + ".xlsx", "Jupiter Migration Tool");
        simpleExporter.exportToCsv(time + ".csv");
        simpleExporter.exportToHtml(time + ".html");

        return String.valueOf(time);
    }

    // report Date
    public List<Map<String, Object>> reportDate(List<reportMailModel> reportMailModels) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (reportMailModel reportMailModels1 : reportMailModels) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("path", reportMailModels1.getPath().substring(reportMailModels1.getPath().lastIndexOf("\\") + 1));
            item.put("threadName", reportMailModels1.getThreadName());
            item.put("status", reportMailModels1.getStatus());
            item.put("type", reportMailModels1.getType());
            item.put("addingDate", reportMailModels1.getAddingDate());
            item.put("countFolders", reportMailModels1.getCountFolders());
            item.put("countDocuments", reportMailModels1.getCountDocuments());
            result.add(item);
        }
        return result;
    }


    // return using jdbc template
    class ReportRowMapper implements RowMapper<reportMailModel> {


        @Override
        public reportMailModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            reportMailModel reportMailModel = new reportMailModel();

            reportMailModel.setAddingDate(rs.getTimestamp("ADDING_DATE"));
            reportMailModel.setCountDocuments(rs.getBigDecimal("COUNT_DOCUMENTS"));
            reportMailModel.setCountFolders(rs.getBigDecimal("COUNT_FOLDERS"));
            reportMailModel.setPath(rs.getString("PATH"));
            reportMailModel.setStatus(rs.getString("STATUS"));
            reportMailModel.setType(rs.getString("TYPE"));
            reportMailModel.setThreadName(rs.getString("THREAD_NAME"));
            return reportMailModel;
        }
    }
}