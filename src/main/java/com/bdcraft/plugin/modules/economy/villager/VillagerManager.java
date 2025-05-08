package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages BD villagers.
 */
public class VillagerManager {
    private final BDCraft plugin;
    private final Map<UUID, BDVillager> bdVillagers;
    private final NamespacedKey bdVillagerKey;
    private final NamespacedKey bdVillagerTypeKey;
    
    /**
     * Creates a new villager manager.
     * @param plugin The plugin instance
     */
    public VillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.bdVillagers = new HashMap<>();
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
    }
    
    /**
     * Creates a new BD dealer.
     * @param location The spawn location
     * @return The dealer
     */
    public BDDealer createDealer(Location location) {
        BDDealer dealer = new BDDealer(plugin, location);
        bdVillagers.put(dealer.getUniqueId(), dealer);
        return dealer;
    }
    
    /**
     * Creates a new BD collector.
     * @param location The spawn location
     * @return The collector
     */
    public BDCollector createCollector(Location location) {
        BDCollector collector = new BDCollector(plugin, location);
        bdVillagers.put(collector.getUniqueId(), collector);
        return collector;
    }
    
    /**
     * Creates a new BD market owner.
     * @param location The spawn location
     * @return The market owner
     */
    public BDMarketOwner createMarketOwner(Location location) {
        BDMarketOwner marketOwner = new BDMarketOwner(plugin, location);
        bdVillagers.put(marketOwner.getUniqueId(), marketOwner);
        return marketOwner;
    }
    
    /**
     * Creates a new BD seasonal trader.
     * @param location The spawn location
     * @return The seasonal trader
     */
    public BDSeasonalTrader createSeasonalTrader(Location location) {
        BDSeasonalTrader seasonalTrader = new BDSeasonalTrader(plugin, location);
        bdVillagers.put(seasonalTrader.getUniqueId(), seasonalTrader);
        return seasonalTrader;
    }
    
    /**
     * Gets a BD villager by UUID.
     * @param uuid The UUID
     * @return The villager, or null if not found
     */
    public BDVillager getVillager(UUID uuid) {
        return bdVillagers.get(uuid);
    }
    
    /**
     * Removes a BD villager.
     * @param uuid The UUID
     */
    public void removeVillager(UUID uuid) {
        BDVillager villager = bdVillagers.remove(uuid);
        if (villager != null) {
            villager.remove();
        }
    }
    
    /**
     * Checks if an entity is a BD villager.
     * @param entity The entity
     * @return True if the entity is a BD villager, false otherwise
     */
    public boolean isBDVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return false;
        }
        
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.has(bdVillagerKey, PersistentDataType.STRING);
    }
    
    /**
     * Gets the BD villager type from an entity.
     * @param entity The entity
     * @return The villager type, or null if not a BD villager
     */
    public String getBDVillagerType(Entity entity) {
        if (!isBDVillager(entity)) {
            return null;
        }
        
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.get(bdVillagerTypeKey, PersistentDataType.STRING);
    }
    
    /**
     * Loads a BD villager from an entity.
     * @param entity The entity
     * @return The BD villager, or null if not a BD villager
     */
    public BDVillager loadBDVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return null;
        }
        
        Villager villager = (Villager) entity;
        String type = getBDVillagerType(entity);
        
        if (type == null) {
            return null;
        }
        
        BDVillager bdVillager;
        
        switch (type) {
            case BDDealer.TYPE:
                bdVillager = new BDDealer(plugin, villager);
                break;
            case BDCollector.TYPE:
                bdVillager = new BDCollector(plugin, villager);
                break;
            case BDMarketOwner.TYPE:
                bdVillager = new BDMarketOwner(plugin, villager);
                break;
            case BDSeasonalTrader.TYPE:
                bdVillager = new BDSeasonalTrader(plugin, villager);
                break;
            default:
                return null;
        }
        
        bdVillagers.put(bdVillager.getUniqueId(), bdVillager);
        return bdVillager;
    }
    
    /**
     * Loads all BD villagers from a world.
     */
    public void loadAllVillagers() {
        plugin.getServer().getWorlds().forEach(world -> {
            world.getEntitiesByClass(Villager.class).forEach(this::loadBDVillager);
        });
    }
}