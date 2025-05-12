package com.bdcraft.plugin.config;

/**
 * Configuration types enum to standardize configuration file naming
 * and prevent typos when accessing configurations.
 */
public enum ConfigType {
    CONFIG("config"),
    ECONOMY("economy"),
    MESSAGES("messages"),
    PERMISSIONS("permissions"),
    REBIRTH("rebirth"),
    VITAL("vital");
    
    private final String fileName;
    
    ConfigType(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
}