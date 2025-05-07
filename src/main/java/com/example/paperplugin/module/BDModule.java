package com.example.paperplugin.module;

import java.util.List;

/**
 * Base interface for all BDCraft modules.
 * All modules must implement this interface to be managed by the ModuleManager.
 */
public interface BDModule {
    
    /**
     * Called when the module is enabled.
     * Initialize resources and register listeners here.
     */
    void onEnable();
    
    /**
     * Called when the module is disabled.
     * Clean up resources here.
     */
    void onDisable();
    
    /**
     * Called when the module is reloaded.
     * Apply configuration changes without restarting.
     */
    void onReload();
    
    /**
     * Gets the module name.
     * This name is used for dependency resolution and configuration.
     * 
     * @return The module name
     */
    String getName();
    
    /**
     * Gets the list of module dependencies.
     * The module will only be enabled after its dependencies.
     * 
     * @return A list of module names that this module depends on
     */
    List<String> getDependencies();
}