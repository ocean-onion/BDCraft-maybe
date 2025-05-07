package com.bdcraft.plugin.modules;

import java.util.List;

/**
 * Interface for BDCraft modules.
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
     * Called when the plugin is reloaded.
     */
    void onReload();
    
    /**
     * Gets the name of the module.
     * @return The module name
     */
    String getName();
    
    /**
     * Gets the module's dependencies.
     * @return The module dependencies
     */
    List<String> getDependencies();
}