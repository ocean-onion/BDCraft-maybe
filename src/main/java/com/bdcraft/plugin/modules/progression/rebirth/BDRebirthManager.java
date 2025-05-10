package com.bdcraft.plugin.modules.progression.rebirth;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.util.ParticleHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Manages player rebirth features.
 */
public class BDRebirthManager {
    private final BDCraft plugin;
    private File dataFile;
    private FileConfiguration data;
    
    // Player data
    private final Map<UUID, Integer> rebirthLevels;
    private final Map<UUID, Boolean> auraEnabled;
    private final Map<UUID, Map<String, Long>> commandUseTimes;
    private final Map<UUID, Long> blessingEffects;
    private final Map<UUID, ExpBoost> expBoosts;
    private final Map<UUID, Double> tradeBonuses;
    private final Map<UUID, Long> tradeBonusExpirations;
    
    // Aura task
    private BukkitTask auraTask;
    
    /**
     * Creates a new rebirth manager.
     *
     * @param plugin The plugin instance
     */
    public BDRebirthManager(BDCraft plugin) {
        this.plugin = plugin;
        this.rebirthLevels = new HashMap<>();
        this.auraEnabled = new HashMap<>();
        this.commandUseTimes = new HashMap<>();
        this.blessingEffects = new HashMap<>();
        this.expBoosts = new HashMap<>();
        this.tradeBonuses = new HashMap<>();
        this.tradeBonusExpirations = new HashMap<>();
        
        // Load data
        loadData();
        
        // Start aura task
        startAuraTask();
    }
    
