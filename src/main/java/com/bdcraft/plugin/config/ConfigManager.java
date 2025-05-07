package com.bdcraft.plugin.config;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages plugin configuration files.
 */
public class ConfigManager {
    private final BDCraft plugin;
    private final Logger logger;
    
    private final Map<String, FileConfiguration> configs;
    private final Map<String, File> configFiles;
    
    private static final String[] CONFIG_FILES = {
            "config.yml",
            "permissions.yml",
            "economy.yml",
            "vital.yml",
            "messages.yml"
    };
    
    /**
     * Creates a new config manager.
     * @param plugin The plugin instance
     */
    public ConfigManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.configs = new HashMap<>();
        this.configFiles = new HashMap<>();
        
        // Load configurations
        loadConfigs();
    }
    
    /**
     * Loads all configuration files.
     */
    private void loadConfigs() {
        for (String configFile : CONFIG_FILES) {
            loadConfig(configFile);
        }
    }
    
    /**
     * Loads a specific configuration file.
     * @param filename The configuration filename
     */
    private void loadConfig(String filename) {
        File file = new File(plugin.getDataFolder(), filename);
        String configName = getConfigName(filename);
        
        if (!file.exists()) {
            // Save default resource
            logger.info("Creating default configuration: " + filename);
            plugin.saveResource(filename, false);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(configName, config);
        configFiles.put(configName, file);
        
        logger.info("Loaded configuration: " + filename);
    }
    
    /**
     * Gets the config name from a filename.
     * @param filename The filename
     * @return The config name
     */
    private String getConfigName(String filename) {
        // Remove .yml extension
        return filename.substring(0, filename.lastIndexOf('.'));
    }
    
    /**
     * Reloads all configuration files.
     */
    public void reloadConfigs() {
        configs.clear();
        configFiles.clear();
        loadConfigs();
    }
    
    /**
     * Gets a configuration by name.
     * @param name The configuration name (without .yml extension)
     * @return The configuration, or null if it doesn't exist
     */
    public FileConfiguration getConfig(String name) {
        return configs.get(name);
    }
    
    /**
     * Gets the main plugin configuration.
     * @return The main configuration
     */
    public FileConfiguration getMainConfig() {
        return getConfig("config");
    }
    
    /**
     * Gets a module-specific configuration section.
     * @param moduleName The module name
     * @return The module configuration section
     */
    public ConfigurationSection getModuleConfig(String moduleName) {
        FileConfiguration config;
        
        // Check if module has a dedicated config file
        if (configs.containsKey(moduleName)) {
            config = configs.get(moduleName);
            return config;
        }
        
        // Otherwise look for section in main config
        config = getMainConfig();
        ConfigurationSection section = config.getConfigurationSection(moduleName);
        
        if (section == null) {
            // Create section if it doesn't exist
            section = config.createSection(moduleName);
            saveConfig("config");
        }
        
        return section;
    }
    
    /**
     * Saves a configuration file.
     * @param name The configuration name (without .yml extension)
     */
    public void saveConfig(String name) {
        FileConfiguration config = configs.get(name);
        File file = configFiles.get(name);
        
        if (config == null || file == null) {
            logger.warning("Tried to save unknown configuration: " + name);
            return;
        }
        
        try {
            config.save(file);
        } catch (IOException e) {
            logger.severe("Failed to save configuration '" + name + "': " + e.getMessage());
        }
    }
    
    /**
     * Saves all configuration files.
     */
    public void saveConfigs() {
        for (String name : configs.keySet()) {
            saveConfig(name);
        }
    }
}