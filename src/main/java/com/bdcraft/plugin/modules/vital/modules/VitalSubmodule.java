package com.bdcraft.plugin.modules.vital.modules;

import com.bdcraft.plugin.modules.vital.BDVitalModule;

/**
 * Interface for all vital submodules.
 */
public interface VitalSubmodule {
    
    /**
     * Gets the name of this submodule.
     * 
     * @return The name
     */
    String getName();
    
    /**
     * Enables this submodule.
     * 
     * @param vitalModule The parent vital module
     */
    void enable(BDVitalModule vitalModule);
    
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