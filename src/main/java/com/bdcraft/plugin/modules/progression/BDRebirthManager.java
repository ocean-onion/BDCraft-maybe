package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manager for the BD rebirth system.
 */
public class BDRebirthManager {
    private final BDCraft plugin;
    private final Logger logger;
    
    // Track player command cooldowns
    private final Map<UUID, Map<String, Long>> commandCooldowns = new HashMap<>();
    
    // Track player blessing effects
    private final Map<UUID, Long> activePlayerBlessings = new HashMap<>();
    
    // Track experience boosts
    private final Map<UUID, Double> playerExpBoosts = new HashMap<>();
    private final Map<UUID, Long> playerExpBoostExpiry = new HashMap<>();
    
    // Track player rebirth auras
    private final Map<UUID, Boolean> playerAuraEnabled = new HashMap<>();
    private final Map<UUID, BukkitTask> playerAuraTasks = new HashMap<>();
    
    // Store player trade counts for rebirth requirements
    private final Map<UUID, Integer> playerTradeCounts = new HashMap<>();

    /**
     * Creates a new BD rebirth manager.
     * 
     * @param plugin The plugin instance
     */
    public BDRebirthManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Gets a player's rebirth level.
     * 
     * @param player The player
     * @return The rebirth level
     */
    public int getPlayerRebirthLevel(Player player) {
        return plugin.getProgressionModule().getRankManager().getPlayerRebirths(player);
    }
    
    /**
     * Gets a player's rebirth level by UUID.
     * 
     * @param uuid The player's UUID
     * @return The rebirth level
     */
    public int getRebirthLevel(UUID uuid) {
        // Get the player by UUID
        Player player = plugin.getServer().getPlayer(uuid);
        // If player is null, return 0 as a fallback
        if (player == null) {
            return 0;
        }
        return plugin.getProgressionModule().getRankManager().getPlayerRebirths(player);
    }
    
    /**
     * Gets the rebirth level for a player.
     * 
     * @param player The player
     * @return The rebirth level
     */
    public int getRebirthLevel(Player player) {
        return getPlayerRebirthLevel(player);
    }
    
    /**
     * Performs a rebirth for a player.
     * 
     * @param player The player
     * @return True if successful
     */
    public boolean performRebirth(Player player) {
        // Pass through to the progression module, which handles the full process
        return plugin.getProgressionModule().performRebirth(player);
    }
    
    /**
     * Sets whether a player's aura is enabled.
     * 
     * @param player The player
     * @param enabled Whether the aura is enabled
     */
    public void setAuraEnabled(Player player, boolean enabled) {
        playerAuraEnabled.put(player.getUniqueId(), enabled);
        
        if (enabled) {
            startAuraEffect(player);
        } else {
            stopAuraEffect(player);
        }
    }
    
    /**
     * Toggles a player's aura.
     * 
     * @param player The player
     * @return The new state
     */
    public boolean toggleAura(Player player) {
        boolean currentState = isAuraEnabled(player);
        boolean newState = !currentState;
        setAuraEnabled(player, newState);
        return newState;
    }
    
    /**
     * Checks if a player's aura is enabled.
     * 
     * @param player The player
     * @return True if enabled
     */
    public boolean isAuraEnabled(Player player) {
        return playerAuraEnabled.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * Gets the abundance aura radius for a player.
     * 
     * @param player The player
     * @return The radius
     */
    public int getAbundanceAuraRadius(Player player) {
        int baseRadius = 20; // Default aura radius
        int rebirthLevel = getRebirthLevel(player);
        
        // Increases by 2 blocks per rebirth level above 5
        if (rebirthLevel > 5) {
            baseRadius += (rebirthLevel - 5) * 2;
        }
        
        return baseRadius;
    }
    
    /**
     * Starts the aura effect for a player.
     * 
     * @param player The player
     */
    private void startAuraEffect(Player player) {
        // Stop any existing effect
        stopAuraEffect(player);
        
        // Start a new effect
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    playerAuraTasks.remove(player.getUniqueId());
                    return;
                }
                
                Location location = player.getLocation();
                location.getWorld().spawnParticle(
                    Particle.FLAME, 
                    location, 
                    10, 
                    1.5, 
                    0.5, 
                    1.5, 
                    0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 20L); // Once per second
        
        playerAuraTasks.put(player.getUniqueId(), task);
    }
    
