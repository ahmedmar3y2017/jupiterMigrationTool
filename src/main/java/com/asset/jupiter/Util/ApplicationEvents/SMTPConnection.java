package com.asset.jupiter.Util.ApplicationEvents;

import javax.mail.*;
import java.util.Properties;

public class SMTPConnection {

    // method test Smtp Connection
    public static void testSmtpConnection(String host, String user, String pass, int port) throws MessagingException {

        Properties props = new Properties();
        // required for gmail
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        // or use getDefaultInstance instance if desired...
        Session session = Session.getInstance(props, null);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, port, user, pass);
        transport.close();

    }
}
