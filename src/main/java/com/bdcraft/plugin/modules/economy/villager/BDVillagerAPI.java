package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementation of the VillagerAPI for BD villager operations.
 */
public class BDVillagerAPI implements VillagerAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final BDVillagerManager villagerManager;
    private final BDMarketManager marketManager;
    
    // Store reputation data: playerUUID -> villagerUUID -> reputation
    private final Map<UUID, Map<UUID, Integer>> reputationData = new HashMap<>();
    
    // Villager type constants for storage
    private static final String TYPE_DEALER = "DEALER";
    private static final String TYPE_COLLECTOR = "COLLECTOR";
    private static final String TYPE_MARKET_OWNER = "MARKET_OWNER";
    private static final String TYPE_SEASONAL = "SEASONAL";
    
    /**
     * Creates a new BD villager API.
     * @param plugin The plugin instance
     * @param villagerManager The villager manager
     * @param marketManager The market manager
     */
    public BDVillagerAPI(BDCraft plugin, BDVillagerManager villagerManager, BDMarketManager marketManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.villagerManager = villagerManager;
        this.marketManager = marketManager;
    }
    
    @Override
    public Villager createDealer(Location location, String marketId) {
        BDMarket market = null;
        if (marketId != null) {
            try {
                UUID marketUUID = UUID.fromString(marketId);
                market = marketManager.getMarket(marketUUID);
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid market UUID: " + marketId);
            }
        }
        
        BDDealer dealer;
        if (market != null) {
            dealer = villagerManager.createDealer(location, market);
        } else {
            dealer = villagerManager.createDealer(location);
        }
        
        // Tag the villager
        tagVillager(dealer.getVillager(), marketId, TYPE_DEALER);
        
        // Set name and custom data
        dealer.getVillager().setCustomName(ChatColor.GREEN + "BD Dealer");
        dealer.getVillager().setCustomNameVisible(true);
        
        return dealer.getVillager();
    }
    
    @Override
    public Villager createCollector(Location location, String marketId) {
        BDMarket market = null;
        if (marketId != null) {
            try {
                UUID marketUUID = UUID.fromString(marketId);
                market = marketManager.getMarket(marketUUID);
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid market UUID: " + marketId);
            }
        }
        
        BDCollector collector;
        if (market != null) {
            collector = villagerManager.createCollector(location, market);
        } else {
            collector = villagerManager.createCollector(location);
        }
        
        // Tag the villager
        tagVillager(collector.getVillager(), marketId, TYPE_COLLECTOR);
        
        // Set name and custom data
        collector.getVillager().setCustomName(ChatColor.GOLD + "BD Collector");
        collector.getVillager().setCustomNameVisible(true);
        
        return collector.getVillager();
    }
    
    @Override
    public Villager createMarketOwner(Location location, String marketId) {
        if (marketId == null) {
            return null; // Market owners must be associated with a market
        }
        
        BDMarket market = null;
        try {
            UUID marketUUID = UUID.fromString(marketId);
            market = marketManager.getMarket(marketUUID);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market UUID: " + marketId);
            return null;
        }
        
        if (market == null) {
            return null;
        }
        
        BDMarketOwner marketOwner = villagerManager.createMarketOwner(location, market);
        
        // Tag the villager
        tagVillager(marketOwner.getVillager(), marketId, TYPE_MARKET_OWNER);
        
        // Set name and custom data
        marketOwner.getVillager().setCustomName(ChatColor.AQUA + "Market Owner");
        marketOwner.getVillager().setCustomNameVisible(true);
        
        return marketOwner.getVillager();
    }
    
    @Override
    public Villager createSeasonalTrader(Location location, String marketId) {
        // Seasonal traders will need a custom implementation
        Villager villager = location.getWorld().spawn(location, Villager.class);
        
        // Tag the villager
        tagVillager(villager, marketId, TYPE_SEASONAL);
        
        // Set name and custom data
        villager.setCustomName(ChatColor.LIGHT_PURPLE + "Seasonal BD Trader");
        villager.setCustomNameVisible(true);
        
        // Set profession and other properties
        villager.setProfession(Villager.Profession.FARMER);
        villager.setVillagerType(Villager.Type.PLAINS);
        
        return villager;
    }
    
    @Override
    public boolean isBDVillager(Villager villager) {
        return villagerManager.isBDVillager(villager);
    }
    
    @Override
    public String getBDVillagerType(Villager villager) {
        return villagerManager.getBDVillagerType(villager);
    }
    
    @Override
    public int getReputation(Player player, Villager villager) {
        UUID playerUuid = player.getUniqueId();
        UUID villagerUuid = villager.getUniqueId();
        
        // Get or create player reputation map
        Map<UUID, Integer> playerReputation = reputationData.getOrDefault(playerUuid, new HashMap<>());
        
        // Get reputation or default to 0
        return playerReputation.getOrDefault(villagerUuid, 0);
    }
    
    @Override
    public void setReputation(Player player, Villager villager, int reputation) {
        UUID playerUuid = player.getUniqueId();
        UUID villagerUuid = villager.getUniqueId();
        
        // Get or create player reputation map
        Map<UUID, Integer> playerReputation = reputationData.computeIfAbsent(playerUuid, k -> new HashMap<>());
        
        // Set reputation
        playerReputation.put(villagerUuid, reputation);
    }
    
    @Override
    public int changeReputation(Player player, Villager villager, int change) {
        int currentReputation = getReputation(player, villager);
        int newReputation = currentReputation + change;
        
        // Cap reputation between -100 and 100
        newReputation = Math.max(-100, Math.min(100, newReputation));
        
        setReputation(player, villager, newReputation);
        return newReputation;
    }
    
    @Override
    public int getMarketReputation(Player player, String marketId) {
        if (marketId == null) {
            return 0;
        }
        
        BDMarket market = null;
        try {
            UUID marketUUID = UUID.fromString(marketId);
            market = marketManager.getMarket(marketUUID);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market UUID: " + marketId);
            return 0;
        }
        
        if (market == null) {
            return 0;
        }
        
        // TODO: Calculate average reputation across all market villagers
        // For now, just return a default value
        return 0;
    }
    
    @Override
    public boolean registerVillager(Villager villager, String marketId, String type) {
        if (villager == null || type == null) {
            return false;
        }
        
        // Validate type
        if (!type.equals(TYPE_DEALER) && !type.equals(TYPE_COLLECTOR) && 
            !type.equals(TYPE_MARKET_OWNER) && !type.equals(TYPE_SEASONAL)) {
            return false;
        }
        
        // Tag the villager
        tagVillager(villager, marketId, type);
        
        return true;
    }
    
    @Override
    public boolean unregisterVillager(Villager villager) {
        if (villager == null) {
            return false;
        }
        
        // Check if it's a BD villager
        if (!isBDVillager(villager)) {
            return false;
        }
        
        // Remove all BD tags
        removeVillagerTags(villager);
        
        // Remove from manager if it's tracked
        BDVillager bdVillager = villagerManager.getVillager(villager);
        if (bdVillager != null) {
            villagerManager.removeVillager(bdVillager);
        }
        
        return true;
    }
    
    @Override
    public String getMarketId(Villager villager) {
        if (!isBDVillager(villager)) {
            return null;
        }
        
        // Get market ID from persistent data
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        NamespacedKey marketKey = new NamespacedKey(plugin, "bd_market_id");
        
        if (pdc.has(marketKey, PersistentDataType.STRING)) {
            return pdc.get(marketKey, PersistentDataType.STRING);
        }
        
        return null;
    }
    
    /**
     * Tags a villager with BD data.
     * 
     * @param villager The villager to tag
     * @param marketId The market ID
     * @param type The villager type
     */
    private void tagVillager(Villager villager, String marketId, String type) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        
        // Mark as BD villager
        pdc.set(new NamespacedKey(plugin, "bd_villager"), PersistentDataType.BYTE, (byte) 1);
        
        // Set type
        pdc.set(new NamespacedKey(plugin, "bd_villager_type"), PersistentDataType.STRING, type);
        
        // Set market ID if provided
        if (marketId != null) {
            pdc.set(new NamespacedKey(plugin, "bd_market_id"), PersistentDataType.STRING, marketId);
        }
    }
    
    /**
     * Removes BD tags from a villager.
     * 
     * @param villager The villager
     */
    private void removeVillagerTags(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        
        // Remove all BD tags
        pdc.remove(new NamespacedKey(plugin, "bd_villager"));
        pdc.remove(new NamespacedKey(plugin, "bd_villager_type"));
        pdc.remove(new NamespacedKey(plugin, "bd_market_id"));
    }
    
    /**
     * Saves reputation data.
     */
    public void saveReputationData() {
        // TODO: Implement persistence for reputation data
    }
    
    /**
     * Loads reputation data.
     */
    public void loadReputationData() {
        // TODO: Implement loading of reputation data
    }
}