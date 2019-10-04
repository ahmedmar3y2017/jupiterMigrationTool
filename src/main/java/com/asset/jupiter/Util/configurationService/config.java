package com.asset.jupiter.Util.configurationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// all configuration Properties
@Service
public class config {

    @Value("${use_Digital_signature}")
    private String useDigitalSignature;

    @Value("${numOfException}")
    private String numOfException;
    @Value("${automatic_Mode}")
    private String automaticMode;

    @Value("${integrationFolderPath}")
    private String integrationFolderPath;

    @Value("${JUPITER_SCHEMA}")
    private String JUPITER_SCHEMA;
    @Value("${JUPITER_MED_SCHEMA}")
    private String JUPITER_MED_SCHEMA;

    @Value("${tool_resources}")
    private String tool_resources;
    @Value("${spring.mail.clientName}")
    private String client_name;

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getTool_resources() {
        return tool_resources;
    }

    public void setTool_resources(String tool_resources) {
        this.tool_resources = tool_resources;
    }

    public String getUseDigitalSignature() {
        return useDigitalSignature;
    }

    public void setUseDigitalSignature(String useDigitalSignature) {
        this.useDigitalSignature = useDigitalSignature;
    }

    public String getNumOfException() {
        return numOfException;
    }

    public void setNumOfException(String numOfException) {
        this.numOfException = numOfException;
    }

    public String getAutomaticMode() {
        return automaticMode;
    }

    public void setAutomaticMode(String automaticMode) {
        this.automaticMode = automaticMode;
    }

    public String getIntegrationFolderPath() {
        return integrationFolderPath;
    }

    public void setIntegrationFolderPath(String integrationFolderPath) {
        this.integrationFolderPath = integrationFolderPath;
    }

    public String getJUPITER_SCHEMA() {
        return JUPITER_SCHEMA;
    }

    public void setJUPITER_SCHEMA(String JUPITER_SCHEMA) {
        this.JUPITER_SCHEMA = JUPITER_SCHEMA;
    }

    public String getJUPITER_MED_SCHEMA() {
        return JUPITER_MED_SCHEMA;
    }

    public void setJUPITER_MED_SCHEMA(String JUPITER_MED_SCHEMA) {
        this.JUPITER_MED_SCHEMA = JUPITER_MED_SCHEMA;
    }


}
