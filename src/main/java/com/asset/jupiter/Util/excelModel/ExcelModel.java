package com.asset.jupiter.Util.excelModel;

import java.util.ArrayList;
import java.util.List;

public class ExcelModel {

    List<RowModel> rowlModels = new ArrayList<>();

    public List<RowModel> getrowlModels() {
        return rowlModels;
    }

    public void setrowlModels(List<RowModel> rowlModels) {
        this.rowlModels = rowlModels;
    }

    @Override
    public String toString() {
        return "ExcelModel{" +
                "rowlModels=" + rowlModels +
                '}';
    }
}
