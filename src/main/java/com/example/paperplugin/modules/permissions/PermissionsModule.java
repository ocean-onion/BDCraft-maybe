package com.example.paperplugin.modules.permissions;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.module.AbstractBDModule;
import com.example.paperplugin.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Permissions module implementation for the BDCraft plugin.
 * Handles player groups, permissions, and ranks.
 */
public class PermissionsModule extends AbstractBDModule {

    private FileConfiguration permissionsConfig;
    private File permissionsConfigFile;
    private final Map<UUID, String> playerGroups;
    private final Map<String, Set<String>> groupPermissions;
    
    // Constants for the module
    private static final String MODULE_NAME = "perms";
    private static final String CONFIG_FILE_NAME = "permissions.yml";

    /**
     * Creates a new PermissionsModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public PermissionsModule(PaperPlugin plugin, ModuleManager moduleManager) {
        super(plugin, moduleManager);
        this.playerGroups = new HashMap<>();
        this.groupPermissions = new HashMap<>();
    }

    /**
     * Called when the module is enabled.
     * Initializes the permissions system.
     */
    @Override
    public void onEnable() {
        plugin.getLogger().info("Enabling Permissions Module...");
        
        // Load permissions configuration
        loadPermissionsConfig();
        
        // Load groups and permissions
        loadGroups();
        
        // Load player assignments
        loadPlayerGroups();
        
        // Register the permissions service
        registerPermissionsService();
        
        plugin.getLogger().info("Permissions Module enabled successfully!");
    }

    /**
     * Called when the module is disabled.
     * Saves all permissions data.
     */
    @Override
    public void onDisable() {
        plugin.getLogger().info("Disabling Permissions Module...");
        
        // Save player groups
        savePlayerGroups();
        
        plugin.getLogger().info("Permissions Module disabled successfully!");
    }

    /**
     * Called when the module is reloaded.
     * Reloads permissions configuration.
     */
    @Override
    public void onReload() {
        plugin.getLogger().info("Reloading Permissions Module...");
        
        // Reload permissions configuration
        reloadPermissionsConfig();
        
        // Reload groups and permissions
        loadGroups();
        
        // Reload player assignments
        loadPlayerGroups();
        
        plugin.getLogger().info("Permissions Module reloaded successfully!");
    }

    /**
     * Gets the module name.
     *
     * @return The module name
     */
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Gets the module dependencies.
     * 
     * @return List of module dependencies
     */
    @Override
    public List<String> getDependencies() {
        // No dependencies for this module
        return Collections.emptyList();
    }

    /**
     * Loads the permissions configuration.
     */
    private void loadPermissionsConfig() {
        permissionsConfigFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        
        if (!permissionsConfigFile.exists()) {
            plugin.saveResource(CONFIG_FILE_NAME, false);
        }
        
        permissionsConfig = YamlConfiguration.loadConfiguration(permissionsConfigFile);
        plugin.getLogger().info("Permissions configuration loaded!");
    }

    /**
     * Reloads the permissions configuration.
     */
    private void reloadPermissionsConfig() {
        permissionsConfig = YamlConfiguration.loadConfiguration(permissionsConfigFile);
        plugin.getLogger().info("Permissions configuration reloaded!");
    }

    /**
     * Saves the permissions configuration.
     */
    private void savePermissionsConfig() {
        try {
            permissionsConfig.save(permissionsConfigFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save permissions config!", e);
        }
    }

    /**
     * Loads all groups and their permissions from the config.
     */
    private void loadGroups() {
        // Clear existing groups
        groupPermissions.clear();
        
        // Load from config
        if (permissionsConfig.contains("groups")) {
            for (String groupName : permissionsConfig.getConfigurationSection("groups").getKeys(false)) {
                Set<String> permissions = new HashSet<>();
                
                List<String> permList = permissionsConfig.getStringList("groups." + groupName + ".permissions");
                permissions.addAll(permList);
                
                groupPermissions.put(groupName, permissions);
            }
        }
        
        plugin.getLogger().info("Loaded " + groupPermissions.size() + " permission groups");
    }

    /**
     * Loads player group assignments from the config.
     */
    private void loadPlayerGroups() {
        // Clear existing player groups
        playerGroups.clear();
        
        // Load from config
        if (permissionsConfig.contains("players")) {
            for (String uuidString : permissionsConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                String group = permissionsConfig.getString("players." + uuidString);
                playerGroups.put(uuid, group);
            }
        }
        
        plugin.getLogger().info("Loaded group assignments for " + playerGroups.size() + " players");
    }

    /**
     * Saves player group assignments to the config.
     */
    private void savePlayerGroups() {
        // Clear existing player entries
        permissionsConfig.set("players", null);
        
        // Save current player groups
        for (Map.Entry<UUID, String> entry : playerGroups.entrySet()) {
            permissionsConfig.set("players." + entry.getKey().toString(), entry.getValue());
        }
        
        savePermissionsConfig();
        plugin.getLogger().info("Saved group assignments for " + playerGroups.size() + " players");
    }

    /**
     * Registers the permissions service with the module manager.
     */
    private void registerPermissionsService() {
        // Create the permissions service implementation
        PermissionsService permissionsService = new PermissionsServiceImpl(this);
        
        // Register it with the module manager
        moduleManager.registerService(PermissionsService.class, permissionsService);
        
        plugin.getLogger().info("Permissions service registered!");
    }

    /**
     * Gets a player's group.
     *
     * @param playerId The player UUID
     * @return The player's group, or "default" if not assigned
     */
    public String getPlayerGroup(UUID playerId) {
        return playerGroups.getOrDefault(playerId, "default");
    }

    /**
     * Sets a player's group.
     *
     * @param playerId The player UUID
     * @param group The group name
     */
    public void setPlayerGroup(UUID playerId, String group) {
        // Check if the group exists
        if (!groupPermissions.containsKey(group)) {
            return;
        }
        
        playerGroups.put(playerId, group);
    }

    /**
     * Checks if a player has a specific permission.
     *
     * @param playerId The player UUID
     * @param permission The permission to check
     * @return true if the player has the permission
     */
    public boolean hasPermission(UUID playerId, String permission) {
        String group = getPlayerGroup(playerId);
        Set<String> permissions = groupPermissions.getOrDefault(group, Collections.emptySet());
        
        return permissions.contains(permission) || permissions.contains("*");
    }

    /**
     * Gets the permissions for a group.
     *
     * @param group The group name
     * @return Set of permissions for the group
     */
    public Set<String> getGroupPermissions(String group) {
        return new HashSet<>(groupPermissions.getOrDefault(group, Collections.emptySet()));
    }

    /**
     * Gets all available groups.
     *
     * @return Set of group names
     */
    public Set<String> getGroups() {
        return new HashSet<>(groupPermissions.keySet());
    }
}