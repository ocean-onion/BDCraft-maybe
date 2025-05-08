package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
    private static final int MARKET_RADIUS = 49; // Documentation specifies 49x49 block radius
    
    private final UUID id;
    private final Location center;
    private final UUID founderId;
    private final String founderName;
    private int level;
    private double priceModifier;
    
    // Maps trader UUIDs to their types
    private final Map<UUID, String> traders;
    
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
        return MARKET_RADIUS / 2; // 49x49 area, so radius is half of 49
    }
    
    /**
     * Shows the market boundary to a player.
     * @param player The player to show the boundary to
     * @param plugin The plugin instance
     */
    public void showBoundary(Player player, BDCraft plugin) {
        int radius = getRadius();
        int particlesPerCircle = 80; // Number of particles to create a complete circle
        
        // Schedule a series of tasks to show particles for 30 seconds (600 ticks)
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            // Create a circle of particles at player's height
            for (int i = 0; i < particlesPerCircle; i++) {
                double angle = 2 * Math.PI * i / particlesPerCircle;
                double x = center.getX() + radius * Math.cos(angle);
                double z = center.getZ() + radius * Math.sin(angle);
                
                Location particleLoc = new Location(center.getWorld(), x, player.getLocation().getY(), z);
                player.spawnParticle(Particle.REDSTONE, particleLoc, 1, 
                        new org.bukkit.Particle.DustOptions(org.bukkit.Color.fromRGB(255, 215, 0), 1.0f));
            }
        }, 0L, 20L); // Run every second
        
        // Schedule task to stop after 30 seconds
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.getScheduler().cancelTask(taskId);
            player.sendMessage(ChatColor.GOLD + "Market boundary display has ended.");
        }, 30 * 20L); // 30 seconds
        
        player.sendMessage(ChatColor.GOLD + "Showing market boundary with radius " + radius + " for 30 seconds.");
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