package com.asset.jupiter.Util.ApplicationEvents;

import com.asset.jupiter.Util.exceptions.JupiterException;

import java.io.File;

public class toolResourcesCheck {

    // test Tool Resources Folder Exists And Access
    public static void testToolResourcesFolder(String toolResourcesFolder) throws JupiterException {

        File toolResources = new File(toolResourcesFolder);

        if (!toolResources.exists() || !toolResources.isDirectory()) {
            // throw New Exception
            throw new JupiterException("Error Tool Resources Not Exists Or Not A folder ", JupiterException.ToolResourcesNotExists_Exception);

        } else {
            // cant access
            if (!toolResources.canWrite()) {
                // throw New Exception
                throw new JupiterException("Error Tool Resources Not Access ", JupiterException.ToolResourcesNotAccess_Exception);

            }
            // can access
            else {
                // check Log folder if not exists create It
                File fileLog = new File(toolResourcesFolder + "/log");
                if (!fileLog.exists())
                    fileLog.mkdir();
                // check Reports folder if not exists create It
                File fileReports = new File(toolResourcesFolder + "/reports");

                if (!fileReports.exists())
                    fileReports.mkdir();


            }
        }

    }
}
