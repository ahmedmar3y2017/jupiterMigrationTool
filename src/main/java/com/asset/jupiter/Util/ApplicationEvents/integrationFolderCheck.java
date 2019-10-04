package com.asset.jupiter.Util.ApplicationEvents;

import com.asset.jupiter.Util.exceptions.JupiterException;

import java.io.File;

public class integrationFolderCheck {


    // test Shared Folder Exists And Access
    public static void testSharedFolder(String sharedFolderPath) throws JupiterException {

        File f = new File(sharedFolderPath);

        if (!f.exists() || !f.isDirectory()) {
            // throw New Exception
            throw new JupiterException("Error Shared Folder Not Exists Or Not A folder ", JupiterException.SharedFolderNotExists_Exception);

        } else {
            if (!f.canWrite()) {
                // throw New Exception
                throw new JupiterException("Error Shared Folder Not Access ", JupiterException.SharedFolderNotAccess_Exception);

            }
        }

    }
}
