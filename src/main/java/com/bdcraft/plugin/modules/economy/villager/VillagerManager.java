package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages BD villagers.
 */
public class VillagerManager implements Listener {
    private final BDCraft plugin;
    private final Map<UUID, BDVillager> villagers;
    private final NamespacedKey bdVillagerKey;
    private final NamespacedKey bdVillagerTypeKey;
    private final NamespacedKey bdVillagerUuidKey;
    
    /**
     * Creates a new villager manager.
     * 
     * @param plugin The plugin instance
     */
    public VillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.villagers = new HashMap<>();
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdVillagerUuidKey = new NamespacedKey(plugin, "bd_villager_uuid");
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Creates a new BD dealer.
     * 
     * @param location The spawn location
     * @return The dealer
     */
    public BDDealer createDealer(Location location) {
        BDDealer dealer = new BDDealer(plugin, location);
        villagers.put(dealer.getUniqueId(), dealer);
        return dealer;
    }
    
    /**
     * Creates a new BD dealer with a specified market.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The dealer
     */
    public BDDealer createDealer(Market market, Location location) {
        BDDealer dealer = new BDDealer(plugin, location, market);
        villagers.put(dealer.getUniqueId(), dealer);
        return dealer;
    }
    
    /**
     * Creates a new BD collector.
     * 
     * @param location The spawn location
     * @return The collector
     */
    public BDCollector createCollector(Location location) {
        BDCollector collector = new BDCollector(plugin, location);
        villagers.put(collector.getUniqueId(), collector);
        return collector;
    }
    
    /**
     * Creates a new BD collector with a specified market.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The collector
     */
    public BDCollector createCollector(Market market, Location location) {
        BDCollector collector = new BDCollector(plugin, location, market);
        villagers.put(collector.getUniqueId(), collector);
        return collector;
    }
    
    /**
     * Creates a new BD market owner.
     * 
     * @param location The spawn location
     * @return The market owner
     */
    public BDMarketOwner createMarketOwner(Location location) {
        BDMarketOwner marketOwner = new BDMarketOwner(plugin, location);
        villagers.put(marketOwner.getUniqueId(), marketOwner);
        return marketOwner;
    }
    
    /**
     * Creates a new market owner villager with a specified market.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The market owner
     */
    public BDMarketOwner createMarketOwner(Market market, Location location) {
        BDMarketOwner marketOwner = new BDMarketOwner(plugin, location, market);
        villagers.put(marketOwner.getUniqueId(), marketOwner);
        return marketOwner;
    }
    
    /**
     * Creates a new BD seasonal trader.
     * 
     * @param location The spawn location
     * @return The seasonal trader
     */
    public BDSeasonalTrader createSeasonalTrader(Location location) {
        BDSeasonalTrader seasonalTrader = new BDSeasonalTrader(plugin, location);
        villagers.put(seasonalTrader.getUniqueId(), seasonalTrader);
        return seasonalTrader;
    }
    
    /**
     * Gets a BD villager by UUID.
     * 
     * @param uuid The UUID
     * @return The villager, or null if not found
     */
    public BDVillager getVillager(UUID uuid) {
        return villagers.get(uuid);
    }
    
