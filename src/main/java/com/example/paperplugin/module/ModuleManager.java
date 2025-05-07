package com.example.paperplugin.module;

import com.example.paperplugin.PaperPlugin;

import java.util.*;
import java.util.logging.Level;

/**
 * Manages all BD modules.
 * Handles loading, enabling, disabling, and reloading modules.
 */
public class ModuleManager {
    
    private final PaperPlugin plugin;
    private final Map<String, BDModule> modules;
    private final Map<Class<?>, Object> services;
    private final Set<String> enabledModules;
    
    /**
     * Creates a new ModuleManager.
     *
     * @param plugin The main plugin instance
     */
    public ModuleManager(PaperPlugin plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
        this.services = new HashMap<>();
        this.enabledModules = new HashSet<>();
    }
    
    /**
     * Registers a module with the module manager.
     *
     * @param module The module to register
     */
    public void registerModule(BDModule module) {
        modules.put(module.getName(), module);
        plugin.getLogger().info("Registered module: " + module.getName());
    }
    
    /**
     * Initializes all registered modules.
     * This checks the configuration to determine which modules should be enabled.
     */
    public void initializeModules() {
        plugin.getLogger().info("Initializing modules...");
        
        // Clear the enabled modules set
        enabledModules.clear();
        
        // Check which modules are enabled in the config
        if (plugin.getConfig().contains("modules")) {
            for (String moduleName : modules.keySet()) {
                if (plugin.getConfig().getBoolean("modules." + moduleName, false)) {
                    enabledModules.add(moduleName);
                    plugin.getLogger().info("Module '" + moduleName + "' is enabled in config");
                } else {
                    plugin.getLogger().info("Module '" + moduleName + "' is disabled in config");
                }
            }
        }
        
        plugin.getLogger().info("Modules initialized successfully!");
    }
    
    /**
     * Enables all modules that are enabled in the configuration.
     * Modules are enabled in dependency order.
     */
    public void enableModules() {
        plugin.getLogger().info("Enabling modules...");
        
        // Sort modules by dependency
        List<String> sortedModules = sortModulesByDependency(enabledModules);
        
        // Enable modules in order
        for (String moduleName : sortedModules) {
            BDModule module = modules.get(moduleName);
            
            try {
                module.onEnable();
                plugin.getLogger().info("Enabled module: " + moduleName);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to enable module: " + moduleName, e);
                enabledModules.remove(moduleName);
            }
        }
        
        plugin.getLogger().info("Modules enabled successfully!");
    }
    
    /**
     * Disables all enabled modules.
     * Modules are disabled in reverse dependency order.
     */
    public void disableModules() {
        plugin.getLogger().info("Disabling modules...");
        
        // Sort modules by dependency
        List<String> sortedModules = sortModulesByDependency(enabledModules);
        
        // Disable modules in reverse order
        Collections.reverse(sortedModules);
        
        for (String moduleName : sortedModules) {
            BDModule module = modules.get(moduleName);
            
            try {
                module.onDisable();
                plugin.getLogger().info("Disabled module: " + moduleName);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to disable module: " + moduleName, e);
            }
        }
        
        plugin.getLogger().info("Modules disabled successfully!");
    }
    
    /**
     * Reloads all enabled modules.
     */
    public void reloadModules() {
        plugin.getLogger().info("Reloading modules...");
        
        // Sort modules by dependency
        List<String> sortedModules = sortModulesByDependency(enabledModules);
        
        // Reload modules in order
        for (String moduleName : sortedModules) {
            BDModule module = modules.get(moduleName);
            
            try {
                module.onReload();
                plugin.getLogger().info("Reloaded module: " + moduleName);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to reload module: " + moduleName, e);
            }
        }
        
        plugin.getLogger().info("Modules reloaded successfully!");
    }
    
    /**
     * Gets a module by name.
     *
     * @param name The module name
     * @return The module, or null if not found
     */
    public BDModule getModule(String name) {
        return modules.get(name);
    }
    
    /**
     * Gets all registered modules.
     *
     * @return Map of module names to modules
     */
    public Map<String, BDModule> getModules() {
        return new HashMap<>(modules);
    }
    
    /**
     * Gets the names of all enabled modules.
     *
     * @return Set of enabled module names
     */
    public Set<String> getEnabledModules() {
        return new HashSet<>(enabledModules);
    }
    
    /**
     * Checks if a module is enabled.
     *
     * @param name The module name
     * @return true if the module is enabled
     */
    public boolean isModuleEnabled(String name) {
        return enabledModules.contains(name);
    }
    
    /**
     * Sorts modules by dependency order.
     * Modules with no dependencies come first, followed by modules whose dependencies are satisfied.
     *
     * @param moduleNames The set of module names to sort
     * @return Sorted list of module names
     */
    private List<String> sortModulesByDependency(Set<String> moduleNames) {
        // Create a copy of the module names set
        Set<String> remaining = new HashSet<>(moduleNames);
        List<String> sorted = new ArrayList<>();
        
        // Continue until all modules are sorted
        while (!remaining.isEmpty()) {
            boolean progress = false;
            
            // Find modules whose dependencies are satisfied
            Iterator<String> iterator = remaining.iterator();
            while (iterator.hasNext()) {
                String moduleName = iterator.next();
                BDModule module = modules.get(moduleName);
                
                // Check if all dependencies are already in the sorted list
                boolean dependenciesSatisfied = true;
                for (String dependency : module.getDependencies()) {
                    if (!sorted.contains(dependency)) {
                        dependenciesSatisfied = false;
                        break;
                    }
                }
                
                // If dependencies are satisfied, add to sorted list
                if (dependenciesSatisfied) {
                    sorted.add(moduleName);
                    iterator.remove();
                    progress = true;
                }
            }
            
            // If no progress was made, there might be a circular dependency
            if (!progress) {
                plugin.getLogger().warning("Possible circular dependency detected in modules: " + remaining);
                
                // Add the remaining modules to the sorted list
                sorted.addAll(remaining);
                remaining.clear();
            }
        }
        
        return sorted;
    }
    
    /**
     * Registers a service with the module manager.
     * Services are shared components that can be used by multiple modules.
     *
     * @param serviceClass The service interface class
     * @param implementation The service implementation
     * @param <T> The service type
     */
    public <T> void registerService(Class<T> serviceClass, T implementation) {
        services.put(serviceClass, implementation);
        plugin.getLogger().info("Registered service: " + serviceClass.getSimpleName());
    }
    
    /**
     * Gets a service by class.
     *
     * @param serviceClass The service interface class
     * @param <T> The service type
     * @return The service implementation, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }
}