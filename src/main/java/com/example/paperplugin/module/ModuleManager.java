package com.example.paperplugin.module;

import com.example.paperplugin.PaperPlugin;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Manages all modules in the plugin.
 * Handles module lifecycle, dependencies, and communication.
 */
public class ModuleManager {

    private final PaperPlugin plugin;
    private final Map<String, BDModule> modules;
    private final Map<Class<?>, Object> services;
    private List<String> moduleLoadOrder;
    private boolean initialized;

    /**
     * Creates a new ModuleManager.
     *
     * @param plugin The main plugin instance
     */
    public ModuleManager(PaperPlugin plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
        this.services = new HashMap<>();
        this.moduleLoadOrder = new ArrayList<>();
        this.initialized = false;
    }

    /**
     * Registers a module with the ModuleManager.
     *
     * @param module The module to register
     * @return true if the module was registered successfully, false otherwise
     */
    public boolean registerModule(BDModule module) {
        if (modules.containsKey(module.getName())) {
            plugin.getLogger().warning("Module with name " + module.getName() + " is already registered!");
            return false;
        }

        modules.put(module.getName(), module);
        plugin.getLogger().info("Registered module: " + module.getName());
        return true;
    }

    /**
     * Initializes all registered modules.
     * Resolves dependencies and creates a valid loading order.
     *
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeModules() {
        if (initialized) {
            plugin.getLogger().warning("ModuleManager is already initialized!");
            return false;
        }

        try {
            // Build dependency graph and determine load order
            moduleLoadOrder = calculateLoadOrder();
            initialized = true;
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize modules", e);
            return false;
        }
    }

    /**
     * Enables all modules in dependency order.
     *
     * @return true if all modules were enabled successfully, false otherwise
     */
    public boolean enableModules() {
        if (!initialized) {
            plugin.getLogger().warning("Cannot enable modules: ModuleManager is not initialized!");
            return false;
        }

        // Enable modules in dependency order
        for (String moduleName : moduleLoadOrder) {
            BDModule module = modules.get(moduleName);
            if (isModuleEnabled(moduleName)) {
                try {
                    plugin.getLogger().info("Enabling module: " + moduleName);
                    module.onEnable();
                    plugin.getLogger().info("Module enabled: " + moduleName);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to enable module: " + moduleName, e);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Disables all modules in reverse dependency order.
     */
    public void disableModules() {
        if (!initialized) {
            plugin.getLogger().warning("Cannot disable modules: ModuleManager is not initialized!");
            return;
        }

        // Disable modules in reverse dependency order
        List<String> reverseOrder = new ArrayList<>(moduleLoadOrder);
        Collections.reverse(reverseOrder);

        for (String moduleName : reverseOrder) {
            BDModule module = modules.get(moduleName);
            try {
                plugin.getLogger().info("Disabling module: " + moduleName);
                module.onDisable();
                plugin.getLogger().info("Module disabled: " + moduleName);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error disabling module: " + moduleName, e);
            }
        }
    }

    /**
     * Reloads all modules.
     */
    public void reloadModules() {
        if (!initialized) {
            plugin.getLogger().warning("Cannot reload modules: ModuleManager is not initialized!");
            return;
        }

        for (String moduleName : moduleLoadOrder) {
            BDModule module = modules.get(moduleName);
            if (isModuleEnabled(moduleName)) {
                try {
                    plugin.getLogger().info("Reloading module: " + moduleName);
                    module.onReload();
                    plugin.getLogger().info("Module reloaded: " + moduleName);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error reloading module: " + moduleName, e);
                }
            }
        }
    }

    /**
     * Gets a module by name.
     *
     * @param name The name of the module to get
     * @return The module, or null if not found
     */
    public BDModule getModule(String name) {
        return modules.get(name);
    }

    /**
     * Checks if a module is enabled in the configuration.
     *
     * @param moduleName The name of the module to check
     * @return true if the module is enabled, false otherwise
     */
    public boolean isModuleEnabled(String moduleName) {
        // Get from config with default of true
        return plugin.getConfig().getBoolean("modules." + moduleName, true);
    }

    /**
     * Registers a service for other modules to use.
     *
     * @param serviceClass The class of the service
     * @param service The service implementation
     * @param <T> The service type
     */
    public <T> void registerService(Class<T> serviceClass, T service) {
        services.put(serviceClass, service);
        plugin.getLogger().info("Registered service: " + serviceClass.getSimpleName());
    }

    /**
     * Gets a service by its class.
     *
     * @param serviceClass The class of the service to get
     * @param <T> The service type
     * @return The service implementation, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    /**
     * Calculates the module loading order based on dependencies.
     * Uses a topological sort to respect dependencies.
     *
     * @return A list of module names in loading order
     * @throws IllegalStateException if there is a circular dependency
     */
    private List<String> calculateLoadOrder() throws IllegalStateException {
        // Create a graph of dependencies
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegrees = new HashMap<>();

        // Initialize graph and in-degrees
        for (BDModule module : modules.values()) {
            String moduleName = module.getName();
            graph.put(moduleName, new HashSet<>());
            inDegrees.put(moduleName, 0);
        }

        // Build the graph
        for (BDModule module : modules.values()) {
            String moduleName = module.getName();
            List<String> dependencies = module.getDependencies();

            for (String dependency : dependencies) {
                // Check if dependency exists
                if (!modules.containsKey(dependency)) {
                    plugin.getLogger().warning("Module " + moduleName + " depends on unknown module: " + dependency);
                    continue;
                }

                // Add edge in graph
                graph.get(dependency).add(moduleName);
                inDegrees.put(moduleName, inDegrees.get(moduleName) + 1);
            }
        }

        // Perform topological sort
        List<String> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        // Add all nodes with no dependencies to the queue
        for (Map.Entry<String, Integer> entry : inDegrees.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Update in-degrees of neighbors
            for (String neighbor : graph.get(current)) {
                int newDegree = inDegrees.get(neighbor) - 1;
                inDegrees.put(neighbor, newDegree);

                if (newDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Check for circular dependencies
        if (result.size() != modules.size()) {
            String circularDeps = inDegrees.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("Circular dependencies detected among modules: " + circularDeps);
        }

        return result;
    }
}