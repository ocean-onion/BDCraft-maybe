package com.bdcraft.plugin.modules;

import java.util.List;

/**
 * Interface for BDCraft modules.
 */
public interface BDModule extends Module {
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
     * Gets the module's dependencies.
     * @return The module dependencies
     */
    List<String> getDependencies();
    
    /**
     * Default implementation of Module.enable()
     */
    @Override
    default void enable() {
        onEnable();
    }
    
    /**
     * Default implementation of Module.disable()
     */
    @Override
    default void disable() {
        onDisable();
    }
}