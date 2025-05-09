package com.bdcraft.plugin.cache;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Manages caches for the plugin.
 * This manager ensures caches are cleaned up regularly and provides
 * centralized access to different caches used by the plugin.
 */
public class BDCacheManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, BDCache<?, ?>> caches = new HashMap<>();
    private BukkitTask cleanupTask;
    
    // Common cache names
    public static final String PLAYER_DATA_CACHE = "player_data";
    public static final String MARKET_CACHE = "market_data";
    public static final String ITEM_CACHE = "item_data";
    public static final String PERMISSION_CACHE = "permission_data";
    public static final String VILLAGER_CACHE = "villager_data";
    
    /**
     * Creates a new cache manager.
     * 
     * @param plugin The plugin instance
     */
    public BDCacheManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        
        // Initialize default caches
        initializeDefaultCaches();
        
        // Schedule cleanup task
        scheduleCleanupTask();
    }
    
    /**
     * Initializes default caches.
     */
    private void initializeDefaultCaches() {
        // Player data cache (5 minutes, concurrent)
        registerCache(PLAYER_DATA_CACHE, new BDCache<>(
                TimeUnit.MINUTES.toMillis(5), true));
        
        // Market data cache (2 minutes, concurrent)
        registerCache(MARKET_CACHE, new BDCache<>(
                TimeUnit.MINUTES.toMillis(2), true));
        
        // Item data cache (10 minutes, non-concurrent)
        registerCache(ITEM_CACHE, new BDCache<>(
                TimeUnit.MINUTES.toMillis(10), false));
        
        // Permission cache (1 minute, concurrent)
        registerCache(PERMISSION_CACHE, new BDCache<>(
                TimeUnit.MINUTES.toMillis(1), true));
        
        // Villager data cache (3 minutes, concurrent)
        registerCache(VILLAGER_CACHE, new BDCache<>(
                TimeUnit.MINUTES.toMillis(3), true));
    }
    
    /**
     * Schedules a task to clean up expired cache entries.
     */
    private void scheduleCleanupTask() {
        // Clean up caches every 5 minutes
        cleanupTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::cleanupAllCaches,
                6000, // 5 minutes (in ticks)
                6000  // 5 minutes (in ticks)
        );
    }
    
    /**
     * Registers a cache.
     * 
     * @param name The cache name
     * @param cache The cache
     */
    public void registerCache(String name, BDCache<?, ?> cache) {
        caches.put(name, cache);
    }
    
    /**
     * Gets a cache by name.
     * 
     * @param <K> The key type
     * @param <V> The value type
     * @param name The cache name
     * @return The cache, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <K, V> BDCache<K, V> getCache(String name) {
        return (BDCache<K, V>) caches.get(name);
    }
    
    /**
     * Cleans up all caches.
     */
    public void cleanupAllCaches() {
        caches.forEach((name, cache) -> {
            int before = cache.size();
            cache.cleanup();
            int after = cache.size();
            
            if (before != after) {
                logger.fine("Cleaned up " + (before - after) + " entries from " + name + " cache");
            }
        });
    }
    
    /**
     * Clears all caches.
     */
    public void clearAllCaches() {
        caches.forEach((name, cache) -> {
            int size = cache.size();
            cache.invalidateAll();
            logger.info("Cleared " + size + " entries from " + name + " cache");
        });
    }
    
    /**
     * Shuts down the cache manager.
     */
    public void shutdown() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        
        clearAllCaches();
    }
    
    /**
     * Gets a player data cache key for a player UUID.
     * 
     * @param playerUuid The player UUID
     * @param dataType The data type
     * @return The cache key
     */
    public static String getPlayerDataKey(UUID playerUuid, String dataType) {
        return playerUuid.toString() + ":" + dataType;
    }
}