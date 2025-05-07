package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementation of the VillagerAPI interface.
 * Handles BD villagers and their interactions.
 */
public class BDVillagerManager implements VillagerAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final NamespacedKey bdVillagerKey;
    private final NamespacedKey bdVillagerTypeKey;
    private final NamespacedKey bdMarketIdKey;
    private BDItemManager itemManager;
    
    // Reputation storage (player UUID -> villager UUID -> reputation value)
    private final Map<UUID, Map<UUID, Integer>> playerReputations;
    
    /**
     * Creates a new BD villager manager.
     * @param plugin The plugin instance
     */
    public BDVillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.bdVillagerKey = new NamespacedKey(plugin, "bd_villager");
        this.bdVillagerTypeKey = new NamespacedKey(plugin, "bd_villager_type");
        this.bdMarketIdKey = new NamespacedKey(plugin, "bd_market_id");
        this.playerReputations = new HashMap<>();
    }
    
    /**
     * Sets the item manager (called by BDEconomyModule after item manager is initialized).
     * @param itemManager The item manager
     */
    public void setItemManager(BDItemManager itemManager) {
        this.itemManager = itemManager;
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
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        if (itemManager == null) {
            logger.warning("Item manager not set for villager manager - trades will not be properly set up");
            villager.setRecipes(recipes);
            return;
        }
        
        switch (type) {
            case "DEALER":
                addDealerTrades(recipes);
                break;
            case "COLLECTOR":
                addCollectorTrades(recipes);
                break;
            case "MARKET_OWNER":
                addMarketOwnerTrades(recipes);
                break;
            case "SEASONAL":
                addSeasonalTraderTrades(recipes);
                break;
            default:
                logger.warning("Unknown BD villager type: " + type);
                break;
        }
        
        villager.setRecipes(recipes);
    }
    
    /**
     * Adds trades for BD Dealers.
     * @param recipes The recipes to add to
     */
    private void addDealerTrades(List<MerchantRecipe> recipes) {
        // Regular BD Seeds (wheat): 5 seeds for 1 emerald
        MerchantRecipe regularSeedRecipe = new MerchantRecipe(
                itemManager.createBDSeed(5), // Result (5 regular BD seeds)
                10 // Max uses before restocking
        );
        regularSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        recipes.add(regularSeedRecipe);
        
        // Green BD Seeds (beetroot): 1 seed for 9 emeralds
        MerchantRecipe greenSeedRecipe = new MerchantRecipe(
                itemManager.createGreenBDSeed(1), // Result (1 green BD seed)
                5 // Max uses before restocking
        );
        greenSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 9));
        recipes.add(greenSeedRecipe);
        
        // Purple BD Seeds (pumpkin): 1 seed for 25 emeralds (rare)
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(
                itemManager.createPurpleBDSeed(1), // Result (1 purple BD seed)
                2 // Max uses before restocking (very limited)
        );
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        recipes.add(purpleSeedRecipe);
        
        // BD Harvester: Special tool for 16 diamonds
        MerchantRecipe harvesterRecipe = new MerchantRecipe(
                itemManager.createBDHarvester(), // Result (BD Harvester)
                3 // Max uses before restocking
        );
        harvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 16));
        recipes.add(harvesterRecipe);
    }
    
    /**
     * Adds trades for BD Collectors.
     * @param recipes The recipes to add to
     */
    private void addCollectorTrades(List<MerchantRecipe> recipes) {
        // Regular BD Crops: 10 crops for 2 emeralds
        MerchantRecipe regularCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 2), // Result (2 emeralds)
                50 // Max uses before restocking
        );
        regularCropRecipe.addIngredient(itemManager.createBDCrop(10)); // 10 regular BD crops
        recipes.add(regularCropRecipe);
        
        // Green BD Crops: 5 crops for 10 emeralds
        MerchantRecipe greenCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 10), // Result (10 emeralds)
                30 // Max uses before restocking
        );
        greenCropRecipe.addIngredient(itemManager.createGreenBDCrop(5)); // 5 green BD crops
        recipes.add(greenCropRecipe);
        
        // Purple BD Crops: 3 crops for 20 emeralds
        MerchantRecipe purpleCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 20), // Result (20 emeralds)
                15 // Max uses before restocking
        );
        purpleCropRecipe.addIngredient(itemManager.createPurpleBDCrop(3)); // 3 purple BD crops
        recipes.add(purpleCropRecipe);
        
        // Bulk trade: 50 regular BD crops for 1 diamond
        MerchantRecipe bulkCropRecipe = new MerchantRecipe(
                new ItemStack(Material.DIAMOND, 1), // Result (1 diamond)
                5 // Max uses before restocking
        );
        bulkCropRecipe.addIngredient(itemManager.createBDCrop(50)); // 50 regular BD crops
        recipes.add(bulkCropRecipe);
    }
    
    /**
     * Adds trades for Market Owner villagers.
     * @param recipes The recipes to add to
     */
    private void addMarketOwnerTrades(List<MerchantRecipe> recipes) {
        // Market upgrade to level 2: 20 emeralds + 5 diamonds
        MerchantRecipe upgradeRecipe = new MerchantRecipe(
                createUpgradeCertificate(2), // Result (Level 2 upgrade certificate)
                1 // Max uses before restocking
        );
        upgradeRecipe.addIngredient(new ItemStack(Material.EMERALD, 20));
        upgradeRecipe.addIngredient(new ItemStack(Material.DIAMOND, 5));
        recipes.add(upgradeRecipe);
        
        // House token: 3 diamonds + 10 emeralds
        MerchantRecipe houseTokenRecipe = new MerchantRecipe(
                itemManager.createHouseToken(), // Result (House Token)
                3 // Max uses before restocking
        );
        houseTokenRecipe.addIngredient(new ItemStack(Material.DIAMOND, 3));
        houseTokenRecipe.addIngredient(new ItemStack(Material.EMERALD, 10));
        recipes.add(houseTokenRecipe);
        
        // Market information: 1 emerald
        MerchantRecipe infoRecipe = new MerchantRecipe(
                createMarketInfoBook(), // Result (Market info book)
                10 // Max uses before restocking
        );
        infoRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        recipes.add(infoRecipe);
    }
    
    /**
     * Adds trades for Seasonal BD Trader villagers.
     * @param recipes The recipes to add to
     */
    private void addSeasonalTraderTrades(List<MerchantRecipe> recipes) {
        // Season-specific special items
        // For simplicity, we'll add a few example items
        
        // Ultimate BD Harvester: 32 diamonds
        MerchantRecipe ultimateHarvesterRecipe = new MerchantRecipe(
                itemManager.createUltimateBDHarvester(), // Result (Ultimate BD Harvester)
                1 // Max uses before restocking (very limited)
        );
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 32));
        recipes.add(ultimateHarvesterRecipe);
        
        // Rare Purple BD Seeds: 15 emeralds + 1 diamond (better price than dealer)
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(
                itemManager.createPurpleBDSeed(2), // Result (2 purple BD seeds)
                1 // Max uses before restocking (extremely limited)
        );
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 15));
        purpleSeedRecipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        recipes.add(purpleSeedRecipe);
    }
    
    /**
     * Creates a special certificate item for market upgrades.
     * @param level The upgrade level
     * @return The upgrade certificate item
     */
    private ItemStack createUpgradeCertificate(int level) {
        ItemStack certificate = new ItemStack(Material.PAPER);
        ItemMeta meta = certificate.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "Market Upgrade Certificate (Level " + level + ")");
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right-click to upgrade your market to level " + level);
        lore.add(ChatColor.GRAY + "Must be used by the market owner");
        
        meta.setLore(lore);
        certificate.setItemMeta(meta);
        
        return certificate;
    }
    
    /**
     * Creates a special book with market information.
     * @return The market information book
     */
    private ItemStack createMarketInfoBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "Market Information");
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Contains market information and stats");
        
        meta.setLore(lore);
        book.setItemMeta(meta);
        
        return book;
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
        if (villager == null) {
            return false;
        }
        
        // Set custom name
        villager.setCustomName("BD " + type);
        villager.setCustomNameVisible(true);
        
        // Set persistent data
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(bdVillagerKey, PersistentDataType.BYTE, (byte) 1);
        pdc.set(bdVillagerTypeKey, PersistentDataType.STRING, type);
        
        if (marketId != null) {
            pdc.set(bdMarketIdKey, PersistentDataType.STRING, marketId);
        }
        
        // Add trades
        addTradesForType(villager, type);
        
        return true;
    }
    
    @Override
    public boolean unregisterVillager(Villager villager) {
        if (!isBDVillager(villager)) {
            return false;
        }
        
        // Remove BD metadata
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.remove(bdVillagerKey);
        pdc.remove(bdVillagerTypeKey);
        pdc.remove(bdMarketIdKey);
        
        // Remove custom name
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
        
        // Clear trades
        villager.setRecipes(new ArrayList<>());
        
        return true;
    }
    
    @Override
    public String getMarketId(Villager villager) {
        if (!isBDVillager(villager)) {
            return null;
        }
        
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        return pdc.has(bdMarketIdKey, PersistentDataType.STRING) ?
                pdc.get(bdMarketIdKey, PersistentDataType.STRING) : null;
    }
}