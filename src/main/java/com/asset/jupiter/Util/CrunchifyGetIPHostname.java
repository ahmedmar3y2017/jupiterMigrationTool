package com.asset.jupiter.Util;

import com.asset.jupiter.Util.exceptions.JupiterException;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Crunchify.com
 */
@Component
public class CrunchifyGetIPHostname {


    public String getIp() {
        InetAddress ip = null;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            new JupiterException(e.getMessage(), 5);
        }

        return ip.toString();
    }

    public String getUniqueId() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 3).toUpperCase();
    }


}

