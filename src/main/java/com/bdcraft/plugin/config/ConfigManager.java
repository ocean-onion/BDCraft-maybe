package com.bdcraft.plugin.config;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages configuration files for the plugin and modules.
 */
public class ConfigManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, FileConfiguration> configMap = new HashMap<>();
    private FileConfiguration mainConfig;
    
    /**
     * Creates a new ConfigManager.
     * @param plugin The main plugin instance
     */
    public ConfigManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Loads the main configuration file and initializes defaults if needed.
     */
    public void loadConfig() {
        // Create the data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            boolean created = plugin.getDataFolder().mkdirs();
            if (!created) {
                logger.severe("Failed to create plugin data folder!");
            }
        }
        
        // Load or create the main config.yml
        plugin.saveDefaultConfig();
        mainConfig = plugin.getConfig();
        configMap.put("config", mainConfig);
        
        // Load or create module configuration files
        loadModuleConfig("economy");
        loadModuleConfig("permissions");
        loadModuleConfig("vital");
        loadModuleConfig("messages");
        
        logger.info("Configuration loaded successfully");
    }
    
    /**
     * Loads a module-specific configuration file.
     * @param moduleName The name of the module
     */
    private void loadModuleConfig(String moduleName) {
        File configFile = new File(plugin.getDataFolder(), moduleName + ".yml");
        
        if (!configFile.exists()) {
            // Save default config from resource if it exists
            try {
                plugin.saveResource(moduleName + ".yml", false);
            } catch (IllegalArgumentException e) {
                // Resource doesn't exist, create a new file
                try {
                    boolean created = configFile.createNewFile();
                    if (!created) {
                        logger.severe("Failed to create " + moduleName + ".yml config file!");
                        return;
                    }
                } catch (IOException ex) {
                    logger.severe("Failed to create " + moduleName + ".yml config file: " + ex.getMessage());
                    return;
                }
            }
        }
        
        // Load the configuration file
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configMap.put(moduleName, config);
        logger.info("Loaded " + moduleName + ".yml configuration");
    }
    
    /**
     * Gets the main configuration.
     * @return The main configuration
     */
    public FileConfiguration getMainConfig() {
        return mainConfig;
    }
    
    /**
     * Gets a module-specific configuration.
     * @param moduleName The name of the module
     * @return The module configuration, or null if not found
     */
    public FileConfiguration getModuleConfig(String moduleName) {
        return configMap.get(moduleName);
    }
    
    /**
     * Saves a module-specific configuration.
     * @param moduleName The name of the module
     */
    public void saveModuleConfig(String moduleName) {
        FileConfiguration config = configMap.get(moduleName);
        if (config == null) {
            logger.warning("Attempted to save non-existent config: " + moduleName);
            return;
        }
        
        try {
            File configFile = new File(plugin.getDataFolder(), moduleName + ".yml");
            config.save(configFile);
            logger.info("Saved " + moduleName + ".yml configuration");
        } catch (IOException e) {
            logger.severe("Failed to save " + moduleName + ".yml configuration: " + e.getMessage());
        }
    }
    
    /**
     * Reloads all configuration files.
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        mainConfig = plugin.getConfig();
        configMap.put("config", mainConfig);
        
        // Reload module configurations
        for (String moduleName : configMap.keySet()) {
            if (!moduleName.equals("config")) {
                loadModuleConfig(moduleName);
            }
        }
        
        logger.info("All configurations reloaded");
    }
}