package com.bdcraft.plugin.api;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * API for permission operations.
 */
public interface PermissionAPI {
    /**
     * Checks if a player has a permission.
     * @param player The player
     * @param permission The permission
     * @return Whether the player has the permission
     */
    boolean hasPermission(Player player, String permission);
    
    /**
     * Gets a player's group.
     * @param player The player
     * @return The player's group
     */
    String getGroup(Player player);
    
    /**
     * Sets a player's group.
     * @param player The player
     * @param group The group
     * @return Whether the operation was successful
     */
    boolean setGroup(Player player, String group);
    
    /**
     * Checks if a group exists.
     * @param group The group
     * @return Whether the group exists
     */
    boolean groupExists(String group);
    
    /**
     * Creates a new group.
     * @param group The group name
     * @param parentGroup The parent group (can be null)
     * @return Whether the operation was successful
     */
    boolean createGroup(String group, String parentGroup);
    
    /**
     * Adds a permission to a group.
     * @param group The group
     * @param permission The permission
     * @return Whether the operation was successful
     */
    boolean addGroupPermission(String group, String permission);
    
    /**
     * Removes a permission from a group.
     * @param group The group
     * @param permission The permission
     * @return Whether the operation was successful
     */
    boolean removeGroupPermission(String group, String permission);
    
    /**
     * Gets a group's permissions.
     * @param group The group
     * @return The group's permissions
     */
    List<String> getGroupPermissions(String group);
    
    /**
     * Gets a player's permissions.
     * @param player The player
     * @return The player's permissions
     */
    List<String> getPlayerPermissions(Player player);
    
    /**
     * Gets a group's prefix.
     * @param group The group
     * @return The group's prefix
     */
    String getGroupPrefix(String group);
    
    /**
     * Sets a group's prefix.
     * @param group The group
     * @param prefix The prefix
     * @return Whether the operation was successful
     */
    boolean setGroupPrefix(String group, String prefix);
    
    /**
     * Gets a group's suffix.
     * @param group The group
     * @return The group's suffix
     */
    String getGroupSuffix(String group);
    
    /**
     * Sets a group's suffix.
     * @param group The group
     * @param suffix The suffix
     * @return Whether the operation was successful
     */
    boolean setGroupSuffix(String group, String suffix);
}