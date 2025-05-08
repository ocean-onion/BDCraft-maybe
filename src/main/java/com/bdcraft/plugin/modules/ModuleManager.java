package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages plugin modules.
 */
public class ModuleManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, Module> modules;
    
    /**
     * Creates a new module manager.
     * @param plugin The plugin instance
     */
    public ModuleManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.modules = new HashMap<>();
    }
    
    /**
     * Registers a module.
     * @param module The module
     */
    public void registerModule(Module module) {
        String name = module.getName();
        
        if (modules.containsKey(name)) {
            logger.warning("Module with name '" + name + "' already registered!");
            return;
        }
        
        modules.put(name, module);
    }
    
    /**
     * Gets a module by name.
     * @param name The module name
     * @return The module
     */
    public Module getModule(String name) {
        return modules.get(name);
    }
    
    /**
     * Gets a module by class.
     * @param moduleClass The module class
     * @return The module
     */
    public Module getModule(Class<? extends Module> moduleClass) {
        for (Module module : modules.values()) {
            if (moduleClass.isInstance(module)) {
                return module;
            }
        }
        
        return null;
    }
    
    /**
     * Gets all modules.
     * @return The modules
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules.values());
    }
    
    /**
     * Enables all modules.
     */
    public void enableModules() {
        for (Module module : modules.values()) {
            try {
                module.enable();
                logger.info("Enabled module: " + module.getName());
            } catch (Exception e) {
                logger.severe("Failed to enable module: " + module.getName());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Disables all modules.
     */
    public void disableModules() {
        for (Module module : modules.values()) {
            try {
                module.disable();
                logger.info("Disabled module: " + module.getName());
            } catch (Exception e) {
                logger.severe("Failed to disable module: " + module.getName());
                e.printStackTrace();
            }
        }
    }
}