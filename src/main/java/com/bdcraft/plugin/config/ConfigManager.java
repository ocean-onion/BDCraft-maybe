package com.bdcraft.plugin.config;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manages configuration files for the plugin and its modules.
 */
public class ConfigManager {
    private final BDCraft plugin;
    private final Map<String, FileConfiguration> moduleConfigs;
    
    /**
     * Creates a new config manager.
     *
     * @param plugin The plugin instance
     */
    public ConfigManager(BDCraft plugin) {
        this.plugin = plugin;
        this.moduleConfigs = new HashMap<>();
    }
    
    /**
     * Gets a configuration file by its type.
     * 
     * @param configType The configuration type
     * @return The configuration
     */
    public FileConfiguration getConfig(ConfigType configType) {
        return getModuleConfig(configType.getFileName());
    }
    
    /**
     * Gets the configuration for a module.
     *
     * @param moduleName The module name
     * @return The module configuration
     */
    public FileConfiguration getModuleConfig(String moduleName) {
        // If already loaded, return it
        if (moduleConfigs.containsKey(moduleName)) {
            return moduleConfigs.get(moduleName);
        }
        
        // Otherwise, load it
        File configFile = new File(plugin.getDataFolder(), moduleName + ".yml");
        
        // If file doesn't exist, create default
        if (!configFile.exists()) {
            try {
                plugin.saveResource(moduleName + ".yml", false);
            } catch (IllegalArgumentException e) {
                // Resource doesn't exist in jar, create empty file
                try {
                    configFile.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to create config file for " + moduleName, ex);
                    return new YamlConfiguration();
                }
            }
        }
        
        // Load and cache the config
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        moduleConfigs.put(moduleName, config);
        
        return config;
    }
    
    /**
     * Saves a module's configuration.
     *
     * @param moduleName The module name
     */
    public void saveModuleConfig(String moduleName) {
        if (!moduleConfigs.containsKey(moduleName)) {
            plugin.getLogger().warning("Tried to save non-loaded config for " + moduleName);
            return;
        }
        
        try {
            File configFile = new File(plugin.getDataFolder(), moduleName + ".yml");
            moduleConfigs.get(moduleName).save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config for " + moduleName, e);
        }
    }
    
    /**
     * Reloads a module's configuration.
     *
     * @param moduleName The module name
     * @return The reloaded configuration
     */
    public FileConfiguration reloadModuleConfig(String moduleName) {
        // Remove from cache
        moduleConfigs.remove(moduleName);
        
        // Load fresh
        return getModuleConfig(moduleName);
    }
    
    /**
     * Gets a configuration file by name.
     * This is an alias for getModuleConfig to be more consistent with naming conventions.
     *
     * @param configName The configuration file name
     * @return The configuration
     */
    public FileConfiguration getConfig(String configName) {
        return getModuleConfig(configName.replace(".yml", ""));
    }
}