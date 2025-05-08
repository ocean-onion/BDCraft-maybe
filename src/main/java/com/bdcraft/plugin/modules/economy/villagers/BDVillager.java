package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.bdcraft.plugin.BDCraft;

import java.util.UUID;

/**
 * Base class for BD specialized villagers.
 */
public abstract class BDVillager {
    protected final BDCraft plugin;
    protected final UUID uuid;
    protected Villager entity;
    protected String displayName;
    protected VillagerType bdType;
    
    /**
     * Creates a new BD villager.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param entity The villager entity (or null if not spawned yet)
     * @param displayName The display name of the villager
     * @param bdType The BD villager type
     */
    public BDVillager(BDCraft plugin, UUID uuid, Villager entity, String displayName, VillagerType bdType) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.entity = entity;
        this.displayName = displayName;
        this.bdType = bdType;
        
        if (entity != null) {
            applyVillagerProperties();
        }
    }
    
    /**
     * Spawns the villager at the specified location.
     * 
     * @param location The location to spawn the villager
     * @return The spawned villager entity
     */
    public Villager spawn(Location location) {
        // Spawn the villager entity
        entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Apply properties
        applyVillagerProperties();
        
        return entity;
    }
    
    /**
     * Applies properties to the villager entity.
     */
    protected void applyVillagerProperties() {
        // Set the display name
        entity.setCustomName(ChatColor.GOLD + displayName);
        entity.setCustomNameVisible(true);
        
        // Apply common properties
        entity.setProfession(getBukkitProfession());
        entity.setVillagerType(getBukkitVillagerType());
        entity.setVillagerLevel(5); // Max level
        entity.setVillagerExperience(0);
        entity.setCanPickupItems(false);
        entity.setAI(true);
        entity.setInvulnerable(true);
        entity.setSilent(false);
        entity.setRemoveWhenFarAway(false);
        
        // Store BD type and UUID in persistent data
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, "bd_villager_type"), 
                PersistentDataType.STRING, bdType.name());
        container.set(new NamespacedKey(plugin, "bd_villager_uuid"), 
                PersistentDataType.STRING, uuid.toString());
    }
    
    /**
     * Sets up trades for this villager.
     */
    public abstract void setupTrades();
    
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
     * Gets the UUID of the villager.
     * 
     * @return The UUID
     */
    public UUID getUuid() {
        return uuid;
    }
    
    /**
     * Gets the villager entity.
     * 
     * @return The villager entity
     */
    public Villager getEntity() {
        return entity;
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
     * Gets the BD villager type.
     * 
     * @return The BD villager type
     */
    public VillagerType getBDType() {
        return bdType;
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
     * Despawns the villager.
     */
    public void despawn() {
        if (isAlive()) {
            entity.remove();
            entity = null;
        }
    }
    
    /**
     * Enum for BD villager types.
     */
    public enum VillagerType {
        MARKET_OWNER,      // Manages market upgrades
        BD_DEALER,         // Buy/sell seeds and crops
        SEASONAL_TRADER,   // Special rare trades 
        HOUSE_OWNER,       // Manages a house (from House Token)
        BD_COLLECTOR       // Special crop collector at higher-level markets
    }
}