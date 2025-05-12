package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;

/**
 * Interface for all main module managers.
 */
public interface ModuleManager {
    
    /**
     * Gets the name of this module.
     * 
     * @return The name
     */
    String getName();
    
    /**
     * Enables this module.
     * 
     * @param plugin The plugin instance
     */
    void enable(BDCraft plugin);
    
    /**
     * Disables this module.
     */
    void disable();
    
    /**
     * Reloads this module.
     */
    void reload();
    
    /**
     * Checks if this module is enabled.
     * 
     * @return Whether this module is enabled
     */
    boolean isEnabled();
    
    /**
     * Gets a submodule by name.
     * 
     * @param name The name of the submodule
     * @return The submodule, or null if not found
     */
    Object getSubmodule(String name);
    
    /**
     * Registers a submodule.
     * 
     * @param submodule The submodule to register
     */
    void registerSubmodule(Object submodule);
}