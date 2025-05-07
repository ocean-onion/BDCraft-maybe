package com.example.paperplugin.config;

import com.example.paperplugin.PaperPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Configuration wrapper for the plugin.
 * Provides easy access to configuration values with proper defaults.
 */
public class PluginConfig {

    private final PaperPlugin plugin;
    private final FileConfiguration config;
    
    // Default values
    private static final String DEFAULT_GREETING_FORMAT = "Hello, {player}! Welcome to your Paper 1.21 plugin!";
    private static final String DEFAULT_WELCOME_FORMAT = "Welcome to the server, {player}!";
    private static final boolean DEFAULT_DEBUG_MODE = false;
    private static final boolean DEFAULT_SHOW_WELCOME_MESSAGE = true;

    /**
     * Constructor for PluginConfig.
     *
     * @param plugin The main plugin instance
     */
    public PluginConfig(PaperPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        
        // Set up default values if they don't exist
        setDefaults();
    }

    /**
     * Sets up default configuration values.
     */
    private void setDefaults() {
        config.addDefault("messages.greeting-format", DEFAULT_GREETING_FORMAT);
        config.addDefault("messages.welcome-format", DEFAULT_WELCOME_FORMAT);
        config.addDefault("settings.debug", DEFAULT_DEBUG_MODE);
        config.addDefault("settings.show-welcome-message", DEFAULT_SHOW_WELCOME_MESSAGE);
        config.addDefault("config-version", 1);
        
        // Save defaults
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    /**
     * Gets the greeting format from the config.
     *
     * @return The greeting format string
     */
    public String getGreetingFormat() {
        return config.getString("messages.greeting-format", DEFAULT_GREETING_FORMAT);
    }

    /**
     * Gets the welcome message format from the config.
     *
     * @return The welcome message format string
     */
    public String getWelcomeMessageFormat() {
        return config.getString("messages.welcome-format", DEFAULT_WELCOME_FORMAT);
    }

    /**
     * Checks if debug mode is enabled.
     *
     * @return true if debug mode is enabled, false otherwise
     */
    public boolean isDebugEnabled() {
        return config.getBoolean("settings.debug", DEFAULT_DEBUG_MODE);
    }

    /**
     * Checks if welcome messages should be shown.
     *
     * @return true if welcome messages should be shown, false otherwise
     */
    public boolean showWelcomeMessages() {
        return config.getBoolean("settings.show-welcome-message", DEFAULT_SHOW_WELCOME_MESSAGE);
    }

    /**
     * Gets the configuration version.
     *
     * @return The configuration version
     */
    public int getConfigVersion() {
        return config.getInt("config-version", 1);
    }

    /**
     * Reloads the configuration from disk.
     */
    public void reload() {
        plugin.reloadConfig();
    }
}
