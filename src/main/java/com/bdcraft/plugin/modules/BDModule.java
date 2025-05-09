package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;

/**
 * Base class for all BDCraft modules.
 */
public abstract class BDModule {
    protected final BDCraft plugin;
    private final String name;
    
    /**
     * Creates a new module.
     *
     * @param plugin The plugin instance
     * @param name The module name
     */
    public BDModule(BDCraft plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    /**
     * Gets the module name.
     *
     * @return The module name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Called when the module is enabled.
     */
    public abstract void onEnable();
    
    /**
     * Called when the module is disabled.
     */
    public abstract void onDisable();
    
    /**
     * Reloads the module data and configuration.
     */
    public void reload() {
        // Default implementation does nothing
    }
    
    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance
     */
    public BDCraft getPlugin() {
        return plugin;
    }
}