package com.asset.jupiter.Util.Enums;

public enum JupiterMediaExtension {
    JUP(".jup"),
    THUMB(".thumb");

    private final String extension;

    private JupiterMediaExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}