    /**
     * Loads rebirth data.
     */
    private void loadData() {
        // Create data directory if it doesn't exist
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create or load data file
        dataFile = new File(dataDir, "rebirth.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create rebirth data file", e);
            }
        }
        
        // Load data
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load rebirth levels
        if (data.contains("levels")) {
            for (String uuidString : data.getConfigurationSection("levels").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int level = data.getInt("levels." + uuidString);
                    rebirthLevels.put(uuid, level);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in rebirth data: " + uuidString);
                }
            }
        }
        
        // Load aura settings
        if (data.contains("aura")) {
            for (String uuidString : data.getConfigurationSection("aura").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    boolean enabled = data.getBoolean("aura." + uuidString);
                    auraEnabled.put(uuid, enabled);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in aura data: " + uuidString);
                }
            }
        }
        
        // Load command use times
        if (data.contains("command-uses")) {
            for (String uuidString : data.getConfigurationSection("command-uses").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    Map<String, Long> commandTimes = new HashMap<>();
                    
                    for (String command : data.getConfigurationSection("command-uses." + uuidString).getKeys(false)) {
                        long time = data.getLong("command-uses." + uuidString + "." + command);
                        commandTimes.put(command, time);
                    }
                    
                    commandUseTimes.put(uuid, commandTimes);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in command-use data: " + uuidString);
                }
            }
        }
        
        // Load blessing effects
        if (data.contains("blessings")) {
            for (String uuidString : data.getConfigurationSection("blessings").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    long endTime = data.getLong("blessings." + uuidString);
                    
                    // Only add if not expired
                    if (endTime > System.currentTimeMillis()) {
                        blessingEffects.put(uuid, endTime);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in blessing data: " + uuidString);
                }
            }
        }
        
        // Load exp boosts
        if (data.contains("exp-boosts")) {
            for (String uuidString : data.getConfigurationSection("exp-boosts").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    double amount = data.getDouble("exp-boosts." + uuidString + ".amount");
                    long endTime = data.getLong("exp-boosts." + uuidString + ".end-time");
                    
                    // Only add if not expired
                    if (endTime > System.currentTimeMillis()) {
                        ExpBoost boost = new ExpBoost(amount, endTime);
                        expBoosts.put(uuid, boost);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in exp-boost data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded rebirth data for " + rebirthLevels.size() + " players.");
    }
    
    /**
     * Saves rebirth data.
     */
    private void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save rebirth levels
        for (Map.Entry<UUID, Integer> entry : rebirthLevels.entrySet()) {
            data.set("levels." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save aura settings
        for (Map.Entry<UUID, Boolean> entry : auraEnabled.entrySet()) {
            data.set("aura." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save command use times
        for (Map.Entry<UUID, Map<String, Long>> entry : commandUseTimes.entrySet()) {
            String uuidString = entry.getKey().toString();
            
            for (Map.Entry<String, Long> commandEntry : entry.getValue().entrySet()) {
                data.set("command-uses." + uuidString + "." + commandEntry.getKey(), commandEntry.getValue());
            }
        }
        
        // Save blessing effects
        for (Map.Entry<UUID, Long> entry : blessingEffects.entrySet()) {
            // Only save if not expired
            if (entry.getValue() > System.currentTimeMillis()) {
                data.set("blessings." + entry.getKey().toString(), entry.getValue());
            } else {
                data.set("blessings." + entry.getKey().toString(), null);
            }
        }
        
        // Save exp boosts
        for (Map.Entry<UUID, ExpBoost> entry : expBoosts.entrySet()) {
            String uuidString = entry.getKey().toString();
            ExpBoost boost = entry.getValue();
            
            // Only save if not expired
            if (boost.getEndTime() > System.currentTimeMillis()) {
                data.set("exp-boosts." + uuidString + ".amount", boost.getAmount());
                data.set("exp-boosts." + uuidString + ".end-time", boost.getEndTime());
            } else {
                data.set("exp-boosts." + uuidString, null);
            }
        }
        
        // Save data file
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save rebirth data", e);
        }
    }
    
    /**
     * Gets a player's rebirth level.
     *
     * @param player The player
     * @return The rebirth level
     */
    public int getPlayerRebirthLevel(Player player) {
        return rebirthLevels.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Gets a player's rebirth level by UUID.
     *
     * @param uuid The player's UUID
     * @return The rebirth level
     */
    public int getPlayerRebirthLevel(UUID uuid) {
        return rebirthLevels.getOrDefault(uuid, 0);
    }
    
    /**
     * Sets a player's rebirth level.
     *
     * @param player The player
     * @param level The new rebirth level
     */
    public void setPlayerRebirthLevel(Player player, int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Rebirth level cannot be negative");
        }
        
        rebirthLevels.put(player.getUniqueId(), level);
        
        // Enable aura by default for reborn players
        if (level > 0 && !auraEnabled.containsKey(player.getUniqueId())) {
            auraEnabled.put(player.getUniqueId(), true);
        }
        
        // Save data
        saveData();
    }
    
    /**
     * Performs rebirth for a player.
     *
     * @param player The player
     * @param resetRank Whether to reset the player's rank
     * @return True if rebirth was successful
     */
    public boolean rebirth(Player player, boolean resetRank) {
        int currentLevel = getPlayerRebirthLevel(player);
        int newLevel = currentLevel + 1;
        
        // Set rebirth level
        rebirthLevels.put(player.getUniqueId(), newLevel);
        
        // Enable aura if this is their first rebirth
        if (currentLevel == 0) {
            auraEnabled.put(player.getUniqueId(), true);
        }
        
        // Reset rank if requested
        if (resetRank) {
            plugin.getProgressionModule().setPlayerRank(player, 0); // Reset to Newcomer
        }
        
        // Save data
        saveData();
        
        // Show effects
        showRebirthEffect(player);
        
        return true;
    }
    
    /**
     * Performs rebirth for a player, with default rank reset.
     *
     * @param player The player
     * @return True if rebirth was successful
     */
    public boolean performRebirth(Player player) {
        return rebirth(player, true);
    }
    
    /**
     * Gets the rebirth level for a player by UUID.
     *
     * @param uuid The player UUID
     * @return The rebirth level
     */
    public int getRebirthLevel(UUID uuid) {
        return rebirthLevels.getOrDefault(uuid, 0);
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
     * Shows the rebirth effect.
     *
     * @param player The player
     */
    private void showRebirthEffect(Player player) {
        Location location = player.getLocation();
        
        // Play particles
        ParticleHelper.spawnWitchParticles(location, 100, 1, 2, 1, 0.5);
        location.getWorld().spawnParticle(Particle.PORTAL, location, 200, 1, 2, 1, 1);
        
        // Play sound
        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
        
        // Send message to all players
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + 
                ChatColor.LIGHT_PURPLE + " has been reborn and reached rebirth level " + 
                getPlayerRebirthLevel(player) + "!");
    }
    
    /**
     * Starts the aura display task.
     */
    private void startAuraTask() {
        if (auraTask != null) {
            auraTask.cancel();
        }
        
        auraTask = Bukkit.getScheduler().runTaskTimer(plugin, this::showAuraEffects, 20L, 20L);
    }
    
    /**
     * Shows aura effects for all players with auras enabled.
     */
    private void showAuraEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            
            // Check if player has rebirth level and aura enabled
            if (rebirthLevels.getOrDefault(uuid, 0) > 0 && auraEnabled.getOrDefault(uuid, false)) {
                showAuraEffect(player);
            }
        }
    }
    
    /**
     * Toggles a player's aura visibility.
     *
     * @param player The player
     * @return The new state (true if enabled)
     */
    public boolean toggleAura(Player player) {
        UUID uuid = player.getUniqueId();
        boolean currentState = auraEnabled.getOrDefault(uuid, false);
        boolean newState = !currentState;
        
        auraEnabled.put(uuid, newState);
        saveData();
        
        return newState;
    }
    
    /**
     * Gets trade count for a player.
     * 
     * @param player The player
     * @return The trade count
     */
    public int getTradeCount(Player player) {
        // In a real implementation, this would retrieve from storage
        // For now, simulate based on rebirth level to maintain functionality
        int rebirthLevel = getRebirthLevel(player);
        return 500 + (rebirthLevel * 100);
    }
    
    /**
     * Gets the top players by rebirth level.
     *
     * @param limit Maximum number of players to return
     * @return List of entries with UUID and rebirth level
     */
    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        return rebirthLevels.entrySet().stream()
            .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if a player is on blessing cooldown.
     *
     * @param player The player
     * @return True if on cooldown
     */
    public boolean isOnBlessCooldown(Player player) {
        if (!commandUseTimes.containsKey(player.getUniqueId())) {
            return false;
        }
        
        Map<String, Long> cooldowns = commandUseTimes.get(player.getUniqueId());
        if (!cooldowns.containsKey("bless")) {
            return false;
        }
        
        long lastUse = cooldowns.get("bless");
        long cooldownTime = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        return System.currentTimeMillis() - lastUse < cooldownTime;
    }
    
    /**
     * Gets the remaining cooldown time for blessing.
     *
     * @param player The player
     * @return Remaining time in milliseconds
     */
    public long getBlessCooldownRemaining(Player player) {
        if (!commandUseTimes.containsKey(player.getUniqueId())) {
            return 0;
        }
        
        Map<String, Long> cooldowns = commandUseTimes.get(player.getUniqueId());
        if (!cooldowns.containsKey("bless")) {
            return 0;
        }
        
        long lastUse = cooldowns.get("bless");
        long cooldownTime = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        long elapsed = System.currentTimeMillis() - lastUse;
        
        return Math.max(0, cooldownTime - elapsed);
    }
    
    /**
     * Blesses another player, giving them a temporary boost.
     *
     * @param player The player giving the blessing
     * @param target The player receiving the blessing
     * @return True if successful
     */
    public boolean blessPlayer(Player player, Player target) {
        if (isOnBlessCooldown(player)) {
            return false;
        }
        
        // Apply blessing effect
        blessingEffects.put(target.getUniqueId(), System.currentTimeMillis() + (30 * 60 * 1000)); // 30 minutes
        
        // Set cooldown
        if (!commandUseTimes.containsKey(player.getUniqueId())) {
            commandUseTimes.put(player.getUniqueId(), new HashMap<>());
        }
        commandUseTimes.get(player.getUniqueId()).put("bless", System.currentTimeMillis());
        
        // Save data
        saveData();
        
        return true;
    }
    
    /**
     * Checks if a player has an active blessing.
     *
     * @param uuid The player's UUID
     * @return True if blessed
     */
    public boolean hasActiveBlessing(UUID uuid) {
        if (!blessingEffects.containsKey(uuid)) {
            return false;
        }
        
        return blessingEffects.get(uuid) > System.currentTimeMillis();
    }
    
    /**
     * Checks if a player is eligible for rebirth.
     *
     * @param player The player
     * @return True if eligible
     */
    public boolean isEligibleForRebirth(Player player) {
        // Check if player is at maximum rank (5 - Agricultural Expert)
        int currentRank = plugin.getProgressionModule().getPlayerRank(player);
        if (currentRank < 4) { // 0-indexed, so 4 is Agricultural Expert (Rank 5)
            return false;
        }
        
        // Check if player has enough currency (100,000)
        int balance = plugin.getEconomyModule().getPlayerBalance(player);
        if (balance < 100000) {
            return false;
        }
        
        // Check if player has completed enough trades (500)
        int tradeCount = getTradeCount(player);
        if (tradeCount < 500) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Predicts seasonal items for deity-level players.
     *
     * @param player The player
     * @return True if prediction was shown
     */
    public boolean predictSeasonalItems(Player player) {
        // Only rebirth level 4+ can predict seasonal items
        if (getRebirthLevel(player) < 4) {
            player.sendMessage(ChatColor.RED + "You need rebirth level 4 or higher to predict seasonal items.");
            return false;
        }
        
        // Show prediction
        player.sendMessage(ChatColor.GOLD + "== Seasonal Trader Preview ==");
        player.sendMessage(ChatColor.YELLOW + "The following items will be available tomorrow:");
        
        // In a real implementation, this would load from a schedule
        // For now, provide a simulated preview
        player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Green Elderberry Seeds " + 
                ChatColor.GRAY + "(350 coins)");
        player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Premium Fertilizer " + 
                ChatColor.GRAY + "(275 coins)");
        player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Magical Growth Potion " + 
                ChatColor.GRAY + "(520 coins)");
        
        return true;
    }
    
    /**
     * Shows the aura effect for a player.
     *
     * @param player The player
     */
    /**
     * Sets a trade bonus for a player.
     *
     * @param playerId The player's UUID
     * @param bonusMultiplier The bonus multiplier (1.0 = no bonus)
     * @param durationMillis The duration of the bonus in milliseconds
     */
    public void setPlayerTradeBonus(UUID playerId, double bonusMultiplier, long durationMillis) {
        tradeBonuses.put(playerId, bonusMultiplier);
        tradeBonusExpirations.put(playerId, System.currentTimeMillis() + durationMillis);
        
        // Save the data
        saveData();
        
        // Notify if player is online
        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.isOnline()) {
            player.sendMessage(ChatColor.GREEN + "You received a trade bonus: " + 
                    ChatColor.GOLD + String.format("%.0f%%", (bonusMultiplier - 1.0) * 100) + 
                    ChatColor.GREEN + " for " + formatDuration(durationMillis / 1000));
        }
    }
    
    /**
     * Formats a duration in seconds to a readable format.
     *
     * @param seconds The duration in seconds
     * @return The formatted duration
     */
    private String formatDuration(long seconds) {
        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            return (seconds / 60) + " minutes";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " hours";
        } else {
            return (seconds / 86400) + " days";
        }
    }
    
    private void showAuraEffect(Player player) {
        Location location = player.getLocation().add(0, 1, 0);
        int rebirthLevel = rebirthLevels.get(player.getUniqueId());
        
        // Different effects based on rebirth level
        if (rebirthLevel <= 3) {
            // Basic aura
            ParticleHelper.spawnEnchantmentParticles(location, 10, 0.5, 0.5, 0.5, 0);
        } else if (rebirthLevel <= 7) {
            // Intermediate aura
            location.getWorld().spawnParticle(Particle.PORTAL, location, 10, 0.5, 0.5, 0.5, 0.05);
        } else {
            // Advanced aura
            ParticleHelper.spawnWitchParticles(location, 10, 0.5, 0.5, 0.5, 0.05);
        }
    }
    
    /**
     * Toggles a player's aura.
     *
     * @param player The player
     * @return The new aura state
     */
    public boolean togglePlayerAura(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Check if player has rebirth level
        if (rebirthLevels.getOrDefault(uuid, 0) <= 0) {
            return false;
        }
        
        boolean currentState = auraEnabled.getOrDefault(uuid, false);
        auraEnabled.put(uuid, !currentState);
        
        // Save data
        saveData();
        
        return !currentState;
    }
    
    /**
     * Gets whether a player's aura is enabled.
     *
     * @param uuid The player's UUID
     * @return True if the aura is enabled
     */
    public boolean isAuraEnabled(UUID uuid) {
        return rebirthLevels.getOrDefault(uuid, 0) > 0 && auraEnabled.getOrDefault(uuid, false);
    }
    
    /**
     * Records command usage for cooldown tracking.
     *
     * @param uuid The player's UUID
     * @param command The command name
     */
    public void recordCommandUse(UUID uuid, String command) {
        Map<String, Long> playerCommands = commandUseTimes.computeIfAbsent(uuid, k -> new HashMap<>());
        playerCommands.put(command, System.currentTimeMillis());
        
        // Save data
        saveData();
    }
    
    /**
     * Gets the last time a command was used.
     *
     * @param uuid The player's UUID
     * @param command The command name
     * @return The last use time, or 0 if never used
     */
    public long getLastCommandUse(UUID uuid, String command) {
        Map<String, Long> playerCommands = commandUseTimes.get(uuid);
        
        if (playerCommands == null) {
            return 0;
        }
        
        return playerCommands.getOrDefault(command, 0L);
    }
    
    /**
     * Adds a blessing effect to a player.
     *
     * @param uuid The player's UUID
     * @param endTime The time when the blessing ends
     */
    public void addBlessingEffect(UUID uuid, long endTime) {
        blessingEffects.put(uuid, endTime);
        
        // Save data
        saveData();
    }
    
    /**
     * Checks if a player has an active blessing.
     *
     * @param uuid The player's UUID
     * @return True if the player has an active blessing
     */
    public boolean hasActiveBlessing(UUID uuid) {
        Long endTime = blessingEffects.get(uuid);
        
        if (endTime == null) {
            return false;
        }
        
        // Check if expired
        if (endTime <= System.currentTimeMillis()) {
            blessingEffects.remove(uuid);
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the expiration time of a player's blessing.
     *
     * @param uuid The player's UUID
     * @return The blessing end time, or 0 if no active blessing
     */
    public long getBlessingEndTime(UUID uuid) {
        if (!hasActiveBlessing(uuid)) {
            return 0;
        }
        
        return blessingEffects.get(uuid);
    }
    
    /**
     * Sets an exp boost for a player.
     *
     * @param uuid The player's UUID
     * @param amount The boost amount (multiplier)
     * @param duration The boost duration in milliseconds
     */
    public void setPlayerExpBoost(UUID uuid, double amount, long duration) {
        long endTime = System.currentTimeMillis() + duration;
        ExpBoost boost = new ExpBoost(amount, endTime);
        expBoosts.put(uuid, boost);
        
        // Save data
        saveData();
    }
    
    /**
     * Gets a player's exp boost multiplier.
     *
     * @param uuid The player's UUID
     * @return The boost multiplier (1.0 = no boost)
     */
    public double getPlayerExpBoostMultiplier(UUID uuid) {
        ExpBoost boost = expBoosts.get(uuid);
        
        if (boost == null) {
            return 1.0;
        }
        
        // Check if expired
        if (boost.getEndTime() <= System.currentTimeMillis()) {
            expBoosts.remove(uuid);
            return 1.0;
        }
        
        return boost.getAmount();
    }
    
    /**
     * Gets the total bonus from rebirth.
     *
     * @param uuid The player's UUID
     * @return The total bonus multiplier
     */
    public double getTotalRebirthBonus(UUID uuid) {
        int rebirthLevel = rebirthLevels.getOrDefault(uuid, 0);
        if (rebirthLevel <= 0) {
            return 1.0;
        }
        
        // Get bonus per level from config
        double bonusPerLevel = plugin.getConfig().getDouble("progression.rebirth.bonus-per-level", 5) / 100.0;
        
        // Apply additional bonus from blessing
        double blessingBonus = hasActiveBlessing(uuid) ? 0.1 : 0.0; // 10% extra if blessed
        
        // Apply exp boost
        double expBoost = getPlayerExpBoostMultiplier(uuid) - 1.0;
        
        // Calculate total bonus (base + rebirth level bonus + blessing + exp boost)
        return 1.0 + (rebirthLevel * bonusPerLevel) + blessingBonus + expBoost;
    }
    
    /**
     * Cleans up expired effects.
     */
    public void cleanupExpiredEffects() {
        long currentTime = System.currentTimeMillis();
        
        // Clean up blessings
        blessingEffects.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        
        // Clean up exp boosts
        expBoosts.entrySet().removeIf(entry -> entry.getValue().getEndTime() <= currentTime);
        
        // Save data
        saveData();
    }
    
    /**
     * Reloads rebirth data.
     */
    public void reloadData() {
        // Clear current data
        rebirthLevels.clear();
        auraEnabled.clear();
        commandUseTimes.clear();
        blessingEffects.clear();
        expBoosts.clear();
        
        // Load data
        loadData();
        
        plugin.getLogger().info("Reloaded rebirth data.");
    }
    
    /**
     * Represents an experience boost.
     */
    public static class ExpBoost {
        private final double amount;
        private final long endTime;
        
        /**
         * Creates a new exp boost.
         *
         * @param amount The boost amount (multiplier)
         * @param endTime The time when the boost ends
         */
        public ExpBoost(double amount, long endTime) {
            this.amount = amount;
            this.endTime = endTime;
        }
        
        /**
         * Gets the boost amount.
         *
         * @return The boost amount
         */
        public double getAmount() {
            return amount;
        }
        
        /**
         * Gets the end time.
         *
         * @return The end time
         */
        public long getEndTime() {
            return endTime;
        }
    }
}