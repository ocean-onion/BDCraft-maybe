package com.bdcraft.plugin.modules.economy.modules.villager.impl;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages enhanced villager functionality specific to BDCraft.
 * Extends the basic villager management with BDCraft-specific features.
 */
public class BDVillagerManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final VillagerManager villagerManager;
    private final Map<String, VillagerType> villagerTypes = new HashMap<>();
    
    /**
     * Creates a new BDVillagerManager.
     *
     * @param plugin The BDCraft plugin instance
     * @param villagerManager The basic VillagerManager instance
     */
    public BDVillagerManager(BDCraft plugin, VillagerManager villagerManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.villagerManager = villagerManager;
    }
    
    /**
     * Initializes the BDVillagerManager by registering villager types.
     */
    public void initialize() {
        logger.info("Initializing BDVillagerManager...");
        registerVillagerTypes();
    }
    
    /**
     * Registers available villager types.
     */
    private void registerVillagerTypes() {
        // Register default villager types
        registerVillagerType("trader", "Trader Villager", Villager.Profession.FARMER);
        registerVillagerType("banker", "Banker Villager", Villager.Profession.LIBRARIAN);
        registerVillagerType("questgiver", "Quest Giver", Villager.Profession.CLERIC);
    }
    
    /**
     * Registers a villager type.
     *
     * @param id The internal ID of the villager type
     * @param displayName The display name of the villager type
     * @param profession The Bukkit profession for this villager type
     */
    public void registerVillagerType(String id, String displayName, Villager.Profession profession) {
        VillagerType type = new VillagerType(id, displayName, profession);
        villagerTypes.put(id, type);
        logger.info("Registered villager type: " + id);
    }
    
    /**
     * Creates a BDCraft villager of the specified type.
     *
     * @param player The player creating the villager
     * @param typeId The type ID of the villager to create
     * @return The created BDVillager, or null if creation failed
     */
    public BDVillager createVillager(Player player, String typeId) {
        if (!villagerTypes.containsKey(typeId)) {
            logger.warning("Unknown villager type: " + typeId);
            return null;
        }
        
        BDVillager villager = villagerManager.createVillager(player, typeId);
        if (villager != null) {
            // Spawn the actual entity
            Location location = villager.getLocation();
            if (location.getWorld() != null) {
                Villager entity = location.getWorld().spawn(location, Villager.class);
                
                // Apply type-specific settings
                VillagerType type = villagerTypes.get(typeId);
                entity.setProfession(type.getProfession());
                entity.setCustomName(type.getDisplayName());
                entity.setCustomNameVisible(true);
                
                villager.setEntity(entity);
            }
        }
        
        return villager;
    }
    
    /**
     * Gets all registered villager types.
     *
     * @return A map of villager type IDs to VillagerType objects
     */
    public Map<String, VillagerType> getVillagerTypes() {
        return new HashMap<>(villagerTypes);
    }
    
    /**
     * Internal class representing a type of BDCraft villager.
     */
    public static class VillagerType {
        private final String id;
        private final String displayName;
        private final Villager.Profession profession;
        
        /**
         * Creates a new VillagerType.
         *
         * @param id The internal ID of the type
         * @param displayName The display name of the type
         * @param profession The Bukkit profession for this type
         */
        public VillagerType(String id, String displayName, Villager.Profession profession) {
            this.id = id;
            this.displayName = displayName;
            this.profession = profession;
        }
        
        /**
         * Gets the internal ID of this type.
         *
         * @return The type ID
         */
        public String getId() {
            return id;
        }
        
        /**
         * Gets the display name of this type.
         *
         * @return The display name
         */
        public String getDisplayName() {
            return displayName;
        }
        
        /**
         * Gets the Bukkit profession for this type.
         *
         * @return The profession
         */
        public Villager.Profession getProfession() {
            return profession;
        }
    }
}