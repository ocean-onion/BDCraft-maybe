package com.bdcraft.plugin.modules.vital.warp;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Manages market warps.
 */
public class MarketWarpManager {
    private final BDCraft plugin;
    private final Map<UUID, MarketWarp> marketWarps;
    private final Map<UUID, Long> warpCooldowns;
    private final String dataFilePath;
    
    /**
     * Creates a new market warp manager.
     * @param plugin The plugin instance
     */
    public MarketWarpManager(BDCraft plugin) {
        this.plugin = plugin;
        this.marketWarps = new ConcurrentHashMap<>();
        this.warpCooldowns = new ConcurrentHashMap<>();
        this.dataFilePath = plugin.getDataFolder() + "/data/bdvital/warps.json";
        
        // Create directories if they don't exist
        new File(plugin.getDataFolder() + "/data/bdvital").mkdirs();
        
        // Load warps
        loadWarps();
    }
    
    /**
     * Loads market warps from the data file.
     */
    private void loadWarps() {
        File file = new File(dataFilePath);
        
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<List<MarketWarp>>(){}.getType();
            List<MarketWarp> warps = gson.fromJson(reader, type);
            
            if (warps != null) {
                for (MarketWarp warp : warps) {
                    marketWarps.put(warp.getMarketId(), warp);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load market warps", e);
        }
    }
    
    /**
     * Saves market warps to the data file.
     */
    public void saveWarps() {
        File file = new File(dataFilePath);
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<MarketWarp> warps = new ArrayList<>(marketWarps.values());
            gson.toJson(warps, writer);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save market warps", e);
        }
    }
    
    /**
     * Sets a market warp.
     * @param market The market
     * @param location The warp location
     * @return Whether the operation was successful
     */
    public boolean setWarp(Market market, Location location) {
        if (market == null) {
            return false;
        }
        
        // Check if market level allows warps
        if (market.getLevel() < 2) {
            return false;
        }
        
        // Create or update warp
        MarketWarp warp = new MarketWarp(market.getId(), location.clone());
        marketWarps.put(market.getId(), warp);
        
        // Save warps
        saveWarps();
        
        return true;
    }
    
    /**
     * Removes a market warp.
     * @param marketId The market ID
     * @return Whether the operation was successful
     */
    public boolean removeWarp(UUID marketId) {
        if (!marketWarps.containsKey(marketId)) {
            return false;
        }
        
        marketWarps.remove(marketId);
        
        // Save warps
        saveWarps();
        
        return true;
    }
    
    /**
     * Gets all market warps.
     * @return The market warps
     */
    public List<MarketWarp> getAllWarps() {
        return new ArrayList<>(marketWarps.values());
    }
    
    /**
     * Gets a market warp.
     * @param marketId The market ID
     * @return The market warp, or null if not found
     */
    public MarketWarp getWarp(UUID marketId) {
        return marketWarps.get(marketId);
    }
    
    /**
     * Gets the cooldown for a market warp based on market level.
     * @param marketLevel The market level
     * @return The cooldown in minutes
     */
    public int getWarpCooldown(int marketLevel) {
        // These values match the documentation
        switch (marketLevel) {
            case 2:
                return 60; // 1 hour cooldown
            case 3:
                return 30; // 30 minute cooldown
            case 4:
                return 15; // 15 minute cooldown
            default:
                return 60; // Default to 1 hour
        }
    }
    
    /**
     * Checks if a player is on cooldown for using warps.
     * @param playerId The player ID
     * @return Whether the player is on cooldown
     */
    public boolean isOnCooldown(UUID playerId) {
        if (!warpCooldowns.containsKey(playerId)) {
            return false;
        }
        
        long cooldownEnd = warpCooldowns.get(playerId);
        return System.currentTimeMillis() < cooldownEnd;
    }
    
    /**
     * Gets the remaining cooldown time for a player.
     * @param playerId The player ID
     * @return The remaining cooldown time in seconds, or 0 if not on cooldown
     */
    public int getRemainingCooldown(UUID playerId) {
        if (!warpCooldowns.containsKey(playerId)) {
            return 0;
        }
        
        long cooldownEnd = warpCooldowns.get(playerId);
        long remaining = cooldownEnd - System.currentTimeMillis();
        
        if (remaining <= 0) {
            warpCooldowns.remove(playerId);
            return 0;
        }
        
        return (int) (remaining / 1000);
    }
    
    /**
     * Sets a cooldown for a player.
     * @param playerId The player ID
     * @param marketLevel The market level
     */
    public void setCooldown(UUID playerId, int marketLevel) {
        int cooldownMinutes = getWarpCooldown(marketLevel);
        long cooldownEnd = System.currentTimeMillis() + (cooldownMinutes * 60 * 1000);
        warpCooldowns.put(playerId, cooldownEnd);
    }
    
    /**
     * A class representing a market warp.
     */
    public static class MarketWarp {
        private final UUID marketId;
        private String worldName;
        private double x;
        private double y;
        private double z;
        private float yaw;
        private float pitch;
        
        /**
         * Creates a new market warp.
         * @param marketId The market ID
         * @param location The warp location
         */
        public MarketWarp(UUID marketId, Location location) {
            this.marketId = marketId;
            this.worldName = location.getWorld().getName();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }
        
        /**
         * Gets the market ID.
         * @return The market ID
         */
        public UUID getMarketId() {
            return marketId;
        }
        
        /**
         * Gets the warp location.
         * @param plugin The plugin instance
         * @return The warp location, or null if the world doesn't exist
         */
        public Location getLocation(BDCraft plugin) {
            World world = plugin.getServer().getWorld(worldName);
            
            if (world == null) {
                return null;
            }
            
            return new Location(world, x, y, z, yaw, pitch);
        }
    }
}