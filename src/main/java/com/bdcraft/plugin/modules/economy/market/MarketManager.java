package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.villager.BDVillagerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages the market system for BDCraft.
 */
public class MarketManager {
    private final BDCraft plugin;
    private BDVillagerManager villagerManager;
    private BDItemManager itemManager;
    private final Logger logger;
    
    private final Map<String, PlayerMarket> markets;
    private final Set<UUID> marketCreationCooldowns;
    
    private final int marketSize;
    private final int maxMarketsPerPlayer;
    private final long marketCreationCooldownSeconds;
    
    /**
     * Creates a new market manager.
     * @param plugin The plugin instance
     */
    public MarketManager(BDCraft plugin) {
        this.plugin = plugin;
        this.villagerManager = null; // Will be initialized later
        this.itemManager = null; // Will be initialized later
        this.logger = plugin.getLogger();
        
        this.markets = new HashMap<>();
        this.marketCreationCooldowns = new HashSet<>();
        
        // Load market settings
        ConfigurationSection economyConfig = plugin.getConfigManager().getModuleConfig("economy");
        this.marketSize = economyConfig.getInt("market.size", 49);
        this.maxMarketsPerPlayer = economyConfig.getInt("market.maxPerPlayer", 2);
        this.marketCreationCooldownSeconds = economyConfig.getLong("market.creationCooldown", 3600);
        
        // Load existing markets
        loadMarkets();
        
        // Register event listeners
        registerListeners();
    }
    
    /**
     * Loads existing markets from storage.
     */
    private void loadMarkets() {
        // In a real implementation, this would load from a database or file
        // For now, just initialize an empty map
    }
    
    /**
     * Registers event listeners.
     */
    private void registerListeners() {
        // Register the listeners when fully implemented
    }
    
    /**
     * Initializes the dependencies after all modules are loaded.
     * @param villagerManager The villager manager
     * @param itemManager The item manager
     */
    public void initialize(BDVillagerManager villagerManager, BDItemManager itemManager) {
        this.villagerManager = villagerManager;
        this.itemManager = itemManager;
    }
    
    /**
     * Attempts to create a market at the given location.
     * @param player The player
     * @param location The location
     * @return Whether the market was created
     */
    public boolean createMarket(Player player, Location location) {
        // Check permission
        if (!player.hasPermission("bdcraft.market.create")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create markets.");
            return false;
        }
        
        // Check cooldown
        if (isOnCooldown(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You must wait before creating another market.");
            return false;
        }
        
        // Check max markets per player
        if (getPlayerMarketCount(player.getUniqueId()) >= maxMarketsPerPlayer) {
            player.sendMessage(ChatColor.RED + "You have reached the maximum number of markets.");
            return false;
        }
        
        // Validate market structure
        if (!isValidMarketStructure(location)) {
            player.sendMessage(ChatColor.RED + "Invalid market structure. You need a 3x3 platform with a specific pattern.");
            return false;
        }
        
        // Check for overlapping markets
        if (isOverlappingMarket(location)) {
            player.sendMessage(ChatColor.RED + "This location overlaps with an existing market.");
            return false;
        }
        
        // Generate a unique market ID
        String marketId = UUID.randomUUID().toString().substring(0, 8);
        
        // Create market center
        Location centerLocation = location.clone().add(0, 1, 0);
        
        // Spawn market villagers
        Villager dealer = villagerManager.createDealer(centerLocation.clone().add(1, 0, 1), marketId);
        Villager owner = villagerManager.createMarketOwner(centerLocation.clone().add(-1, 0, -1), marketId);
        
        // Create market object
        PlayerMarket market = new PlayerMarket(
                marketId,
                player.getUniqueId(),
                location,
                this.marketSize,
                new ArrayList<>(),
                new HashSet<>(),
                System.currentTimeMillis()
        );
        
        // Register villagers
        market.addVillager(dealer.getUniqueId());
        market.addVillager(owner.getUniqueId());
        
        // Save market
        markets.put(marketId, market);
        
        // Apply cooldown
        setOnCooldown(player.getUniqueId());
        
        // Notify the player
        player.sendMessage(ChatColor.GREEN + "Market created successfully! Market ID: " + marketId);
        
        return true;
    }
    
    /**
     * Attempts to add a collector house to a market.
     * @param player The player
     * @param location The location
     * @return Whether the collector house was added
     */
    public boolean addCollectorHouse(Player player, Location location) {
        // Find the market at this location
        PlayerMarket market = getMarketAtLocation(location);
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "This location is not inside a market.");
            return false;
        }
        
