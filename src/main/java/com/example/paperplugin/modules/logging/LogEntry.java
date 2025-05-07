package com.example.paperplugin.modules.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a single log entry in the BDCraft logging system.
 */
public class LogEntry {
    private final long timestamp;
    private final LogLevel level;
    private final String category;
    private final String message;
    
    // Date formatter for timestamp display
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a new log entry.
     *
     * @param timestamp The timestamp of the log entry (milliseconds since epoch)
     * @param level The log level
     * @param category The log category
     * @param message The log message
     */
    public LogEntry(long timestamp, LogLevel level, String category, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.category = category;
        this.message = message;
    }
    
    /**
     * Gets the timestamp of the log entry.
     *
     * @return The timestamp in milliseconds since epoch
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the log level.
     *
     * @return The log level
     */
    public LogLevel getLevel() {
        return level;
    }
    
    /**
     * Gets the log category.
     *
     * @return The log category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Gets the log message.
     *
     * @return The log message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets a formatted timestamp string.
     *
     * @return The formatted timestamp
     */
    public String getFormattedTimestamp() {
        return DATE_FORMAT.format(new Date(timestamp));
    }
    
    /**
     * Gets a formatted log entry.
     *
     * @return The formatted log entry
     */
    @Override
    public String toString() {
        return String.format("[%s] [%s] [%s] %s", 
                getFormattedTimestamp(), 
                level.name(), 
                category, 
                message);
    }
}