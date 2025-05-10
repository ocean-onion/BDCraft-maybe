package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for BD villagers.
 */
public abstract class BDVillager {
    protected final BDCraft plugin;
    protected final UUID uuid;
    protected Villager entity;
    protected String displayName;
    protected final Map<UUID, Integer> playerReputations;
    
    // Keys for persistent data
    protected final NamespacedKey bdVillagerKey;
    protected final NamespacedKey bdVillagerTypeKey;
    protected final NamespacedKey bdVillagerUuidKey;
    
    /**
     * Creates a new BD villager.
     * 
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param name The villager name
     * @param profession The villager profession
     * @param type The villager type
     */
    public BDVillager(BDCraft plugin, Location location, String name, Profession profession, Type type) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        this.displayName = name;
        this.playerReputations = new HashMap<>();
        
        // Create keys
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdVillagerUuidKey = new NamespacedKey(plugin, "bd_villager_uuid");
        
        // Spawn villager
        this.entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Apply properties
        applyVillagerProperties(name, profession, type);
        
        // Initialize trades
        initializeTrades();
    }
    
    /**
     * Creates a new BD villager with an existing entity.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param entity The villager entity
     * @param displayName The display name of the villager
     */
    public BDVillager(BDCraft plugin, UUID uuid, Villager entity, String displayName) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.entity = entity;
        this.displayName = displayName;
        this.playerReputations = new HashMap<>();
        
        // Create keys
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdVillagerUuidKey = new NamespacedKey(plugin, "bd_villager_uuid");
    }
    
    /**
     * Creates a new BD villager from an existing villager.
     * 
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDVillager(BDCraft plugin, Villager villager) {
        this.plugin = plugin;
        this.entity = villager;
        this.playerReputations = new HashMap<>();
        
        // Create keys
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdVillagerUuidKey = new NamespacedKey(plugin, "bd_villager_uuid");
        
        // Extract UUID from persistent data, or generate new one
        PersistentDataContainer container = villager.getPersistentDataContainer();
        String uuidStr = container.get(bdVillagerUuidKey, PersistentDataType.STRING);
        
        if (uuidStr != null) {
            this.uuid = UUID.fromString(uuidStr);
        } else {
            this.uuid = UUID.randomUUID();
            container.set(bdVillagerUuidKey, PersistentDataType.STRING, this.uuid.toString());
        }
        
        // Extract display name
        this.displayName = villager.getCustomName();
        if (this.displayName == null) {
            this.displayName = getVillagerTypeName();
        }
    }
    
    /**
     * Applies standard properties to the villager entity.
     * 
     * @param name The display name
     * @param profession The profession
     * @param type The villager type
     */
    protected void applyVillagerProperties(String name, Profession profession, Type type) {
        if (entity == null) {
            return;
        }
        
        // Get appropriate color
        ChatColor nameColor = getNameColor();
        
        // Set display properties
        entity.setCustomName(nameColor + name);
        entity.setCustomNameVisible(true);
        
        // Set profession and type
        entity.setProfession(profession);
        entity.setVillagerType(type);
        entity.setVillagerLevel(getVillagerLevel());
        
        // Set common properties
        entity.setAdult();
        entity.setInvulnerable(true);
        entity.setPersistent(true);
        entity.setCanPickupItems(false);
        entity.setRemoveWhenFarAway(false);
        
        // Store metadata
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(bdVillagerKey, PersistentDataType.STRING, "true");
        container.set(bdVillagerTypeKey, PersistentDataType.STRING, getVillagerType());
        container.set(bdVillagerUuidKey, PersistentDataType.STRING, uuid.toString());
    }
    
    /**
     * Spawns the villager at the specified location.
     * 
     * @param location The location to spawn the villager
     * @return The spawned villager entity
     */
    public Villager spawn(Location location) {
        if (isAlive()) {
            despawn();
        }
        
        // Spawn the villager entity
        entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Apply properties
        applyVillagerProperties(displayName, getBukkitProfession(), getBukkitVillagerType());
        
        // Initialize trades
        initializeTrades();
        
        return entity;
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
     * 
     * @return The villager type
     */
    public abstract String getVillagerType();
    
    /**
     * Gets the type of the villager (compatibility method).
     * 
     * @return The type string
     */
    public String getType() {
        return getVillagerType();
    }
    
    /**
     * Gets a friendly name for this villager type.
     * 
     * @return The villager type name
     */
    public abstract String getVillagerTypeName();
    
    /**
     * Gets the Bukkit profession for this villager.
     * 
     * @return The Bukkit profession
     */
    protected abstract Profession getBukkitProfession();
    
    /**
     * Gets the Bukkit villager type for this villager.
     * 
     * @return The Bukkit villager type
     */
    protected Type getBukkitVillagerType() {
        return Type.PLAINS; // Default villager type
    }
    
    /**
     * Gets the villager level for this BD villager (1-5).
     * 
     * @return The villager level
     */
    protected abstract int getVillagerLevel();
    
    /**
     * Gets the appropriate name color for this villager type.
     * 
     * @return The chat color for this villager's name
     */
    protected abstract ChatColor getNameColor();
    
    /**
     * Gets the villager.
     * 
     * @return The villager
     */
    public Villager getVillager() {
        return entity;
    }
    
    /**
     * Gets the villager's UUID.
     * 
     * @return The UUID
     */
    public UUID getUniqueId() {
        return uuid;
    }
    
    /**
     * Gets the villager's UUID (compatibility method).
     * 
     * @return The UUID
     */
    public UUID getUUID() {
        return getUniqueId();
    }
    
    /**
     * Gets the villager's location.
     * 
     * @return The location
     */
    public Location getLocation() {
        return entity != null ? entity.getLocation() : null;
    }
    
    /**
     * Gets the display name of the villager.
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Checks if this villager is alive.
     * 
     * @return True if the villager is alive, false otherwise
     */
    public boolean isAlive() {
        return entity != null && !entity.isDead();
    }
    
    /**
     * Teleports the villager to the specified location.
     * 
     * @param location The location to teleport to
     * @return True if the teleport was successful, false otherwise
     */
    public boolean teleport(Location location) {
        if (!isAlive()) {
            return false;
        }
        
        return entity.teleport(location);
    }
    
    /**
     * Gets a player's reputation with this villager.
     * 
     * @param playerUuid The player UUID
     * @return The reputation
     */
    public int getReputation(UUID playerUuid) {
        return playerReputations.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Sets a player's reputation with this villager.
     * 
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
     * 
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
     * Despawns the villager.
     */
    public void despawn() {
        if (isAlive()) {
            entity.remove();
            entity = null;
        }
    }
    
    /**
     * Removes the villager (alias for despawn).
     */
    public void remove() {
        despawn();
    }
    
    /**
     * Handles a player interacting with this villager.
     * 
     * @param player The player
     * @return True if interaction was handled
     */
    public boolean onInteract(Player player) {
        return false; // Default implementation does nothing
    }
    
    /**
     * Handles the villager getting damaged.
     * 
     * @param event The damage event
     * @return True if the damage event should be cancelled
     */
    public boolean onDamage(EntityDamageEvent event) {
        return true; // Default implementation cancels all damage
    }
    
    /**
     * Handles the villager trying to change career/profession.
     * 
     * @param newProfession The new profession
     * @return True if the profession change should be cancelled
     */
    public boolean onProfessionChange(Profession newProfession) {
        return true; // Default implementation prevents profession changes
    }
    
    /**
     * Called on each tick for this villager to handle any periodic tasks.
     */
    public void onTick() {
        // Default implementation does nothing
    }
    
    /**
     * Checks if this villager should be removed from the world.
     * 
     * @return True if the villager should be removed
     */
    public boolean shouldRemove() {
        return false; // Default implementation keeps villagers alive
    }
    
    /**
     * Called when this villager is removed from the world.
     */
    public void onRemove() {
        // Default implementation does nothing
    }
}