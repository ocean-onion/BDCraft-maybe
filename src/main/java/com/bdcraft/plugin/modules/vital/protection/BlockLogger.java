package com.bdcraft.plugin.modules.vital.protection;

import com.bdcraft.plugin.BDCraft;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Handles logging of block changes for the protection system.
 */
public class BlockLogger {
    private final BDCraft plugin;
    private final Map<String, List<BlockAction>> blockActions;
    private final String dataFilePath;
    private boolean loggingEnabled;
    private int logRetentionDays;
    
    /**
     * Creates a new block logger.
     * @param plugin The plugin instance
     */
    public BlockLogger(BDCraft plugin) {
        this.plugin = plugin;
        this.blockActions = new ConcurrentHashMap<>();
        this.dataFilePath = plugin.getDataFolder() + "/data/bdvital/block_logs.json";
        
        // Create directories if they don't exist
        new File(plugin.getDataFolder() + "/data/bdvital").mkdirs();
        
        // Load configuration
        loadConfig();
        
        // Load logs
        loadLogs();
        
        // Schedule log cleanup
        scheduleCleanup();
    }
    
    /**
     * Loads the logger configuration.
     */
    private void loadConfig() {
        // Default values
        this.loggingEnabled = true;
        this.logRetentionDays = 30;
        
        // Get config values
        try {
            this.loggingEnabled = plugin.getConfig().getBoolean("bdvital.protection.log-blocks", true);
            this.logRetentionDays = plugin.getConfig().getInt("bdvital.protection.log-retention", 30);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load block logger configuration", e);
        }
    }
    
    /**
     * Loads logs from the data file.
     */
    private void loadLogs() {
        File file = new File(dataFilePath);
        
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<Map<String, List<BlockAction>>>(){}.getType();
            Map<String, List<BlockAction>> logs = gson.fromJson(reader, type);
            
            if (logs != null) {
                blockActions.putAll(logs);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load block logs", e);
        }
    }
    
