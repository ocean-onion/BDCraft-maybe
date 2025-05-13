package com.bdcraft.plugin;

import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.api.MarketAPI;
import com.bdcraft.plugin.api.ProgressionAPI;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.compat.PluginConflictManager;
import com.bdcraft.plugin.config.ConfigManager;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.BDModuleManager;
import com.bdcraft.plugin.modules.perms.BDPermissionAPI;
import com.bdcraft.plugin.modules.display.BDDisplayModule;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.BDEconomyAPI;
import com.bdcraft.plugin.modules.economy.modules.market.MarketManager;
import com.bdcraft.plugin.modules.economy.modules.villager.impl.VillagerManager;
import com.bdcraft.plugin.modules.progression.BDProgressionModule;

import com.bdcraft.plugin.util.PluginBlocker;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Main class for the BDCraft plugin.
 */
public class BDCraft extends JavaPlugin {
    // Modules
    private List<Object> modules;
    private BDEconomyModule economyModule;
    private BDProgressionModule progressionModule;
    private BDDisplayModule displayModule;
    
    // Module management
    private ModuleManager moduleManager;
    
    // Configuration management
    private ConfigManager configManager;
    
    // Permission API for other plugins
    private BDPermissionAPI permissionAPI;
    
    // API implementations
    private BDEconomyAPI economyAPI;
    private ProgressionAPI progressionAPI;
    
    // Plugin conflict management
    private PluginConflictManager conflictManager;
    
