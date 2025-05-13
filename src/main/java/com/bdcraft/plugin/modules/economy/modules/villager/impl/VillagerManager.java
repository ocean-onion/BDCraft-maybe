package com.bdcraft.plugin.modules.economy.modules.villager.impl;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages basic villager functionality in the BDCraft plugin.
 */
public class VillagerManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, BDVillager> villagers = new HashMap<>();
    
    /**
     * Creates a new VillagerManager.
     *
     * @param plugin The BDCraft plugin instance
     */
    public VillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Initializes the VillagerManager by loading villagers from configuration.
     */
    public void initialize() {
        logger.info("Initializing VillagerManager...");
        loadVillagers();
    }
    
    /**
     * Loads villagers from configuration.
     */
    private void loadVillagers() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection villagersSection = config.getConfigurationSection("villagers");
        
        if (villagersSection != null) {
            for (String key : villagersSection.getKeys(false)) {
                try {
                    ConfigurationSection villagerSection = villagersSection.getConfigurationSection(key);
                    if (villagerSection != null) {
                        UUID uuid = UUID.fromString(key);
                        String type = villagerSection.getString("type", "default");
                        UUID ownerUUID = UUID.fromString(villagerSection.getString("owner", ""));
                        
                        // Location details
                        ConfigurationSection locSection = villagerSection.getConfigurationSection("location");
                        if (locSection != null) {
                            // Implementation to load location would go here
                            // For now, we'll just use a placeholder
                            // Location location = new Location(...)
                            
                            // Create the villager (commented out due to missing location)
                            // BDVillager villager = new BDVillager(uuid, type, ownerUUID, location);
                            // villagers.put(uuid, villager);
                        }
                    }
                } catch (Exception e) {
                    logger.warning("Error loading villager with key " + key + ": " + e.getMessage());
                }
            }
        }
        
        logger.info("Loaded " + villagers.size() + " villagers.");
    }
    
    /**
     * Creates a new villager for a player.
     *
     * @param player The player creating the villager
     * @param type The type of villager to create
     * @return The created BDVillager, or null if creation failed
     */
    public BDVillager createVillager(Player player, String type) {
        UUID villagerUUID = UUID.randomUUID();
        Location location = player.getLocation();
        
        BDVillager villager = new BDVillager(villagerUUID, type, player.getUniqueId(), location);
        villagers.put(villagerUUID, villager);
        
        // Additional implementation to spawn the actual entity would go here
        
        saveVillager(villager);
        return villager;
    }
    
    /**
     * Gets a villager by UUID.
     *
     * @param uuid The UUID of the villager
     * @return The BDVillager, or null if not found
     */
    public BDVillager getVillager(UUID uuid) {
        return villagers.get(uuid);
    }
    
    /**
     * Removes a villager.
     *
     * @param uuid The UUID of the villager to remove
     * @return true if the villager was removed, false otherwise
     */
    public boolean removeVillager(UUID uuid) {
        BDVillager villager = villagers.remove(uuid);
        if (villager != null) {
            // Remove the villager from the game world
            if (villager.getEntity() != null) {
                villager.getEntity().remove();
            }
            
            // Remove from configuration
            FileConfiguration config = plugin.getConfig();
            ConfigurationSection villagersSection = config.getConfigurationSection("villagers");
            if (villagersSection != null) {
                villagersSection.set(uuid.toString(), null);
                plugin.saveConfig();
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Saves a villager to configuration.
     *
     * @param villager The villager to save
     */
    private void saveVillager(BDVillager villager) {
        FileConfiguration config = plugin.getConfig();
        String path = "villagers." + villager.getUUID().toString();
        
        config.set(path + ".type", villager.getType());
        config.set(path + ".owner", villager.getOwnerUUID().toString());
        
        Location loc = villager.getLocation();
        if (loc != null && loc.getWorld() != null) {
            config.set(path + ".location.world", loc.getWorld().getName());
            config.set(path + ".location.x", loc.getX());
            config.set(path + ".location.y", loc.getY());
            config.set(path + ".location.z", loc.getZ());
            config.set(path + ".location.yaw", loc.getYaw());
            config.set(path + ".location.pitch", loc.getPitch());
        }
        
        plugin.saveConfig();
    }
    
    /**
     * Cleans up resources used by the VillagerManager.
     */
    public void cleanup() {
        // Save all villagers before shutting down
        for (BDVillager villager : villagers.values()) {
            saveVillager(villager);
        }
        
        villagers.clear();
    }
}