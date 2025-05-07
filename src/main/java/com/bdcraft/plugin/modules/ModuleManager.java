package com.bdcraft.plugin.modules;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.perms.BDPermsModule;
import com.bdcraft.plugin.modules.vital.BDVitalModule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the loading and unloading of plugin modules.
 */
public class ModuleManager {
    
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, BDModule> modules;
    
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
     * Loads all enabled modules.
     */
    public void loadModules() {
        logger.info("Loading modules...");
        
        // Check which modules are enabled in the configuration
        boolean economyEnabled = plugin.getConfig().getBoolean("modules.economy", true);
        boolean permsEnabled = plugin.getConfig().getBoolean("modules.perms", true);
        boolean vitalEnabled = plugin.getConfig().getBoolean("modules.vital", true);
        
        // Create module instances
        if (permsEnabled) {
            registerModule(new BDPermsModule(plugin, this));
        }
        
        if (economyEnabled) {
            registerModule(new BDEconomyModule(plugin));
        }
        
        if (vitalEnabled) {
            registerModule(new BDVitalModule(plugin, this));
        }
        
        // Enable modules in dependency order
        enableModules();
        
        logger.info("Loaded " + modules.size() + " modules.");
    }
    
    /**
     * Unloads all modules.
     */
    public void unloadModules() {
        logger.info("Unloading modules...");
        
        // Disable modules in reverse dependency order
        disableModules();
        
        // Clear module cache
        modules.clear();
        
        logger.info("All modules unloaded.");
    }
    
    /**
     * Reloads all modules.
     */
    public void reloadModules() {
        logger.info("Reloading modules...");
        
        // Call onReload for each module
        for (BDModule module : modules.values()) {
            try {
                module.onReload();
                logger.info("Reloaded module: " + module.getName());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error reloading module: " + module.getName(), e);
            }
        }
        
        logger.info("All modules reloaded.");
    }
    
    /**
     * Registers a module.
     * @param module The module to register
     */
    public void registerModule(BDModule module) {
        modules.put(module.getName().toLowerCase(), module);
    }
    
    /**
     * Gets a module by name.
     * @param name The module name
     * @return The module, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T extends BDModule> T getModule(String name) {
        return (T) modules.get(name.toLowerCase());
    }
    
    /**
     * Enables modules in dependency order.
     */
    public void enableModules() {
        List<String> enabledModules = new LinkedList<>();
        
        // Keep trying to enable modules until no more can be enabled
        boolean progress = true;
        while (progress) {
            progress = false;
            
            for (BDModule module : new LinkedList<>(modules.values())) {
                // Skip already enabled modules
                if (enabledModules.contains(module.getName().toLowerCase())) {
                    continue;
                }
                
                // Check if all dependencies are enabled
                boolean dependenciesMet = true;
                for (String dependency : module.getDependencies()) {
                    if (!enabledModules.contains(dependency.toLowerCase())) {
                        dependenciesMet = false;
                        break;
                    }
                }
                
                // Enable module if dependencies are met
                if (dependenciesMet) {
                    try {
                        module.onEnable();
                        enabledModules.add(module.getName().toLowerCase());
                        progress = true;
                        logger.info("Enabled module: " + module.getName());
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error enabling module: " + module.getName(), e);
                        modules.remove(module.getName().toLowerCase());
                    }
                }
            }
        }
        
        // Check for unresolved dependencies
        for (BDModule module : new LinkedList<>(modules.values())) {
            if (!enabledModules.contains(module.getName().toLowerCase())) {
                logger.warning("Could not enable module " + module.getName() + " due to missing dependencies");
                logger.warning("Required dependencies: " + String.join(", ", module.getDependencies()));
                modules.remove(module.getName().toLowerCase());
            }
        }
    }
    
    /**
     * Disables modules in reverse dependency order.
     */
    public void disableModules() {
        // Create a list of modules ordered by dependencies
        List<String> moduleOrder = new LinkedList<>();
        List<String> visited = new LinkedList<>();
        
        // Helper function for topological sort
        class TopoSort {
            void visit(String moduleName) {
                if (visited.contains(moduleName)) {
                    return;
                }
                
                visited.add(moduleName);
                
                BDModule module = modules.get(moduleName.toLowerCase());
                if (module != null) {
                    for (String dependency : module.getDependencies()) {
                        if (modules.containsKey(dependency.toLowerCase())) {
                            visit(dependency.toLowerCase());
                        }
                    }
                    
                    moduleOrder.add(moduleName.toLowerCase());
                }
            }
        }
        
        // Perform topological sort
        TopoSort topoSort = new TopoSort();
        for (String moduleName : new LinkedList<>(modules.keySet())) {
            topoSort.visit(moduleName);
        }
        
        // Disable modules in reverse order
        for (int i = moduleOrder.size() - 1; i >= 0; i--) {
            String moduleName = moduleOrder.get(i);
            BDModule module = modules.get(moduleName);
            
            if (module != null) {
                try {
                    module.onDisable();
                    logger.info("Disabled module: " + module.getName());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error disabling module: " + module.getName(), e);
                }
            }
        }
    }
}