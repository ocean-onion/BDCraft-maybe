package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.modules.auction.BDAuctionModule;
import com.bdcraft.plugin.modules.economy.modules.market.BDMarketModule;
import com.bdcraft.plugin.modules.economy.modules.villager.BDVillagerModule;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Main economy module that manages all economy-related features.
 */
public class BDEconomyModule implements ModuleManager {
    private final BDCraft plugin;
    private boolean enabled = false;
    private final Map<String, SubmoduleBase> submodules = new HashMap<>();
    
    /**
     * Creates a new economy module.
     * 
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Economy";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDEconomy module");
        
        // Register and enable submodules
        registerSubmodules();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDEconomy module");
        
        // Disable submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.disable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error disabling submodule " + submodule.getName(), e);
            }
        }
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        // Reload submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.reload();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error reloading submodule " + submodule.getName(), e);
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Object getSubmodule(String name) {
        return submodules.get(name);
    }
    
    @Override
    public void registerSubmodule(Object submodule) {
        if (!(submodule instanceof SubmoduleBase)) {
            throw new IllegalArgumentException("Submodule must implement SubmoduleBase");
        }
        
        SubmoduleBase base = (SubmoduleBase) submodule;
        submodules.put(base.getName(), base);
        
        if (enabled) {
            // Enable the submodule if the parent module is already enabled
            base.enable(this);
        }
    }
    
    /**
     * Registers all submodules.
     */
    private void registerSubmodules() {
        // Register market module
        registerSubmodule(new BDMarketModule(plugin));
        
        // Register auction module
        registerSubmodule(new BDAuctionModule(plugin));
        
        // Register villager module
        registerSubmodule(new BDVillagerModule(plugin));
        
        // Enable all submodules
        for (SubmoduleBase submodule : submodules.values()) {
            boolean enableSubmodule = plugin.getConfig(ConfigType.CONFIG)
                    .getBoolean("modules.economy." + submodule.getName().toLowerCase() + ".enabled", true);
            
            if (enableSubmodule) {
                try {
                    submodule.enable(this);
                    plugin.getLogger().info("Enabled " + submodule.getName() + " submodule");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error enabling submodule " + submodule.getName(), e);
                }
            }
        }
    }
}