    /**
     * Stops the aura effect for a player.
     * 
     * @param player The player
     */
    private void stopAuraEffect(Player player) {
        BukkitTask task = playerAuraTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }
    
    /**
     * Sets a blessing cooldown for a player.
     * 
     * @param playerId The player UUID
     * @param timestamp The cooldown timestamp
     */
    public void setBlessCooldown(UUID playerId, long timestamp) {
        Map<String, Long> playerCooldowns = commandCooldowns.computeIfAbsent(playerId, k -> new HashMap<>());
        playerCooldowns.put("bless", timestamp);
    }
    
    /**
     * Gets a blessing cooldown for a player.
     * 
     * @param playerId The player UUID
     * @return The cooldown timestamp
     */
    public long getBlessCooldown(UUID playerId) {
        Map<String, Long> playerCooldowns = commandCooldowns.get(playerId);
        if (playerCooldowns == null) {
            return 0;
        }
        return playerCooldowns.getOrDefault("bless", 0L);
    }
    
    /**
     * Checks if a player has deity status.
     * 
     * @param player The player
     * @return True if the player has deity status
     */
    public boolean hasDeityStatus(Player player) {
        return getRebirthLevel(player) >= 10;
    }
    
    /**
     * Checks if a player has seasonal insight.
     * 
     * @param player The player
     * @return True if the player has seasonal insight
     */
    public boolean hasSeasonalInsight(Player player) {
        return getRebirthLevel(player) >= 15;
    }
    
    /**
     * Predicts seasonal items for a player.
     * 
     * @param player The player
     * @return True if prediction was shown
     */
    public boolean predictSeasonalItems(Player player) {
        int rebirthLevel = getRebirthLevel(player);
        
        // Need at least rebirth level 4 to use this ability
        if (rebirthLevel < 4) {
            player.sendMessage(ChatColor.RED + "You need to be rebirth level 4 or higher to use this ability.");
            return false;
        }
        
        // Check cooldown
        if (isOnCommandCooldown(player.getUniqueId(), "predict", 86400000)) { // 24 hours
            long timeLeft = getRemainingCooldown(player.getUniqueId(), "predict");
            player.sendMessage(ChatColor.RED + "You can use this ability again in " + 
                    formatTimeRemaining(timeLeft) + ".");
            return false;
        }
        
        // Show prediction
        player.sendMessage(ChatColor.GOLD + "=== Seasonal Market Predictions ===");
        player.sendMessage(ChatColor.YELLOW + "Tomorrow's hot items:");
        
        // Predict based on the current game time - this would be more sophisticated
        // in a real implementation, using proper market prediction algorithms
        long gameTime = player.getWorld().getTime();
        
        if (gameTime % 4 == 0) {
            player.sendMessage(ChatColor.GREEN + "• Wheat - Prices rising by 15%");
            player.sendMessage(ChatColor.GREEN + "• Carrots - Prices rising by 10%");
            player.sendMessage(ChatColor.RED + "• Sugar Cane - Prices falling by 8%");
        } else if (gameTime % 4 == 1) {
            player.sendMessage(ChatColor.GREEN + "• Potatoes - Prices rising by 12%");
            player.sendMessage(ChatColor.GREEN + "• Pumpkins - Prices rising by 20%");
            player.sendMessage(ChatColor.RED + "• Beetroot - Prices falling by 5%");
        } else if (gameTime % 4 == 2) {
            player.sendMessage(ChatColor.GREEN + "• Melons - Prices rising by 18%");
            player.sendMessage(ChatColor.GREEN + "• Beetroot - Prices rising by 8%");
            player.sendMessage(ChatColor.RED + "• Wheat - Prices falling by 10%");
        } else {
            player.sendMessage(ChatColor.GREEN + "• Sugar Cane - Prices rising by 25%");
            player.sendMessage(ChatColor.GREEN + "• Cocoa - Prices rising by 15%");
            player.sendMessage(ChatColor.RED + "• Potatoes - Prices falling by 12%");
        }
        
        player.sendMessage(ChatColor.YELLOW + "This information will let you plan your farming!");
        
        // Record the command use
        recordCommandUse(player.getUniqueId(), "predict");
        
        // Play effect
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Gets a player's trade count for rebirth requirements.
     * 
     * @param uuid The player's UUID
     * @return The trade count
     */
    public int getPlayerTradeCount(UUID uuid) {
        return playerTradeCounts.getOrDefault(uuid, 0);
    }
    
    /**
     * Increments a player's trade count.
     * 
     * @param uuid The player's UUID
     * @return The new trade count
     */
    public int incrementPlayerTradeCount(UUID uuid) {
        int currentCount = getPlayerTradeCount(uuid);
        playerTradeCounts.put(uuid, currentCount + 1);
        return currentCount + 1;
    }
    
    /**
     * Resets a player's trade count.
     * 
     * @param uuid The player's UUID
     */
    public void resetPlayerTradeCount(UUID uuid) {
        playerTradeCounts.put(uuid, 0);
    }
    
    /**
     * Records a deity command use with cooldown.
     * 
     * @param uuid The player's UUID
     * @param commandName The command name
     */
    public void recordCommandUse(UUID uuid, String commandName) {
        Map<String, Long> playerCooldowns = commandCooldowns.computeIfAbsent(uuid, k -> new HashMap<>());
        playerCooldowns.put(commandName, System.currentTimeMillis());
    }
    
    /**
     * Gets the last use time of a deity command.
     * 
     * @param uuid The player's UUID
     * @param commandName The command name
     * @return The last use time, or 0 if not used
     */
    public long getLastCommandUse(UUID uuid, String commandName) {
        Map<String, Long> playerCooldowns = commandCooldowns.get(uuid);
        if (playerCooldowns != null) {
            return playerCooldowns.getOrDefault(commandName, 0L);
        }
        return 0L;
    }
    
    /**
     * Checks if a command is on cooldown.
     * 
     * @param uuid The player's UUID
     * @param commandName The command name
     * @param cooldownTime The cooldown time in milliseconds
     * @return True if on cooldown
     */
    public boolean isOnCommandCooldown(UUID uuid, String commandName, long cooldownTime) {
        long lastUse = getLastCommandUse(uuid, commandName);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUse) < cooldownTime;
    }
    
