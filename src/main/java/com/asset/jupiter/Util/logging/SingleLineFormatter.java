/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.logging;

/**
 *
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */

import java.util.Calendar;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class SingleLineFormatter extends SimpleFormatter
{
    public SingleLineFormatter()
    {
        super();
    }

    public String format(LogRecord logRecord)
    {
        String logMessage="";
        logMessage+="****"+Calendar.getInstance().getTime().toString()+", "+logRecord.getMessage()+"\r\n";
        return logMessage;
    }

    public String formatMessage(LogRecord logRecord)
    {
        return format(logRecord);
    }
}