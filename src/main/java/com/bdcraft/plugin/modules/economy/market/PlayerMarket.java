package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player-owned market in the game.
 */
public class PlayerMarket {
    private final String id;
    private final UUID owner;
    private final Location center;
    private final int size;
    private final List<UUID> villagers;
    private final Set<UUID> associates;
    private final long creationTime;
    private int level;
    
    /**
     * Creates a new player market.
     * @param id The market ID
     * @param owner The owner's UUID
     * @param center The center location
     * @param size The market size (in blocks)
     * @param villagers The market villagers
     * @param associates The market associates
     * @param creationTime The creation timestamp
     */
    public PlayerMarket(String id, UUID owner, Location center, int size, List<UUID> villagers, Set<UUID> associates, long creationTime) {
        this.id = id;
        this.owner = owner;
        this.center = center;
        this.size = size;
        this.villagers = new ArrayList<>(villagers);
        this.associates = new HashSet<>(associates);
        this.creationTime = creationTime;
        this.level = 1; // Start at level 1
    }
    
    /**
     * Gets the market ID.
     * @return The market ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the owner's UUID.
     * @return The owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }
    
    /**
     * Gets the center location.
     * @return The center location
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Gets the market size.
     * @return The market size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Gets the villagers in this market.
     * @return The villagers
     */
    public List<UUID> getVillagers() {
        return new ArrayList<>(villagers);
    }
    
    /**
     * Adds a villager to this market.
     * @param uuid The villager's UUID
     */
    public void addVillager(UUID uuid) {
        if (!villagers.contains(uuid)) {
            villagers.add(uuid);
        }
    }
    
    /**
     * Removes a villager from this market.
     * @param uuid The villager's UUID
     * @return Whether the villager was removed
     */
    public boolean removeVillager(UUID uuid) {
        return villagers.remove(uuid);
    }
    
    /**
     * Gets the associates of this market.
     * @return The associates
     */
    public Set<UUID> getAssociates() {
        return new HashSet<>(associates);
    }
    
    /**
     * Adds an associate to this market.
     * @param uuid The associate's UUID
     */
    public void addAssociate(UUID uuid) {
        associates.add(uuid);
    }
    
    /**
     * Removes an associate from this market.
     * @param uuid The associate's UUID
     * @return Whether the associate was removed
     */
    public boolean removeAssociate(UUID uuid) {
        return associates.remove(uuid);
    }
    
    /**
     * Gets the creation time.
     * @return The creation time
     */
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Gets the market level.
     * @return The market level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the market level.
     * @param level The new level
     */
    public void setLevel(int level) {
        this.level = level;
    }
}