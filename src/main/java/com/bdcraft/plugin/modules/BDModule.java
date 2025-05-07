package com.bdcraft.plugin.modules;

import java.util.List;

/**
 * Interface for all BDCraft plugin modules.
 */
public interface BDModule {
    
    /**
     * Called when the module is enabled.
     */
    void onEnable();
    
    /**
     * Called when the module is disabled.
     */
    void onDisable();
    
    /**
     * Called when the module is reloaded.
     */
    void onReload();
    
    /**
     * Gets the name of the module.
     * @return The module name
     */
    String getName();
    
    /**
     * Gets the names of modules that this module depends on.
     * @return A list of module names
     */
    List<String> getDependencies();
}