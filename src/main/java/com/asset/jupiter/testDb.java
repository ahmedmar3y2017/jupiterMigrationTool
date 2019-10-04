package com.asset.jupiter;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class testDb {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.asset.com.eg");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("Ahmed.Marey@asset.com.eg"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("Ahmed.Marey@asset.com.eg"));
        message.setSubject("Notification");
        message.setText("Successful!", "UTF-8"); // as "text/plain"
        message.setSentDate(new Date());
        Transport.send(message);
    }
}
