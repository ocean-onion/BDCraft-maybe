package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.VillagerAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Manages player-created markets and associated villagers.
 */
public class MarketManager {
    private final BDCraft plugin;
    private final Map<String, PlayerMarket> markets; // Market ID -> Market
    private final Map<UUID, String> playerMarkets; // Player UUID -> Market ID
    private final Map<String, UUID> marketOwners; // Market ID -> Owner UUID
    private final Map<String, Set<UUID>> marketAssociates; // Market ID -> Set of Associate UUIDs
    private final Map<UUID, String> villagerToMarket; // Villager UUID -> Market ID

    private final NamespacedKey marketKey;
    private final NamespacedKey marketLevelKey;
    private final NamespacedKey marketStructureKey;
    
    /**
     * Creates a new market manager.
     * @param plugin The plugin instance
     */
    public MarketManager(BDCraft plugin) {
        this.plugin = plugin;
        this.markets = new ConcurrentHashMap<>();
        this.playerMarkets = new ConcurrentHashMap<>();
        this.marketOwners = new ConcurrentHashMap<>();
        this.marketAssociates = new ConcurrentHashMap<>();
        this.villagerToMarket = new ConcurrentHashMap<>();
        
        this.marketKey = new NamespacedKey(plugin, "bd_market");
        this.marketLevelKey = new NamespacedKey(plugin, "bd_market_level");
        this.marketStructureKey = new NamespacedKey(plugin, "bd_market_structure");
        
        loadMarkets();
    }
    
    /**
     * Loads markets from configuration.
     */
    private void loadMarkets() {
        // Configuration loading implementation
        plugin.getLogger().info("Loading markets from configuration...");
        
        // This would be expanded to load from a file/database
        // For now, just initialize empty collections
        markets.clear();
        playerMarkets.clear();
        marketOwners.clear();
        marketAssociates.clear();
        villagerToMarket.clear();
    }
    
    /**
     * Saves markets to configuration.
     */
    public void saveMarkets() {
        // Configuration saving implementation
        plugin.getLogger().info("Saving markets to configuration...");
        
        // This would be expanded to save to a file/database
    }
    
    /**
     * Creates a new market.
     * @param player The market owner
     * @param location The market center location
     * @return True if the market was created successfully
     */
    public boolean createMarket(Player player, Location location) {
        UUID playerUUID = player.getUniqueId();
        
        // Check if player already has a market
        int maxMarketsPerPlayer = plugin.getConfig().getInt("player-markets.max-markets-per-player", 1);
        if (getPlayerMarketCount(playerUUID) >= maxMarketsPerPlayer) {
            player.sendMessage("§cYou've reached your maximum number of markets.");
            return false;
        }
        
        // Check if location is too close to another market
        int minDistance = plugin.getConfig().getInt("player-markets.min-distance", 30);
        for (PlayerMarket market : markets.values()) {
            if (market.getCenter().getWorld().equals(location.getWorld()) && 
                market.getCenter().distance(location) < minDistance) {
                player.sendMessage("§cThis location is too close to another market.");
                return false;
            }
        }
        
        // Check structure requirements
        if (!validateMarketStructure(location)) {
            player.sendMessage("§cThis location doesn't meet market structure requirements.");
            return false;
        }
        
        // Create market
        String marketId = UUID.randomUUID().toString().substring(0, 8);
        int marketRadius = plugin.getConfig().getInt("player-markets.market-radius", 49);
        
        PlayerMarket market = new PlayerMarket(marketId, playerUUID, location, marketRadius, 1);
        markets.put(marketId, market);
        playerMarkets.put(playerUUID, marketId);
        marketOwners.put(marketId, playerUUID);
        marketAssociates.put(marketId, new HashSet<>());
        
        // Spawn villagers
        spawnMarketVillagers(market);
        
        // Show boundary particles
        showMarketBoundary(player, market);
        
        player.sendMessage("§aCreated a new market with ID: §e" + marketId);
        return true;
    }
    
