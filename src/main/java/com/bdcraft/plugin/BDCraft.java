package com.bdcraft.plugin;

import com.bdcraft.plugin.compat.PluginConflictManager;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.display.BDDisplayModule;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import com.bdcraft.plugin.modules.progression.BDProgressionModule;
import com.bdcraft.plugin.util.PluginBlocker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Main class for the BDCraft plugin.
 */
public class BDCraft extends JavaPlugin {
    // Modules
    private List<BDModule> modules;
    private BDEconomyModule economyModule;
    private BDProgressionModule progressionModule;
    private BDDisplayModule displayModule;
    
    // Plugin conflict management
    private PluginConflictManager conflictManager;
    
    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Initialize modules list
        modules = new ArrayList<>();
        
        // Initialize plugin conflict manager
        boolean blockCompetingPlugins = getConfig().getBoolean("plugin.block-competing-plugins", true);
        if (blockCompetingPlugins) {
            getLogger().info("Plugin conflict management is enabled");
            conflictManager = new PluginConflictManager(this);
            conflictManager.checkForConflicts();
        }
        
        // Initialize modules
        try {
            // Initialize economy module
            economyModule = new BDEconomyModule(this);
            modules.add(economyModule);
            
            // Initialize progression module
            progressionModule = new BDProgressionModule(this);
            modules.add(progressionModule);
            
            // Initialize display module (depends on economy and progression)
            displayModule = new BDDisplayModule(this, economyModule, progressionModule);
            modules.add(displayModule);
            
            // Enable all modules
            for (BDModule module : modules) {
                module.onEnable();
            }
            
            getLogger().info("BDCraft has been enabled!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable BDCraft:", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        // Disable all modules in reverse order
        if (modules != null) {
            for (int i = modules.size() - 1; i >= 0; i--) {
                try {
                    modules.get(i).onDisable();
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Error disabling module: " + modules.get(i).getName(), e);
                }
            }
        }
        
        getLogger().info("BDCraft has been disabled!");
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
    public BDMarketManager getMarketManager() {
        return economyModule.getMarketManager();
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
     * Gets a module by name.
     *
     * @param name The module name
     * @return The module, or null if not found
     */
    public BDModule getModule(String name) {
        for (BDModule module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        
        return null;
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