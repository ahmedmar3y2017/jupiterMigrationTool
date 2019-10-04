package com.asset.jupiter.Web.Controllers;


import org.hibernate.validator.constraints.NotBlank;

public class SearchCriteria {

    @NotBlank(message = "fileName can't empty!")
    String fileName;

    public SearchCriteria() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}