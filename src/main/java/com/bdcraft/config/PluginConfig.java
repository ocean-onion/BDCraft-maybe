package com.bdcraft.config;

import com.bdcraft.BDCraftPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all configuration files for the BDCraft plugin.
 */
public class PluginConfig {

    private final BDCraftPlugin plugin;
    private final Map<String, FileConfiguration> configFiles;

    /**
     * Creates a new PluginConfig.
     *
     * @param plugin The main plugin instance
     */
    public PluginConfig(BDCraftPlugin plugin) {
        this.plugin = plugin;
        this.configFiles = new HashMap<>();
        
        // Load default config
        loadConfig("config.yml");
        
        // Load other required configs
        loadConfig("messages.yml");
        loadConfig("economy.yml");
        loadConfig("permissions.yml");
        loadConfig("vital.yml");
    }

    /**
     * Loads a configuration file.
     *
     * @param fileName The name of the file to load
     * @return The loaded configuration
     */
    public FileConfiguration loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configFiles.put(fileName, config);
        
        return config;
    }

    /**
     * Gets a configuration file.
     *
     * @param fileName The name of the file to get
     * @return The configuration file, or null if not found
     */
    public FileConfiguration getConfig(String fileName) {
        return configFiles.getOrDefault(fileName, null);
    }

    /**
     * Gets the main configuration file.
     *
     * @return The main configuration file
     */
    public FileConfiguration getMainConfig() {
        return getConfig("config.yml");
    }

    /**
     * Gets the messages configuration file.
     *
     * @return The messages configuration file
     */
    public FileConfiguration getMessagesConfig() {
        return getConfig("messages.yml");
    }

    /**
     * Gets the economy configuration file.
     *
     * @return The economy configuration file
     */
    public FileConfiguration getEconomyConfig() {
        return getConfig("economy.yml");
    }

    /**
     * Gets the permissions configuration file.
     *
     * @return The permissions configuration file
     */
    public FileConfiguration getPermissionsConfig() {
        return getConfig("permissions.yml");
    }

    /**
     * Gets the vital configuration file.
     *
     * @return The vital configuration file
     */
    public FileConfiguration getVitalConfig() {
        return getConfig("vital.yml");
    }

    /**
     * Gets a configuration section for a module.
     *
     * @param moduleName The module name
     * @return The configuration section, or null if not found
     */
    public ConfigurationSection getModuleConfig(String moduleName) {
        return getMainConfig().getConfigurationSection("modules." + moduleName);
    }

    /**
     * Saves a configuration file.
     *
     * @param fileName The name of the file to save
     */
    public void saveConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        FileConfiguration config = configFiles.get(fileName);
        
        if (config != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save config to " + configFile + ": " + e.getMessage());
            }
        }
    }

    /**
     * Saves all configuration files.
     */
    public void saveAllConfigs() {
        for (String fileName : configFiles.keySet()) {
            saveConfig(fileName);
        }
    }

    /**
     * Reloads all configuration files.
     */
    public void reloadAllConfigs() {
        configFiles.clear();
        
        // Reload all configs
        loadConfig("config.yml");
        loadConfig("messages.yml");
        loadConfig("economy.yml");
        loadConfig("permissions.yml");
        loadConfig("vital.yml");
    }
}