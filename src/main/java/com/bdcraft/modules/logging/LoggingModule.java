package com.bdcraft.modules.logging;

import com.bdcraft.BDCraftPlugin;
import com.bdcraft.module.AbstractBDModule;
import com.bdcraft.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Module for the BDCraft logging system.
 * Provides centralized logging functionality for the plugin.
 */
public class LoggingModule extends AbstractBDModule {
    
    private static final String MODULE_NAME = "logging";
    private static final int DEFAULT_MAX_MEMORY_LOGS = 1000;
    private static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private FileConfiguration loggingConfig;
    private LoggingService loggingService;
    private final LinkedList<LogEntry> memoryLogs;
    private int maxMemoryLogs;
    private File logDirectory;
    private String currentLogFile;
    
    /**
     * Creates a new LoggingModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public LoggingModule(BDCraftPlugin plugin, ModuleManager moduleManager) {
        super(plugin, moduleManager);
        this.memoryLogs = new LinkedList<>();
    }
    
    @Override
    public void onEnable() {
        // Load config
        this.loggingConfig = plugin.getPluginConfig().getMainConfig().getConfigurationSection("modules.logging") != null 
                ? plugin.getPluginConfig().getMainConfig() 
                : plugin.getConfig();
        
        // Set up logging directory
        setupLoggingDirectory();
        
        // Set max memory logs
        this.maxMemoryLogs = loggingConfig.getInt("logging.max-memory-logs", DEFAULT_MAX_MEMORY_LOGS);
        
        // Initialize the logging service
        this.loggingService = new LoggingServiceImpl(this);
        
        // Log module enabled
        loggingService.info("Module", "Logging module enabled");
    }
    
    @Override
    public void onDisable() {
        loggingService.info("Module", "Logging module disabled");
        
        // Flush any remaining logs to disk
        flushLogs();
    }
    
    @Override
    public void onReload() {
        // Reload config
        this.loggingConfig = plugin.getPluginConfig().getMainConfig().getConfigurationSection("modules.logging") != null 
                ? plugin.getPluginConfig().getMainConfig() 
                : plugin.getConfig();
        
        // Update max memory logs
        this.maxMemoryLogs = loggingConfig.getInt("logging.max-memory-logs", DEFAULT_MAX_MEMORY_LOGS);
        
        loggingService.info("Module", "Logging module reloaded");
    }
    
    @Override
    public String getName() {
        return MODULE_NAME;
    }
    
    /**
     * Gets the logging service.
     *
     * @return The logging service
     */
    public LoggingService getLoggingService() {
        return loggingService;
    }
    
    /**
     * Gets the logging configuration.
     *
     * @return The logging configuration
     */
    public FileConfiguration getLoggingConfig() {
        return loggingConfig;
    }
    
    /**
     * Sets up the logging directory.
     */
    private void setupLoggingDirectory() {
        // Create logs directory
        logDirectory = new File(plugin.getDataFolder(), "logs");
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        
        // Set current log file
        currentLogFile = "bdcraft-" + LOG_DATE_FORMAT.format(new Date()) + ".log";
    }
    
    /**
     * Logs an action to memory and disk.
     *
     * @param level The log level
     * @param category The log category
     * @param message The log message
     */
    void logAction(LogLevel level, String category, String message) {
        // Create log entry
        LogEntry logEntry = new LogEntry(System.currentTimeMillis(), level, category, message);
        
        // Add to memory logs
        memoryLogs.addFirst(logEntry);
        
        // Trim memory logs if necessary
        while (memoryLogs.size() > maxMemoryLogs) {
            memoryLogs.removeLast();
        }
        
        // Log to console if configured
        if (loggingConfig.getBoolean("logging.console-output", true)) {
            switch (level) {
                case DEBUG:
                case INFO:
                    plugin.getLogger().info(logEntry.toString());
                    break;
                case WARNING:
                    plugin.getLogger().warning(logEntry.toString());
                    break;
                case ERROR:
                case CRITICAL:
                    plugin.getLogger().severe(logEntry.toString());
                    break;
            }
        }
        
        // Write to disk asynchronously
        if (loggingConfig.getBoolean("logging.file-output", true)) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                writeLogToDisk(logEntry);
            });
        }
    }
    
    /**
     * Writes a log entry to disk.
     *
     * @param logEntry The log entry to write
     */
    private void writeLogToDisk(LogEntry logEntry) {
        File logFile = new File(logDirectory, currentLogFile);
        
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logEntry.toString() + System.lineSeparator());
        } catch (IOException e) {
            plugin.getLogger().severe("Error writing to log file: " + e.getMessage());
        }
    }
    
    /**
     * Gets recent log entries.
     *
     * @param count Maximum number of entries to return
     * @return List of recent log entries, most recent first
     */
    List<LogEntry> getRecentLogs(int count) {
        // Limit count to available logs
        count = Math.min(count, memoryLogs.size());
        
        // Return a copy of the logs
        return new ArrayList<>(memoryLogs.subList(0, count));
    }
    
    /**
     * Flushes all memory logs to disk.
     */
    private void flushLogs() {
        if (loggingConfig.getBoolean("logging.file-output", true)) {
            File logFile = new File(logDirectory, currentLogFile);
            
            try (FileWriter writer = new FileWriter(logFile, true)) {
                for (LogEntry logEntry : memoryLogs) {
                    writer.write(logEntry.toString() + System.lineSeparator());
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Error flushing logs to disk: " + e.getMessage());
            }
        }
    }
}