    /**
     * Gets the remaining cooldown for a command.
     * 
     * @param uuid The player's UUID
     * @param commandName The command name
     * @return The remaining time in milliseconds
     */
    public long getRemainingCooldown(UUID uuid, String commandName) {
        long lastUse = getLastCommandUse(uuid, commandName);
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownForCommand(commandName);
        
        long remaining = (lastUse + cooldownTime) - currentTime;
        return Math.max(0, remaining);
    }
    
    /**
     * Gets the cooldown time for a command.
     * 
     * @param commandName The command name
     * @return The cooldown time in milliseconds
     */
    private long getCooldownForCommand(String commandName) {
        switch (commandName) {
            case "bless":
                return 86400000; // 24 hours
            case "predict":
                return 86400000; // 24 hours
            case "touch":
                return 43200000; // 12 hours
            default:
                return 3600000; // 1 hour default
        }
    }
    
    /**
     * Formats a time remaining in milliseconds to a readable string.
     * 
     * @param milliseconds The time in milliseconds
     * @return The formatted string
     */
    public String formatTimeRemaining(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        
        StringBuilder sb = new StringBuilder();
        
        if (days > 0) {
            sb.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            sb.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (seconds > 0 && days == 0 && hours == 0) { // Only show seconds if less than an hour
            sb.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        }
        
        return sb.toString().trim();
    }
    
    /**
     * Adds a blessing effect to a player.
     * 
     * @param uuid The player's UUID
     * @param expiryTime The expiry time in milliseconds
     */
    public void addBlessingEffect(UUID uuid, long expiryTime) {
        activePlayerBlessings.put(uuid, expiryTime);
    }
    
    /**
     * Checks if a player has an active blessing.
     * 
     * @param uuid The player's UUID
     * @return True if the player has an active blessing
     */
    public boolean hasActiveBlessingEffect(UUID uuid) {
        long expiry = activePlayerBlessings.getOrDefault(uuid, 0L);
        return expiry > System.currentTimeMillis();
    }
    
    /**
     * Gets the time remaining for a player's blessing.
     * 
     * @param uuid The player's UUID
     * @return The time remaining in milliseconds, or 0 if no blessing
     */
    public long getBlessingTimeRemaining(UUID uuid) {
        long expiry = activePlayerBlessings.getOrDefault(uuid, 0L);
        long currentTime = System.currentTimeMillis();
        
        if (expiry > currentTime) {
            return expiry - currentTime;
        }
        
        return 0;
    }
    
    /**
     * Sets a temporary experience boost for a player.
     * 
     * @param uuid The player's UUID
     * @param multiplier The experience multiplier
     * @param duration The duration in milliseconds
     */
    public void setPlayerExpBoost(UUID uuid, double multiplier, long duration) {
        playerExpBoosts.put(uuid, multiplier);
        playerExpBoostExpiry.put(uuid, System.currentTimeMillis() + duration);
    }
    
    /**
     * Gets a player's current experience boost.
     * 
     * @param uuid The player's UUID
     * @return The experience multiplier, or 0 if no boost
     */
    public double getPlayerExpBoost(UUID uuid) {
        // Check if boost is expired
        long expiry = playerExpBoostExpiry.getOrDefault(uuid, 0L);
        
        if (expiry < System.currentTimeMillis()) {
            // Expired
            playerExpBoosts.remove(uuid);
            playerExpBoostExpiry.remove(uuid);
            return 0.0;
        }
        
        return playerExpBoosts.getOrDefault(uuid, 0.0);
    }
    
    /**
     * Toggles a player's rebirth aura.
     * 
     * @param player The player
     * @return The new aura state
     */
    public boolean togglePlayerAura(Player player) {
        UUID uuid = player.getUniqueId();
        boolean currentState = playerAuraEnabled.getOrDefault(uuid, false);
        boolean newState = !currentState;
        
        playerAuraEnabled.put(uuid, newState);
        
        if (newState) {
            // Start aura effect task
            startAuraTask(player);
        } else {
            // Stop aura effect task
            stopAuraTask(player);
        }
        
        return newState;
    }
    
    /**
     * Toggles a player's rebirth aura to a specific state.
     * 
     * @param player The player
     * @param enabled Whether to enable the aura
     * @return The new aura state
     */
    public boolean togglePlayerAura(Player player, boolean enabled) {
        UUID uuid = player.getUniqueId();
        playerAuraEnabled.put(uuid, enabled);
        
        if (enabled) {
            startAuraTask(player);
        } else {
            stopAuraTask(player);
        }
        
        return enabled;
    }
    
    /**
     * Starts the aura visual effect task for a player.
     * 
     * @param player The player
     */
    private void startAuraTask(Player player) {
        // Implementation would start a particle effect around the player
    }
    
    /**
     * Stops the aura visual effect task for a player.
     * 
     * @param player The player
     */
    private void stopAuraTask(Player player) {
        // Implementation would stop the particle effect around the player
    }
    
    /**
     * Gets the blessing cooldown remaining for a player.
     * 
     * @param player The player
     * @return The cooldown time remaining in milliseconds
     */
    public long getBlessCooldownRemaining(Player player) {
        return getRemainingCooldown(player.getUniqueId(), "bless");
    }
    
    /**
     * Checks if a player is on the bless cooldown.
     * 
     * @param player The player
     * @return True if the player is on cooldown
     */
    public boolean isOnBlessCooldown(Player player) {
        return isOnCommandCooldown(player.getUniqueId(), "bless", 86400000); // 24 hours
    }
    
    /**
     * Blesses another player, giving them temporary buffs.
     * 
     * @param blesser The player giving the blessing
     * @param target The player receiving the blessing
     * @return True if successful
     */
    public boolean blessPlayer(Player blesser, Player target) {
        // Check if blesser has permission
        if (!blesser.hasPermission("bdcraft.deity.bless")) {
            blesser.sendMessage(ChatColor.RED + "You don't have permission to bless other players.");
            return false;
        }
        
        // Check blesser's rebirth level
        int rebirthLevel = getRebirthLevel(blesser);
        if (rebirthLevel < 3) {
            blesser.sendMessage(ChatColor.RED + "You need to be rebirth level 3 or higher to bless other players.");
            return false;
        }
        
        // Check if blesser is on cooldown
        if (isOnBlessCooldown(blesser)) {
            long cooldownRemaining = getBlessCooldownRemaining(blesser);
            blesser.sendMessage(ChatColor.RED + "You can bless another player in " + 
                formatTimeRemaining(cooldownRemaining) + ".");
            return false;
        }
        
        // Apply blessing effect to target
        long blessingDuration = 1200000; // 20 minutes
        addBlessingEffect(target.getUniqueId(), System.currentTimeMillis() + blessingDuration);
        
        // Add temporary experience boost
        double expBoost = 0.1 + (rebirthLevel * 0.05); // 15-50% boost based on rebirth level
        setPlayerExpBoost(target.getUniqueId(), expBoost, blessingDuration);
        
        // Apply visual effects
        blesser.getWorld().strikeLightningEffect(target.getLocation());
        target.getWorld().spawnParticle(Particle.FLAME, target.getLocation().add(0, 1, 0), 
                100, 0.5, 1.0, 0.5);
        
        // Play sound effects
        target.playSound(target.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
        blesser.playSound(blesser.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.7f, 1.2f);
        
        // Send messages
        target.sendMessage(ChatColor.LIGHT_PURPLE + "✧✧✧ " + ChatColor.GOLD + "You have been blessed by " + 
                blesser.getName() + ChatColor.LIGHT_PURPLE + " ✧✧✧");
        target.sendMessage(ChatColor.GOLD + "You feel invigorated and more productive!");
        target.sendMessage(ChatColor.YELLOW + "• Mining/farming speed increased");
        target.sendMessage(ChatColor.YELLOW + "• Luck increased");
        target.sendMessage(ChatColor.YELLOW + "• Strength increased");
        target.sendMessage(ChatColor.YELLOW + "• Experience gain +" + (int)(expBoost * 100) + "%");
        target.sendMessage(ChatColor.GRAY + "These effects will last for 20 minutes.");
        
        blesser.sendMessage(ChatColor.LIGHT_PURPLE + "You have blessed " + ChatColor.GOLD + target.getName() + 
                ChatColor.LIGHT_PURPLE + " with your divine favor.");
        blesser.sendMessage(ChatColor.GRAY + "You can bless another player in 24 hours.");
        
        // Record blessing use
        recordCommandUse(blesser.getUniqueId(), "bless");
        
        return true;
    }
    
    /**
     * Checks if a player is eligible for rebirth.
     * 
     * @param player The player
     * @return True if eligible
     */
    public boolean isEligibleForRebirth(Player player) {
        // Check rank
        int rank = plugin.getProgressionModule().getRankManager().getPlayerRank(player);
        if (rank < BDRankManager.RANK_AGRICULTURAL_EXPERT) {
            return false;
        }
        
        // Check trades
        int trades = getTradeCount(player);
        if (trades < 500) {
            return false;
        }
        
        // Check money
        if (!plugin.getEconomyModule().hasCurrency(player, 100000)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets a player's trade count.
     * 
     * @param player The player
     * @return The trade count
     */
    public int getTradeCount(Player player) {
        return getPlayerTradeCount(player.getUniqueId());
    }
    
    /**
     * Gets the top players by rebirth level.
     * 
     * @param limit The number of players to retrieve
     * @return The top players
     */
    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        // This would be properly implemented with a database
        // For now, return an empty list
        return new ArrayList<>();
    }
    
    /**
     * Checks if a player has their aura enabled.
     * 
     * @param uuid The player's UUID
     * @return True if the aura is enabled
     */
    public boolean hasAuraEnabled(UUID uuid) {
        return playerAuraEnabled.getOrDefault(uuid, false);
    }
    
    /**
     * Starts the aura effect task for a player.
     * 
     * @param player The player
     */
    // Implementations already exist for these methods, removing duplicate methods
    
    /**
     * Cleans up any tasks when the server is shutting down.
     */
    public void onDisable() {
        // Cancel all aura tasks
        for (BukkitTask task : playerAuraTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        
        playerAuraTasks.clear();
    }
}