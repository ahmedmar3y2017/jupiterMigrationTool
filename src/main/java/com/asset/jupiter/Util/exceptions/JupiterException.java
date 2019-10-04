/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.jupiter.Util.exceptions;

/**
 * @author Ahmed Gamal <ahmed.gamal@asset.com.eg>
 */
public class JupiterException extends Exception {
    private int errcode;

    public static final int UNSUPPORTED_JSONFILE_MODE = 1;
    public static final int MISSING_JSON_PARAMETERS = 2;
    public static final int NULL_OR_EMPTY_VLAUE = 3;
    public static final int Async_Exception_Handler = 4;
    public static final int IO_EXCEPTION = 5;
    public static final int ERROR_INSERT_DATABASE = 6;
    public static final int NOT_FOUND_IN_DATABASE = 7;
    public static final int Execution_Exception = 8;
    public static final int Interrupted_Exception = 9;
    public static final int SQL_Exception = 10;
    public static final int SearchMethod_Exception = 11;
    public static final int SearchIndexClass_Exception = 12;
    public static final int CreateMedia_Exception = 13;
    public static final int CreateItem_Exception = 14;
    public static final int CreateMediaFolder_Exception = 15;
    public static final int CreateItemFolder_Exception = 16;
    public static final int TableId_Exception = 17;
    public static final int IndexClassName_Exception = 18;
    public static final int FILE_NOT_EXIST_EXCEPTION = 19;
    public static final int Access_Rule_Create_EXCEPTION = 20;
    public static final int Items_Count_Update_EXCEPTION = 21;
    public static final int ItemIndexClass_Create_EXCEPTION = 22;
    public static final int Links_Create_EXCEPTION = 23;
    public static final int Items_Create_EXCEPTION = 24;
    public static final int FieldLabel_Exception = 25;
    public static final int Multi_Parent_EXCEPTION = 26;
    public static final int Search_Model_EXCEPTION = 27;
    public static final int KnowledgePool_Exception = 28;
    public static final int NO_FOLDERS_Exception = 29;
    public static final int MediaManager_Create_Exception = 30;
    public static final int Ftrscheduler_Create_Exception = 31;
    public static final int MediaServer_NotFound_Exception = 32;
    public static final int Items_Count_Select_EXCEPTION = 33;
    public static final int Items_Count_Insert_EXCEPTION = 34;
    public static final int CreateJsonFile_Exception = 35;
    public static final int CreateActionListener_Exception = 36;
    public static final int CreateException_Exception = 37;
    public static final int SharedFolderNotExists_Exception = 40;
    public static final int SharedFolderNotAccess_Exception = 41;
    public static final int Startup_AuthenticationFailedException_Exception = 42;
    public static final int Startup_MessagingException_Exception = 43;
    public static final int Startup_SQLException_Exception = 44;
    public static final int Startup_ClassNotFoundException_Exception = 45;
    public static final int load_mail_source_files_Exception = 46;
    public static final int ITEM_NOT_FOUND_INPARENT_EXCEPTION = 47;
    public static final int Multi_ITEM_INParent_EXCEPTION = 48;
    public static final int ITEM_NOT_FOUND_INJUPITER_EXCEPTION = 49;
    public static final int ToolResourcesNotExists_Exception = 50;
    public static final int ToolResourcesNotAccess_Exception = 51;


    /**
     * JupiterException constructor comment.
     */
    public JupiterException() {
        super();
    }

    /**
     * JupiterException constructor comment.
     *
     * @param s java.lang.String
     */
    public JupiterException(String s) {
        super(s);

    }

    /**
     * Insert the method's description here.
     * Creation date: (4/19/2001 2:45:14 PM)
     *
     * @param ex      java.lang.String
     * @param errcode int
     */
    public JupiterException(String ex, int errcode) {
        super(ex + " -> StatusCode : " + errcode);
        this.errcode = errcode;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/19/2001 2:49:17 PM)
     *
     * @return int
     */
    public int getErrCode() {
        return errcode;
    }

    @Override
    public String toString() {
        return "JUP:" + getErrCode() + " " + super.toString();
    }
}
