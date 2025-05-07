package com.example.paperplugin.modules.logging;

/**
 * Defines log levels for the BDCraft logging system.
 */
public enum LogLevel {
    /**
     * Detailed information for debugging purposes.
     */
    DEBUG,
    
    /**
     * General information about system operation.
     */
    INFO,
    
    /**
     * Warning events that might cause issues.
     */
    WARNING,
    
    /**
     * Error events that might still allow the system to continue.
     */
    ERROR,
    
    /**
     * Critical events that prevent the system from working properly.
     */
    CRITICAL
}