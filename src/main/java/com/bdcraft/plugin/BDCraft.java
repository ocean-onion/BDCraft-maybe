package com.bdcraft.plugin;

import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.api.PermissionAPI;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.commands.admin.GiveItemCommand;
import com.bdcraft.plugin.config.ConfigManager;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.perms.BDPermsModule;
import com.bdcraft.plugin.modules.vital.BDVitalModule;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.logging.Logger;

/**
 * Main plugin class for BDCraft.
 */
public class BDCraft extends JavaPlugin {
    private Logger logger;
    private ConfigManager configManager;
    private ModuleManager moduleManager;
    
    // APIs
    private EconomyAPI economyAPI;
    private PermissionAPI permissionAPI;
    private VillagerAPI villagerAPI;
    
    /**
     * Default constructor for the plugin.
     */
    public BDCraft() {
        super();
    }
    
    /**
     * Test constructor for mocking the plugin.
     */
    protected BDCraft(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }
    
    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Enabling BDCraft...");
        
        // Initialize managers
        configManager = new ConfigManager(this);
        moduleManager = new ModuleManager(this);
        
        // Register modules
        moduleManager.registerModule(new BDPermsModule(this, moduleManager));
        moduleManager.registerModule(new BDEconomyModule(this));
        moduleManager.registerModule(new BDVitalModule(this, moduleManager));
        
        // Enable modules
        moduleManager.enableModules();
        
        // Register commands
        registerCommands();
        
        logger.info("BDCraft enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling BDCraft...");
        
        // Disable modules
        if (moduleManager != null) {
            moduleManager.disableModules();
        }
        
        // Save configuration
        if (configManager != null) {
            configManager.saveConfigs();
        }
        
        logger.info("BDCraft disabled!");
    }
    
    /**
     * Gets the module manager.
     * @return The module manager
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    /**
     * Gets the configuration manager.
     * @return The configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Gets the economy API.
     * @return The economy API
     */
    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }
    
    /**
     * Sets the economy API.
     * @param economyAPI The economy API
     */
    public void setEconomyAPI(EconomyAPI economyAPI) {
        this.economyAPI = economyAPI;
    }
    
    /**
     * Gets the permission API.
     * @return The permission API
     */
    public PermissionAPI getPermissionAPI() {
        return permissionAPI;
    }
    
    /**
     * Sets the permission API.
     * @param permissionAPI The permission API
     */
    public void setPermissionAPI(PermissionAPI permissionAPI) {
        this.permissionAPI = permissionAPI;
    }
    
    /**
     * Gets the villager API.
     * @return The villager API
     */
    public VillagerAPI getVillagerAPI() {
        return villagerAPI;
    }
    
    /**
     * Sets the villager API.
     * @param villagerAPI The villager API
     */
    public void setVillagerAPI(VillagerAPI villagerAPI) {
        this.villagerAPI = villagerAPI;
    }
    
    /**
     * Registers commands for the plugin.
     */
    private void registerCommands() {
        // Register admin commands
        new GiveItemCommand(this);
        
        logger.info("Commands registered successfully!");
    }
}