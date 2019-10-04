package com.asset.jupiter.Util.MailConfig;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class mail {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String toMail;

    @NotNull
    private String subject;
    @NotNull
//    @Min(10)
    private String message;

    public mail() {
    }

    public mail(@NotNull String name, @NotNull @Email String toMail, String subject, @NotNull String message) {
        this.name = name;
        this.toMail = toMail;
        this.message = message;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "mail{" +
                "name='" + name + '\'' +
                ", toMail='" + toMail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}