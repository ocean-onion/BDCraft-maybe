package com.bdcraft.plugin.modules.economy.modules.villager.impl;

import com.bdcraft.plugin.api.VillagerAPI;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the VillagerAPI interface.
 */
public class VillagerAPIImpl implements VillagerAPI {
    private final JavaPlugin plugin;
    private final Map<UUID, Map<UUID, Integer>> reputations = new HashMap<>();
    private final int minReputation = -100;
    private final int maxReputation = 100;
    private final VillagerManager villagerManager;
    
    /**
     * Creates a new VillagerAPIImpl.
     *
     * @param plugin The plugin instance
     * @param villagerManager The villager manager
     */
    public VillagerAPIImpl(JavaPlugin plugin, VillagerManager villagerManager) {
        this.plugin = plugin;
        this.villagerManager = villagerManager;
    }
    
    @Override
    public int getReputation(Player player, Villager villager) {
        Map<UUID, Integer> playerReps = reputations.getOrDefault(villager.getUniqueId(), new HashMap<>());
        return playerReps.getOrDefault(player.getUniqueId(), 0);
    }
    
    @Override
    public int changeReputation(Player player, Villager villager, int change) {
        int current = getReputation(player, villager);
        int newRep = Math.max(minReputation, Math.min(maxReputation, current + change));
        setReputation(player, villager, newRep);
        return newRep;
    }
    
    @Override
    public void setReputation(Player player, Villager villager, int reputation) {
        Map<UUID, Integer> playerReps = reputations.computeIfAbsent(villager.getUniqueId(), k -> new HashMap<>());
        playerReps.put(player.getUniqueId(), Math.max(minReputation, Math.min(maxReputation, reputation)));
    }
    
    @Override
    public int getMaxReputation() {
        return maxReputation;
    }
    
    @Override
    public int getMinReputation() {
        return minReputation;
    }
    
    @Override
    public boolean isBDVillager(Villager villager) {
        if (villager.hasMetadata("bdvillager")) {
            List<MetadataValue> values = villager.getMetadata("bdvillager");
            for (MetadataValue value : values) {
                if (value.getOwningPlugin() == plugin) {
                    return value.asBoolean();
                }
            }
        }
        return false;
    }
    
    @Override
    public String getBDVillagerType(Villager villager) {
        if (!isBDVillager(villager)) {
            return null;
        }
        
        if (villager.hasMetadata("bdvillager_type")) {
            List<MetadataValue> values = villager.getMetadata("bdvillager_type");
            for (MetadataValue value : values) {
                if (value.getOwningPlugin() == plugin) {
                    return value.asString();
                }
            }
        }
        return null;
    }
    
    /**
     * Marks a villager as a BDVillager.
     *
     * @param villager The villager entity
     * @param type The villager type
     */
    public void markAsBDVillager(Villager villager, String type) {
        villager.setMetadata("bdvillager", new FixedMetadataValue(plugin, true));
        villager.setMetadata("bdvillager_type", new FixedMetadataValue(plugin, type));
    }
}