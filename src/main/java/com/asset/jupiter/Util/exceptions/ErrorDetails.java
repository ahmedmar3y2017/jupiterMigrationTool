package com.asset.jupiter.Util.exceptions;

import java.util.Date;

public class ErrorDetails {

    private Date timestamp;
    private String message;
    private int code;

    public ErrorDetails(Date timestamp, String message, int code) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.code = code;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
