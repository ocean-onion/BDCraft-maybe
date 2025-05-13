package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Main implementation of the ModuleManager interface that manages modules.
 */
public class BDModuleManager implements ModuleManager {
    private final BDCraft plugin;
    private final Map<String, Object> modules = new HashMap<>();
    private boolean enabled = false;
    
    /**
     * Creates a new module manager.
     * 
     * @param plugin The plugin instance
     */
    public BDModuleManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "ModuleManager";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDModuleManager");
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDModuleManager");
        
        // Disable all modules
        for (Object module : modules.values()) {
            if (module instanceof ModuleManager) {
                try {
                    ((ModuleManager) module).disable();
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error disabling module " + ((ModuleManager) module).getName(), e);
                }
            }
        }
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        // Reload all modules
        for (Object module : modules.values()) {
            if (module instanceof ModuleManager) {
                try {
                    ((ModuleManager) module).reload();
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error reloading module " + ((ModuleManager) module).getName(), e);
                }
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Object getSubmodule(String name) {
        // This implementation doesn't have submodules, but returns modules
        return getModule(name);
    }
    
    @Override
    public void registerSubmodule(Object submodule) {
        // This implementation doesn't have submodules, but registers modules
        if (submodule instanceof ModuleManager) {
            registerModule((ModuleManager) submodule);
        } else {
            throw new IllegalArgumentException("Submodule must implement ModuleManager");
        }
    }
    
    /**
     * Registers a module.
     * 
     * @param module The module to register
     */
    public void registerModule(ModuleManager module) {
        modules.put(module.getName().toLowerCase(), module);
        
        if (enabled) {
            // Enable the module if the manager is already enabled
            module.enable(plugin);
        }
    }
    
    /**
     * Gets a module by name.
     * 
     * @param name The name of the module
     * @return The module, or null if not found
     */
    public Object getModule(String name) {
        return modules.get(name.toLowerCase());
    }
}