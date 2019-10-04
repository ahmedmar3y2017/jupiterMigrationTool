package com.asset.jupiter.Util.excelModel;

public class RowModel {
    private int rowNum;
    private String path;
    private String status;
    private String processingDate;
    private String finishStatus;
    private String desc;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String toString() {
        return "RowModel{" +
                "rowNum=" + rowNum +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                ", processingDate='" + processingDate + '\'' +
                ", finishStatus='" + finishStatus + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
