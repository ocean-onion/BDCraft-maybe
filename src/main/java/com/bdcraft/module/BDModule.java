package com.bdcraft.module;

import java.util.List;

/**
 * Interface for all BD modules.
 * Modules are self-contained components that can be enabled or disabled.
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
     * Gets the module name.
     * This should match the configuration key in config.yml.
     *
     * @return The module name
     */
    String getName();
    
    /**
     * Gets a list of module dependencies.
     * These modules must be loaded before this module.
     *
     * @return List of module names that this module depends on
     */
    List<String> getDependencies();
}