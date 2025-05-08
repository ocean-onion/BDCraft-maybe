package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.villagers.BDVillager.VillagerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all BD villagers.
 */
public class VillagerManager implements Listener {
    private final BDCraft plugin;
    private final Map<UUID, BDVillager> villagers;
    
    /**
     * Creates a new villager manager.
     * 
     * @param plugin The plugin instance
     */
    public VillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.villagers = new HashMap<>();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Creates a new market owner villager.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The created villager
     */
    public MarketOwnerVillager createMarketOwner(BDMarket market, Location location) {
        MarketOwnerVillager villager = new MarketOwnerVillager(plugin, location, market);
        
        villagers.put(villager.getUuid(), villager);
        return villager;
    }
    
    /**
     * Creates a new BD dealer villager.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The created villager
     */
    public BDDealerVillager createBDDealer(BDMarket market, Location location) {
        UUID uuid = UUID.randomUUID();
        BDDealerVillager villager = new BDDealerVillager(plugin, uuid, market, null);
        villager.spawn(location);
        
        villagers.put(uuid, villager);
        return villager;
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
        if (!container.has(new NamespacedKey(plugin, "bd_villager_uuid"), PersistentDataType.STRING)) {
            return null;
        }
        
        String uuidString = container.get(new NamespacedKey(plugin, "bd_villager_uuid"), PersistentDataType.STRING);
        return villagers.get(UUID.fromString(uuidString));
    }
    
    /**
     * Gets all villagers of a specific type.
     * 
     * @param type The villager type
     * @return A list of villagers of the specified type
     */
    public List<BDVillager> getVillagers(VillagerType type) {
        List<BDVillager> result = new ArrayList<>();
        
        for (BDVillager villager : villagers.values()) {
            if (villager.getBDType() == type) {
                result.add(villager);
            }
        }
        
        return result;
    }
    
    /**
     * Gets all market owner villagers.
     * 
     * @return A list of market owner villagers
     */
    public List<MarketOwnerVillager> getMarketOwners() {
        List<MarketOwnerVillager> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(VillagerType.MARKET_OWNER)) {
            result.add((MarketOwnerVillager) villager);
        }
        
        return result;
    }
    
    /**
     * Gets all BD dealer villagers.
     * 
     * @return A list of BD dealer villagers
     */
    public List<BDDealerVillager> getBDDealers() {
        List<BDDealerVillager> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(VillagerType.BD_DEALER)) {
            result.add((BDDealerVillager) villager);
        }
        
        return result;
    }
    
    /**
     * Removes a villager.
     * 
     * @param uuid The UUID of the villager to remove
     */
    public void removeVillager(UUID uuid) {
        BDVillager villager = villagers.get(uuid);
        if (villager != null) {
            villager.despawn();
            villagers.remove(uuid);
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
     * Handles a market upgrade.
     * 
     * @param market The market that was upgraded
     */
    public void handleMarketUpgrade(BDMarket market) {
        // Update trades for all villagers in this market
        for (MarketOwnerVillager owner : getMarketOwners()) {
            if (owner.getMarket().equals(market)) {
                owner.updateTrades();
            }
        }
        
        for (BDDealerVillager dealer : getBDDealers()) {
            if (dealer.getMarket().equals(market)) {
                dealer.updateTrades();
            }
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
        
        // Handle specific villager interactions here if needed
        if (bdVillager.getBDType() == VillagerType.MARKET_OWNER) {
            // Additional market owner interaction logic can go here
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
            event.setCancelled(true);
        }
    }
    
    /**
     * Handles trade result processing.
     */
    @EventHandler
    public void onTradeProcess(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory)) {
            return;
        }
        
        // Only process when clicking the result slot in a merchant inventory
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        
        MerchantInventory merchantInv = (MerchantInventory) event.getInventory();
        Villager villager = (Villager) merchantInv.getMerchant();
        
        BDVillager bdVillager = getVillager(villager);
        if (bdVillager == null) {
            return;
        }
        
        // Handle specific trade results here
        if (bdVillager.getBDType() == VillagerType.MARKET_OWNER) {
            MarketOwnerVillager marketOwner = (MarketOwnerVillager) bdVillager;
            MerchantRecipe recipe = merchantInv.getMerchant().getRecipe(merchantInv.getSelectedRecipeIndex());
            
            if (recipe.getResult().getType() == org.bukkit.Material.PAPER) {
                // Handle market upgrade
                event.setCancelled(true);
                
                // Process the upgrade
                marketOwner.getMarket().upgrade();
                
                // Consume the ingredients
                event.getWhoClicked().sendMessage("§aYour market has been upgraded to level " + 
                        marketOwner.getMarket().getLevel() + "!");
                
                // Update trades
                handleMarketUpgrade(marketOwner.getMarket());
            }
        }
    }
}