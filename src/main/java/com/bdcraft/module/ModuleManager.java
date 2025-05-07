package com.bdcraft.module;

import com.bdcraft.BDCraftPlugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the loading, enabling, and disabling of modules.
 * Handles dependencies between modules and ensures they are loaded in the correct order.
 */
public class ModuleManager {

    private final BDCraftPlugin plugin;
    private final Map<String, BDModule> modules;
    private final List<String> moduleLoadOrder;

    /**
     * Creates a new ModuleManager.
     *
     * @param plugin The main plugin instance
     */
    public ModuleManager(BDCraftPlugin plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
        this.moduleLoadOrder = new ArrayList<>();
    }

    /**
     * Registers a module with the manager.
     *
     * @param module The module to register
     */
    public void registerModule(BDModule module) {
        modules.put(module.getName(), module);
        plugin.getLogger().info("Module registered: " + module.getName());
    }

    /**
     * Initializes all registered modules, resolving dependencies.
     */
    public void initializeModules() {
        plugin.getLogger().info("Resolving module dependencies...");
        
        // Clear existing load order
        moduleLoadOrder.clear();
        
        // Map of module names to their dependencies
        Map<String, List<String>> dependencyMap = new HashMap<>();
        
        // Populate dependency map
        for (BDModule module : modules.values()) {
            dependencyMap.put(module.getName(), module.getDependencies());
        }
        
        // Check for missing dependencies
        for (BDModule module : modules.values()) {
            for (String dependency : module.getDependencies()) {
                if (!modules.containsKey(dependency)) {
                    plugin.getLogger().warning("Module " + module.getName() + " depends on " + dependency + ", but it is not registered!");
                }
            }
        }
        
        // Resolve dependencies
        Set<String> visited = new HashSet<>();
        Set<String> currentPath = new HashSet<>();
        
        for (String moduleName : modules.keySet()) {
            if (!visited.contains(moduleName)) {
                resolveDependencies(moduleName, dependencyMap, visited, currentPath);
            }
        }
        
        plugin.getLogger().info("Module initialization completed. Load order: " + String.join(", ", moduleLoadOrder));
    }

    /**
     * Recursively resolves module dependencies using depth-first search.
     */
    private void resolveDependencies(String moduleName, Map<String, List<String>> dependencyMap, 
                                    Set<String> visited, Set<String> currentPath) {
        visited.add(moduleName);
        currentPath.add(moduleName);
        
        for (String dependency : dependencyMap.getOrDefault(moduleName, Collections.emptyList())) {
            if (!visited.contains(dependency)) {
                resolveDependencies(dependency, dependencyMap, visited, currentPath);
            } else if (currentPath.contains(dependency)) {
                // Circular dependency detected
                plugin.getLogger().severe("Circular dependency detected: " + moduleName + " -> " + dependency);
                throw new IllegalStateException("Circular dependency detected");
            }
        }
        
        currentPath.remove(moduleName);
        moduleLoadOrder.add(moduleName);
    }

    /**
     * Enables all modules in the correct dependency order.
     */
    public void enableModules() {
        plugin.getLogger().info("Enabling modules...");
        
        for (String moduleName : moduleLoadOrder) {
            BDModule module = modules.get(moduleName);
            if (module != null) {
                try {
                    plugin.getLogger().info("Enabling module: " + moduleName);
                    module.onEnable();
                } catch (Exception e) {
                    plugin.getLogger().severe("Error enabling module " + moduleName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        plugin.getLogger().info("All modules enabled successfully.");
    }

    /**
     * Disables all modules in reverse dependency order.
     */
    public void disableModules() {
        plugin.getLogger().info("Disabling modules...");
        
        // Disable in reverse order
        List<String> reverseOrder = new ArrayList<>(moduleLoadOrder);
        Collections.reverse(reverseOrder);
        
        for (String moduleName : reverseOrder) {
            BDModule module = modules.get(moduleName);
            if (module != null) {
                try {
                    plugin.getLogger().info("Disabling module: " + moduleName);
                    module.onDisable();
                } catch (Exception e) {
                    plugin.getLogger().severe("Error disabling module " + moduleName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        plugin.getLogger().info("All modules disabled successfully.");
    }

    /**
     * Reloads all modules in the correct dependency order.
     */
    public void reloadModules() {
        plugin.getLogger().info("Reloading modules...");
        
        for (String moduleName : moduleLoadOrder) {
            BDModule module = modules.get(moduleName);
            if (module != null) {
                try {
                    plugin.getLogger().info("Reloading module: " + moduleName);
                    module.onReload();
                } catch (Exception e) {
                    plugin.getLogger().severe("Error reloading module " + moduleName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        plugin.getLogger().info("All modules reloaded successfully.");
    }

    /**
     * Gets a module by name.
     *
     * @param moduleName The module name
     * @return The module, or null if not found
     */
    public BDModule getModule(String moduleName) {
        return modules.get(moduleName);
    }

    /**
     * Gets all registered modules.
     *
     * @return Collection of registered modules
     */
    public Collection<BDModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }
}