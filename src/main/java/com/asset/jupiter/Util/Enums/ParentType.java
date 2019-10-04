package com.asset.jupiter.Util.Enums;

public enum ParentType {

    KNOWLEDGEPOOL("1"),
    FOLDER("2");

    private final String parentType;

    ParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentType() {
        return this.parentType;
    }
}
