package com.asset.jupiter.Util.exceptions;

public class PropertiesNotFoundException extends Exception {
    private int errcode;
    public static final int MISSING_PROPERTY = 1;
    /**
     * PropertiesNotFoundException constructor comment.
     */
    public PropertiesNotFoundException() {
        super();
    }

    /**
     * PropertiesNotFoundException constructor comment.
     * @param s java.lang.String
     */
    public PropertiesNotFoundException(String s) {
        super(s);

    }
    /**
     * Insert the method's description here.
     * Creation date: (4/19/2001 2:45:14 PM)
     * @param ex java.lang.String
     * @param errcode int
     */
    public PropertiesNotFoundException(String ex, int errcode) {
        super(ex);
        this.errcode = errcode;
    }

    /**
     * Insert the method's description here.
     * Creation date: (4/19/2001 2:49:17 PM)
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