    /**
     * Validates if a location meets the requirements for a market structure.
     * @param location The location to check
     * @return True if the structure is valid
     */
    private boolean validateMarketStructure(Location location) {
        // This would be a complex check for the market structure
        // For now, implementing a basic check
        
        // Check if we're in a building with a roof, walls, door, and bed
        Block centerBlock = location.getBlock();
        
        // Check for a 3x3 roof
        boolean hasRoof = true;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block roofBlock = centerBlock.getRelative(x, 3, z);
                if (roofBlock.getType() == Material.AIR) {
                    hasRoof = false;
                    break;
                }
            }
            if (!hasRoof) break;
        }
        
        // Check for walls (simplified)
        boolean hasWalls = true;
        for (int x = -2; x <= 2; x += 4) {
            for (int z = -2; z <= 2; z++) {
                Block wallBlock = centerBlock.getRelative(x, 1, z);
                if (wallBlock.getType() == Material.AIR) {
                    hasWalls = false;
                    break;
                }
            }
            if (!hasWalls) break;
        }
        
        for (int z = -2; z <= 2; z += 4) {
            for (int x = -2; x <= 2; x++) {
                Block wallBlock = centerBlock.getRelative(x, 1, z);
                if (wallBlock.getType() == Material.AIR) {
                    hasWalls = false;
                    break;
                }
            }
            if (!hasWalls) break;
        }
        
        // Check for door (simplified)
        boolean hasDoor = false;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 && Math.abs(z) != 2) continue; // Only check perimeter
                
                Block doorBlock = centerBlock.getRelative(x, 1, z);
                if (doorBlock.getType().toString().contains("DOOR")) {
                    hasDoor = true;
                    break;
                }
            }
            if (hasDoor) break;
        }
        
        // Check for bed (simplified)
        boolean hasBed = false;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Block bedBlock = centerBlock.getRelative(x, 1, z);
                if (bedBlock.getType().toString().contains("BED")) {
                    hasBed = true;
                    break;
                }
            }
            if (hasBed) break;
        }
        
        return hasRoof && hasWalls && hasDoor && hasBed;
    }
    
    /**
     * Spawns the villagers for a new market.
     * @param market The market
     */
    private void spawnMarketVillagers(PlayerMarket market) {
        VillagerAPI villagerAPI = plugin.getVillagerAPI();
        Location center = market.getCenter();
        
        // Spawn dealer
        Location dealerLoc = findSafeLocation(center, 2);
        Villager dealer = villagerAPI.createDealer(dealerLoc, market.getId());
        villagerToMarket.put(dealer.getUniqueId(), market.getId());
        
        // Spawn market owner
        Location ownerLoc = findSafeLocation(center, 3);
        Villager owner = villagerAPI.createMarketOwner(ownerLoc, market.getId());
        villagerToMarket.put(owner.getUniqueId(), market.getId());
    }
    
    /**
     * Finds a safe location near a center point.
     * @param center The center location
     * @param radius The search radius
     * @return A safe location
     */
    private Location findSafeLocation(Location center, int radius) {
        Location location = center.clone();
        World world = center.getWorld();
        
        // Try to find a safe spot within the radius
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location testLoc = center.clone().add(x, 0, z);
                Block block = testLoc.getBlock();
                Block above = block.getRelative(0, 1, 0);
                Block below = block.getRelative(0, -1, 0);
                
                if (block.getType() == Material.AIR && 
                    above.getType() == Material.AIR && 
                    below.getType().isSolid()) {
                    return testLoc;
                }
            }
        }
        
        // If no safe spot found, return original with y+1
        return center.clone().add(0, 1, 0);
    }
    
    /**
     * Shows the boundary of a market to a player.
     * @param player The player
     * @param market The market
     */
    public void showMarketBoundary(Player player, PlayerMarket market) {
        int radius = market.getRadius();
        Location center = market.getCenter();
        World world = center.getWorld();
        int centerX = center.getBlockX();
        int centerY = center.getBlockY();
        int centerZ = center.getBlockZ();
        
        int particleCount = 100; // Number of particles per side
        int visualizeTime = plugin.getConfig().getInt("player-markets.market-visualize-time", 10);
        
        // Schedule visualization task
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Run for the specified time
                long endTime = System.currentTimeMillis() + (visualizeTime * 1000L);
                
                while (System.currentTimeMillis() < endTime) {
                    // Draw boundary particles
                    for (int i = 0; i < particleCount; i++) {
                        double progress = (double) i / particleCount;
                        double x, z;
                        
                        // Top side
                        x = centerX - radius + (2 * radius * progress);
                        z = centerZ - radius;
                        spawnParticle(player, world, x, centerY + 1, z);
                        
                        // Bottom side
                        x = centerX - radius + (2 * radius * progress);
                        z = centerZ + radius;
                        spawnParticle(player, world, x, centerY + 1, z);
                        
                        // Left side
                        x = centerX - radius;
                        z = centerZ - radius + (2 * radius * progress);
                        spawnParticle(player, world, x, centerY + 1, z);
                        
                        // Right side
                        x = centerX + radius;
                        z = centerZ - radius + (2 * radius * progress);
                        spawnParticle(player, world, x, centerY + 1, z);
                    }
                    
                    // Sleep to control particle rate
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                plugin.getLogger().log(Level.WARNING, "Market boundary visualization interrupted", e);
            }
        });
    }
    
    /**
     * Spawns a particle at a location for a player.
     * @param player The player
     * @param world The world
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    private void spawnParticle(Player player, World world, double x, double y, double z) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            world.spawnParticle(Particle.VILLAGER_HAPPY, x, y, z, 1, 0, 0, 0, 0);
        });
    }
    
    /**
     * Adds a collector to a market.
     * @param player The player
     * @param location The collector location
     * @return True if the collector was added successfully
     */
    public boolean addCollector(Player player, Location location) {
        // Check if location is in a market
        PlayerMarket market = getMarketAtLocation(location);
        if (market == null) {
            player.sendMessage("§cThis location is not in a market.");
            return false;
        }
        
        // Check if player is owner or associate
        if (!isMarketOwnerOrAssociate(player.getUniqueId(), market.getId())) {
            player.sendMessage("§cYou don't have permission to add collectors to this market.");
            return false;
        }
        
        // Check collector limit
        int marketLevel = market.getLevel();
        int maxCollectors;
        switch (marketLevel) {
            case 1:
                maxCollectors = plugin.getConfig().getInt("player-markets.max-collectors-initial", 3);
                break;
            case 2:
                maxCollectors = plugin.getConfig().getInt("market-benefits.level2.collector-limit", 5);
                break;
            case 3:
                maxCollectors = plugin.getConfig().getInt("market-benefits.level3.collector-limit", 7);
                break;
            case 4:
                maxCollectors = plugin.getConfig().getInt("market-benefits.level4.collector-limit", 10);
                break;
            default:
                maxCollectors = 3;
        }
        
        if (market.getCollectorCount() >= maxCollectors) {
            player.sendMessage("§cThis market has reached its collector limit.");
            return false;
        }
        
        // Check structure requirements
        if (!validateCollectorStructure(location)) {
            player.sendMessage("§cThis location doesn't meet collector house requirements.");
            return false;
        }
        
        // Spawn collector
        VillagerAPI villagerAPI = plugin.getVillagerAPI();
        Villager collector = villagerAPI.createCollector(location, market.getId());
        villagerToMarket.put(collector.getUniqueId(), market.getId());
        
        // Update market
        market.incrementCollectorCount();
        
        player.sendMessage("§aAdded a collector to the market.");
        return true;
    }
    
    /**
     * Validates if a location meets the requirements for a collector structure.
     * @param location The location to check
     * @return True if the structure is valid
     */
    private boolean validateCollectorStructure(Location location) {
        // Similar to validateMarketStructure but for collector houses
        // For now, implementing a basic check
        
        Block centerBlock = location.getBlock();
        
        // Check for a 3x3 roof
        boolean hasRoof = true;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block roofBlock = centerBlock.getRelative(x, 3, z);
                if (roofBlock.getType() == Material.AIR) {
                    hasRoof = false;
                    break;
                }
            }
            if (!hasRoof) break;
        }
        
        // Check for walls (simplified)
        boolean hasWalls = true;
        for (int x = -2; x <= 2; x += 4) {
            for (int z = -2; z <= 2; z++) {
                Block wallBlock = centerBlock.getRelative(x, 1, z);
                if (wallBlock.getType() == Material.AIR) {
                    hasWalls = false;
                    break;
                }
            }
            if (!hasWalls) break;
        }
        
        for (int z = -2; z <= 2; z += 4) {
            for (int x = -2; x <= 2; x++) {
                Block wallBlock = centerBlock.getRelative(x, 1, z);
                if (wallBlock.getType() == Material.AIR) {
                    hasWalls = false;
                    break;
                }
            }
            if (!hasWalls) break;
        }
        
        // Check for door (simplified)
        boolean hasDoor = false;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 && Math.abs(z) != 2) continue; // Only check perimeter
                
                Block doorBlock = centerBlock.getRelative(x, 1, z);
                if (doorBlock.getType().toString().contains("DOOR")) {
                    hasDoor = true;
                    break;
                }
            }
            if (hasDoor) break;
        }
        
        // Check for bed (simplified)
        boolean hasBed = false;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Block bedBlock = centerBlock.getRelative(x, 1, z);
                if (bedBlock.getType().toString().contains("BED")) {
                    hasBed = true;
                    break;
                }
            }
            if (hasBed) break;
        }
        
        return hasRoof && hasWalls && hasDoor && hasBed;
    }
    
    /**
     * Upgrades a market to the next level.
     * @param player The player
     * @param marketId The market ID
     * @return True if the market was upgraded successfully
     */
    public boolean upgradeMarket(Player player, String marketId) {
        PlayerMarket market = getMarket(marketId);
        if (market == null) {
            player.sendMessage("§cMarket not found.");
            return false;
        }
        
        // Check if player is the owner
        if (!isMarketOwner(player.getUniqueId(), marketId)) {
            player.sendMessage("§cYou don't own this market.");
            return false;
        }
        
        // Check current level
        int currentLevel = market.getLevel();
        if (currentLevel >= 4) {
            player.sendMessage("§cThis market is already at maximum level.");
            return false;
        }
        
        // Check upgrade requirements
        int nextLevel = currentLevel + 1;
        ConfigurationSection upgradeCosts = plugin.getConfig().getConfigurationSection("market-upgrade-costs.level" + nextLevel);
        if (upgradeCosts == null) {
            player.sendMessage("§cUpgrade configuration not found.");
            return false;
        }
        
        // TODO: Check if player has required resources
        // This would involve checking player inventory for diamonds and economy for currency
        
        // Upgrade market
        market.setLevel(nextLevel);
        
        // TODO: Take required resources from player
        
        player.sendMessage("§aUpgraded market to level " + nextLevel + "!");
        return true;
    }
    
    /**
     * Removes a market.
     * @param player The player
     * @param marketId The market ID
     * @return True if the market was removed successfully
     */
    public boolean removeMarket(Player player, String marketId) {
        PlayerMarket market = getMarket(marketId);
        if (market == null) {
            player.sendMessage("§cMarket not found.");
            return false;
        }
        
        // Check if player is the owner
        if (!isMarketOwner(player.getUniqueId(), marketId)) {
            player.sendMessage("§cYou don't own this market.");
            return false;
        }
        
        // Remove villagers
        removeMarketVillagers(marketId);
        
        // Remove market
        markets.remove(marketId);
        playerMarkets.remove(player.getUniqueId());
        marketOwners.remove(marketId);
        marketAssociates.remove(marketId);
        
        player.sendMessage("§aRemoved market with ID: §e" + marketId);
        return true;
    }
    
    /**
     * Removes all villagers associated with a market.
     * @param marketId The market ID
     */
    private void removeMarketVillagers(String marketId) {
        // Find and remove all villagers associated with this market
        for (Iterator<Map.Entry<UUID, String>> it = villagerToMarket.entrySet().iterator(); it.hasNext();) {
            Map.Entry<UUID, String> entry = it.next();
            if (entry.getValue().equals(marketId)) {
                UUID villagerUUID = entry.getKey();
                plugin.getServer().getWorlds().forEach(world -> {
                    world.getEntities().forEach(entity -> {
                        if (entity instanceof Villager && entity.getUniqueId().equals(villagerUUID)) {
                            entity.remove();
                        }
                    });
                });
                it.remove();
            }
        }
    }
    
    /**
     * Adds an associate to a market.
     * @param player The owner
     * @param associate The associate to add
     * @param marketId The market ID
     * @return True if the associate was added successfully
     */
    public boolean addAssociate(Player player, Player associate, String marketId) {
        PlayerMarket market = getMarket(marketId);
        if (market == null) {
            player.sendMessage("§cMarket not found.");
            return false;
        }
        
        // Check if player is the owner
        if (!isMarketOwner(player.getUniqueId(), marketId)) {
            player.sendMessage("§cYou don't own this market.");
            return false;
        }
        
        // Add associate
        Set<UUID> associates = marketAssociates.computeIfAbsent(marketId, k -> new HashSet<>());
        associates.add(associate.getUniqueId());
        
        player.sendMessage("§aAdded §e" + associate.getName() + " §aas an associate to your market.");
        associate.sendMessage("§aYou have been added as an associate to §e" + player.getName() + "'s §amarket.");
        return true;
    }
    
    /**
     * Removes an associate from a market.
     * @param player The owner
     * @param associate The associate to remove
     * @param marketId The market ID
     * @return True if the associate was removed successfully
     */
    public boolean removeAssociate(Player player, Player associate, String marketId) {
        PlayerMarket market = getMarket(marketId);
        if (market == null) {
            player.sendMessage("§cMarket not found.");
            return false;
        }
        
        // Check if player is the owner
        if (!isMarketOwner(player.getUniqueId(), marketId)) {
            player.sendMessage("§cYou don't own this market.");
            return false;
        }
        
        // Remove associate
        Set<UUID> associates = marketAssociates.get(marketId);
        if (associates != null) {
            associates.remove(associate.getUniqueId());
        }
        
        player.sendMessage("§aRemoved §e" + associate.getName() + " §afrom your market.");
        associate.sendMessage("§cYou have been removed as an associate from §e" + player.getName() + "'s §cmarket.");
        return true;
    }
    
    /**
     * Gets a market by ID.
     * @param marketId The market ID
     * @return The market, or null if not found
     */
    public PlayerMarket getMarket(String marketId) {
        return markets.get(marketId);
    }
    
    /**
     * Gets a player's market.
     * @param playerUUID The player UUID
     * @return The market, or null if not found
     */
    public PlayerMarket getPlayerMarket(UUID playerUUID) {
        String marketId = playerMarkets.get(playerUUID);
        return marketId != null ? markets.get(marketId) : null;
    }
    
    /**
     * Gets the number of markets a player owns.
     * @param playerUUID The player UUID
     * @return The number of markets
     */
    public int getPlayerMarketCount(UUID playerUUID) {
        return playerMarkets.containsKey(playerUUID) ? 1 : 0;
    }
    
    /**
     * Gets the market at a location.
     * @param location The location
     * @return The market, or null if not found
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
     * Checks if a location is in a market.
     * @param location The location
     * @param market The market
     * @return True if the location is in the market
     */
    public boolean isInMarket(Location location, PlayerMarket market) {
        Location center = market.getCenter();
        if (!center.getWorld().equals(location.getWorld())) {
            return false;
        }
        
        int radius = market.getRadius();
        double distance = center.distance(location);
        return distance <= radius;
    }
    
    /**
     * Checks if a player is the owner of a market.
     * @param playerUUID The player UUID
     * @param marketId The market ID
     * @return True if the player is the owner
     */
    public boolean isMarketOwner(UUID playerUUID, String marketId) {
        UUID ownerUUID = marketOwners.get(marketId);
        return ownerUUID != null && ownerUUID.equals(playerUUID);
    }
    
    /**
     * Checks if a player is an associate of a market.
     * @param playerUUID The player UUID
     * @param marketId The market ID
     * @return True if the player is an associate
     */
    public boolean isMarketAssociate(UUID playerUUID, String marketId) {
        Set<UUID> associates = marketAssociates.get(marketId);
        return associates != null && associates.contains(playerUUID);
    }
    
    /**
     * Checks if a player is the owner or an associate of a market.
     * @param playerUUID The player UUID
     * @param marketId The market ID
     * @return True if the player is the owner or an associate
     */
    public boolean isMarketOwnerOrAssociate(UUID playerUUID, String marketId) {
        return isMarketOwner(playerUUID, marketId) || isMarketAssociate(playerUUID, marketId);
    }
    
    /**
     * Gets all markets.
     * @return A collection of all markets
     */
    public Collection<PlayerMarket> getAllMarkets() {
        return markets.values();
    }
}