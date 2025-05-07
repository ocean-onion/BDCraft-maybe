package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.VillagerAPI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the VillagerAPI interface.
 * Handles BD villagers and their interactions.
 */
public class BDVillagerManager implements VillagerAPI {
    private final BDCraft plugin;
    private final NamespacedKey bdVillagerKey;
    private final NamespacedKey bdVillagerTypeKey;
    private final NamespacedKey bdMarketIdKey;
    
    // Reputation storage (player UUID -> villager UUID -> reputation value)
    private final Map<UUID, Map<UUID, Integer>> playerReputations;
    
    /**
     * Creates a new BD villager manager.
     * @param plugin The plugin instance
     */
    public BDVillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdMarketIdKey = new NamespacedKey(plugin, "bd_market_id");
        this.playerReputations = new HashMap<>();
    }
    
    @Override
    public Villager createDealer(Location location, String marketId) {
        return createBDVillager(location, marketId, "DEALER", Villager.Profession.FARMER, Villager.Type.PLAINS);
    }
    
    @Override
    public Villager createCollector(Location location, String marketId) {
        return createBDVillager(location, marketId, "COLLECTOR", Villager.Profession.LIBRARIAN, Villager.Type.PLAINS);
    }
    
    @Override
    public Villager createMarketOwner(Location location, String marketId) {
        return createBDVillager(location, marketId, "MARKET_OWNER", Villager.Profession.NITWIT, Villager.Type.PLAINS);
    }
    
    @Override
    public Villager createSeasonalTrader(Location location, String marketId) {
        return createBDVillager(location, marketId, "SEASONAL", Villager.Profession.MASON, Villager.Type.SWAMP);
    }
    
    /**
     * Creates a BD villager with the specified properties.
     * @param location The location to spawn the villager
     * @param marketId The market ID, or null if not in a market
     * @param type The BD villager type
     * @param profession The villager profession
     * @param villagerType The villager type
     * @return The spawned villager
     */
    private Villager createBDVillager(Location location, String marketId, String type, Villager.Profession profession, Villager.Type villagerType) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Set basic properties
        villager.setProfession(profession);
        villager.setVillagerType(villagerType);
        villager.setCustomName("BD " + type);
        villager.setCustomNameVisible(true);
        villager.setAI(true);
        villager.setCanPickupItems(false);
        villager.setRemoveWhenFarAway(false);
        
        // Set persistent data
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(bdVillagerKey, PersistentDataType.BYTE, (byte) 1);
        pdc.set(bdVillagerTypeKey, PersistentDataType.STRING, type);
        
        if (marketId != null) {
            pdc.set(bdMarketIdKey, PersistentDataType.STRING, marketId);
        }
        
        // Add appropriate trades based on type
        addTradesForType(villager, type);
        
        return villager;
    }
    
    /**
     * Adds appropriate trades for a specific villager type.
     * @param villager The villager
     * @param type The BD villager type
     */
    private void addTradesForType(Villager villager, String type) {
        // This would be expanded to add different trades based on type
        // For now, just clearing trades to avoid defaults
        villager.setRecipes(new java.util.ArrayList<MerchantRecipe>());
    }
    
    @Override
    public boolean isBDVillager(Villager villager) {
        return villager.getPersistentDataContainer().has(bdVillagerKey, PersistentDataType.BYTE);
    }
    
    @Override
    public String getBDVillagerType(Villager villager) {
        if (!isBDVillager(villager)) {
            return null;
        }
        
        return villager.getPersistentDataContainer().get(bdVillagerTypeKey, PersistentDataType.STRING);
    }
    
    @Override
    public int getReputation(Player player, Villager villager) {
        if (!isBDVillager(villager)) {
            return 0;
        }
        
        UUID playerUUID = player.getUniqueId();
        UUID villagerUUID = villager.getUniqueId();
        
        Map<UUID, Integer> reputations = playerReputations.get(playerUUID);
        if (reputations == null) {
            return 0;
        }
        
        return reputations.getOrDefault(villagerUUID, 0);
    }
    
    @Override
    public void setReputation(Player player, Villager villager, int reputation) {
        if (!isBDVillager(villager)) {
            return;
        }
        
        UUID playerUUID = player.getUniqueId();
        UUID villagerUUID = villager.getUniqueId();
        
        Map<UUID, Integer> reputations = playerReputations.computeIfAbsent(playerUUID, k -> new HashMap<>());
        reputations.put(villagerUUID, reputation);
    }
    
    @Override
    public int changeReputation(Player player, Villager villager, int change) {
        if (!isBDVillager(villager)) {
            return 0;
        }
        
        int currentReputation = getReputation(player, villager);
        int newReputation = currentReputation + change;
        
        setReputation(player, villager, newReputation);
        
        return newReputation;
    }
    
    @Override
    public int getMarketReputation(Player player, String marketId) {
        if (marketId == null) {
            return 0;
        }
        
        // Find all villagers in this market and average their reputation
        int totalReputation = 0;
        int villagerCount = 0;
        
        UUID playerUUID = player.getUniqueId();
        Map<UUID, Integer> reputations = playerReputations.get(playerUUID);
        
        if (reputations == null || reputations.isEmpty()) {
            return 0;
        }
        
        for (Villager villager : player.getWorld().getEntitiesByClass(Villager.class)) {
            if (!isBDVillager(villager)) {
                continue;
            }
            
            String vilMarketId = getMarketId(villager);
            if (marketId.equals(vilMarketId)) {
                totalReputation += reputations.getOrDefault(villager.getUniqueId(), 0);
                villagerCount++;
            }
        }
        
        return villagerCount > 0 ? totalReputation / villagerCount : 0;
    }
    
    @Override
    public boolean registerVillager(Villager villager, String marketId, String type) {
        if (villager.getPersistentDataContainer().has(bdVillagerKey, PersistentDataType.BYTE)) {
            return false; // Already a BD villager
        }
        
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(bdVillagerKey, PersistentDataType.BYTE, (byte) 1);
        pdc.set(bdVillagerTypeKey, PersistentDataType.STRING, type);
        
        if (marketId != null) {
            pdc.set(bdMarketIdKey, PersistentDataType.STRING, marketId);
        }
        
        // Add appropriate trades based on type
        addTradesForType(villager, type);
        
        return true;
    }
    
    @Override
    public boolean unregisterVillager(Villager villager) {
        if (!isBDVillager(villager)) {
            return false;
        }
        
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.remove(bdVillagerKey);
        pdc.remove(bdVillagerTypeKey);
        pdc.remove(bdMarketIdKey);
        
        // Reset name
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
        
        return true;
    }
    
    @Override
    public String getMarketId(Villager villager) {
        if (!isBDVillager(villager)) {
            return null;
        }
        
        return villager.getPersistentDataContainer().has(bdMarketIdKey, PersistentDataType.STRING) ?
               villager.getPersistentDataContainer().get(bdMarketIdKey, PersistentDataType.STRING) : null;
    }
}