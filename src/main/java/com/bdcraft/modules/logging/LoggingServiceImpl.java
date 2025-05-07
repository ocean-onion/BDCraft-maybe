package com.bdcraft.modules.logging;

import java.util.List;

/**
 * Implementation of the LoggingService interface.
 * Provides logging functionality for the BDCraft plugin.
 */
public class LoggingServiceImpl implements LoggingService {
    
    private final LoggingModule loggingModule;
    private LogLevel currentLogLevel;
    
    /**
     * Creates a new LoggingServiceImpl.
     *
     * @param loggingModule The logging module
     */
    public LoggingServiceImpl(LoggingModule loggingModule) {
        this.loggingModule = loggingModule;
        
        // Set default log level, or load from config
        String configLevel = loggingModule.getLoggingConfig().getString("logging.level", "INFO");
        try {
            this.currentLogLevel = LogLevel.valueOf(configLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.currentLogLevel = LogLevel.INFO;
        }
    }
    
    @Override
    public void debug(String category, String message) {
        if (currentLogLevel.ordinal() <= LogLevel.DEBUG.ordinal()) {
            loggingModule.logAction(LogLevel.DEBUG, category, message);
        }
    }
    
    @Override
    public void info(String category, String message) {
        if (currentLogLevel.ordinal() <= LogLevel.INFO.ordinal()) {
            loggingModule.logAction(LogLevel.INFO, category, message);
        }
    }
    
    @Override
    public void warning(String category, String message) {
        if (currentLogLevel.ordinal() <= LogLevel.WARNING.ordinal()) {
            loggingModule.logAction(LogLevel.WARNING, category, message);
        }
    }
    
    @Override
    public void error(String category, String message) {
        if (currentLogLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
            loggingModule.logAction(LogLevel.ERROR, category, message);
        }
    }
    
    @Override
    public void critical(String category, String message) {
        if (currentLogLevel.ordinal() <= LogLevel.CRITICAL.ordinal()) {
            loggingModule.logAction(LogLevel.CRITICAL, category, message);
        }
    }
    
    @Override
    public List<LogEntry> getRecentLogs(int count) {
        return loggingModule.getRecentLogs(count);
    }
    
    @Override
    public LogLevel getLogLevel() {
        return currentLogLevel;
    }
    
    @Override
    public void setLogLevel(LogLevel level) {
        this.currentLogLevel = level;
        
        // Update the config
        loggingModule.getLoggingConfig().set("logging.level", level.name());
    }
}