package com.bdcraft.plugin.modules;

/**
 * Base interface for all submodules.
 */
public interface SubmoduleBase {
    
    /**
     * Gets the name of this submodule.
     * 
     * @return The name
     */
    String getName();
    
    /**
     * Enables this submodule.
     * 
     * @param parentModule The parent module
     */
    void enable(ModuleManager parentModule);
    
    /**
     * Disables this submodule.
     */
    void disable();
    
    /**
     * Reloads this submodule.
     */
    void reload();
    
    /**
     * Checks if this submodule is enabled.
     * 
     * @return Whether this submodule is enabled
     */
    boolean isEnabled();
}