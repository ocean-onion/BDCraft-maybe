package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.MarketAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the MarketAPI interface.
 * This class provides a bridge between the API interface and the internal
 * market system implementation.
 */
public class MarketAPIImpl implements MarketAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final MarketManager marketManager;
    
    /**
     * Creates a new MarketAPIImpl.
     *
     * @param plugin The plugin instance
     * @param marketManager The market manager
     */
    public MarketAPIImpl(BDCraft plugin, MarketManager marketManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.marketManager = marketManager;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String createMarket(Location location, Player owner) {
        if (location == null || owner == null) {
            logger.warning("Attempted to create market with null location or owner");
            return null;
        }
        
        try {
            // Generate a default market name based on player's name
            String marketName = owner.getName() + "'s Market";
            
            // Call the MarketManager's createMarket method with the correct signature
            Market market = marketManager.createMarket(owner, marketName, location);
            return market != null ? market.getId().toString() : null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to create market at " + location, e);
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeMarket(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            logger.warning("Attempted to remove market with null or empty ID");
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            return marketManager.removeMarket(id);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to remove market " + marketId, e);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarketAt(Location location) {
        if (location == null) {
            return null;
        }
        
        Market market = marketManager.getMarketAt(location);
        return market != null ? market.getId().toString() : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getMarketOwner(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return null;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getOwnerId() : null;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setMarketOwner(String marketId, UUID owner) {
        if (marketId == null || marketId.isEmpty() || owner == null) {
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                return false;
            }
            
            // Since the Market class doesn't have a direct method to change ownership,
            // we need to implement ownership transfer by creating a new market with the same properties
            
            // Get the player corresponding to the new owner UUID
            org.bukkit.OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(owner);
            String ownerName = offlinePlayer.getName();
            if (ownerName == null) {
                ownerName = "Unknown";
            }
            
            // First, make a copy of associates list before removing the market
            List<UUID> associates = market.getAssociatesList();
            
            // Get other market properties needed for recreation
            String marketName = market.getName();
            Location centerLocation = market.getCenterLocation();
            int marketLevel = market.getLevel();
            
            // Remove the old market
            if (!marketManager.removeMarket(id)) {
                logger.warning("Failed to remove old market during ownership transfer: " + marketId);
                return false;
            }
            
            // Create a new market with the same properties but with a new owner
            Player newOwnerPlayer = plugin.getServer().getPlayer(owner);
            Market newMarket;
            
            if (newOwnerPlayer != null && newOwnerPlayer.isOnline()) {
                // Use online player
                newMarket = marketManager.createMarket(newOwnerPlayer, marketName, centerLocation);
            } else {
                // Cannot create market with offline player
                logger.warning("Cannot transfer market ownership to offline player: " + ownerName);
                return false;
            }
            
            if (newMarket == null) {
                logger.warning("Failed to create new market during ownership transfer");
                return false;
            }
            
            // Restore the market level
            for (int i = 1; i < marketLevel; i++) {
                newMarket.upgrade();
            }
            
            // Restore associates
            for (UUID associateId : associates) {
                if (!associateId.equals(owner)) { // Don't add the new owner as an associate
                    newMarket.addAssociate(associateId);
                }
            }
            
            // Save changes
            marketManager.saveMarkets();
            
            logger.info("Successfully transferred market " + marketId + " ownership to " + ownerName);
            return true;
            
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to set market owner for " + marketId, e);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<UUID> getMarketAssociates(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getAssociatesList() : new ArrayList<>();
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return new ArrayList<>();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addMarketAssociate(String marketId, UUID associate) {
        if (marketId == null || marketId.isEmpty() || associate == null) {
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                return false;
            }
            
            boolean result = market.addAssociate(associate);
            
            if (result) {
                marketManager.saveMarkets();
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to add associate to market " + marketId, e);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeMarketAssociate(String marketId, UUID associate) {
        if (marketId == null || marketId.isEmpty() || associate == null) {
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                return false;
            }
            
            boolean result = market.removeAssociate(associate);
            
            if (result) {
                marketManager.saveMarkets();
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to remove associate from market " + marketId, e);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getMarketLevel(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return 0;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getLevel() : 0;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upgradeMarket(String marketId, Player player) {
        if (marketId == null || marketId.isEmpty() || player == null) {
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                logger.warning("Market not found for upgrade: " + marketId);
                return false;
            }
            
            // Check if player is the owner or has admin permissions
            if (!market.isOwner(player.getUniqueId()) && 
                !player.hasPermission("bdcraft.admin.market.upgrade")) {
                player.sendMessage("You don't have permission to upgrade this market.");
                return false;
            }
            
            // Check if market is already at max level
            int currentLevel = market.getLevel();
            if (currentLevel >= 4) {
                player.sendMessage("This market is already at maximum level.");
                return false;
            }
            
            // Calculate upgrade costs based on current level
            int diamondCost;
            int currencyCost;
            
            switch (currentLevel) {
                case 1:
                    diamondCost = 16;
                    currencyCost = 5000;
                    break;
                case 2:
                    diamondCost = 32;
                    currencyCost = 10000;
                    break;
                case 3:
                    diamondCost = 64;
                    currencyCost = 25000;
                    break;
                default:
                    diamondCost = 0;
                    currencyCost = 0;
            }
            
            // Check if player has admin permissions to bypass costs
            boolean bypassCosts = player.hasPermission("bdcraft.admin.market.upgrade.free");
            
            // Check if player has enough resources
            if (!bypassCosts) {
                // Check diamonds
                if (!player.getInventory().containsAtLeast(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND), diamondCost)) {
                    player.sendMessage("You need " + diamondCost + " diamonds to upgrade this market.");
                    return false;
                }
                
                // Check currency using economy API
                EconomyAPI economyAPI = plugin.getEconomyAPI();
                if (economyAPI == null || !economyAPI.hasEnough(player.getUniqueId(), currencyCost)) {
                    player.sendMessage("You need " + currencyCost + " currency to upgrade this market.");
                    return false;
                }
                
                // Take resources
                // Take diamonds
                player.getInventory().removeItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND, diamondCost));
                
                // Take currency
                economyAPI.withdrawPlayer(player.getUniqueId(), currencyCost);
            }
            
            // Perform the upgrade
            boolean upgraded = market.upgrade();
            
            if (upgraded) {
                // Save the changes
                marketManager.saveMarkets();
                
                // Notify player
                player.sendMessage("Market upgraded to level " + market.getLevel() + "!");
                
                // Log the upgrade
                logger.info("Player " + player.getName() + " upgraded market " + market.getName() + " to level " + market.getLevel());
                
                return true;
            } else {
                player.sendMessage("Failed to upgrade market.");
                return false;
            }
            
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to upgrade market " + marketId, e);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showMarketBoundaries(String marketId, Player player) {
        if (marketId == null || marketId.isEmpty() || player == null) {
            return;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                player.sendMessage("Market not found");
                return;
            }
            
            // Get world and market center/radius
            World world = player.getServer().getWorld(market.getWorldName());
            if (world == null) {
                player.sendMessage("Market world not found");
                return;
            }
            
            int centerX = market.getCenterX();
            int centerY = market.getCenterY();
            int centerZ = market.getCenterZ();
            int radius = market.getRadius();
            
            // Create temporary blocks showing the market boundaries
            List<Block> changedBlocks = new ArrayList<>();
            
            // Create a grid at ground level
            for (int x = centerX - radius; x <= centerX + radius; x += 4) {
                for (int z = centerZ - radius; z <= centerZ + radius; z += 4) {
                    // Only create blocks at the boundary
                    if (Math.abs(x - centerX) == radius || Math.abs(z - centerZ) == radius || 
                        x == centerX || z == centerZ) {
                        // Find the highest block at this position
                        int y = world.getHighestBlockYAt(x, z);
                        Block block = world.getBlockAt(x, y + 1, z);
                        
                        if (block.getType() == Material.AIR) {
                            changedBlocks.add(block);
                            player.sendBlockChange(block.getLocation(), Material.WHITE_WOOL.createBlockData());
                        }
                    }
                }
            }
            
            // Mark the center
            Block centerBlock = world.getBlockAt(centerX, world.getHighestBlockYAt(centerX, centerZ) + 1, centerZ);
            if (centerBlock.getType() == Material.AIR) {
                changedBlocks.add(centerBlock);
                player.sendBlockChange(centerBlock.getLocation(), Material.RED_WOOL.createBlockData());
            }
            
            // Restore the blocks after 30 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        for (Block block : changedBlocks) {
                            player.sendBlockChange(block.getLocation(), block.getBlockData());
                        }
                    }
                }
            }.runTaskLater(plugin, 30 * 20L); // 30 seconds * 20 ticks
            
            player.sendMessage("Market boundaries shown for 30 seconds");
            
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to show market boundaries for " + marketId, e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Location getMarketCenter(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return null;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getCenterLocation() : null;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getMarketRadius(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return 0;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getRadius() : 0;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMarketPermission(String marketId, Player player) {
        if (marketId == null || marketId.isEmpty() || player == null) {
            return false;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            
            if (market == null) {
                return false;
            }
            
            return market.isOwner(player.getUniqueId()) || market.isAssociate(player.getUniqueId());
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getCollectorCount(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return 0;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getCollectorsCount() : 0;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return 0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxCollectorCount(String marketId) {
        if (marketId == null || marketId.isEmpty()) {
            return 0;
        }
        
        try {
            UUID id = UUID.fromString(marketId);
            Market market = marketManager.getMarket(id);
            return market != null ? market.getMaxCollectors() : 0;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid market ID format: " + marketId);
            return 0;
        }
    }
}