    /**
     * Gets a BD villager from a Bukkit entity.
     * 
     * @param entity The entity
     * @return The villager, or null if the entity is not a BD villager
     */
    public BDVillager getVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return null;
        }
        
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (!container.has(bdVillagerUuidKey, PersistentDataType.STRING)) {
            return null;
        }
        
        String uuidString = container.get(bdVillagerUuidKey, PersistentDataType.STRING);
        return villagers.get(UUID.fromString(uuidString));
    }
    
    /**
     * Gets all villagers.
     * 
     * @return A list of all villagers
     */
    public List<BDVillager> getVillagers() {
        return new ArrayList<>(villagers.values());
    }
    
    /**
     * Gets all villagers of a specific type.
     * 
     * @param type The villager type
     * @return A list of villagers of the specified type
     */
    public List<BDVillager> getVillagers(String type) {
        List<BDVillager> result = new ArrayList<>();
        
        for (BDVillager villager : villagers.values()) {
            if (villager.getVillagerType().equals(type)) {
                result.add(villager);
            }
        }
        
        return result;
    }
    
    /**
     * Gets all dealers.
     * 
     * @return A list of all dealers
     */
    public List<BDDealer> getDealers() {
        List<BDDealer> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(BDDealer.TYPE)) {
            result.add((BDDealer) villager);
        }
        
        return result;
    }
    
    /**
     * Gets all collectors.
     * 
     * @return A list of all collectors
     */
    public List<BDCollector> getCollectors() {
        List<BDCollector> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(BDCollector.TYPE)) {
            result.add((BDCollector) villager);
        }
        
        return result;
    }
    
    /**
     * Gets all market owners.
     * 
     * @return A list of all market owners
     */
    public List<BDMarketOwner> getMarketOwners() {
        List<BDMarketOwner> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(BDMarketOwner.TYPE)) {
            result.add((BDMarketOwner) villager);
        }
        
        return result;
    }
    
    /**
     * Gets all seasonal traders.
     * 
     * @return A list of all seasonal traders
     */
    public List<BDSeasonalTrader> getSeasonalTraders() {
        List<BDSeasonalTrader> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(BDSeasonalTrader.TYPE)) {
            result.add((BDSeasonalTrader) villager);
        }
        
        return result;
    }
    
    /**
     * Removes a BD villager.
     * 
     * @param uuid The UUID
     */
    public void removeVillager(UUID uuid) {
        BDVillager villager = villagers.remove(uuid);
        if (villager != null) {
            villager.remove();
        }
    }
    
    /**
     * Removes all villagers.
     */
    public void removeAllVillagers() {
        for (BDVillager villager : new ArrayList<>(villagers.values())) {
            villager.despawn();
        }
        
        villagers.clear();
    }
    
    /**
     * Checks if an entity is a BD villager.
     * 
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
     * 
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
     * 
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
        
        // Check if we already have this villager loaded by UUID
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (container.has(bdVillagerUuidKey, PersistentDataType.STRING)) {
            String uuidStr = container.get(bdVillagerUuidKey, PersistentDataType.STRING);
            UUID uuid = UUID.fromString(uuidStr);
            
            if (villagers.containsKey(uuid)) {
                return villagers.get(uuid);
            }
        }
        
        // Create new villager instance based on type
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
        
        villagers.put(bdVillager.getUniqueId(), bdVillager);
        return bdVillager;
    }
    
    /**
     * Loads all BD villagers from all worlds.
     */
    public void loadAllVillagers() {
        plugin.getServer().getWorlds().forEach(world -> {
            world.getEntitiesByClass(Villager.class).forEach(this::loadBDVillager);
        });
        
        plugin.getLogger().info("Loaded " + villagers.size() + " BD villagers");
    }
    
    /**
     * Updates all villagers.
     */
    public void updateAllVillagers() {
        for (BDVillager villager : new ArrayList<>(villagers.values())) {
            // Remove dead villagers
            if (!villager.isAlive() || villager.shouldRemove()) {
                villager.onRemove();
                villagers.remove(villager.getUniqueId());
                continue;
            }
            
            // Process tick for live villagers
            villager.onTick();
        }
    }
    
    /**
     * Handles villager interaction events.
     */
    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }
        
        BDVillager bdVillager = getVillager(event.getRightClicked());
        if (bdVillager == null) {
            return;
        }
        
        // Let the villager handle the interaction
        if (bdVillager.onInteract(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Prevents BD villagers from acquiring new trades.
     */
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        BDVillager bdVillager = getVillager(event.getEntity());
        if (bdVillager != null) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Prevents BD villagers from taking damage.
     */
    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) {
        BDVillager bdVillager = getVillager(event.getEntity());
        if (bdVillager != null) {
            if (bdVillager.onDamage(event)) {
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * Handles a market upgrade.
     * 
     * @param market The market that was upgraded
     */
    public void handleMarketUpgrade(Market market) {
        // Update trades for all villagers in this market
        for (BDVillager villager : villagers.values()) {
            if (villager instanceof BDMarketOwner) {
                BDMarketOwner owner = (BDMarketOwner) villager;
                if (owner.getMarket() != null && owner.getMarket().getId().equals(market.getId())) {
                    owner.updateTrades();
                }
            } else if (villager instanceof BDDealer) {
                BDDealer dealer = (BDDealer) villager;
                if (dealer.getMarket() != null && dealer.getMarket().getId().equals(market.getId())) {
                    dealer.updateTrades();
                }
            }
        }
    }
}