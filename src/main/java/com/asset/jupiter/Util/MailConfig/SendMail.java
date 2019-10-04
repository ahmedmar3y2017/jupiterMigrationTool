package com.asset.jupiter.Util.MailConfig;


import com.asset.jupiter.Util.ThreadLogs;
import com.asset.jupiter.Util.configurationService.config;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class SendMail {

    private EmailCfg emailCfg;

    @Autowired
    private TemplateEngine templateEngine;


    public SendMail(EmailCfg emailCfg) {
        this.emailCfg = emailCfg;
    }

    @Autowired
    config config;

//    @Async("mailAsyncExecutor")
//    public void sendMail(mail mail) {
//        // Create a mail sender
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(this.emailCfg.getHost());
//        mailSender.setPort(this.emailCfg.getPort());
//        mailSender.setUsername(this.emailCfg.getUsername());
//        mailSender.setPassword(this.emailCfg.getPassword());
//
//        Properties properties = new Properties();
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.smtp.auth", "true");
//
//        mailSender.setJavaMailProperties(properties);
//
//        // Create an email instance
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(mail.getToMail());
//        mailMessage.setTo("ahmedmar3y108@gmail.com");
//        mailMessage.setSubject("New mail from " + mail.getName());
//        mailMessage.setText(mail.getMessage());
//
//        // Send mail
//        mailSender.send(mailMessage);
//    }

    @Async("mailAsyncExecutor")
    public void prepareAndSendTemplate(mail mail, String imageName, boolean isError, String logPath) throws MessagingException {

        // if null
        if (mail.getName().isEmpty()) {
            mail.setName(emailCfg.getName());

        }
        if (mail.getToMail().isEmpty()) {
            mail.setToMail(emailCfg.getTo());

        }

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.emailCfg.getHost());
        properties.put("mail.smtp.port", this.emailCfg.getPort());
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailCfg.getUsername(), emailCfg.getPassword());
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(this.emailCfg.getUsername()));
        InternetAddress[] toAddresses = {new InternetAddress(mail.getToMail())};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(mail.getSubject());


        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");
        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        // html content
        messageBodyPart.setContent(build(mail.getName(), mail.getMessage()), "text/html");
        multipart.addBodyPart(messageBodyPart);

        //--------------------------- send log if error as an attachment --------------------------

        // if error mail send log as an attachment
        if (isError == true) {

            // get json file path
            String infoFileName= logPath.substring(logPath.lastIndexOf("\\") + 1, logPath.lastIndexOf("."));
            // get all log pathes
            List<String> allThreadLogs = ThreadLogs.getAllThreadLogs(infoFileName, config.getTool_resources() + "/log/ThreadsLogs");

            // send each log file as an attachment
            for (String logThread : allThreadLogs) {
                MimeBodyPart logPart = new MimeBodyPart();
                File file = new File(logThread);
                ByteArrayDataSource bds = null;
                try {
                    bds = new ByteArrayDataSource(Files.readAllBytes(file.toPath()), "text/plain");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logPart.setFileName(file.getName());
                logPart.setDataHandler(new DataHandler(bds));
                multipart.addBodyPart(logPart);

            }

        }


        // -------------------------- image 1 ---------
        BodyPart ndImagePart = new MimeBodyPart();
        DataSource fds1 = null;
        fds1 = new FileDataSource(config.getTool_resources() + "/asset.jpg");
        ndImagePart.setDataHandler(new DataHandler(fds1));
        ndImagePart.addHeader("Content-ID", "<asset>");
        multipart.addBodyPart(ndImagePart);
        // ------------------------ image 2 ---------------------
        MimeBodyPart stImagePart = new MimeBodyPart();
        DataSource fds2 = null;
        fds2 = new FileDataSource(new File(config.getTool_resources() + "/" + imageName));
        stImagePart.setDataHandler(new DataHandler(fds2));
        stImagePart.addHeader("Content-ID", "<rocket>");
        multipart.addBodyPart(stImagePart);


        // set plain text message
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        // sends the e-mail
        synchronized (this) {
            Transport.send(msg);
        }
    }


    // build html by template Engine
    public String build(String name, String message) {
        Context context = new Context();
        context.setVariable("name", "Dear " + name + ",");
        context.setVariable("message", message);
        return templateEngine.process("mail/mailTemplate.html", context);
    }


    @Async("mailAsyncExecutor")
    public void prepareAndSendTemplateWithTable(mail mail,
                                                int fCount,
                                                int dCount,
                                                int sfCount,
                                                int sdCount,
                                                int ffCount,
                                                int fdCount,
                                                String imageName) throws MessagingException {

        // if null
        if (mail.getName().isEmpty()) {
            mail.setName(emailCfg.getName());

        }
        if (mail.getToMail().isEmpty()) {
            mail.setToMail(emailCfg.getTo());

        }

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.emailCfg.getHost());
        properties.put("mail.smtp.port", this.emailCfg.getPort());
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailCfg.getUsername(), emailCfg.getPassword());
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(this.emailCfg.getUsername()));
        InternetAddress[] toAddresses = {new InternetAddress(mail.getToMail())};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(mail.getSubject());

        // subject will bw box Number
        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");
        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(buildWithTable(mail.getName(), mail.getMessage(),
                fCount,
                dCount,
                sfCount,
                sdCount,
                ffCount,
                fdCount), "text/html");
        multipart.addBodyPart(messageBodyPart);
        // second part (the image)
        messageBodyPart = new MimeBodyPart();
