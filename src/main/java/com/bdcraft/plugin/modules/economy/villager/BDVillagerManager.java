package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages BD Villagers in the game.
 */
public class BDVillagerManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, BDVillager> villagers;
    
    /**
     * Creates a new BD villager manager.
     * @param plugin The plugin instance
     */
    public BDVillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.villagers = new HashMap<>();
    }
    
    /**
     * Creates a BD Collector villager.
     * @param location The location to spawn the villager
     * @return The created villager
     */
    public BDCollector createCollector(Location location) {
        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        BDCollector collector = new BDCollector(plugin, entity);
        villagers.put(entity.getUniqueId(), collector);
        
        return collector;
    }
    
    /**
     * Creates a BD Collector villager for a market.
     * @param location The location to spawn the villager
     * @param market The associated market
     * @return The created villager
     */
    public BDCollector createCollector(Location location, Market market) {
        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        BDCollector collector = new BDCollector(plugin, entity, market);
        villagers.put(entity.getUniqueId(), collector);
        
        return collector;
    }
    
    /**
     * Creates a BD Dealer villager.
     * @param location The location to spawn the villager
     * @return The created villager
     */
    public BDDealer createDealer(Location location) {
        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        BDDealer dealer = new BDDealer(plugin, entity);
        villagers.put(entity.getUniqueId(), dealer);
        
        return dealer;
    }
    
    /**
     * Creates a BD Dealer villager for a market.
     * @param location The location to spawn the villager
     * @param market The associated market
     * @return The created villager
     */
    public BDDealer createDealer(Location location, Market market) {
        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        BDDealer dealer = new BDDealer(plugin, entity, market);
        villagers.put(entity.getUniqueId(), dealer);
        
        return dealer;
    }
    
    /**
     * Creates a BD Market Owner villager.
     * @param location The location to spawn the villager
     * @param market The associated market
     * @return The created villager
     */
    public BDMarketOwner createMarketOwner(Location location, Market market) {
        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        BDMarketOwner marketOwner = new BDMarketOwner(plugin, entity, market);
        villagers.put(entity.getUniqueId(), marketOwner);
        
        return marketOwner;
    }
    
    /**
     * Gets a BD Villager by its entity.
     * @param entity The villager entity
     * @return The BD Villager, or null if not found
     */
    public BDVillager getVillager(Entity entity) {
        return villagers.get(entity.getUniqueId());
    }
    
    /**
     * Checks if an entity is a BD Villager.
     * @param entity The entity to check
     * @return Whether the entity is a BD Villager
     */
    public boolean isBDVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return false;
        }
        
        // Check if in memory
        if (villagers.containsKey(entity.getUniqueId())) {
            return true;
        }
        
        // Check persistent data
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        org.bukkit.NamespacedKey bdVillagerKey = new org.bukkit.NamespacedKey("bdcraft", "bd_villager");
        
        return pdc.has(bdVillagerKey, PersistentDataType.BYTE);
    }
    
    /**
     * Gets the type of a BD Villager.
     * @param entity The villager entity
     * @return The type, or null if not a BD Villager
     */
    public String getBDVillagerType(Entity entity) {
        if (!isBDVillager(entity)) {
            return null;
        }
        
        // Check if in memory
        BDVillager villager = villagers.get(entity.getUniqueId());
        if (villager != null) {
            return villager.getType();
        }
        
        // Check persistent data
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        org.bukkit.NamespacedKey bdVillagerTypeKey = new org.bukkit.NamespacedKey("bdcraft", "bd_villager_type");
        
        return pdc.get(bdVillagerTypeKey, PersistentDataType.STRING);
    }
    
    /**
     * Loads a BD Villager from its entity.
     * @param entity The villager entity
     * @return The loaded BD Villager, or null if not a BD Villager
     */
    public BDVillager loadVillager(Entity entity) {
        if (!isBDVillager(entity)) {
            return null;
        }
        
        // Check if already loaded
        if (villagers.containsKey(entity.getUniqueId())) {
            return villagers.get(entity.getUniqueId());
        }
        
        // Load based on type
        String type = getBDVillagerType(entity);
        if (type == null) {
            return null;
        }
        
        BDVillager villager = null;
        
        if (type.equals("COLLECTOR")) {
            villager = new BDCollector(plugin, (Villager) entity);
        } else if (type.equals("DEALER")) {
            villager = new BDDealer(plugin, (Villager) entity);
        } else if (type.equals("MARKET_OWNER")) {
            // Market owners need a market - handled elsewhere
            logger.warning("Attempted to load market owner without a market: " + entity.getUniqueId());
            return null;
        }
        
        if (villager != null) {
            villagers.put(entity.getUniqueId(), villager);
        }
        
        return villager;
    }
    
    /**
     * Removes a BD Villager.
     * @param villager The villager to remove
     */
    public void removeVillager(BDVillager villager) {
        villagers.remove(villager.getUUID());
        villager.remove();
    }
    
    /**
     * Removes a BD Villager by UUID.
     * @param villagerUuid The UUID of the villager to remove
     */
    public void removeVillager(UUID villagerUuid) {
        BDVillager villager = villagers.get(villagerUuid);
        if (villager != null) {
            removeVillager(villager);
        }
    }
    
    /**
     * Gets all registered BD Villagers.
     * @return The villagers
     */
    public Map<UUID, BDVillager> getVillagers() {
        return villagers;
    }
    
    /**
     * Updates all villagers.
     */
    public void updateVillagers() {
        for (BDVillager villager : villagers.values()) {
            if (villager instanceof BDCollector) {
                ((BDCollector) villager).updateTrades();
            } else if (villager instanceof BDDealer) {
                ((BDDealer) villager).updateTrades();
            } else if (villager instanceof BDMarketOwner) {
                ((BDMarketOwner) villager).updateTrades();
            }
        }
    }
}