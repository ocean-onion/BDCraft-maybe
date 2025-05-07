package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Manages plugin modules.
 */
public class ModuleManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, BDModule> modules;
    private final List<String> enabledModules;
    
    /**
     * Creates a new module manager.
     * @param plugin The plugin instance
     */
    public ModuleManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.modules = new HashMap<>();
        this.enabledModules = new ArrayList<>();
    }
    
    /**
     * Registers a module.
     * @param module The module to register
     */
    public void registerModule(BDModule module) {
        String name = module.getName();
        if (modules.containsKey(name)) {
            logger.warning("Tried to register module '" + name + "' twice!");
            return;
        }
        
        modules.put(name, module);
        logger.info("Registered module: " + name);
    }
    
    /**
     * Enables all registered modules in the correct order based on dependencies.
     */
    public void enableModules() {
        // Create a set of modules that still need to be enabled
        Set<String> toEnable = new HashSet<>(modules.keySet());
        
        // Track dependencies
        Map<String, Set<String>> moduleDependents = new HashMap<>();
        
        // Initialize tracking
        for (String moduleName : toEnable) {
            moduleDependents.put(moduleName, new HashSet<>());
        }
        
        // For each module that depends on another module, add it to the dependency map
        for (String moduleName : toEnable) {
            BDModule module = modules.get(moduleName);
            
            for (String dependency : module.getDependencies()) {
                if (!modules.containsKey(dependency)) {
                    logger.severe("Module '" + moduleName + "' has unmet dependency: " + dependency);
                    return;
                }
                
                // Add dependent
                moduleDependents.get(dependency).add(moduleName);
            }
        }
        
        // Enable modules in order
        enableModulesRecursive(toEnable, moduleDependents);
    }
    
    /**
     * Enables modules recursively in the correct order.
     * @param toEnable Modules that need to be enabled
     * @param moduleDependents Map of modules to the modules that depend on them
     */
    private void enableModulesRecursive(Set<String> toEnable, Map<String, Set<String>> moduleDependents) {
        if (toEnable.isEmpty()) {
            return;
        }
        
        // Find modules that have no dependencies
        Set<String> toEnableNow = new HashSet<>();
        
        for (String moduleName : toEnable) {
            BDModule module = modules.get(moduleName);
            
            boolean canEnable = true;
            for (String dependency : module.getDependencies()) {
                if (toEnable.contains(dependency)) {
                    canEnable = false;
                    break;
                }
            }
            
            if (canEnable) {
                toEnableNow.add(moduleName);
            }
        }
        
        if (toEnableNow.isEmpty()) {
            logger.severe("Circular dependency detected in modules!");
            return;
        }
        
        // Enable the modules
        for (String moduleName : toEnableNow) {
            enableModule(moduleName);
            toEnable.remove(moduleName);
        }
        
        // Continue with the rest
        enableModulesRecursive(toEnable, moduleDependents);
    }
    
    /**
     * Enables a specific module.
     * @param moduleName The module name
     */
    private void enableModule(String moduleName) {
        BDModule module = modules.get(moduleName);
        if (module == null) {
            logger.warning("Tried to enable unknown module: " + moduleName);
            return;
        }
        
        if (enabledModules.contains(moduleName)) {
            return; // Already enabled
        }
        
        // Enable dependencies first
        for (String dependency : module.getDependencies()) {
            enableModule(dependency);
        }
        
        // Enable the module
        try {
            module.onEnable();
            enabledModules.add(moduleName);
            logger.info("Enabled module: " + moduleName);
        } catch (Exception e) {
            logger.severe("Failed to enable module '" + moduleName + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Disables all enabled modules in reverse order of dependencies.
     */
    public void disableModules() {
        // Disable modules in reverse order
        for (int i = enabledModules.size() - 1; i >= 0; i--) {
            String moduleName = enabledModules.get(i);
            BDModule module = modules.get(moduleName);
            
            try {
                module.onDisable();
                logger.info("Disabled module: " + moduleName);
            } catch (Exception e) {
                logger.severe("Failed to disable module '" + moduleName + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        enabledModules.clear();
    }
    
    /**
     * Reloads all enabled modules.
     */
    public void reloadModules() {
        logger.info("Reloading modules...");
        
        // Reload each module
        for (String moduleName : enabledModules) {
            BDModule module = modules.get(moduleName);
            
            try {
                module.onReload();
                logger.info("Reloaded module: " + moduleName);
            } catch (Exception e) {
                logger.severe("Failed to reload module '" + moduleName + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Gets a module by name.
     * @param name The module name
     * @return The module, or null if it doesn't exist
     */
    public BDModule getModule(String name) {
        return modules.get(name);
    }
    
    /**
     * Gets a list of all registered modules.
     * @return The registered modules
     */
    public List<String> getRegisteredModules() {
        return new ArrayList<>(modules.keySet());
    }
    
    /**
     * Gets a list of all enabled modules.
     * @return The enabled modules
     */
    public List<String> getEnabledModules() {
        return new ArrayList<>(enabledModules);
    }
}