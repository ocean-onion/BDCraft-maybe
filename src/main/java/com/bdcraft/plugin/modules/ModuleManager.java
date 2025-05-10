package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all modules in the plugin.
 */
public class ModuleManager {
    private final BDCraft plugin;
    private final Map<String, BDModule> modules;
    
    /**
     * Creates a new module manager.
     *
     * @param plugin The plugin instance
     */
    public ModuleManager(BDCraft plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
    }
    
    /**
     * Registers a module.
     *
     * @param module The module to register
     */
    public void registerModule(BDModule module) {
        modules.put(module.getName().toLowerCase(), module);
    }
    
    /**
     * Gets a module by name.
     *
     * @param name The module name
     * @return The module, or null if not found
     */
    public BDModule getModule(String name) {
        return modules.get(name.toLowerCase());
    }
    
    /**
     * Checks if a module is registered.
     *
     * @param name The module name
     * @return Whether the module is registered
     */
    public boolean hasModule(String name) {
        return modules.containsKey(name.toLowerCase());
    }
    
    /**
     * Gets all registered modules.
     *
     * @return All registered modules
     */
    public Map<String, BDModule> getModules() {
        return modules;
    }
}