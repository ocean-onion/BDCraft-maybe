package com.bdcraft.modules.logging;

/**
 * Enumeration for log levels in the BDCraft logging system.
 * The log levels are ordered by severity, with DEBUG being the least severe
 * and CRITICAL being the most severe.
 */
public enum LogLevel {
    /**
     * Debug information, useful for development.
     */
    DEBUG,
    
    /**
     * General information, standard logging level.
     */
    INFO,
    
    /**
     * Warning information, potential issues.
     */
    WARNING,
    
    /**
     * Error information, actual issues.
     */
    ERROR,
    
    /**
     * Critical information, severe issues.
     */
    CRITICAL
}