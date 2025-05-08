package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.villagers.BDDealerVillager;
import com.bdcraft.plugin.modules.economy.villagers.BDVillager.VillagerType;
import com.bdcraft.plugin.modules.economy.villagers.MarketOwnerVillager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a BD market.
 */
public class BDMarket {
    public static final int MAX_MARKET_LEVEL = 5;
    private static final int MARKET_AREA_SIZE = 49; // Documentation specifies 49x49 block area
    
    private final UUID id;
    private final Location center;
    private final UUID founderId;
    private final String founderName;
    private int level;
    private double priceModifier;
    
    // Maps trader UUIDs to their types
    private final Map<UUID, String> traders;
    
    // List of market associates (max 5 according to documentation)
    private final List<UUID> associates;
    
    // Market settings
    private boolean publicAccess = true;
    private boolean openBuild = false;
    private boolean openBreak = false;
    private boolean particles = true;
    private boolean sounds = true;
    private boolean removed = false;
    
    // Market stats
    private double netWorth = 0.0;
    private int weeklyTradeCount = 0;
    
    // Market owner and dealer villagers
    private UUID marketOwnerUuid;
    private UUID bdDealerUuid;
    
    /**
     * Creates a new BD market.
     * @param id The market ID
     * @param center The center of the market
     * @param founderId The founder's ID
     * @param founderName The founder's name
     */
    public BDMarket(UUID id, Location center, UUID founderId, String founderName) {
        this.id = id;
        this.center = center;
        this.founderId = founderId;
        this.founderName = founderName;
        this.level = 1; // Start at level 1
        this.priceModifier = 1.0; // Default price modifier
        this.traders = new HashMap<>();
        this.associates = new ArrayList<>();
    }
    
    /**
     * Gets the market ID.
     * @return The market ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the center of the market.
     * @return The center
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Gets the founder's ID.
     * @return The founder's ID
     */
    public UUID getFounderId() {
        return founderId;
    }
    
    /**
     * Gets the founder's name.
     * @return The founder's name
     */
    public String getFounderName() {
        return founderName;
    }
    
    /**
     * Gets the market level.
     * @return The level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the market level.
     * @param level The new level
     */
    public void setLevel(int level) {
        if (level < 1) {
            level = 1;
        } else if (level > MAX_MARKET_LEVEL) {
            level = MAX_MARKET_LEVEL;
        }
        
        this.level = level;
        
        // Update price modifier with level
        this.priceModifier = 1.0 + (0.1 * (level - 1)); // 10% increase per level
    }
    
    /**
     * Upgrades the market to the next level.
     * @return True if the upgrade was successful, false if already at max level
     */
    public boolean upgrade() {
        if (level >= MAX_MARKET_LEVEL) {
            return false;
        }
        
        level++;
        
        // Update price modifier with level
        this.priceModifier = 1.0 + (0.1 * (level - 1)); // 10% increase per level
        
        // Create upgrade effects at market center
        center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 100, 3, 3, 3);
        center.getWorld().playSound(center, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
        
        // Notify nearby players
        for (Player player : center.getWorld().getPlayers()) {
            if (center.distance(player.getLocation()) <= getRadius() * 1.5) {
                player.sendMessage(ChatColor.GREEN + "The market owned by " + founderName + 
                        " has been upgraded to level " + level + "!");
            }
        }
        
        return true;
    }
    
    /**
     * Gets the price modifier.
     * @return The price modifier
     */
    public double getPriceModifier() {
        return priceModifier;
    }
    
    /**
     * Sets the price modifier.
     * @param priceModifier The new price modifier
     */
    public void setPriceModifier(double priceModifier) {
        if (priceModifier < 0.5) {
            priceModifier = 0.5;
        } else if (priceModifier > 2.0) {
            priceModifier = 2.0;
        }
        
        this.priceModifier = priceModifier;
    }
    
    /**
     * Sets the market owner villager.
     * @param villager The market owner villager
     */
    public void setMarketOwner(MarketOwnerVillager villager) {
        if (villager != null) {
            this.marketOwnerUuid = villager.getUuid();
            addTrader(villager.getUuid(), VillagerType.MARKET_OWNER.name());
        }
    }
    
    /**
     * Sets the BD dealer villager.
     * @param villager The BD dealer villager
     */
    public void setBDDealer(BDDealerVillager villager) {
        if (villager != null) {
            this.bdDealerUuid = villager.getUuid();
            addTrader(villager.getUuid(), VillagerType.BD_DEALER.name());
        }
    }
    
