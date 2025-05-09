package com.bdcraft.plugin.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A simple, efficient cache implementation for the plugin.
 * This cache is completely self-contained and does not rely on any external libraries.
 * 
 * @param <K> The key type
 * @param <V> The value type
 */
public class BDCache<K, V> {
    private final Map<K, CacheEntry<V>> cache;
    private final long defaultTtl;
    private final boolean concurrent;
    
    /**
     * Creates a new cache with default TTL and non-concurrent operation.
     */
    public BDCache() {
        this(60000, false); // Default 60 second TTL, non-concurrent
    }
    
    /**
     * Creates a new cache with specified TTL and concurrency setting.
     * 
     * @param defaultTtl The default time-to-live in milliseconds
     * @param concurrent Whether to use thread-safe implementation
     */
    public BDCache(long defaultTtl, boolean concurrent) {
        this.defaultTtl = defaultTtl;
        this.concurrent = concurrent;
        
        // Use appropriate map implementation based on concurrency needs
        if (concurrent) {
            this.cache = new ConcurrentHashMap<>();
        } else {
            this.cache = new HashMap<>();
        }
    }
    
    /**
     * Gets a value from the cache, or computes it if not present or expired.
     * 
     * @param key The key
     * @param mappingFunction The function to compute the value if not cached
     * @return The value
     */
    public V get(K key, Function<K, V> mappingFunction) {
        return get(key, mappingFunction, defaultTtl);
    }
    
    /**
     * Gets a value from the cache, or computes it if not present or expired.
     * 
     * @param key The key
     * @param mappingFunction The function to compute the value if not cached
     * @param ttl The time-to-live for this entry in milliseconds
     * @return The value
     */
    public V get(K key, Function<K, V> mappingFunction, long ttl) {
        CacheEntry<V> entry = cache.get(key);
        long now = System.currentTimeMillis();
        
        // Check if entry exists and is not expired
        if (entry != null && !entry.isExpired(now)) {
            return entry.getValue();
        }
        
        // Compute new value
        V value = mappingFunction.apply(key);
        
        // Store in cache
        cache.put(key, new CacheEntry<>(value, now + ttl));
        
        return value;
    }
    
    /**
     * Puts a value in the cache with the default TTL.
     * 
     * @param key The key
     * @param value The value
     */
    public void put(K key, V value) {
        put(key, value, defaultTtl);
    }
    
    /**
     * Puts a value in the cache with a specified TTL.
     * 
     * @param key The key
     * @param value The value
     * @param ttl The time-to-live in milliseconds
     */
    public void put(K key, V value, long ttl) {
        long expiry = System.currentTimeMillis() + ttl;
        cache.put(key, new CacheEntry<>(value, expiry));
    }
    
    /**
     * Checks if a key is in the cache and not expired.
     * 
     * @param key The key
     * @return Whether the key is in the cache and not expired
     */
    public boolean contains(K key) {
        CacheEntry<V> entry = cache.get(key);
        return entry != null && !entry.isExpired(System.currentTimeMillis());
    }
    
    /**
     * Invalidates a cache entry.
     * 
     * @param key The key
     */
    public void invalidate(K key) {
        cache.remove(key);
    }
    
    /**
     * Invalidates all cache entries.
     */
    public void invalidateAll() {
        cache.clear();
    }
    
    /**
     * Removes expired entries from the cache.
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        
        if (concurrent) {
            // For concurrent map, safely iterate and remove
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        } else {
            // For non-concurrent map, use iterator
            cache.values().removeIf(entry -> entry.isExpired(now));
        }
    }
    
    /**
     * Gets the size of the cache, including expired entries.
     * 
     * @return The cache size
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Gets the size of the cache, excluding expired entries.
     * 
     * @return The active cache size
     */
    public int activeSize() {
        long now = System.currentTimeMillis();
        return (int) cache.values().stream()
                .filter(entry -> !entry.isExpired(now))
                .count();
    }
    
    /**
     * A cache entry with a value and expiry time.
     * 
     * @param <T> The value type
     */
    private static class CacheEntry<T> {
        private final T value;
        private final long expiryTime;
        
        /**
         * Creates a new cache entry.
         * 
         * @param value The value
         * @param expiryTime The expiry time in milliseconds since epoch
         */
        public CacheEntry(T value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        /**
         * Gets the value.
         * 
         * @return The value
         */
        public T getValue() {
            return value;
        }
        
        /**
         * Checks if this entry is expired.
         * 
         * @param currentTime The current time in milliseconds since epoch
         * @return Whether this entry is expired
         */
        public boolean isExpired(long currentTime) {
            return currentTime > expiryTime;
        }
    }
}