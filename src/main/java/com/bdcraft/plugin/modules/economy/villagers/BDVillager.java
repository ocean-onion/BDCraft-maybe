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
        // Set the display name with the appropriate color from documentation
        ChatColor nameColor = getNameColor();
        entity.setCustomName(nameColor + displayName);
        entity.setCustomNameVisible(true);
        
        // Apply common properties
        entity.setProfession(getBukkitProfession());
        entity.setVillagerType(getBukkitVillagerType());
        entity.setVillagerLevel(getVillagerLevel()); // Level from documentation
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
     * Gets the villager level for this BD villager.
     * 
     * @return The villager level (1-5)
     */
    protected int getVillagerLevel() {
        // Per documentation:
        // BD Dealer: Level 2 (Experienced)
        // BD Collector: Level 3 (Expert)
        // Market Owner: Level 4 (Expert)
        // Seasonal BD Trader: Level 1 (Novice)
        switch (bdType) {
            case BD_DEALER:
                return 2;
            case BD_COLLECTOR:
                return 3;
            case MARKET_OWNER:
                return 4;
            case SEASONAL_TRADER:
                return 1;
            default:
                return 1;
        }
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
     * Gets the appropriate name color for this villager type based on documentation.
     * 
     * @return The chat color for this villager's name
     */
    protected ChatColor getNameColor() {
        // Per documentation:
        // BD Dealer: GREEN
        // BD Collector: AQUA (blue)
        // Market Owner: GOLD
        // Seasonal BD Trader: YELLOW
        switch (bdType) {
            case BD_DEALER:
                return ChatColor.GREEN;
            case BD_COLLECTOR:
                return ChatColor.AQUA;
            case MARKET_OWNER:
                return ChatColor.GOLD;
            case SEASONAL_TRADER:
                return ChatColor.YELLOW;
            default:
                return ChatColor.WHITE;
        }
    }
    
    /**
     * Enum for BD villager types.
     */
    public enum VillagerType {
        MARKET_OWNER,      // Manages market upgrades
        BD_DEALER,         // Sells seeds and tools
        BD_COLLECTOR,      // Buys crops (spawns from House Token)
        SEASONAL_TRADER    // Special rare trades
    }
}