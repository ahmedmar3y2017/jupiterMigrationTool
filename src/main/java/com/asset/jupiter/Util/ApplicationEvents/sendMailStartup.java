package com.asset.jupiter.Util.ApplicationEvents;

import com.asset.jupiter.Util.MailConfig.mail;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

public class sendMailStartup {


    public static void prepareAndSendTemplate(mail mail,
                                              String name,
                                              String toMail,
                                              String host,
                                              String port,
                                              String username,
                                              String password,
                                              String templateName,
                                              String imageName

    ) throws MessagingException, IOException {

        // if null
        if (mail.getName().isEmpty()) {
            mail.setName(name);

        }
        if (mail.getToMail().isEmpty()) {
            mail.setToMail(toMail);

        }

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = {new InternetAddress(mail.getToMail())};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject("Jupiter Migration Tool Service");
        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");
        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        //build(mail.getName(), mail.getMessage())

        String html_message = new String(Files.readAllBytes(Paths.get(new ClassPathResource("src/main/resources/templates/mail/" + templateName + ".html").getPath())));

        html_message = html_message.replace("${name}", "Dear " + mail.getName() + ",");
        html_message = html_message.replace("${message}", mail.getMessage());


        messageBodyPart.setContent(html_message, "text/html");
        multipart.addBodyPart(messageBodyPart);
        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds1 = new FileDataSource(new ClassPathResource("src/main/resources/templates/mail/images/asset.jpg").getPath());

        messageBodyPart.setDataHandler(new DataHandler(fds1));
        messageBodyPart.addHeader("Content-ID", "<asset>");

        multipart.addBodyPart(messageBodyPart);

        //Third part (the image)
        messageBodyPart = new MimeBodyPart();

        DataSource fds2 = new FileDataSource(new ClassPathResource("src/main/resources/templates/mail/images/"+imageName).getPath());

        messageBodyPart.setDataHandler(new DataHandler(fds2));
        messageBodyPart.addHeader("Content-ID", "<rocket>");
        // add image to the multipart
        multipart.addBodyPart(messageBodyPart);
        // set plain text message
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        // sends the e-mail
        synchronized (sendMailStartup.class) {
            Transport.send(msg);
        }
    }


}
