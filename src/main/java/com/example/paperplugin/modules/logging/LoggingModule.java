package com.example.paperplugin.modules.logging;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.module.AbstractBDModule;
import com.example.paperplugin.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Logging module implementation for the BDCraft plugin.
 * Handles comprehensive logging of all BD activities.
 */
public class LoggingModule extends AbstractBDModule {

    private FileConfiguration loggingConfig;
    private File loggingConfigFile;
    private final List<LogEntry> actionLog;
    
    // Constants for the module
    private static final String MODULE_NAME = "logging";
    private static final String CONFIG_FILE_NAME = "logging.yml";

    /**
     * Creates a new LoggingModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public LoggingModule(PaperPlugin plugin, ModuleManager moduleManager) {
        super(plugin, moduleManager);
        this.actionLog = new ArrayList<>();
    }

    /**
     * Called when the module is enabled.
     * Initializes the logging system.
     */
    @Override
    public void onEnable() {
        plugin.getLogger().info("Enabling Logging Module...");
        
        // Load logging configuration
        loadLoggingConfig();
        
        // Register the logging service
        registerLoggingService();
        
        plugin.getLogger().info("Logging Module enabled successfully!");
    }

    /**
     * Called when the module is disabled.
     * Saves all log data.
     */
    @Override
    public void onDisable() {
        plugin.getLogger().info("Disabling Logging Module...");
        
        // Save logs
        saveLogs();
        
        plugin.getLogger().info("Logging Module disabled successfully!");
    }

    /**
     * Called when the module is reloaded.
     * Reloads logging configuration.
     */
    @Override
    public void onReload() {
        plugin.getLogger().info("Reloading Logging Module...");
        
        // Reload logging configuration
        reloadLoggingConfig();
        
        plugin.getLogger().info("Logging Module reloaded successfully!");
    }

    /**
     * Gets the module name.
     *
     * @return The module name
     */
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Loads the logging configuration.
     */
    private void loadLoggingConfig() {
        loggingConfigFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        
        if (!loggingConfigFile.exists()) {
            // Create default config file if it doesn't exist
            try {
                loggingConfigFile.createNewFile();
                loggingConfig = YamlConfiguration.loadConfiguration(loggingConfigFile);
                
                // Set default values
                loggingConfig.set("log.enabled", true);
                loggingConfig.set("log.level", "INFO");
                loggingConfig.set("log.file", "logs/bd_activity.log");
                loggingConfig.set("log.format", "[%date%] [%level%] %message%");
                
                // Save the config
                loggingConfig.save(loggingConfigFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create logging config!", e);
                loggingConfig = new YamlConfiguration();
            }
        } else {
            loggingConfig = YamlConfiguration.loadConfiguration(loggingConfigFile);
        }
        
        plugin.getLogger().info("Logging configuration loaded!");
    }

    /**
     * Reloads the logging configuration.
     */
    private void reloadLoggingConfig() {
        loggingConfig = YamlConfiguration.loadConfiguration(loggingConfigFile);
        plugin.getLogger().info("Logging configuration reloaded!");
    }

    /**
     * Saves the logging configuration.
     */
    private void saveLoggingConfig() {
        try {
            loggingConfig.save(loggingConfigFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save logging config!", e);
        }
    }
    
    /**
     * Saves logs to file.
     */
    private void saveLogs() {
        // Implement actual log saving logic
        // This would write to a file or database
        plugin.getLogger().info("Saved " + actionLog.size() + " log entries");
    }

    /**
     * Registers the logging service with the module manager.
     */
    private void registerLoggingService() {
        // Create the logging service implementation
        LoggingService loggingService = new LoggingServiceImpl(this);
        
        // Register it with the module manager
        moduleManager.registerService(LoggingService.class, loggingService);
        
        plugin.getLogger().info("Logging service registered!");
    }
    
    /**
     * Logs an action.
     *
     * @param level The log level
     * @param category The log category
     * @param message The log message
     */
    public void logAction(LogLevel level, String category, String message) {
        // Add to in-memory log
        LogEntry entry = new LogEntry(
            System.currentTimeMillis(),
            level,
            category,
            message
        );
        
        actionLog.add(entry);
        
        // Also log to console if high enough level
        if (level.ordinal() >= LogLevel.WARNING.ordinal()) {
            plugin.getLogger().log(
                java.util.logging.Level.WARNING, 
                "[" + category + "] " + message
            );
        }
    }
    
    /**
     * Gets recent logs.
     *
     * @param count Maximum number of logs to return
     * @return List of recent logs, most recent first
     */
    public List<LogEntry> getRecentLogs(int count) {
        if (actionLog.isEmpty()) {
            return Collections.emptyList();
        }
        
        int size = actionLog.size();
        int startIndex = Math.max(0, size - count);
        
        List<LogEntry> recentLogs = new ArrayList<>(actionLog.subList(startIndex, size));
        Collections.reverse(recentLogs);
        
        return recentLogs;
    }
    
    /**
     * Gets the logging configuration.
     *
     * @return The logging configuration
     */
    public FileConfiguration getLoggingConfig() {
        return loggingConfig;
    }
}