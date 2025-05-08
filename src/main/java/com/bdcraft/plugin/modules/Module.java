package com.bdcraft.plugin.modules;

/**
 * Interface for plugin modules.
 */
public interface Module {
    /**
     * Gets the name of the module.
     * @return The module name
     */
    String getName();
    
    /**
     * Enables the module.
     */
    void enable();
    
    /**
     * Disables the module.
     */
    void disable();
}