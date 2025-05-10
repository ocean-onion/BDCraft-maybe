package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.VillagerAPI;
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
 * Implementation of the VillagerAPI interface for BD villagers.
 */
public class VillagerAPIImpl implements VillagerAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final VillagerManager villagerManager;
    
    // Store reputation data: playerUUID -> villagerUUID -> reputation
    private final Map<UUID, Map<UUID, Integer>> reputationData = new HashMap<>();
    
    // Reputation bounds
    private static final int MAX_REPUTATION = 100;
    private static final int MIN_REPUTATION = -100;
    
    /**
     * Creates a new VillagerAPI implementation.
     *
     * @param plugin The plugin instance
     * @param villagerManager The villager manager
     */
    public VillagerAPIImpl(BDCraft plugin, VillagerManager villagerManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.villagerManager = villagerManager;
    }
    
    /**
     * Gets a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @return The reputation
     */
    @Override
    public int getReputation(Player player, Villager villager) {
        if (player == null || villager == null) {
            return 0;
        }
        
        UUID playerUUID = player.getUniqueId();
        UUID villagerUUID = villager.getUniqueId();
        
        // If we have reputation data for this player
        if (reputationData.containsKey(playerUUID)) {
            Map<UUID, Integer> playerRep = reputationData.get(playerUUID);
            
            // If we have reputation data for this villager
            if (playerRep.containsKey(villagerUUID)) {
                return playerRep.get(villagerUUID);
            }
        }
        
        // Default reputation is 0
        return 0;
    }
    
    /**
     * Changes a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @param change The change
     * @return The new reputation
     */
    @Override
    public int changeReputation(Player player, Villager villager, int change) {
        if (player == null || villager == null || change == 0) {
            return getReputation(player, villager);
        }
        
        int currentRep = getReputation(player, villager);
        int newRep = Math.max(MIN_REPUTATION, Math.min(MAX_REPUTATION, currentRep + change));
        
        setReputation(player, villager, newRep);
        return newRep;
    }
    
    /**
     * Sets a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @param reputation The reputation
     */
    @Override
    public void setReputation(Player player, Villager villager, int reputation) {
        if (player == null || villager == null) {
            return;
        }
        
        UUID playerUUID = player.getUniqueId();
        UUID villagerUUID = villager.getUniqueId();
        
        // Get or create reputation map for this player
        Map<UUID, Integer> playerRep = reputationData.computeIfAbsent(
            playerUUID, k -> new HashMap<>());
        
        // Set the reputation, clamped to min/max
        int clampedRep = Math.max(MIN_REPUTATION, Math.min(MAX_REPUTATION, reputation));
        playerRep.put(villagerUUID, clampedRep);
    }
    
    /**
     * Gets the maximum reputation a player can have with a villager.
     *
     * @return The maximum reputation
     */
    @Override
    public int getMaxReputation() {
        return MAX_REPUTATION;
    }
    
    /**
     * Gets the minimum reputation a player can have with a villager.
     *
     * @return The minimum reputation
     */
    @Override
    public int getMinReputation() {
        return MIN_REPUTATION;
    }
    
    /**
     * Checks if a villager is a BDVillager.
     *
     * @param villager The villager to check
     * @return True if the villager is a BDVillager
     */
    @Override
    public boolean isBDVillager(Villager villager) {
        if (villager == null) {
            return false;
        }
        
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        NamespacedKey bdKey = new NamespacedKey(plugin, "bd_villager");
        
        return pdc.has(bdKey, PersistentDataType.BYTE);
    }
    
    /**
     * Gets the type of a BDVillager.
     *
     * @param villager The villager
     * @return The villager type, or null if not a BDVillager
     */
    @Override
    public String getBDVillagerType(Villager villager) {
        if (villager == null || !isBDVillager(villager)) {
            return null;
        }
        
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        NamespacedKey typeKey = new NamespacedKey(plugin, "bd_villager_type");
        
        if (pdc.has(typeKey, PersistentDataType.STRING)) {
            return pdc.get(typeKey, PersistentDataType.STRING);
        }
        
        return null;
    }
}