//        DataSource fds1 = new FileDataSource(new ClassPathResource("src/main/resources/templates/mail/images/asset.jpg").getPath());
        DataSource fds1 = null;
        fds1 = new FileDataSource(config.getTool_resources() + "/asset.jpg");


        messageBodyPart.setDataHandler(new DataHandler(fds1));
        messageBodyPart.addHeader("Content-ID", "<asset>");

        multipart.addBodyPart(messageBodyPart);

        //Third part (the image)
        messageBodyPart = new MimeBodyPart();

//        DataSource fds2 = new FileDataSource(new ClassPathResource("src/main/resources/templates/mail/images/" + imageName).getPath());
        DataSource fds2 = null;
        fds2 = new FileDataSource(new File(config.getTool_resources() + "/" + imageName));


        messageBodyPart.setDataHandler(new DataHandler(fds2));
        messageBodyPart.addHeader("Content-ID", "<rocket>");
        // add image to the multipart
        multipart.addBodyPart(messageBodyPart);
        // set plain text message
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        // sends the e-mail
        synchronized (this) {
            Transport.send(msg);
        }
    }


    // build html by template Engine
    public String buildWithTable(String name, String message,
                                 int fCount,
                                 int dCount,
                                 int sfCount,
                                 int sdCount,
                                 int ffCount,
                                 int fdCount) {
        Context context = new Context();
        context.setVariable("name", "Dear " + name + ",");
        context.setVariable("message", message);

        context.setVariable("fCount", fCount);
        context.setVariable("dCount", dCount);
        context.setVariable("sfCount", sfCount);
        context.setVariable("sdCount", sdCount);
        context.setVariable("ffCount", ffCount);
        context.setVariable("fdCount", fdCount);

        return templateEngine.process("mail/mailStatusTemplate.html", context);
    }

    // send Mail To report
    @Async("mailAsyncExecutor")
    public void prepareAndSendTemplateReport(mail mail, String reportName) throws MessagingException, IOException {


        // if null
        if (mail.getName().isEmpty()) {
            mail.setName(emailCfg.getName());

        }
        if (mail.getToMail().isEmpty()) {
            mail.setToMail(emailCfg.getTo());

        }

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.emailCfg.getHost());
        properties.put("mail.smtp.port", this.emailCfg.getPort());
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailCfg.getUsername(), emailCfg.getPassword());
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(this.emailCfg.getUsername()));
        InternetAddress[] toAddresses = {new InternetAddress(mail.getToMail())};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(mail.getSubject());
        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");
        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
//reportName + ".html"
        String html_message = new String(Files.readAllBytes(Paths.get(config.getTool_resources() + "/reports/" + reportName + ".html")));

        html_message = html_message.replace("image/asset.jpg", "cid:rocket");

        messageBodyPart.setContent(html_message, "text/html");
        multipart.addBodyPart(messageBodyPart);
        // first part (the html)
        messageBodyPart = new MimeBodyPart();
//        DataSource fds1 = new FileDataSource(new ClassPathResource("src/main/resources/reports/image/asset.jpg").getPath());
        DataSource fds1 = new FileDataSource(new File(config.getTool_resources() + "/asset.jpg"));

        messageBodyPart.setDataHandler(new DataHandler(fds1));
        messageBodyPart.addHeader("Content-ID", "<rocket>");

        multipart.addBodyPart(messageBodyPart);


        // set plain text message
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        // sends the e-mail
        synchronized (this) {
            Transport.send(msg);
        }


    }
}
