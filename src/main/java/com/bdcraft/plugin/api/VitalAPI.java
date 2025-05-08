package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * API for core plugin utilities and features.
 * This API provides access to vital plugin functionality.
 */
public interface VitalAPI {
    /**
     * Sends a formatted message to a player.
     * @param player The player
     * @param message The message
     */
    void sendMessage(Player player, String message);
    
    /**
     * Broadcasts a message to all players.
     * @param message The message
     */
    void broadcastMessage(String message);
    
    /**
     * Gets a configuration value.
     * @param path The path to the value
     * @return The value, or null if not found
     */
    Object getConfigValue(String path);
    
    /**
     * Sets a configuration value.
     * @param path The path to the value
     * @param value The value
     */
    void setConfigValue(String path, Object value);
    
    /**
     * Gets online players with a specific permission.
     * @param permission The permission
     * @return List of players with the permission
     */
    List<Player> getPlayersWithPermission(String permission);
    
    /**
     * Teleports a player safely to a location.
     * @param player The player
     * @param location The location
     * @return Whether the teleport was successful
     */
    boolean safeTeleport(Player player, Location location);
    
    /**
     * Gives an item to a player safely (handles full inventory).
     * @param player The player
     * @param item The item
     * @return Whether the item was given successfully
     */
    boolean giveItem(Player player, ItemStack item);
    
    /**
     * Logs a message to the console with the plugin prefix.
     * @param message The message
     * @param severe Whether this is a severe error message
     */
    void logMessage(String message, boolean severe);
    
    /**
     * Checks if a player is in the plugin's database.
     * @param uuid The player's UUID
     * @return Whether the player exists in the database
     */
    boolean playerExists(UUID uuid);
    
    /**
     * Creates a player in the plugin's database.
     * @param uuid The player's UUID
     * @param name The player's name
     */
    void createPlayer(UUID uuid, String name);
    
    /**
     * Gets a list of all registered players.
     * @return List of player UUIDs
     */
    List<UUID> getRegisteredPlayers();
    
    /**
     * Gets the last login time of a player.
     * @param uuid The player's UUID
     * @return The last login time in milliseconds, or -1 if not found
     */
    long getLastLogin(UUID uuid);
    
    /**
     * Updates the last login time of a player.
     * @param uuid The player's UUID
     */
    void updateLastLogin(UUID uuid);
    
    /**
     * Checks if the plugin is in debug mode.
     * @return Whether debug mode is enabled
     */
    boolean isDebugMode();
    
    /**
     * Sets the plugin's debug mode.
     * @param enabled Whether debug mode should be enabled
     */
    void setDebugMode(boolean enabled);
}