    /**
     * Gets the market owner UUID.
     * @return The market owner UUID
     */
    public UUID getMarketOwnerUuid() {
        return marketOwnerUuid;
    }
    
    /**
     * Gets the BD dealer UUID.
     * @return The BD dealer UUID
     */
    public UUID getBDDealerUuid() {
        return bdDealerUuid;
    }
    
    /**
     * Adds a trader to the market.
     * @param traderId The trader's ID
     * @param traderType The trader's type
     */
    public void addTrader(UUID traderId, String traderType) {
        traders.put(traderId, traderType);
    }
    
    /**
     * Removes a trader from the market.
     * @param traderId The trader's ID
     */
    public void removeTrader(UUID traderId) {
        traders.remove(traderId);
    }
    
    /**
     * Gets all trader IDs in the market.
     * @return The trader IDs
     */
    public List<UUID> getTraders() {
        return new ArrayList<>(traders.keySet());
    }
    
    /**
     * Gets the type of a trader.
     * @param traderId The trader's ID
     * @return The trader's type, or null if not found
     */
    public String getTraderType(UUID traderId) {
        return traders.get(traderId);
    }
    
    /**
     * Gets the number of traders of a specific type.
     * @param traderType The trader type
     * @return The number of traders
     */
    public int getTraderCount(String traderType) {
        int count = 0;
        
        for (String type : traders.values()) {
            if (type.equalsIgnoreCase(traderType)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Gets the total number of traders.
     * @return The total number of traders
     */
    public int getTotalTraderCount() {
        return traders.size();
    }
    
    /**
     * Gets the radius of the market.
     * @return The radius
     */
    public int getRadius() {
        return 24; // Documentation specifies a 49x49 area (24 blocks from center in each direction)
    }
    
    /**
     * Shows the market boundary to a player using wool blocks according to documentation.
     * - Top layer blocks within boundary become red wool for 10 seconds
     * - Corner blocks become black wool for 10 seconds
     * - All changed blocks automatically revert to original state
     * - Temporary blocks are unbreakable during visualization
     * 
     * @param player The player to show the boundary to
     * @param plugin The plugin instance
     */
    public void showBoundary(Player player, BDCraft plugin) {
        int radius = getRadius();
        World world = center.getWorld();
        
        // Store original blocks to restore them later
        Map<Location, org.bukkit.block.BlockState> originalBlocks = new HashMap<>();
        
        // Create a task to visualize the boundary with wool blocks
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                // Get the highest block Y at player location for visualization height
                int visualY = world.getHighestBlockYAt(player.getLocation()) + 1;
                
                // Mark boundary with red wool (14 is the data value for red wool)
                // Top and bottom edges
                for (int x = -radius; x <= radius; x++) {
                    // Top edge
                    markBoundaryBlock(world, originalBlocks, center.getBlockX() + x, visualY, center.getBlockZ() - radius, 14);
                    markBoundaryBlock(world, originalBlocks, center.getBlockX() + x, visualY, center.getBlockZ() + radius, 14);
                    
                    // Left and right edges
                    markBoundaryBlock(world, originalBlocks, center.getBlockX() - radius, visualY, center.getBlockZ() + x, 14);
                    markBoundaryBlock(world, originalBlocks, center.getBlockX() + radius, visualY, center.getBlockZ() + x, 14);
                }
                
                // Mark corners with black wool (15 is the data value for black wool)
                markBoundaryBlock(world, originalBlocks, center.getBlockX() - radius, visualY, center.getBlockZ() - radius, 15);
                markBoundaryBlock(world, originalBlocks, center.getBlockX() + radius, visualY, center.getBlockZ() - radius, 15);
                markBoundaryBlock(world, originalBlocks, center.getBlockX() - radius, visualY, center.getBlockZ() + radius, 15);
                markBoundaryBlock(world, originalBlocks, center.getBlockX() + radius, visualY, center.getBlockZ() + radius, 15);
                
                player.sendMessage(ChatColor.GOLD + "Showing market boundary with radius " + radius + " blocks for 10 seconds.");
                
                // Schedule a task to restore the original blocks after 10 seconds
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Map.Entry<Location, org.bukkit.block.BlockState> entry : originalBlocks.entrySet()) {
                        entry.getValue().update(true, false);
                    }
                    player.sendMessage(ChatColor.GOLD + "Market boundary display has ended. Original blocks restored.");
                }, 10 * 20L); // 10 seconds
            } catch (Exception e) {
                plugin.getLogger().severe("Error showing market boundary: " + e.getMessage());
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "An error occurred while showing the market boundary.");
            }
        });
    }
    
    /**
     * Helper method to mark a boundary block with wool
     * @param world The world
     * @param originalBlocks Map to store original block states
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param woolData The wool color data value (14 for red, 15 for black)
     */
    private void markBoundaryBlock(World world, Map<Location, org.bukkit.block.BlockState> originalBlocks, int x, int y, int z, int woolData) {
        Location loc = new Location(world, x, y, z);
        Block block = world.getBlockAt(loc);
        
        // Store the original block state before changing it
        originalBlocks.put(loc, block.getState());
        
        // In Paper 1.21, we can use the material names directly for colored wool
        if (woolData == 15) { // Black wool
            block.setType(Material.BLACK_WOOL);
        } else { // Red wool (default)
            block.setType(Material.RED_WOOL);
        }
    }
    
    /**
     * Shows the market info to a player.
     * @param player The player to show the info to
     */
    public void showInfo(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Market Information ===");
        player.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + founderName);
        player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + level + "/" + MAX_MARKET_LEVEL);
        player.sendMessage(ChatColor.YELLOW + "Radius: " + ChatColor.WHITE + getRadius() + " blocks");
        player.sendMessage(ChatColor.YELLOW + "Price Modifier: " + ChatColor.WHITE + 
                String.format("%.2f", priceModifier) + "x");
        player.sendMessage(ChatColor.YELLOW + "Traders: " + ChatColor.WHITE + getTotalTraderCount());
        
        // Show associates info
        player.sendMessage(ChatColor.YELLOW + "Associates: " + ChatColor.WHITE + associates.size() + "/5");
        if (!associates.isEmpty()) {
            StringBuilder associateNames = new StringBuilder();
            for (UUID associateId : associates) {
                Player associatePlayer = Bukkit.getPlayer(associateId);
                if (associatePlayer != null) {
                    if (associateNames.length() > 0) {
                        associateNames.append(", ");
                    }
                    associateNames.append(associatePlayer.getName());
                }
            }
            player.sendMessage(ChatColor.YELLOW + "Associate Names: " + ChatColor.WHITE + associateNames.toString());
        }
    }
    
    /**
     * Adds an associate to the market.
     * Maximum 5 associates per market according to documentation.
     * 
     * @param playerId The UUID of the player to add as an associate
     * @param playerName The name of the player to add as an associate
     * @return True if the player was added, false if already an associate or max associates reached
     */
    public boolean addAssociate(UUID playerId, String playerName) {
        // Check if player is already an associate
        if (associates.contains(playerId)) {
            return false;
        }
        
        // Max 5 associates as specified in documentation
        if (associates.size() >= 5) {
            return false;
        }
        
        associates.add(playerId);
        
        // Notify nearby players of the new associate
        center.getWorld().getPlayers().stream()
                .filter(p -> center.distance(p.getLocation()) <= getRadius())
                .forEach(p -> p.sendMessage(ChatColor.GREEN + playerName + 
                        " has been added as an associate to " + founderName + "'s market!"));
        
        return true;
    }
    
    /**
     * Removes an associate from the market.
     * 
     * @param playerId The UUID of the player to remove as an associate
     * @param playerName The name of the player to remove as an associate
     * @return True if the player was removed, false if not an associate
     */
    public boolean removeAssociate(UUID playerId, String playerName) {
        if (!associates.contains(playerId)) {
            return false;
        }
        
        associates.remove(playerId);
        
        // Notify nearby players of the associate removal
        center.getWorld().getPlayers().stream()
                .filter(p -> center.distance(p.getLocation()) <= getRadius())
                .forEach(p -> p.sendMessage(ChatColor.YELLOW + playerName + 
                        " is no longer an associate of " + founderName + "'s market."));
        
        return true;
    }
    
    /**
     * Checks if a player is an associate of the market.
     * 
     * @param playerId The UUID of the player to check
     * @return True if the player is an associate, false otherwise
     */
    public boolean isAssociate(UUID playerId) {
        return associates.contains(playerId);
    }
    
    /**
     * Gets all associates of the market.
     * 
     * @return A list of associate UUIDs
     */
    public List<UUID> getAssociates() {
        return new ArrayList<>(associates);
    }
    
    /**
     * Gets the number of associates in the market.
     * 
     * @return The number of associates
     */
    public int getAssociateCount() {
        return associates.size();
    }
    
    /**
     * Checks if a location is within the market radius.
     * @param location The location to check
     * @return Whether the location is within the radius
     */
    public boolean isInRadius(Location location) {
        if (!center.getWorld().equals(location.getWorld())) {
            return false;
        }
        
        double distance = center.distance(location);
        return distance <= getRadius();
    }
}