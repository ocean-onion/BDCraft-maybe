package com.bdcraft;

import com.bdcraft.config.PluginConfig;
import com.bdcraft.modules.economy.EconomyModule;
import com.bdcraft.modules.logging.LoggingModule;
import com.bdcraft.modules.permissions.PermissionsModule;
import com.bdcraft.modules.vital.VitalModule;
import com.bdcraft.modules.placeholder.PlaceholderModule;
import com.bdcraft.module.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the BDCraft plugin.
 * This plugin implements a modular architecture for comprehensive Minecraft server management.
 *
 * @version 1.0
 */
public class BDCraftPlugin extends JavaPlugin {

    private PluginConfig pluginConfig;
    private ModuleManager moduleManager;

    /**
     * Called when the plugin is enabled.
     * Initializes modules, registers commands and event listeners.
     */
    @Override
    public void onEnable() {
        // Log startup message
        getLogger().info("BDCraft is starting up!");
        
        // Load configuration
        loadConfiguration();
        
        // Initialize the module system
        initializeModuleSystem();
        
        getLogger().info("BDCraft has been enabled successfully!");
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        getLogger().info("BDCraft is shutting down!");
        
        // Disable all modules
        if (moduleManager != null) {
            moduleManager.disableModules();
        }
        
        // Save any configuration changes
        saveConfig();
        
        getLogger().info("BDCraft has been disabled successfully!");
    }
    
    /**
     * Loads the plugin configuration.
     */
    private void loadConfiguration() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Create default message configuration
        saveResource("messages.yml", false);
        
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
        // Register the vital (essential utilities) module
        moduleManager.registerModule(new VitalModule(this, moduleManager));
        
        // Register the permissions module
        moduleManager.registerModule(new PermissionsModule(this, moduleManager));
        
        // Register the logging module
        moduleManager.registerModule(new LoggingModule(this, moduleManager));
        
        // Register the economy module
        moduleManager.registerModule(new EconomyModule(this, moduleManager));
        
        // Register the placeholder module
        moduleManager.registerModule(new PlaceholderModule(this, moduleManager));
        
        getLogger().info("Core modules registered!");
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