package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages rebirth progression for players.
 */
public class BDRebirthManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, Integer> playerRebirthLevels;
    private final Map<UUID, Integer> playerTradeCounters;
    private final Map<UUID, Long> playerBlessCooldowns;
    private final Map<UUID, Boolean> playerAuraToggles;
    private File rebirthFile;
    private FileConfiguration rebirthConfig;
    
    // Minimum requirements for rebirth
    private static final int MIN_CURRENCY_FOR_REBIRTH = 100000;
    private static final int MIN_TRADES_FOR_REBIRTH = 500;
    
    /**
     * Creates a new rebirth manager.
     * 
     * @param plugin The plugin instance
     */
    public BDRebirthManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerRebirthLevels = new HashMap<>();
        this.playerTradeCounters = new HashMap<>();
        this.playerBlessCooldowns = new HashMap<>();
        this.playerAuraToggles = new HashMap<>();
        
        // Load player data
        loadData();
        
        // Start aura effect task for deity players
        startAuraEffectTask();
    }
    
    /**
     * Loads rebirth data from configuration.
     */
    private void loadData() {
        rebirthFile = new File(plugin.getDataFolder(), "rebirth.yml");
        
        if (!rebirthFile.exists()) {
            plugin.saveResource("rebirth.yml", false);
        }
        
        rebirthConfig = YamlConfiguration.loadConfiguration(rebirthFile);
        
        // Load rebirth levels
        if (rebirthConfig.contains("levels")) {
            for (String uuidStr : rebirthConfig.getConfigurationSection("levels").getKeys(false)) {
                try {
                    UUID playerUuid = UUID.fromString(uuidStr);
                    int level = rebirthConfig.getInt("levels." + uuidStr);
                    
                    playerRebirthLevels.put(playerUuid, level);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid UUID in rebirth.yml: " + uuidStr);
                }
            }
        }
        
        // Load trade counters
        if (rebirthConfig.contains("trades")) {
            for (String uuidStr : rebirthConfig.getConfigurationSection("trades").getKeys(false)) {
                try {
                    UUID playerUuid = UUID.fromString(uuidStr);
                    int trades = rebirthConfig.getInt("trades." + uuidStr);
                    
                    playerTradeCounters.put(playerUuid, trades);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid UUID in rebirth.yml: " + uuidStr);
                }
            }
        }
        
        // Load aura toggles
        if (rebirthConfig.contains("aura_toggles")) {
            for (String uuidStr : rebirthConfig.getConfigurationSection("aura_toggles").getKeys(false)) {
                try {
                    UUID playerUuid = UUID.fromString(uuidStr);
                    boolean enabled = rebirthConfig.getBoolean("aura_toggles." + uuidStr);
                    
                    playerAuraToggles.put(playerUuid, enabled);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid UUID in rebirth.yml: " + uuidStr);
                }
            }
        }
        
        // Load bless cooldowns
        if (rebirthConfig.contains("bless_cooldowns")) {
            for (String uuidStr : rebirthConfig.getConfigurationSection("bless_cooldowns").getKeys(false)) {
                try {
                    UUID playerUuid = UUID.fromString(uuidStr);
                    long cooldown = rebirthConfig.getLong("bless_cooldowns." + uuidStr);
                    
                    playerBlessCooldowns.put(playerUuid, cooldown);
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid UUID in rebirth.yml: " + uuidStr);
                }
            }
        }
    }
    
    /**
     * Saves rebirth data to configuration.
     */
    public void saveData() {
        // Save rebirth levels
        rebirthConfig.set("levels", null);
        for (Map.Entry<UUID, Integer> entry : playerRebirthLevels.entrySet()) {
            rebirthConfig.set("levels." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save trade counters
        rebirthConfig.set("trades", null);
        for (Map.Entry<UUID, Integer> entry : playerTradeCounters.entrySet()) {
            rebirthConfig.set("trades." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save aura toggles
        rebirthConfig.set("aura_toggles", null);
        for (Map.Entry<UUID, Boolean> entry : playerAuraToggles.entrySet()) {
            rebirthConfig.set("aura_toggles." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save bless cooldowns
        rebirthConfig.set("bless_cooldowns", null);
        for (Map.Entry<UUID, Long> entry : playerBlessCooldowns.entrySet()) {
            rebirthConfig.set("bless_cooldowns." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save to file
        try {
            rebirthConfig.save(rebirthFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save rebirth.yml", e);
        }
    }
    
    /**
     * Gets a player's rebirth level.
     * 
     * @param player The player
     * @return The rebirth level
     */
    public int getRebirthLevel(Player player) {
        return playerRebirthLevels.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Gets a player's rebirth level by UUID.
     * 
     * @param uuid The player UUID
     * @return The rebirth level
     */
    public int getRebirthLevel(UUID uuid) {
        return playerRebirthLevels.getOrDefault(uuid, 0);
    }
    
    /**
     * Gets a player's trade count.
     * 
     * @param player The player
     * @return The trade count
     */
    public int getTradeCount(Player player) {
        return playerTradeCounters.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Gets a player's trade count by UUID.
     * 
     * @param uuid The player UUID
     * @return The trade count
     */
    public int getTradeCount(UUID uuid) {
        return playerTradeCounters.getOrDefault(uuid, 0);
    }
    
    /**
     * Increments a player's trade count.
     * 
     * @param player The player
     */
    public void incrementTradeCount(Player player) {
        int currentCount = getTradeCount(player);
        playerTradeCounters.put(player.getUniqueId(), currentCount + 1);
    }
    
    /**
     * Checks if a player is eligible for rebirth.
     * 
     * @param player The player
     * @return Whether the player is eligible
     */
    public boolean isEligibleForRebirth(Player player) {
        // Check player rank
        int rank = plugin.getProgressionModule().getRankManager().getPlayerRank(player);
        if (rank < 5) {
            return false;
        }
        
        // Check player balance
        int balance = plugin.getEconomyModule().getPlayerBalance(player);
        if (balance < MIN_CURRENCY_FOR_REBIRTH) {
            return false;
        }
        
        // Check trade count
        int trades = getTradeCount(player);
        return trades >= MIN_TRADES_FOR_REBIRTH;
    }
    
    /**
     * Performs a rebirth for a player.
     * 
     * @param player The player
     * @return Whether the rebirth was successful
     */
    public boolean performRebirth(Player player) {
        if (!isEligibleForRebirth(player)) {
            player.sendMessage(ChatColor.RED + "You are not eligible for rebirth. Use /bdrebirth check to check requirements.");
            return false;
        }
        
        // Get current rebirth level
        int currentLevel = getRebirthLevel(player);
        
        // Reset player progress
        plugin.getEconomyModule().resetPlayerBalance(player);
        plugin.getProgressionModule().getRankManager().setPlayerRank(player, 1);
        
        // Increment rebirth level
        playerRebirthLevels.put(player.getUniqueId(), currentLevel + 1);
        
        // Save data
        saveData();
        
        // Apply visual effects
        applyRebirthEffect(player);
        
        // Notify player
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "        REBIRTH COMPLETE        " + ChatColor.GOLD + "║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════╣");
        player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.WHITE + "Your new rebirth level: " + ChatColor.GREEN + (currentLevel + 1) + ChatColor.GOLD + "        ║");
        player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.WHITE + "Crop value bonus: " + ChatColor.GREEN + "+" + ((currentLevel + 1) * 10) + "%" + ChatColor.GOLD + "         ║");
        
        // Show new rebirth perks
        showNewRebirthPerks(player, currentLevel + 1);
        
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════╝");
        
        return true;
    }
    
    /**
     * Shows new perks for a rebirth level.
     * 
     * @param player The player
     * @param level The rebirth level
     */
    private void showNewRebirthPerks(Player player, int level) {
        switch (level) {
            case 1:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "+5% double harvest chance" + ChatColor.GOLD + "   ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Gold name prefix [R1]" + ChatColor.GOLD + "      ║");
                break;
            case 2:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Gold particles when farming" + ChatColor.GOLD + "  ║");
                break;
            case 3:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "+10% double harvest chance" + ChatColor.GOLD + "  ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Enhanced gold particles" + ChatColor.GOLD + "     ║");
                break;
            case 5:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "+5% villager reputation gain" + ChatColor.GOLD + " ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Green tint name with aura" + ChatColor.GOLD + "   ║");
                break;
            case 7:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "-25% seed cost (10% chance)" + ChatColor.GOLD + " ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Rainbow-cycling name prefix" + ChatColor.GOLD + "  ║");
                break;
            case 10:
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Agricultural Deity status!" + ChatColor.GOLD + "  ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Golden Touch ability" + ChatColor.GOLD + "        ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Harvester's Blessing" + ChatColor.GOLD + "        ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "+25% better villager trades" + ChatColor.GOLD + "  ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Seasonal Insight ability" + ChatColor.GOLD + "     ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Abundance Aura (20 blocks)" + ChatColor.GOLD + "   ║");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.YELLOW + "NEW: " + ChatColor.WHITE + "Deity commands unlocked" + ChatColor.GOLD + "      ║");
                break;
            default:
                // No specific perks for this level
                break;
        }
    }
    
    /**
     * Applies visual effects when a player rebirths.
     * 
     * @param player The player
     */
    private void applyRebirthEffect(Player player) {
        // Spawn particles, play sound, etc.
        // Implementation depends on Bukkit/Spigot API for particles and sounds
    }
    
    /**
     * Gets the crop value multiplier for a player based on rebirth level.
     * 
     * @param player The player
     * @return The multiplier (1.0 = 100%, 1.1 = 110%, etc.)
     */
    public double getCropValueMultiplier(Player player) {
        int rebirthLevel = getRebirthLevel(player);
        return 1.0 + (rebirthLevel * 0.1); // +10% per rebirth level
    }
    
    /**
     * Checks if a player has the double harvest chance.
     * 
     * @param player The player
     * @return The chance (0.0 - 1.0) of getting a double harvest
     */
    public double getDoubleHarvestChance(Player player) {
        int rebirthLevel = getRebirthLevel(player);
        
        if (rebirthLevel >= 3) {
            return 0.10; // 10% chance at rebirth 3+
        } else if (rebirthLevel >= 1) {
            return 0.05; // 5% chance at rebirth 1-2
        }
        
        return 0.0; // No chance at rebirth 0
    }
    
    /**
     * Gets the reputation gain bonus for a player.
     * 
     * @param player The player
     * @return The bonus (0.0 - 1.0) to reputation gain
     */
    public double getReputationBonus(Player player) {
        int rebirthLevel = getRebirthLevel(player);
        
        if (rebirthLevel >= 5) {
            return 0.05; // 5% bonus at rebirth 5+
        }
        
        return 0.0; // No bonus below rebirth 5
    }
    
    /**
     * Checks if a player has the chance for reduced seed costs.
     * 
     * @param player The player
     * @return True if the player has the chance
     */
    public boolean hasReducedSeedCostChance(Player player) {
        return getRebirthLevel(player) >= 7;
    }
    
    /**
     * Gets the chance for reduced seed costs.
     * 
     * @param player The player
     * @return The chance (0.0 - 1.0) of getting reduced costs
     */
    public double getReducedSeedCostChance(Player player) {
        if (hasReducedSeedCostChance(player)) {
            return 0.10; // 10% chance at rebirth 7+
        }
        
        return 0.0;
    }
    
    /**
     * Gets the reduction amount for seed costs.
     * 
     * @param player The player
     * @return The reduction multiplier (0.0 - 1.0)
     */
    public double getReducedSeedCostAmount(Player player) {
        if (hasReducedSeedCostChance(player)) {
            return 0.25; // 25% reduction
        }
        
        return 0.0;
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
     * Gets the chance for the Golden Touch ability.
     * 
     * @param player The player
     * @return The chance (0.0 - 1.0) of triggering Golden Touch
     */
    public double getGoldenTouchChance(Player player) {
        if (hasDeityStatus(player)) {
            return 0.25; // 25% chance for deity status
        }
        
        return 0.0;
    }
    
    /**
     * Gets the chance for the Harvester's Blessing ability.
     * 
     * @param player The player
     * @return The chance (0.0 - 1.0) of triggering Harvester's Blessing
     */
    public double getHarvesterBlessingChance(Player player) {
        if (hasDeityStatus(player)) {
            return 0.50; // 50% chance for deity status
        }
        
        return 0.0;
    }
    
    /**
     * Gets the Divine Favor trading bonus.
     * 
     * @param player The player
     * @return The bonus (0.0 - 1.0) to trading
     */
    public double getDivineFavorBonus(Player player) {
        if (hasDeityStatus(player)) {
            return 0.25; // 25% bonus for deity status
        }
        
        return 0.0;
    }
    
    /**
     * Gets the Abundance Aura radius.
     * 
     * @param player The player
     * @return The radius in blocks, or 0 if not active
     */
    public int getAbundanceAuraRadius(Player player) {
        if (hasDeityStatus(player) && isAuraEnabled(player)) {
            return 20; // 20 block radius for deity status
        }
        
        return 0;
    }
    
    /**
     * Gets the Abundance Aura bonus.
     * 
     * @param player The player
     * @return The bonus (0.0 - 1.0) from the aura
     */
    public double getAbundanceAuraBonus() {
        return 0.10; // 10% bonus from aura
    }
    
    /**
     * Checks if a player is on bless cooldown.
     * 
     * @param player The player
     * @return Whether the player is on cooldown
     */
    public boolean isOnBlessCooldown(Player player) {
        if (!playerBlessCooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        
        long cooldownTime = playerBlessCooldowns.get(player.getUniqueId());
        return System.currentTimeMillis() < cooldownTime;
    }
    
    /**
     * Gets the remaining cooldown time for a player's blessing.
     * 
     * @param player The player
     * @return The cooldown in milliseconds, or 0 if not on cooldown
     */
    public long getBlessCooldownRemaining(Player player) {
        if (!isOnBlessCooldown(player)) {
            return 0;
        }
        
        long cooldownTime = playerBlessCooldowns.get(player.getUniqueId());
        return cooldownTime - System.currentTimeMillis();
    }
    
    /**
     * Gets the top rebirth players.
     * 
     * @param limit The maximum number of players to return
     * @return A list of player UUIDs and their rebirth levels, sorted by level
     */
    public List<Map.Entry<UUID, Integer>> getTopRebirthPlayers(int limit) {
        List<Map.Entry<UUID, Integer>> sortedPlayers = new ArrayList<>(playerRebirthLevels.entrySet());
        
        // Sort by rebirth level (descending)
        sortedPlayers.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Limit the list
        if (sortedPlayers.size() > limit) {
            sortedPlayers = sortedPlayers.subList(0, limit);
        }
        
        return sortedPlayers;
    }
    
    /**
     * Checks if a player's aura is enabled.
     * 
     * @param player The player
     * @return True if the player's aura is enabled
     */
    public boolean isAuraEnabled(Player player) {
        return playerAuraToggles.getOrDefault(player.getUniqueId(), true);
    }
    
    /**
     * Sets whether a player's aura is enabled.
     * 
     * @param player The player
     * @param enabled Whether the aura is enabled
     */
    public void setAuraEnabled(Player player, boolean enabled) {
        playerAuraToggles.put(player.getUniqueId(), enabled);
        saveData();
    }
    
    /**
     * Toggles a player's aura.
     * 
     * @param player The player
     * @return The new state
     */
    public boolean toggleAura(Player player) {
        boolean newState = !isAuraEnabled(player);
        setAuraEnabled(player, newState);
        return newState;
    }
    
    /**
     * Blesses a target player with a temporary trading boost.
     * 
     * @param sourcePlayer The blessing player
     * @param targetPlayer The target player
     * @return True if the blessing was successful
     */
    public boolean blessPlayer(Player sourcePlayer, Player targetPlayer) {
        if (!hasDeityStatus(sourcePlayer)) {
            sourcePlayer.sendMessage(ChatColor.RED + "You must have Agricultural Deity status to bless other players.");
            return false;
        }
        
        // Check if on cooldown
        if (isOnBlessCooldown(sourcePlayer)) {
            long cooldownRemaining = getBlessCooldownRemaining(sourcePlayer);
            long hours = cooldownRemaining / 3600000;
            long minutes = (cooldownRemaining % 3600000) / 60000;
            
            sourcePlayer.sendMessage(ChatColor.RED + "You can bless another player in " + 
                    hours + "h " + minutes + "m.");
            return false;
        }
        
        // Apply blessing
        plugin.getProgressionModule().applyBlessingEffect(targetPlayer);
        
        // Set 24-hour cooldown
        long cooldownTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
        playerBlessCooldowns.put(sourcePlayer.getUniqueId(), cooldownTime);
        saveData();
        
        // Notify players already handled in applyBlessingEffect
        return true;
    }
    
    /**
     * Starts the aura effect task for deity players.
     */
    private void startAuraEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (hasDeityStatus(player) && isAuraEnabled(player)) {
                        applyAuraEffects(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 100L, 100L); // Run every 5 seconds (100 ticks)
    }
    
    /**
     * Applies aura effects for a deity player.
     * 
     * @param deityPlayer The deity player
     */
    private void applyAuraEffects(Player deityPlayer) {
        int radius = getAbundanceAuraRadius(deityPlayer);
        
        for (Player nearbyPlayer : deityPlayer.getWorld().getPlayers()) {
            // Skip if it's the deity player
            if (nearbyPlayer.equals(deityPlayer)) {
                continue;
            }
            
            // Check if in range
            if (nearbyPlayer.getLocation().distance(deityPlayer.getLocation()) <= radius) {
                // Apply aura effect (visual indication)
                // Implementation depends on Bukkit/Spigot API
            }
        }
    }
    
    /**
     * Predicts the next seasonal trader items for deity players.
     * 
     * @param player The player
     * @return True if the prediction was shown
     */
    public boolean predictSeasonalItems(Player player) {
        if (!hasDeityStatus(player)) {
            player.sendMessage(ChatColor.RED + "You need Agricultural Deity status to predict seasonal items.");
            return false;
        }
        
        // Get next day's seasonal trader items
        // This would integrate with the seasonal trader system
        player.sendMessage(ChatColor.GOLD + "Your seasonal insight reveals tomorrow's special trades:");
        
        // Placeholder - actual implementation would show real predictions
        player.sendMessage(ChatColor.YELLOW + "• " + ChatColor.WHITE + "Special Golden Hoe (25% faster tilling)");
        player.sendMessage(ChatColor.YELLOW + "• " + ChatColor.WHITE + "Enchanted Green Seeds (2x growth rate)");
        player.sendMessage(ChatColor.YELLOW + "• " + ChatColor.WHITE + "Rare Farming Manual (+10% XP for 2 hours)");
        
        return true;
    }
    
    /**
     * Gets the name prefix for a player based on rebirth level.
     * 
     * @param player The player
     * @return The prefix string
     */
    public String getRebirthPrefix(Player player) {
        int level = getRebirthLevel(player);
        
        if (level <= 0) {
            return "";
        }
        
        if (level >= 10) {
            return ChatColor.GOLD + "【" + ChatColor.YELLOW + "✯" + ChatColor.RED + "R" + level + 
                    ChatColor.YELLOW + "✯" + ChatColor.GOLD + "】";
        } else if (level >= 7) {
            // Rainbow effect would be implemented elsewhere, this is static representation
            return ChatColor.GOLD + "【" + ChatColor.GREEN + "R" + level + ChatColor.GOLD + "】";
        } else if (level >= 5) {
            return ChatColor.GOLD + "【" + ChatColor.DARK_GREEN + "R" + level + ChatColor.GOLD + "】";
        } else {
            return ChatColor.GOLD + "[R" + level + "]";
        }
    }
    
    /**
     * Gets the top rebirth players.
     * 
     * @param limit The number of players to include
     * @return The top players and their levels
     */
    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        List<Map.Entry<UUID, Integer>> entries = new ArrayList<>(playerRebirthLevels.entrySet());
        
        // Sort by rebirth level (descending)
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Limit to specified number
        return entries.subList(0, Math.min(limit, entries.size()));
    }
}