        // Check permission
        if (!market.getOwner().equals(player.getUniqueId()) && 
            !market.getAssociates().contains(player.getUniqueId()) &&
            !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to add collector houses to this market.");
            return false;
        }
        
        // Validate house structure
        if (!isValidCollectorHouseStructure(location)) {
            player.sendMessage(ChatColor.RED + "Invalid collector house structure. Make sure it meets the requirements.");
            return false;
        }
        
        // Spawn collector villager
        Villager collector = villagerManager.createCollector(location.clone().add(0, 1, 0), market.getId());
        
        // Register villager
        market.addVillager(collector.getUniqueId());
        
        // Notify the player
        player.sendMessage(ChatColor.GREEN + "Collector house added to the market!");
        
        return true;
    }
    
    /**
     * Checks if the player is on a market creation cooldown.
     * @param uuid The player's UUID
     * @return Whether the player is on cooldown
     */
    private boolean isOnCooldown(UUID uuid) {
        return marketCreationCooldowns.contains(uuid);
    }
    
    /**
     * Sets the player on a market creation cooldown.
     * @param uuid The player's UUID
     */
    private void setOnCooldown(UUID uuid) {
        marketCreationCooldowns.add(uuid);
        
        // Remove the cooldown after the specified time
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            marketCreationCooldowns.remove(uuid);
        }, marketCreationCooldownSeconds * 20L);
    }
    
    /**
     * Gets the number of markets owned by a player.
     * @param uuid The player's UUID
     * @return The number of markets
     */
    private int getPlayerMarketCount(UUID uuid) {
        int count = 0;
        for (PlayerMarket market : markets.values()) {
            if (market.getOwner().equals(uuid)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Checks if the structure at the given location is a valid market structure.
     * @param location The location
     * @return Whether the structure is valid
     */
    private boolean isValidMarketStructure(Location location) {
        // This is a placeholder validation
        // In a full implementation, this would check for a specific pattern of blocks
        
        Block center = location.getBlock();
        if (center.getType() != Material.EMERALD_BLOCK) {
            return false;
        }
        
        // Check for surrounding blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue; // Skip center
                
                Block block = location.clone().add(x, 0, z).getBlock();
                
                // Pattern requires gold blocks in corners and iron blocks on edges
                if ((Math.abs(x) == 1 && Math.abs(z) == 1) && block.getType() != Material.GOLD_BLOCK) {
                    return false;
                } else if ((Math.abs(x) == 1 ^ Math.abs(z) == 1) && block.getType() != Material.IRON_BLOCK) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Checks if the structure at the given location is a valid collector house structure.
     * @param location The location
     * @return Whether the structure is valid
     */
    private boolean isValidCollectorHouseStructure(Location location) {
        // This is a placeholder validation
        // In a full implementation, this would check for a specific pattern of blocks
        
        Block center = location.getBlock();
        if (center.getType() != Material.DIAMOND_BLOCK) {
            return false;
        }
        
        // Check for surrounding blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue; // Skip center
                
                Block block = location.clone().add(x, 0, z).getBlock();
                
                // Pattern requires oak planks surrounding the diamond block
                if (block.getType() != Material.OAK_PLANKS) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Checks if the given location would overlap with an existing market.
     * @param location The location
     * @return Whether the location overlaps
     */
    private boolean isOverlappingMarket(Location location) {
        for (PlayerMarket market : markets.values()) {
            if (isInMarket(location, market)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if a location is inside a market.
     * @param location The location
     * @param market The market
     * @return Whether the location is inside the market
     */
    private boolean isInMarket(Location location, PlayerMarket market) {
        if (!location.getWorld().equals(market.getCenter().getWorld())) {
            return false;
        }
        
        double halfSize = market.getSize() / 2.0;
        double dx = Math.abs(location.getX() - market.getCenter().getX());
        double dz = Math.abs(location.getZ() - market.getCenter().getZ());
        
        return dx <= halfSize && dz <= halfSize;
    }
    
    /**
     * Checks if a location is inside any market.
     * @param location The location
     * @return Whether the location is inside any market
     */
    public boolean isInMarket(Location location) {
        for (PlayerMarket market : markets.values()) {
            if (isInMarket(location, market)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if a player is in their own market at a location.
     * @param player The player
     * @param location The location
     * @return Whether the player is in their own market
     */
    public boolean isPlayerInOwnMarket(Player player, Location location) {
        PlayerMarket market = getMarketAtLocation(location);
        if (market == null) {
            return false;
        }
        
        // Check if player is the owner or an associate
        return market.getOwner().equals(player.getUniqueId()) || 
               market.getAssociates().contains(player.getUniqueId());
    }
    
    /**
     * Gets the market at the given location.
     * @param location The location
     * @return The market, or null if none
     */
    public PlayerMarket getMarketAtLocation(Location location) {
        for (PlayerMarket market : markets.values()) {
            if (isInMarket(location, market)) {
                return market;
            }
        }
        return null;
    }
    
    /**
     * Gets a market by ID.
     * @param id The market ID
     * @return The market, or null if none
     */
    public PlayerMarket getMarket(String id) {
        return markets.get(id);
    }
    
    /**
     * Gets all markets owned by a player.
     * @param uuid The player's UUID
     * @return The player's markets
     */
    public List<PlayerMarket> getPlayerMarkets(UUID uuid) {
        List<PlayerMarket> playerMarkets = new ArrayList<>();
        for (PlayerMarket market : markets.values()) {
            if (market.getOwner().equals(uuid)) {
                playerMarkets.add(market);
            }
        }
        return playerMarkets;
    }
    
    /**
     * Removes a market.
     * @param id The market ID
     * @return Whether the market was removed
     */
    public boolean removeMarket(String id) {
        PlayerMarket market = markets.get(id);
        if (market == null) {
            return false;
        }
        
        // Remove villagers
        for (UUID villagerUuid : market.getVillagers()) {
            Villager villager = findVillagerByUuid(villagerUuid);
            if (villager != null) {
                villager.remove();
            }
        }
        
        // Remove market from map
        markets.remove(id);
        
        return true;
    }
    
    /**
     * Finds a villager by UUID.
     * @param uuid The villager's UUID
     * @return The villager, or null if not found
     */
    private Villager findVillagerByUuid(UUID uuid) {
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                if (villager.getUniqueId().equals(uuid)) {
                    return villager;
                }
            }
        }
        return null;
    }
    
    /**
     * Adds an associate to a market.
     * @param player The player
     * @param targetPlayer The target player
     * @param marketId The market ID
     * @return Whether the associate was added
     */
    public boolean addAssociate(Player player, Player targetPlayer, String marketId) {
        PlayerMarket market = markets.get(marketId);
        if (market == null) {
            player.sendMessage(ChatColor.RED + "Market not found.");
            return false;
        }
        
        // Check permission
        if (!market.getOwner().equals(player.getUniqueId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to add associates to this market.");
            return false;
        }
        
        // Add the associate
        market.addAssociate(targetPlayer.getUniqueId());
        
        // Notify players
        player.sendMessage(ChatColor.GREEN + "Added " + targetPlayer.getName() + " as an associate to your market.");
        targetPlayer.sendMessage(ChatColor.GREEN + "You have been added as an associate to a market owned by " + player.getName() + ".");
        
        return true;
    }
    
    /**
     * Removes an associate from a market.
     * @param player The player
     * @param targetPlayer The target player
     * @param marketId The market ID
     * @return Whether the associate was removed
     */
    public boolean removeAssociate(Player player, Player targetPlayer, String marketId) {
        PlayerMarket market = markets.get(marketId);
        if (market == null) {
            player.sendMessage(ChatColor.RED + "Market not found.");
            return false;
        }
        
        // Check permission
        if (!market.getOwner().equals(player.getUniqueId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to remove associates from this market.");
            return false;
        }
        
        // Remove the associate
        boolean removed = market.removeAssociate(targetPlayer.getUniqueId());
        
        if (!removed) {
            player.sendMessage(ChatColor.RED + targetPlayer.getName() + " is not an associate of this market.");
            return false;
        }
        
        // Notify players
        player.sendMessage(ChatColor.GREEN + "Removed " + targetPlayer.getName() + " as an associate from your market.");
        targetPlayer.sendMessage(ChatColor.YELLOW + "You have been removed as an associate from a market owned by " + player.getName() + ".");
        
        return true;
    }
    
    /**
     * Upgrades a market.
     * @param player The player
     * @param marketId The market ID
     * @return Whether the market was upgraded
     */
    public boolean upgradeMarket(Player player, String marketId) {
        PlayerMarket market = markets.get(marketId);
        if (market == null) {
            player.sendMessage(ChatColor.RED + "Market not found.");
            return false;
        }
        
        // Check permission
        if (!market.getOwner().equals(player.getUniqueId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to upgrade this market.");
            return false;
        }
        
        // Check if the market can be upgraded
        int newLevel = market.getLevel() + 1;
        ConfigurationSection economyConfig = plugin.getConfigManager().getModuleConfig("economy");
        ConfigurationSection upgradeConfig = economyConfig.getConfigurationSection("market.upgrades." + newLevel);
        
        if (upgradeConfig == null) {
            player.sendMessage(ChatColor.RED + "This market cannot be upgraded further.");
            return false;
        }
        
        // Check requirements
        int requiredCurrency = upgradeConfig.getInt("cost", 0);
        int requiredCollectors = upgradeConfig.getInt("collectors", 0);
        
        // Check currency
        if (requiredCurrency > 0) {
            if (!plugin.getEconomyAPI().hasEnough(player.getUniqueId(), requiredCurrency)) {
                player.sendMessage(ChatColor.RED + "You don't have enough currency for this upgrade. You need " + 
                        plugin.getEconomyAPI().formatCurrency(requiredCurrency) + ".");
                return false;
            }
        }
        
        // Check collectors
        if (requiredCollectors > 0) {
            int collectors = 0;
            for (UUID villagerUuid : market.getVillagers()) {
                Villager villager = findVillagerByUuid(villagerUuid);
                if (villager != null && villagerManager.getBDVillagerType(villager).equals("COLLECTOR")) {
                    collectors++;
                }
            }
            
            if (collectors < requiredCollectors) {
                player.sendMessage(ChatColor.RED + "You need at least " + requiredCollectors + 
                        " collectors in your market for this upgrade.");
                return false;
            }
        }
        
        // Apply upgrade
        market.setLevel(newLevel);
        
        // Deduct currency
        if (requiredCurrency > 0) {
            plugin.getEconomyAPI().withdrawMoney(player.getUniqueId(), requiredCurrency);
        }
        
        // Notify the player
        player.sendMessage(ChatColor.GREEN + "Market upgraded to level " + newLevel + "!");
        
        return true;
    }
}