package com.asset.jupiter.Web.Controllers;


import com.asset.jupiter.Web.JasperReports.SimpleReportExporter;
import com.asset.jupiter.Web.JasperReports.SimpleReportFiller;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class reportsController {

    @Autowired
    SimpleReportFiller simpleReportFiller;
    @Autowired
    SimpleReportExporter simpleExporter;


    @Autowired
    private ApplicationContext appContext;

    @RequestMapping(value = "report", method = RequestMethod.GET)
    public void report(HttpServletResponse response) throws Exception {
//        response.setContentType("text/html");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report());
//        InputStream inputStream = this.getClass().getResourceAsStream("/reports/report.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
//        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//        exporter.exportReport();


        simpleReportFiller.setReportFileName("report.jrxml");
        simpleReportFiller.compileReport();
        simpleReportFiller.fillReport(dataSource);

        simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());
        Date date = new Date();
        long time = date.getTime();

        simpleExporter.exportToPdf(time + ".pdf", "Jupiter Migration Tool");
        simpleExporter.exportToXlsx(time + ".xlsx", "Jupiter Migration Tool");
        simpleExporter.exportToCsv(time + ".csv");
        simpleExporter.exportToHtml(time + ".html");

    }

    //
//    @RequestMapping(path = "/products/{businessId}", method = RequestMethod.GET)
//    public ModelAndView reportProducts(@PathVariable String businessId) {
//
//        JasperReportsPdfView view = new JasperReportsPdfView();
//        view.setUrl("classpath:reports/products.jrxml");
//        view.setApplicationContext(appContext);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("datasource", poductService.getAll().stream().filter(ee -> {
//            return ee.getBusiness().getId() == Integer.parseInt(businessId);
//        }).collect(Collectors.toSet()));
//
//        return new ModelAndView(view, params);
//    }
//
//
//    @RequestMapping(path = "/productUnits/{businessId}", method = RequestMethod.GET)
//    public ModelAndView reportProductsUnits(@PathVariable String businessId) {
//
//        JasperReportsPdfView view = new JasperReportsPdfView();
//        view.setUrl("classpath:reports/productUnits.jrxml");
//        view.setApplicationContext(appContext);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("datasource", poductService.getAll().stream().filter(ee -> {
//            return ee.getUnitStock() <= 3;
//        }).collect(Collectors.toSet()));
//
//        return new ModelAndView(view, params);
//    }
    public List<Map<String, Object>> report() {
        List<Map<String, Object>> result = new ArrayList<>();
//        for (Product product : productRepository.findAll()) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", 1);
        item.put("name", "NAME");
        item.put("price", new BigDecimal(20));
        item.put("quantity", 30);
        item.put("categoryName", "CATEGORY");
        result.add(item);
//        }
        return result;
    }

}
