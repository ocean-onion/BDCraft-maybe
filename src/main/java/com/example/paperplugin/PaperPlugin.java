package com.example.paperplugin;

import com.example.paperplugin.commands.HelloCommand;
import com.example.paperplugin.config.PluginConfig;
import com.example.paperplugin.listeners.PlayerJoinListener;
import com.example.paperplugin.module.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Paper plugin.
 * This plugin implements a modular architecture for flexible Minecraft server management.
 *
 * @author Your Name
 * @version 1.0
 */
public class PaperPlugin extends JavaPlugin {

    private PluginConfig pluginConfig;
    private ModuleManager moduleManager;

    /**
     * Called when the plugin is enabled.
     * Initializes modules, registers commands and event listeners.
     */
    @Override
    public void onEnable() {
        // Log startup message
        getLogger().info("PaperPlugin is starting up!");
        
        // Load configuration
        loadConfiguration();
        
        // Initialize the module system
        initializeModuleSystem();
        
        // Register built-in commands
        registerCommands();
        
        // Register common event listeners
        registerEventListeners();
        
        getLogger().info("PaperPlugin has been enabled successfully!");
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        getLogger().info("PaperPlugin is shutting down!");
        
        // Disable all modules
        if (moduleManager != null) {
            moduleManager.disableModules();
        }
        
        // Save any configuration changes
        saveConfig();
        
        getLogger().info("PaperPlugin has been disabled successfully!");
    }
    
    /**
     * Loads the plugin configuration.
     */
    private void loadConfiguration() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Initialize the config wrapper
        this.pluginConfig = new PluginConfig(this);
        
        getLogger().info("Configuration loaded successfully!");
    }
    
    /**
     * Initializes the module system and loads all modules.
     */
    private void initializeModuleSystem() {
        getLogger().info("Initializing module system...");
        
        // Create the module manager
        this.moduleManager = new ModuleManager(this);
        
        // Register core modules
        registerCoreModules();
        
        // Initialize and enable modules
        moduleManager.initializeModules();
        moduleManager.enableModules();
        
        getLogger().info("Module system initialized successfully!");
    }
    
    /**
     * Registers all core modules with the module manager.
     */
    private void registerCoreModules() {
        // This will be implemented with actual modules soon
        getLogger().info("Core modules registered!");
    }
    
    /**
     * Registers all commands for the plugin.
     */
    private void registerCommands() {
        getLogger().info("Registering commands...");
        
        // Register the hello command
        getCommand("hello").setExecutor(new HelloCommand(this));
        
        getLogger().info("Commands registered successfully!");
    }
    
    /**
     * Registers all event listeners for the plugin.
     */
    private void registerEventListeners() {
        getLogger().info("Registering event listeners...");
        
        // Register the player join listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        getLogger().info("Event listeners registered successfully!");
    }
    
    /**
     * Gets the plugin configuration wrapper.
     *
     * @return The plugin configuration wrapper
     */
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
    
    /**
     * Gets the module manager.
     *
     * @return The module manager
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    /**
     * Reloads the plugin configuration and all modules.
     */
    public void reload() {
        getLogger().info("Reloading plugin...");
        
        // Reload configuration
        reloadConfig();
        pluginConfig = new PluginConfig(this);
        
        // Reload modules
        if (moduleManager != null) {
            moduleManager.reloadModules();
        }
        
        getLogger().info("Plugin reloaded successfully!");
    }
}
