package com.example.paperplugin.modules.permissions;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

/**
 * Interface for the permissions service.
 * Provides methods for other modules to interact with the permissions system.
 */
public interface PermissionsService {
    
    /**
     * Checks if a player has a specific permission.
     * 
     * @param player The player
     * @param permission The permission to check
     * @return true if the player has the permission
     */
    boolean hasPermission(Player player, String permission);
    
    /**
     * Checks if a player has a specific permission by UUID.
     * 
     * @param playerId The player UUID
     * @param permission The permission to check
     * @return true if the player has the permission
     */
    boolean hasPermission(UUID playerId, String permission);
    
    /**
     * Gets a player's group.
     * 
     * @param player The player
     * @return The player's group name
     */
    String getGroup(Player player);
    
    /**
     * Gets a player's group by UUID.
     * 
     * @param playerId The player UUID
     * @return The player's group name
     */
    String getGroup(UUID playerId);
    
    /**
     * Sets a player's group.
     * 
     * @param player The player
     * @param group The group name
     * @return true if successful
     */
    boolean setGroup(Player player, String group);
    
    /**
     * Sets a player's group by UUID.
     * 
     * @param playerId The player UUID
     * @param group The group name
     * @return true if successful
     */
    boolean setGroup(UUID playerId, String group);
    
    /**
     * Gets all available groups.
     * 
     * @return Set of group names
     */
    Set<String> getGroups();
    
    /**
     * Gets the permissions for a group.
     * 
     * @param group The group name
     * @return Set of permissions for the group
     */
    Set<String> getGroupPermissions(String group);
}