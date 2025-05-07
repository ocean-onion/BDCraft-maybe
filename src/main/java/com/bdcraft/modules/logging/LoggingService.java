package com.bdcraft.modules.logging;

import java.util.List;

/**
 * Service interface for the BDCraft logging system.
 * Provides methods for logging actions and querying logs.
 */
public interface LoggingService {
    
    /**
     * Logs a debug message.
     *
     * @param category The log category
     * @param message The log message
     */
    void debug(String category, String message);
    
    /**
     * Logs an info message.
     *
     * @param category The log category
     * @param message The log message
     */
    void info(String category, String message);
    
    /**
     * Logs a warning message.
     *
     * @param category The log category
     * @param message The log message
     */
    void warning(String category, String message);
    
    /**
     * Logs an error message.
     *
     * @param category The log category
     * @param message The log message
     */
    void error(String category, String message);
    
    /**
     * Logs a critical message.
     *
     * @param category The log category
     * @param message The log message
     */
    void critical(String category, String message);
    
    /**
     * Gets recent log entries.
     *
     * @param count Maximum number of entries to return
     * @return List of recent log entries, most recent first
     */
    List<LogEntry> getRecentLogs(int count);
    
    /**
     * Gets the current log level.
     *
     * @return The current log level
     */
    LogLevel getLogLevel();
    
    /**
     * Sets the current log level.
     *
     * @param level The new log level
     */
    void setLogLevel(LogLevel level);
}