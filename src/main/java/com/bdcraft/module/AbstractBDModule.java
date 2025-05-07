package com.bdcraft.module;

import com.bdcraft.BDCraftPlugin;

import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of BDModule that provides common functionality.
 * Modules can extend this class for easier implementation.
 */
public abstract class AbstractBDModule implements BDModule {

    protected final BDCraftPlugin plugin;
    protected final ModuleManager moduleManager;

    /**
     * Creates a new AbstractBDModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public AbstractBDModule(BDCraftPlugin plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
    }

    @Override
    public void onEnable() {
        // Default implementation
    }

    @Override
    public void onDisable() {
        // Default implementation
    }

    @Override
    public void onReload() {
        // Default implementation
    }

    @Override
    public List<String> getDependencies() {
        // By default, no dependencies
        return Collections.emptyList();
    }
}