    @Override
    public void onEnable() {
        try {
            getLogger().info("Starting BDCraft plugin...");
            
            // Save default configs
            saveDefaultConfig();
            getLogger().info("Default main configuration saved.");
            
            // Save other default configs
            String[] configFiles = {"economy.yml", "rebirth.yml", "vital.yml", "messages.yml", "permissions.yml"};
            for (String configFile : configFiles) {
                if (!new File(getDataFolder(), configFile).exists()) {
                    saveResource(configFile, false);
                    getLogger().info("Saved default configuration: " + configFile);
                } else {
                    getLogger().info("Configuration already exists: " + configFile);
                }
            }
            getLogger().info("All default configurations saved.");
            
            // Initialize modules list
            modules = new ArrayList<Object>();
            getLogger().info("Module list initialized.");
            
            // Initialize config manager
            configManager = new ConfigManager(this);
            getLogger().info("Config manager initialized.");
            
            // Initialize module manager
            moduleManager = new BDModuleManager(this);
            getLogger().info("Module manager initialized.");
        
            // Initialize plugin conflict manager
            boolean blockCompetingPlugins = getConfig().getBoolean("plugin.block-competing-plugins", true);
            if (blockCompetingPlugins) {
                getLogger().info("Plugin conflict management is enabled");
                conflictManager = new PluginConflictManager(this);
                conflictManager.checkForConflicts();
            }
        
            // Initialize economy module
            getLogger().info("Initializing economy module...");
            economyModule = new BDEconomyModule(this);
            modules.add(economyModule);
            
            // Initialize progression module
            getLogger().info("Initializing progression module...");
            progressionModule = new BDProgressionModule(this);
            modules.add(progressionModule);
            
            // Initialize display module (depends on economy and progression)
            getLogger().info("Initializing display module...");
            displayModule = new BDDisplayModule(this, economyModule, progressionModule);
            modules.add(displayModule);
            
            // Initialize API implementations
            getLogger().info("Initializing API implementations...");
            economyAPI = new BDEconomyAPI(this, economyModule);
            progressionAPI = progressionModule; // BDProgressionModule already implements ProgressionAPI
            getLogger().info("API implementations initialized");
            
            // Enable all modules
            for (Object moduleObj : modules) {
                if (moduleObj instanceof ModuleManager) {
                    ModuleManager module = (ModuleManager) moduleObj;
                    getLogger().info("Enabling module: " + module.getName());
                    module.enable(this);
                }
            }
            
            getLogger().info("BDCraft has been enabled successfully!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable BDCraft:", e);
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Disabling BDCraft plugin...");
        
        // Disable all modules in reverse order
        if (modules != null) {
            getLogger().info("Disabling " + modules.size() + " modules in reverse order...");
            for (int i = modules.size() - 1; i >= 0; i--) {
                try {
                    Object moduleObj = modules.get(i);
                    if (moduleObj instanceof ModuleManager) {
                        ModuleManager module = (ModuleManager) moduleObj;
                        getLogger().info("Disabling module: " + module.getName());
                        module.disable();
                    }
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Error disabling module at index " + i, e);
                    e.printStackTrace();
                }
            }
        } else {
            getLogger().warning("No modules to disable (modules list is null)");
        }
        
        getLogger().info("BDCraft has been disabled successfully!");
    }
    
    /**
     * Gets the economy module.
     *
     * @return The economy module
     */
    public BDEconomyModule getEconomyModule() {
        return economyModule;
    }
    
    /**
     * Gets the progression module.
     *
     * @return The progression module
     */
    public BDProgressionModule getProgressionModule() {
        return progressionModule;
    }
    
    /**
     * Gets the display module.
     *
     * @return The display module
     */
    public BDDisplayModule getDisplayModule() {
        return displayModule;
    }
    
    /**
     * Gets the market manager.
     *
     * @return The market manager
     */
    public MarketManager getMarketManager() {
        return economyModule.getMarketManager();
    }
    
    /**
     * Gets the villager manager.
     *
     * @return The villager manager
     */
    public VillagerManager getVillagerManager() {
        return economyModule.getVillagerManager();
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
     * Gets the plugin conflict manager.
     *
     * @return The plugin conflict manager
     */
    public PluginConflictManager getConflictManager() {
        return conflictManager;
    }
    
    /**
     * Gets the progression API.
     *
     * @return The progression API
     */
    public ProgressionAPI getProgressionAPI() {
        return progressionAPI;
    }
    
    /**
     * Gets the economy API.
     *
     * @return The economy API
     */
    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }
    
    // MarketAPI instance
    private MarketAPI marketAPI;
    
    /**
     * Gets the market API.
     *
     * @return The market API
     */
    public MarketAPI getMarketAPI() {
        return marketAPI;
    }
    
    /**
     * Sets the market API.
     *
     * @param marketAPI The market API
     */
    public void setMarketAPI(MarketAPI marketAPI) {
        this.marketAPI = marketAPI;
    }
    
    // VillagerAPI instance
    private VillagerAPI villagerAPI;
    
    /**
     * Gets the villager API.
     *
     * @return The villager API
     */
    public VillagerAPI getVillagerAPI() {
        return villagerAPI;
    }
    
    /**
     * Sets the villager API.
     *
     * @param villagerAPI The villager API
     */
    public void setVillagerAPI(VillagerAPI villagerAPI) {
        this.villagerAPI = villagerAPI;
    }
    
    /**
     * Gets the configuration manager.
     *
     * @return The configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Gets a configuration file by its type.
     * 
     * @param configType The configuration type
     * @return The configuration
     */
    public org.bukkit.configuration.file.FileConfiguration getConfig(com.bdcraft.plugin.config.ConfigType configType) {
        return this.configManager.getConfig(configType);
    }
    
    /**
     * Sets the permission API.
     *
     * @param permissionAPI The permission API
     */
    public void setPermissionAPI(BDPermissionAPI permissionAPI) {
        this.permissionAPI = permissionAPI;
    }
    
    /**
     * Gets the permission API.
     *
     * @return The permission API
     */
    public BDPermissionAPI getPermissionAPI() {
        return permissionAPI;
    }
    
    /**
     * Gets a module by name.
     *
     * @param name The module name
     * @return The module as a ModuleManager, or null if not found
     */
    public ModuleManager getModule(String name) {
        for (Object moduleObj : modules) {
            if (moduleObj instanceof ModuleManager) {
                ModuleManager module = (ModuleManager) moduleObj;
                if (module.getName().equalsIgnoreCase(name)) {
                    return module;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Gets a module manager by class.
     *
     * @param <T> The module type
     * @param clazz The module class
     * @return The module, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getModule(Class<T> clazz) {
        for (Object moduleObj : modules) {
            if (clazz.isInstance(moduleObj)) {
                return (T) moduleObj;
            }
        }
        
        return null;
    }
    
    /**
     * Creates a NamespacedKey for this plugin.
     * 
     * @param key The key string
     * @return The NamespacedKey
     */
    public NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(this, key);
    }
    
    /**
     * Reloads the plugin and all modules.
     */
    public void reload() {
        // Reload config
        reloadConfig();
        
        // Reload all modules
        for (BDModule module : modules) {
            try {
                module.reload();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Error reloading module: " + module.getName(), e);
            }
        }
        
        getLogger().info("BDCraft has been reloaded!");
    }
}