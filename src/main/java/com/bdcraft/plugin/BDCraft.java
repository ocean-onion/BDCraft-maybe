package com.bdcraft.plugin;

import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.api.PermissionAPI;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.config.ConfigManager;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.perms.BDPermsModule;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * Main BDCraft plugin class that orchestrates module loading and communication.
 */
public class BDCraft extends JavaPlugin {
    private static BDCraft instance;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private EconomyAPI economyAPI;
    private VillagerAPI villagerAPI;
    private PermissionAPI permissionAPI;
    
    @Override
    public void onEnable() {
        instance = this;
        Logger logger = getLogger();
        
        logger.info("Initializing BDCraft plugin for Paper 1.21");
        
        // Initialize configuration
        logger.info("Loading configuration...");
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize module manager and register modules
        logger.info("Initializing module system...");
        moduleManager = new ModuleManager(this);
        
        // Register core modules
        registerCoreModules();
        
        // Enable modules in the correct order based on dependencies
        moduleManager.enableModules();
        
        logger.info("BDCraft plugin has been successfully enabled!");
    }
    
    @Override
    public void onDisable() {
        // Disable modules in reverse dependency order
        if (moduleManager != null) {
            moduleManager.disableModules();
        }
        
        getLogger().info("BDCraft plugin has been disabled.");
    }
    
    /**
     * Registers the three core modules that comprise the basic functionality of BDCraft.
     */
    private void registerCoreModules() {
        // Register Economy Module
        BDEconomyModule economyModule = new BDEconomyModule(this);
        moduleManager.registerModule(economyModule);
        
        // Register Permissions Module
        BDPermsModule permsModule = new BDPermsModule(this, moduleManager);
        moduleManager.registerModule(permsModule);
        
        // Register Vital Module
        BDVitalModule vitalModule = new BDVitalModule(this, moduleManager);
        moduleManager.registerModule(vitalModule);
    }
    
    /**
     * Gets a static instance of the plugin
     * @return The plugin instance
     */
    public static BDCraft getInstance() {
        return instance;
    }
    
    /**
     * Gets the module manager
     * @return The module manager
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    /**
     * Gets the configuration manager
     * @return The configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Gets the Economy API
     * @return The Economy API
     */
    public EconomyAPI getEconomyAPI() {
        // This will be initialized by the economy module during enabling
        return economyAPI;
    }
    
    /**
     * Sets the Economy API (called by the Economy Module)
     * @param economyAPI The Economy API implementation
     */
    public void setEconomyAPI(EconomyAPI economyAPI) {
        this.economyAPI = economyAPI;
    }
    
    /**
     * Gets the Villager API
     * @return The Villager API
     */
    public VillagerAPI getVillagerAPI() {
        // This will be initialized by the economy module during enabling
        return villagerAPI;
    }
    
    /**
     * Sets the Villager API (called by the Economy Module)
     * @param villagerAPI The Villager API implementation
     */
    public void setVillagerAPI(VillagerAPI villagerAPI) {
        this.villagerAPI = villagerAPI;
    }
    
    /**
     * Gets the Permission API
     * @return The Permission API
     */
    public PermissionAPI getPermissionAPI() {
        // This will be initialized by the perms module during enabling
        return permissionAPI;
    }
    
    /**
     * Sets the Permission API (called by the Perms Module)
     * @param permissionAPI The Permission API implementation
     */
    public void setPermissionAPI(PermissionAPI permissionAPI) {
        this.permissionAPI = permissionAPI;
    }
}