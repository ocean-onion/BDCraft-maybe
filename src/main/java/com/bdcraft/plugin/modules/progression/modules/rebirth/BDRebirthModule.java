package com.bdcraft.plugin.modules.progression.modules.rebirth;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.progression.ProgressionManager;
import com.bdcraft.plugin.modules.progression.Rank;
import com.bdcraft.plugin.modules.vital.VitalConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Handles the rebirth mechanic, allowing players to reset their progress for permanent bonuses.
 */
public class BDRebirthModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Settings
    private int defaultMinLevel;
    private int defaultEmeraldCost;
    private boolean resetInventory;
    private boolean resetEnderChest;
    private boolean resetSkills;
    private boolean keepRank;
    private boolean keepPermissions;
    private boolean resetMoney;
    private boolean giveEffectsOnRebirth;
    private double xpMultiplierBase;
    private int maxRebirthLevel;
    private List<String> effectsOnRebirth;
    private Map<Integer, RebirthTier> rebirthTiers;
    
    // Player data
    private final ConcurrentHashMap<UUID, Integer> playerRebirthLevels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Long> rebirthCooldowns = new ConcurrentHashMap<>();
    
    /**
     * Creates a new rebirth module.
     * 
     * @param plugin The plugin instance
     */
    public BDRebirthModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Rebirth";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling BDRebirth submodule");
        
        // Load config
        loadConfig();
        
        // Load player data
        loadPlayerData();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDRebirth submodule");
        
        // Save player data
        savePlayerData();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig(ConfigType.REBIRTH);
        
        defaultMinLevel = config.getInt("rebirth.min_level", 50);
        defaultEmeraldCost = config.getInt("rebirth.emerald_cost", 100);
        resetInventory = config.getBoolean("rebirth.reset.inventory", true);
        resetEnderChest = config.getBoolean("rebirth.reset.ender_chest", false);
        resetSkills = config.getBoolean("rebirth.reset.skills", true);
        keepRank = config.getBoolean("rebirth.keep.rank", false);
        keepPermissions = config.getBoolean("rebirth.keep.permissions", true);
        resetMoney = config.getBoolean("rebirth.reset.money", true);
        giveEffectsOnRebirth = config.getBoolean("rebirth.effects.enabled", true);
        xpMultiplierBase = config.getDouble("rebirth.xp_multiplier_base", 0.1);
        maxRebirthLevel = config.getInt("rebirth.max_level", 100);
        effectsOnRebirth = config.getStringList("rebirth.effects.list");
        
        // Load rebirth tiers
        rebirthTiers = new HashMap<>();
        ConfigurationSection tiersSection = config.getConfigurationSection("rebirth.tiers");
        if (tiersSection != null) {
            for (String key : tiersSection.getKeys(false)) {
                try {
                    int level = Integer.parseInt(key);
                    ConfigurationSection tierSection = tiersSection.getConfigurationSection(key);
                    if (tierSection != null) {
                        String name = tierSection.getString("name", "Rebirth " + level);
                        String color = tierSection.getString("color", "&7");
                        double xpMultiplier = tierSection.getDouble("xp_multiplier", level * xpMultiplierBase);
                        int minLevel = tierSection.getInt("min_level", defaultMinLevel);
                        int emeraldCost = tierSection.getInt("emerald_cost", defaultEmeraldCost * level);
                        
                        rebirthTiers.put(level, new RebirthTier(level, name, color, xpMultiplier, minLevel, emeraldCost));
                    }
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Invalid rebirth tier: " + key);
                }
            }
        }
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register rebirth command
        PluginCommand rebirthCommand = plugin.getCommand("bdrebirth");
        if (rebirthCommand != null) {
            rebirthCommand.setExecutor(this);
        }
    }
    
    /**
     * Loads player data.
     */
    private void loadPlayerData() {
        // Clear existing data
        playerRebirthLevels.clear();
        
        // TODO: Implement loading player data from database or file
    }
    
    /**
     * Saves player data.
     */
    private void savePlayerData() {
        // TODO: Implement saving player data to database or file
    }
    
    /**
     * Gets a player's rebirth level.
     * 
     * @param playerId The player's UUID
     * @return The rebirth level
     */
    public int getRebirthLevel(UUID playerId) {
        return playerRebirthLevels.getOrDefault(playerId, 0);
    }
    
    /**
     * Sets a player's rebirth level.
     * 
     * @param playerId The player's UUID
     * @param level The new level
     */
    public void setRebirthLevel(UUID playerId, int level) {
        if (level < 0) {
            level = 0;
        } else if (level > maxRebirthLevel) {
            level = maxRebirthLevel;
        }
        
        playerRebirthLevels.put(playerId, level);
        
        // Update the player if online
        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.isOnline()) {
            updatePlayerMultipliers(player);
        }
    }
    
    /**
     * Gets a player's rebirth tier.
     * 
     * @param playerId The player's UUID
     * @return The rebirth tier
     */
    public RebirthTier getRebirthTier(UUID playerId) {
        int level = getRebirthLevel(playerId);
        return getRebirthTier(level);
    }
    
    /**
     * Gets a rebirth tier by level.
     * 
     * @param level The level
     * @return The rebirth tier
     */
    public RebirthTier getRebirthTier(int level) {
        // Check if we have a specific tier for this level
        if (rebirthTiers.containsKey(level)) {
            return rebirthTiers.get(level);
        }
        
        // Find the closest tier below this level
        int highestTier = 0;
        for (int tierLevel : rebirthTiers.keySet()) {
            if (tierLevel <= level && tierLevel > highestTier) {
                highestTier = tierLevel;
            }
        }
        
        // Return the closest tier or default
        return rebirthTiers.getOrDefault(highestTier, new RebirthTier(
                level,
                "Rebirth " + level,
                "&7",
                level * xpMultiplierBase,
                defaultMinLevel,
                defaultEmeraldCost * level
        ));
    }
    
    /**
     * Gets a player's XP multiplier based on rebirth level.
     * 
     * @param playerId The player's UUID
     * @return The XP multiplier
     */
    public double getXpMultiplier(UUID playerId) {
        return getRebirthTier(playerId).getXpMultiplier();
    }
    
    /**
     * Updates a player's multipliers and effects.
     * 
     * @param player The player
     */
    public void updatePlayerMultipliers(Player player) {
        // Can implement additional effects or modifiers based on rebirth level
    }
    
    /**
     * Performs a rebirth for a player.
     * 
     * @param playerId The player's UUID
     * @return Whether the rebirth was successful
     */
    public boolean performRebirth(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null || !player.isOnline()) {
            return false;
        }
        
        // Check cooldown
        if (rebirthCooldowns.containsKey(playerId)) {
            long lastRebirth = rebirthCooldowns.get(playerId);
            long now = System.currentTimeMillis();
            long cooldownTime = 60 * 60 * 1000; // 1 hour in milliseconds
            
            if (now - lastRebirth < cooldownTime) {
                long remainingMinutes = (cooldownTime - (now - lastRebirth)) / (60 * 1000);
                player.sendMessage(ChatColor.RED + "You must wait " + remainingMinutes + " minutes before rebirthing again.");
                return false;
            }
        }
        
        int currentLevel = player.getLevel();
        int rebirthLevel = getRebirthLevel(playerId);
        RebirthTier nextTier = getRebirthTier(rebirthLevel + 1);
        
        // Check if player meets requirements
        if (currentLevel < nextTier.getMinLevel()) {
            player.sendMessage(ChatColor.RED + "You need to be level " + nextTier.getMinLevel() + " to rebirth.");
            return false;
        }
        
        // Check if player has enough emeralds
        BDEconomyModule economy = plugin.getEconomyModule();
        if (economy != null) {
            // Check emerald balance
            int emeraldsRequired = nextTier.getEmeraldsRequired();
            if (!economy.hasEnoughEmeralds(player, emeraldsRequired)) {
                player.sendMessage(ChatColor.RED + "You need " + emeraldsRequired + " emeralds to rebirth.");
                return false;
            }
            
            // Deduct emeralds
            economy.removeEmeralds(player, emeraldsRequired);
        }
        
        // Perform the rebirth
        rebirthCooldowns.put(playerId, System.currentTimeMillis());
        setRebirthLevel(playerId, rebirthLevel + 1);
        
        // Reset player stats
        if (resetInventory) {
            player.getInventory().clear();
        }
        
        if (resetEnderChest) {
            player.getEnderChest().clear();
        }
        
        if (resetSkills) {
            player.setLevel(0);
            player.setExp(0);
        }
        
        if (!keepRank && parentModule instanceof ProgressionManager) {
            ((ProgressionManager) parentModule).setRank(playerId, Rank.DEFAULT);
        }
        
        // Apply effects
        if (giveEffectsOnRebirth) {
            applyRebirthEffects(player);
        }
        
        // Broadcast message
        Bukkit.broadcastMessage(ChatColor.GOLD + "§l" + player.getName() + " has reached rebirth level " + (rebirthLevel + 1) + "!");
        
        // Play sound
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Applies rebirth effects to a player.
     * 
     * @param player The player
     */
    private void applyRebirthEffects(Player player) {
        for (String effectStr : effectsOnRebirth) {
            try {
                String[] parts = effectStr.split(":");
                if (parts.length >= 2) {
                    PotionEffectType type = PotionEffectType.getByName(parts[0].toUpperCase());
                    int duration = Integer.parseInt(parts[1]) * 20; // Convert seconds to ticks
                    int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                    
                    if (type != null) {
                        player.addPotionEffect(new PotionEffect(type, duration, amplifier));
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid rebirth effect: " + effectStr);
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            // Show rebirth info
            showRebirthInfo(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "perform":
                // Perform rebirth
                if (performRebirth(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + "You have been reborn!");
                }
                break;
                
            case "info":
                // Show rebirth info
                showRebirthInfo(player);
                break;
                
            case "set":
                // Admin command to set rebirth level
                if (!player.hasPermission("bdprogression.rebirth.admin")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Usage: /bdrebirth set <player> <level>");
                    return true;
                }
                
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                try {
                    int level = Integer.parseInt(args[2]);
                    setRebirthLevel(target.getUniqueId(), level);
                    player.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s rebirth level to " + level + ".");
                    target.sendMessage(ChatColor.GREEN + "Your rebirth level was set to " + level + " by an admin.");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid rebirth level.");
                }
                break;
                
            default:
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Use /bdrebirth info for more information.");
                break;
        }
        
        return true;
    }
    
    /**
     * Shows rebirth info to a player.
     * 
     * @param player The player
     */
    private void showRebirthInfo(Player player) {
        UUID playerId = player.getUniqueId();
        int rebirthLevel = getRebirthLevel(playerId);
        RebirthTier currentTier = getRebirthTier(rebirthLevel);
        RebirthTier nextTier = getRebirthTier(rebirthLevel + 1);
        
        player.sendMessage(ChatColor.GOLD + "=== Rebirth Info ===");
        player.sendMessage(ChatColor.YELLOW + "Current Level: " + ChatColor.translateAlternateColorCodes('&', currentTier.getColor()) + rebirthLevel);
        player.sendMessage(ChatColor.YELLOW + "XP Multiplier: " + ChatColor.GREEN + String.format("%.2f", currentTier.getXpMultiplier()) + "x");
        
        // Show next tier info if not at max level
        if (rebirthLevel < maxRebirthLevel) {
            player.sendMessage(ChatColor.YELLOW + "Next Level Requirements:");
            player.sendMessage(ChatColor.YELLOW + "  - Level: " + ChatColor.WHITE + nextTier.getMinLevel());
            player.sendMessage(ChatColor.YELLOW + "  - Emeralds: " + ChatColor.WHITE + nextTier.getEmeraldCost());
        } else {
            player.sendMessage(ChatColor.YELLOW + "You have reached the maximum rebirth level!");
        }
    }
    
    /**
     * Handles the player join event.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Make sure this player has a rebirth level
        if (!playerRebirthLevels.containsKey(player.getUniqueId())) {
            playerRebirthLevels.put(player.getUniqueId(), 0);
        }
        
        // Update player multipliers
        updatePlayerMultipliers(player);
    }
    
    /**
     * Handles the player XP change event to apply multipliers.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        double multiplier = getXpMultiplier(player.getUniqueId());
        
        if (multiplier > 1.0) {
            int originalAmount = event.getAmount();
            int newAmount = (int) (originalAmount * multiplier);
            event.setAmount(newAmount);
        }
    }
    
    /**
     * Represents a rebirth tier.
     */
    public static class RebirthTier {
        private final int level;
        private final String name;
        private final String color;
        private final double xpMultiplier;
        private final int minLevel;
        private final int emeraldCost;
        
        /**
         * Creates a new rebirth tier.
         * 
         * @param level The level
         * @param name The name
         * @param color The color
         * @param xpMultiplier The XP multiplier
         * @param minLevel The minimum level required
         * @param emeraldCost The emerald cost
         */
        public RebirthTier(int level, String name, String color, double xpMultiplier, int minLevel, int emeraldCost) {
            this.level = level;
            this.name = name;
            this.color = color;
            this.xpMultiplier = xpMultiplier;
            this.minLevel = minLevel;
            this.emeraldCost = emeraldCost;
        }
        
        /**
         * Gets the level of this tier.
         * 
         * @return The level
         */
        public int getLevel() {
            return level;
        }
        
        /**
         * Gets the name of this tier.
         * 
         * @return The name
         */
        public String getName() {
            return name;
        }
        
        /**
         * Gets the color of this tier.
         * 
         * @return The color
         */
        public String getColor() {
            return color;
        }
        
        /**
         * Gets the XP multiplier of this tier.
         * 
         * @return The XP multiplier
         */
        public double getXpMultiplier() {
            return xpMultiplier;
        }
        
        /**
         * Gets the minimum level required for this tier.
         * 
         * @return The minimum level
         */
        public int getMinLevel() {
            return minLevel;
        }
        
        /**
         * Gets the emerald cost of this tier.
         * 
         * @return The emerald cost
         */
        public int getEmeraldCost() {
            return emeraldCost;
        }
    }
}