package com.bdcraft.plugin.api;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * API for interacting with the BDCraft permission system.
 */
public interface PermissionAPI {
    /**
     * Checks if a player has a specific permission.
     * 
     * @param player The player
     * @param permission The permission to check
     * @return True if the player has the permission
     */
    boolean hasPermission(Player player, String permission);
    
    /**
     * Gets the permission group a player belongs to.
     * 
     * @param player The player
     * @return The group name
     */
    String getGroup(Player player);
    
    /**
     * Sets a player's permission group.
     * 
     * @param player The player
     * @param group The group name
     * @return True if the operation was successful
     */
    boolean setGroup(Player player, String group);
    
    /**
     * Checks if a group exists.
     * 
     * @param group The group name
     * @return True if the group exists
     */
    boolean groupExists(String group);
    
    /**
     * Creates a new permission group.
     * 
     * @param group The group name
     * @param parentGroup The parent group (can be null)
     * @return True if the group was created successfully
     */
    boolean createGroup(String group, String parentGroup);
    
    /**
     * Adds a permission to a group.
     * 
     * @param group The group name
     * @param permission The permission to add
     * @return True if the permission was added successfully
     */
    boolean addGroupPermission(String group, String permission);
    
    /**
     * Removes a permission from a group.
     * 
     * @param group The group name
     * @param permission The permission to remove
     * @return True if the permission was removed successfully
     */
    boolean removeGroupPermission(String group, String permission);
    
    /**
     * Gets all permissions for a group.
     * 
     * @param group The group name
     * @return A list of permissions
     */
    List<String> getGroupPermissions(String group);
    
    /**
     * Gets all permissions for a player, including inherited permissions.
     * 
     * @param player The player
     * @return A list of permissions
     */
    List<String> getPlayerPermissions(Player player);
    
    /**
     * Gets the prefix for a group.
     * 
     * @param group The group name
     * @return The group prefix
     */
    String getGroupPrefix(String group);
    
    /**
     * Sets the prefix for a group.
     * 
     * @param group The group name
     * @param prefix The group prefix
     * @return True if the prefix was set successfully
     */
    boolean setGroupPrefix(String group, String prefix);
    
    /**
     * Gets the suffix for a group.
     * 
     * @param group The group name
     * @return The group suffix
     */
    String getGroupSuffix(String group);
    
    /**
     * Sets the suffix for a group.
     * 
     * @param group The group name
     * @param suffix The group suffix
     * @return True if the suffix was set successfully
     */
    boolean setGroupSuffix(String group, String suffix);
}