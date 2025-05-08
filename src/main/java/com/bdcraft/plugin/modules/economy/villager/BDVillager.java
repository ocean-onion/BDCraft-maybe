package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for BD villagers.
 */
public abstract class BDVillager {
    protected final BDCraft plugin;
    protected final Villager villager;
    protected final Map<UUID, Integer> playerReputations;
    
    // Keys for persistent data
    protected final NamespacedKey bdVillagerKey;
    protected final NamespacedKey bdVillagerTypeKey;
    
    /**
     * Creates a new BD villager.
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param name The villager name
     * @param profession The villager profession
     * @param type The villager type
     */
    public BDVillager(BDCraft plugin, Location location, String name, Profession profession, Type type) {
        this.plugin = plugin;
        this.playerReputations = new HashMap<>();
        
        // Create keys
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        
        // Spawn villager
        this.villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Setup villager
        villager.setCustomName(ChatColor.GOLD + name);
        villager.setCustomNameVisible(true);
        villager.setProfession(profession);
        villager.setVillagerType(type);
        villager.setAdult();
        villager.setInvulnerable(true);
        villager.setPersistent(true);
        
        // Set persistent data
        PersistentDataContainer container = villager.getPersistentDataContainer();
        container.set(bdVillagerKey, PersistentDataType.STRING, "true");
        container.set(bdVillagerTypeKey, PersistentDataType.STRING, getVillagerType());
        
        // Initialize trades
        initializeTrades();
    }
    
    /**
     * Creates a new BD villager from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDVillager(BDCraft plugin, Villager villager) {
        this.plugin = plugin;
        this.villager = villager;
        this.playerReputations = new HashMap<>();
        
        // Create keys
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
    }
    
    /**
     * Initializes trades for the villager.
     */
    protected abstract void initializeTrades();
    
    /**
     * Updates the villager's trades.
     */
    public void updateTrades() {
        // To be implemented by subclasses if needed
        // Default implementation does nothing
    }
    
    /**
     * Gets the villager type.
     * @return The villager type
     */
    public abstract String getVillagerType();
    
    /**
     * Gets the type of the villager (compatibility method).
     * @return The type string
     */
    public String getType() {
        return getVillagerType();
    }
    
    /**
     * Gets the villager.
     * @return The villager
     */
    public Villager getVillager() {
        return villager;
    }
    
    /**
     * Gets the villager's UUID.
     * @return The UUID
     */
    public UUID getUniqueId() {
        return villager.getUniqueId();
    }
    
    /**
     * Gets the villager's UUID (compatibility method).
     * @return The UUID
     */
    public UUID getUUID() {
        return getUniqueId();
    }
    
    /**
     * Gets the villager's location.
     * @return The location
     */
    public Location getLocation() {
        return villager.getLocation();
    }
    
    /**
     * Gets a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @return The reputation
     */
    public int getReputation(UUID playerUuid) {
        return playerReputations.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Sets a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @param reputation The reputation
     */
    public void setReputation(UUID playerUuid, int reputation) {
        if (reputation < 0) {
            reputation = 0;
        } else if (reputation > 100) {
            reputation = 100;
        }
        
        playerReputations.put(playerUuid, reputation);
    }
    
    /**
     * Changes a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @param change The reputation change
     * @return The new reputation
     */
    public int changeReputation(UUID playerUuid, int change) {
        int currentRep = getReputation(playerUuid);
        int newRep = currentRep + change;
        
        setReputation(playerUuid, newRep);
        
        return newRep;
    }
    
    /**
     * Removes the villager.
     */
    public void remove() {
        villager.remove();
    }
}