    /**
     * Saves logs to the data file.
     */
    public void saveLogs() {
        if (!loggingEnabled) {
            return;
        }
        
        File file = new File(dataFilePath);
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(blockActions, writer);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save block logs", e);
        }
    }
    
    /**
     * Schedules a cleanup task for old logs.
     */
    private void scheduleCleanup() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::cleanupOldLogs, 
                20 * 60 * 60, // 1 hour delay
                20 * 60 * 60 * 24); // Run every 24 hours
    }
    
    /**
     * Cleans up old logs based on retention policy.
     */
    private void cleanupOldLogs() {
        if (!loggingEnabled) {
            return;
        }
        
        // Get current time
        long now = Instant.now().getEpochSecond();
        long cutoff = now - (logRetentionDays * 24 * 60 * 60);
        
        // Clean up logs
        int removed = 0;
        
        for (Map.Entry<String, List<BlockAction>> entry : blockActions.entrySet()) {
            List<BlockAction> actions = entry.getValue();
            int sizeBefore = actions.size();
            
            // Remove old actions
            actions.removeIf(action -> action.getTimestamp() < cutoff);
            
            // Update removed count
            removed += sizeBefore - actions.size();
            
            // Remove entry if no actions left
            if (actions.isEmpty()) {
                blockActions.remove(entry.getKey());
            }
        }
        
        // Log cleanup
        if (removed > 0) {
            plugin.getLogger().info("Cleaned up " + removed + " old block logs");
            
            // Save logs
            saveLogs();
        }
    }
    
    /**
     * Logs a block action.
     * @param player The player
     * @param block The block
     * @param action The action
     * @param oldMaterial The old material (for BREAK)
     * @param newMaterial The new material (for PLACE)
     */
    public void logAction(Player player, Block block, ActionType action, Material oldMaterial, Material newMaterial) {
        if (!loggingEnabled) {
            return;
        }
        
        // Create key
        String worldName = block.getWorld().getName();
        int chunkX = block.getX() >> 4;
        int chunkZ = block.getZ() >> 4;
        String key = worldName + ":" + chunkX + ":" + chunkZ;
        
        // Get or create actions list
        List<BlockAction> actions = blockActions.computeIfAbsent(key, k -> new ArrayList<>());
        
        // Create action
        BlockAction blockAction = new BlockAction(
                player.getUniqueId(),
                player.getName(),
                block.getX(),
                block.getY(),
                block.getZ(),
                worldName,
                action,
                oldMaterial != null ? oldMaterial.name() : null,
                newMaterial != null ? newMaterial.name() : null,
                Instant.now().getEpochSecond()
        );
        
        // Add action
        actions.add(blockAction);
        
        // Save logs periodically (every 5 minutes)
        if (actions.size() % 100 == 0) {
            saveLogs();
        }
    }
    
    /**
     * Gets block actions in an area.
     * @param worldName The world name
     * @param minX The minimum X coordinate
     * @param minY The minimum Y coordinate
     * @param minZ The minimum Z coordinate
     * @param maxX The maximum X coordinate
     * @param maxY The maximum Y coordinate
     * @param maxZ The maximum Z coordinate
     * @param since The timestamp to filter from (seconds since epoch)
     * @return The block actions
     */
    public List<BlockAction> getActionsInArea(String worldName, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, long since) {
        if (!loggingEnabled) {
            return new ArrayList<>();
        }
        
        // Calculate chunk ranges
        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;
        
        // Collect all relevant actions
        List<BlockAction> result = new ArrayList<>();
        
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                String key = worldName + ":" + chunkX + ":" + chunkZ;
                
                // Get actions for this chunk
                List<BlockAction> chunkActions = blockActions.get(key);
                
                if (chunkActions == null) {
                    continue;
                }
                
                // Filter actions
                for (BlockAction action : chunkActions) {
                    if (action.getWorldName().equals(worldName) &&
                            action.getX() >= minX && action.getX() <= maxX &&
                            action.getY() >= minY && action.getY() <= maxY &&
                            action.getZ() >= minZ && action.getZ() <= maxZ &&
                            action.getTimestamp() >= since) {
                        result.add(action);
                    }
                }
            }
        }
        
        // Sort by timestamp (newest first)
        result.sort(Comparator.comparingLong(BlockAction::getTimestamp).reversed());
        
        return result;
    }
    
    /**
     * Gets all actions by a player.
     * @param playerUuid The player UUID
     * @param since The timestamp to filter from (seconds since epoch)
     * @param limit The maximum number of actions to return
     * @return The block actions
     */
    public List<BlockAction> getActionsByPlayer(UUID playerUuid, long since, int limit) {
        if (!loggingEnabled) {
            return new ArrayList<>();
        }
        
        // Collect all relevant actions
        List<BlockAction> result = new ArrayList<>();
        
        for (List<BlockAction> actions : blockActions.values()) {
            // Filter actions
            for (BlockAction action : actions) {
                if (action.getPlayerUuid().equals(playerUuid) && action.getTimestamp() >= since) {
                    result.add(action);
                }
            }
        }
        
        // Sort by timestamp (newest first)
        result.sort(Comparator.comparingLong(BlockAction::getTimestamp).reversed());
        
        // Limit results
        if (result.size() > limit) {
            result = result.subList(0, limit);
        }
        
        return result;
    }
    
    /**
     * Gets all actions in a specific location.
     * @param location The location
     * @param since The timestamp to filter from (seconds since epoch)
     * @return The block actions
     */
    public List<BlockAction> getActionsAtLocation(Location location, long since) {
        if (!loggingEnabled) {
            return new ArrayList<>();
        }
        
        String worldName = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        
        // Calculate chunk key
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        String key = worldName + ":" + chunkX + ":" + chunkZ;
        
        // Get actions for this chunk
        List<BlockAction> chunkActions = blockActions.get(key);
        
        if (chunkActions == null) {
            return new ArrayList<>();
        }
        
        // Filter actions
        List<BlockAction> result = chunkActions.stream()
                .filter(action -> 
                        action.getWorldName().equals(worldName) &&
                        action.getX() == x &&
                        action.getY() == y &&
                        action.getZ() == z &&
                        action.getTimestamp() >= since)
                .sorted(Comparator.comparingLong(BlockAction::getTimestamp).reversed())
                .collect(Collectors.toList());
        
        return result;
    }
    
    /**
     * An enum for block action types.
     */
    public enum ActionType {
        BREAK,
        PLACE,
        INTERACT
    }
    
    /**
     * A class representing a block action.
     */
    public static class BlockAction {
        private final UUID playerUuid;
        private final String playerName;
        private final int x;
        private final int y;
        private final int z;
        private final String worldName;
        private final ActionType action;
        private final String oldMaterial;
        private final String newMaterial;
        private final long timestamp;
        
        /**
         * Creates a new block action.
         * @param playerUuid The player UUID
         * @param playerName The player name
         * @param x The X coordinate
         * @param y The Y coordinate
         * @param z The Z coordinate
         * @param worldName The world name
         * @param action The action
         * @param oldMaterial The old material
         * @param newMaterial The new material
         * @param timestamp The timestamp
         */
        public BlockAction(UUID playerUuid, String playerName, int x, int y, int z, String worldName, 
                ActionType action, String oldMaterial, String newMaterial, long timestamp) {
            this.playerUuid = playerUuid;
            this.playerName = playerName;
            this.x = x;
            this.y = y;
            this.z = z;
            this.worldName = worldName;
            this.action = action;
            this.oldMaterial = oldMaterial;
            this.newMaterial = newMaterial;
            this.timestamp = timestamp;
        }
        
        /**
         * Gets the player UUID.
         * @return The player UUID
         */
        public UUID getPlayerUuid() {
            return playerUuid;
        }
        
        /**
         * Gets the player name.
         * @return The player name
         */
        public String getPlayerName() {
            return playerName;
        }
        
        /**
         * Gets the X coordinate.
         * @return The X coordinate
         */
        public int getX() {
            return x;
        }
        
        /**
         * Gets the Y coordinate.
         * @return The Y coordinate
         */
        public int getY() {
            return y;
        }
        
        /**
         * Gets the Z coordinate.
         * @return The Z coordinate
         */
        public int getZ() {
            return z;
        }
        
        /**
         * Gets the world name.
         * @return The world name
         */
        public String getWorldName() {
            return worldName;
        }
        
        /**
         * Gets the action.
         * @return The action
         */
        public ActionType getAction() {
            return action;
        }
        
        /**
         * Gets the old material.
         * @return The old material
         */
        public String getOldMaterial() {
            return oldMaterial;
        }
        
        /**
         * Gets the new material.
         * @return The new material
         */
        public String getNewMaterial() {
            return newMaterial;
        }
        
        /**
         * Gets the timestamp.
         * @return The timestamp
         */
        public long getTimestamp() {
            return timestamp;
        }
    }
}