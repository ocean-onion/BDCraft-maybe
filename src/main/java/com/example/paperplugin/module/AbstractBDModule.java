package com.example.paperplugin.module;

import com.example.paperplugin.PaperPlugin;

import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of BDModule that provides common functionality.
 * Modules can extend this class for easier implementation.
 */
public abstract class AbstractBDModule implements BDModule {

    protected final PaperPlugin plugin;
    protected final ModuleManager moduleManager;

    /**
     * Creates a new AbstractBDModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public AbstractBDModule(PaperPlugin plugin, ModuleManager moduleManager) {
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