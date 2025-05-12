package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.progression.modules.rebirth.BDRebirthModule;
import com.bdcraft.plugin.modules.progression.modules.rank.BDRankModule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Main progression module that manages all progression-related features.
 */
public class BDProgressionModule implements ModuleManager, ProgressionManager {
    private final BDCraft plugin;
    private boolean enabled = false;
    private final Map<String, SubmoduleBase> submodules = new HashMap<>();
    
    /**
     * Creates a new progression module.
     * 
     * @param plugin The plugin instance
     */
    public BDProgressionModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Progression";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDProgression module");
        
        // Register and enable submodules
        registerSubmodules();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDProgression module");
        
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
        // Register rebirth module
        registerSubmodule(new BDRebirthModule(plugin));
        
        // Register rank module
        registerSubmodule(new BDRankModule(plugin));
        
        // Enable all submodules
        for (SubmoduleBase submodule : submodules.values()) {
            boolean enableSubmodule = plugin.getConfig(ConfigType.CONFIG)
                    .getBoolean("modules.progression." + submodule.getName().toLowerCase() + ".enabled", true);
            
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
    
    // ProgressionManager implementation
    
    @Override
    public Rank getPlayerRank(UUID playerId) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.getPlayerRank(playerId);
        }
        return Rank.DEFAULT; // Return default rank if module not available
    }
    
    @Override
    public int getRebirthLevel(UUID playerId) {
        BDRebirthModule rebirthModule = (BDRebirthModule) getSubmodule("Rebirth");
        if (rebirthModule != null && rebirthModule.isEnabled()) {
            return rebirthModule.getRebirthLevel(playerId);
        }
        return 0; // Return 0 if module not available
    }
    
    @Override
    public boolean performRebirth(UUID playerId) {
        BDRebirthModule rebirthModule = (BDRebirthModule) getSubmodule("Rebirth");
        if (rebirthModule != null && rebirthModule.isEnabled()) {
            return rebirthModule.performRebirth(playerId);
        }
        return false; // Return false if module not available
    }
    
    @Override
    public boolean setRank(UUID playerId, Rank rank) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.setPlayerRank(playerId, rank);
        }
        return false; // Return false if module not available
    }
}