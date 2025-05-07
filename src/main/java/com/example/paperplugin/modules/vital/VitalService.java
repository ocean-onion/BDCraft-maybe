package com.example.paperplugin.modules.vital;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface for the vital service.
 * Provides methods for other modules to interact with the vital system.
 */
public interface VitalService {
    
    /**
     * Sets the server spawn location.
     * 
     * @param location The spawn location
     */
    void setSpawn(Location location);
    
    /**
     * Gets the server spawn location.
     * 
     * @return The spawn location
     */
    Location getSpawn();
    
    /**
     * Teleports a player to spawn.
     * 
     * @param player The player to teleport
     * @return true if successful
     */
    boolean teleportToSpawn(Player player);
    
    /**
     * Sets a home for a player.
     * 
     * @param player The player
     * @param homeName The home name
     * @param location The home location
     * @return true if successful
     */
    boolean setHome(Player player, String homeName, Location location);
    
    /**
     * Gets a player's home location.
     * 
     * @param player The player
     * @param homeName The home name
     * @return The home location, or null if not found
     */
    Location getHome(Player player, String homeName);
    
    /**
     * Gets all of a player's homes.
     * 
     * @param player The player
     * @return Map of home names to locations
     */
    Map<String, Location> getHomes(Player player);
    
    /**
     * Gets a player's homes by UUID.
     * 
     * @param playerId The player UUID
     * @return Map of home names to locations
     */
    Map<String, Location> getHomes(UUID playerId);
    
    /**
     * Deletes a player's home.
     * 
     * @param player The player
     * @param homeName The home name
     * @return true if successful
     */
    boolean deleteHome(Player player, String homeName);
    
    /**
     * Sets a server warp point.
     * 
     * @param warpName The warp name
     * @param location The warp location
     * @return true if successful
     */
    boolean setWarp(String warpName, Location location);
    
    /**
     * Gets a warp location.
     * 
     * @param warpName The warp name
     * @return The warp location, or null if not found
     */
    Location getWarp(String warpName);
    
    /**
     * Gets all server warps.
     * 
     * @return Map of warp names to locations
     */
    Map<String, Location> getWarps();
    
    /**
     * Gets all warp names.
     * 
     * @return List of warp names
     */
    List<String> getWarpNames();
    
    /**
     * Deletes a server warp.
     * 
     * @param warpName The warp name
     * @return true if successful
     */
    boolean deleteWarp(String warpName);
    
    /**
     * Teleports a player to a warp.
     * 
     * @param player The player
     * @param warpName The warp name
     * @return true if successful
     */
    boolean teleportToWarp(Player player, String warpName);
    
    /**
     * Teleports a player to a home.
     * 
     * @param player The player
     * @param homeName The home name
     * @return true if successful
     */
    boolean teleportToHome(Player player, String homeName);
}