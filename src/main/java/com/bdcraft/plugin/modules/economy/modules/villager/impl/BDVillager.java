package com.bdcraft.plugin.modules.economy.modules.villager.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.UUID;

/**
 * Represents a BDCraft villager with extended functionality.
 */
public class BDVillager {
    private final UUID uuid;
    private final String type;
    private final UUID ownerUUID;
    private Location location;
    private Villager entity;
    
    /**
     * Creates a new BDVillager.
     *
     * @param uuid The unique identifier for this villager
     * @param type The type of villager
     * @param ownerUUID The UUID of the player who owns this villager
     * @param location The location of the villager
     */
    public BDVillager(UUID uuid, String type, UUID ownerUUID, Location location) {
        this.uuid = uuid;
        this.type = type;
        this.ownerUUID = ownerUUID;
        this.location = location;
    }
    
    /**
     * Gets the UUID of this villager.
     *
     * @return The unique identifier
     */
    public UUID getUUID() {
        return uuid;
    }
    
    /**
     * Gets the type of this villager.
     *
     * @return The villager type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the UUID of the player who owns this villager.
     *
     * @return The owner's UUID
     */
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    
    /**
     * Gets the location of this villager.
     *
     * @return The villager's location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Sets the location of this villager.
     *
     * @param location The new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     * Gets the Bukkit entity associated with this villager.
     *
     * @return The villager entity
     */
    public Villager getEntity() {
        return entity;
    }
    
    /**
     * Sets the Bukkit entity associated with this villager.
     *
     * @param entity The villager entity
     */
    public void setEntity(Villager entity) {
        this.entity = entity;
    }
}