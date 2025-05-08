package com.bdcraft.plugin.modules.economy.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.bdcraft.plugin.BDCraft;

/**
 * Base class for BD items.
 */
public abstract class BDItem {
    protected final BDCraft plugin;
    
    /**
     * Creates a new BD item.
     * 
     * @param plugin The plugin instance
     */
    public BDItem(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Creates an item stack for this BD item.
     * 
     * @param amount The amount of items
     * @return The item stack
     */
    public abstract ItemStack createItemStack(int amount);
    
    /**
     * Gets a namespaced key for this plugin.
     * 
     * @param key The key
     * @return The namespaced key
     */
    public NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(plugin, key);
    }
    
    /**
     * Gets a namespaced key for this plugin (static version).
     * 
     * @param key The key
     * @return The namespaced key
     */
    public static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey("bdcraft", key);
    }
}