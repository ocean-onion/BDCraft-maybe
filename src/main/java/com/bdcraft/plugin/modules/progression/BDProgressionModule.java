package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.ProgressionAPI;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.progression.commands.BDBlessCommand;
import com.bdcraft.plugin.modules.progression.commands.BDAuraCommand;
import com.bdcraft.plugin.modules.progression.commands.BDTouchCommand;
import com.bdcraft.plugin.modules.progression.commands.BDInsightCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module for handling BD progression, ranks, and rebirth mechanics.
 */
public class BDProgressionModule implements BDModule, ProgressionAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private BDRankManager rankManager;
    private BDRebirthManager rebirthManager;
    
    // Store active blessing effects with expiration times
    private final Map<UUID, Long> activeBlessings = new HashMap<>();
    
    /**
     * Creates a new BD progression module.
     * @param plugin The plugin instance
     */
    public BDProgressionModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public void onEnable() {
        logger.info("Enabling BD Progression Module...");
        
        // Initialize rank manager
        rankManager = new BDRankManager(plugin);
        
        // Initialize rebirth manager
        rebirthManager = new BDRebirthManager(plugin);
        
        // Register API interface
        plugin.setProgressionAPI(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        logger.info("BD Progression Module enabled.");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling BD Progression Module...");
        
        // Unregister listeners
        
        logger.info("BD Progression Module disabled.");
    }
    
    @Override
    public void onReload() {
        logger.info("Reloading BD Progression Module...");
        
        // Reload rank data
        if (rankManager != null) {
            rankManager.loadRankData();
        }
        
        logger.info("BD Progression Module reloaded.");
    }
    
    @Override
    public List<String> getDependencies() {
        // The progression module depends on the economy module
        return Arrays.asList("Economy");
    }
    
    @Override
    public String getName() {
        return "Progression";
    }
    
    // ProgressionAPI implementation
    
    @Override
    public Rank getPlayerRank(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            int rankValue = rankManager.getPlayerRank(player);
            return convertIntToRank(rankValue);
        }
        // For offline players, we would need to implement data storage
        return Rank.NEWCOMER; // Default to Newcomer
    }
    
    @Override
    public boolean setPlayerRank(UUID uuid, Rank rank) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            rankManager.setPlayerRank(player, convertRankToInt(rank));
            return true;
        }
        return false;
    }
    
    @Override
    public int getPlayerExperience(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getPlayerExperience(player);
        }
        return 0;
    }
    
    @Override
    public int addPlayerExperience(UUID uuid, int amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            rankManager.addPlayerExperience(player, amount);
            // Check for rank up
            boolean didRankUp = rankManager.checkRankUp(player);
            
            if (didRankUp) {
                player.sendMessage(ChatColor.GREEN + "You ranked up to " + 
                        rankManager.getPlayerColoredRankName(player) + ChatColor.GREEN + "!");
            }
            
            return rankManager.getPlayerExperience(player);
        }
        return 0;
    }
    
    /**
     * Adds experience to a player and checks for rank up.
     * This method provides compatibility with the listener classes.
     * 
     * @param uuid The player UUID
     * @param amount The amount of experience to add
     * @return The player's new experience total
     */
    public int addExperience(UUID uuid, int amount) {
        return addPlayerExperience(uuid, amount);
    }
    
    @Override
    public int getRequiredExperience(Rank rank) {
        return rankManager.getExperienceForRank(convertRankToInt(rank));
    }
    
    @Override
    public boolean canProgress(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            int currentRank = rankManager.getPlayerRank(player);
            int currentExp = rankManager.getPlayerExperience(player);
            int requiredExp = rankManager.getExperienceForNextRank(player);
            
            return currentRank < BDRankManager.RANK_MASTER_FARMER && currentExp >= requiredExp;
        }
        return false;
    }
    
    @Override
    public boolean progressPlayer(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null && canProgress(uuid)) {
            return rankManager.checkRankUp(player);
        }
        return false;
    }
    
    @Override
    public int getRebirthCount(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getPlayerRebirths(player);
        }
        return 0;
    }
    
    @Override
    public boolean performRebirth(Player player) {
        if (player == null) {
            return false;
        }
        
        // Check if player meets the rebirth requirements
        int currentRank = rankManager.getPlayerRank(player);
        
        // Player must be at least Agricultural Expert (rank 5)
        if (currentRank < BDRankManager.RANK_AGRICULTURAL_EXPERT) {
            player.sendMessage(ChatColor.RED + "You must be an Agricultural Expert to perform rebirth.");
            return false;
        }
        
        // Check if player has required currency
        int minCurrency = plugin.getConfig().getInt("rebirth.requirements.min_currency", 100000);
        double playerCurrencyDouble = plugin.getEconomyAPI().getBalance(player.getUniqueId());
        int playerCurrency = (int) playerCurrencyDouble;
        
        if (playerCurrency < minCurrency) {
            player.sendMessage(ChatColor.RED + "You need at least " + minCurrency + " BD currency to perform rebirth.");
            player.sendMessage(ChatColor.RED + "Current balance: " + playerCurrency);
            return false;
        }
        
        // Check if player has required trades
        int minTrades = plugin.getConfig().getInt("rebirth.requirements.min_trades", 500);
        int playerTrades = rebirthManager.getPlayerTradeCount(player.getUniqueId());
        
        if (playerTrades < minTrades) {
            player.sendMessage(ChatColor.RED + "You need to complete at least " + minTrades + " trades to perform rebirth.");
            player.sendMessage(ChatColor.RED + "Current trades: " + playerTrades);
            return false;
        }
        
        // Perform the rebirth process
        
        // Take all their currency
        plugin.getEconomyAPI().withdrawMoney(player.getUniqueId(), playerCurrency);
        
        // Reset their rank to Newcomer but increment rebirth counter
        rankManager.setPlayerRank(player, BDRankManager.RANK_NEWCOMER);
        rankManager.addPlayerRebirth(player);
        
        // Clear inventories of specific items (seeds and other BD items)
        // This is a placeholder for more specific inventory management if desired
        
        // Send message and effects
        int rebirthLevel = rankManager.getPlayerRebirths(player);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "✧✧✧ REBIRTH COMPLETE ✧✧✧");
        player.sendMessage(ChatColor.GOLD + "You have been reborn! All progress has been reset.");
        player.sendMessage(ChatColor.GOLD + "Your rebirth level is now " + rebirthLevel);
        player.sendMessage(ChatColor.GOLD + "You now receive the following bonuses:");
        player.sendMessage(ChatColor.YELLOW + "• +" + (rebirthLevel * 10) + "% experience gain");
        player.sendMessage(ChatColor.YELLOW + "• +" + (rebirthLevel * 5) + "% crop yield");
        player.sendMessage(ChatColor.YELLOW + "• +" + (rebirthLevel * 8) + "% better trades");
        
        // Apply visual effects
        player.getWorld().strikeLightningEffect(player.getLocation());
        
        // Apply rebirth aura (if high enough level)
        if (rebirthLevel >= 3) {
            rebirthManager.togglePlayerAura(player, true);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura has been activated!");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Use /bdrank aura to toggle it on or off.");
        }
        
        return true;
    }
    
    @Override
    public Map<String, Double> getRebirthBonuses(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        Map<String, Double> bonuses = new java.util.HashMap<>();
        
        if (player != null) {
            int rebirths = rankManager.getPlayerRebirths(player);
            
            // Add all rebirth bonuses as per documentation
            double expMultiplier = plugin.getConfig().getDouble("rebirth.bonuses.experience_multiplier", 0.1);
            double cropYieldMultiplier = plugin.getConfig().getDouble("rebirth.bonuses.crop_yield_multiplier", 0.05);
            double tradeValueMultiplier = plugin.getConfig().getDouble("rebirth.bonuses.trade_value_multiplier", 0.08);
            
            // Experience bonus (10% per rebirth level)
            bonuses.put("experience", rebirths * expMultiplier);
            
            // Crop yield bonus (5% per rebirth level)
            bonuses.put("crop_yield", rebirths * cropYieldMultiplier);
            
            // Trade value bonus (8% per rebirth level)
            bonuses.put("trade_value", rebirths * tradeValueMultiplier);
            
            // Special bonuses based on rebirth level
            if (rebirths >= 3) {
                bonuses.put("aura_radius", 10.0); // 10 block radius aura effect
            }
            
            if (rebirths >= 5) {
                bonuses.put("reputation_gain", 0.05); // 5% bonus to reputation gain
            }
            
            if (rebirths >= 7) {
                bonuses.put("reduced_seed_cost_chance", 0.10); // 10% chance for reduced seed costs
                bonuses.put("seed_cost_reduction", 0.25); // 25% reduction when triggered
            }
            
            if (rebirths >= 10) {
                bonuses.put("seasonal_insight", 1.0); // Ability to predict seasonal items
            }
        }
        
        return bonuses;
    }
    
    @Override
    public List<UUID> getTopPlayers(int limit) {
        // This would require a proper database implementation
        // Placeholder for now
        return new java.util.ArrayList<>();
    }
    
    @Override
    public List<UUID> getPlayersByRank(Rank rank) {
        // This would require a proper database implementation
        // Placeholder for now
        return new java.util.ArrayList<>();
    }
    
    @Override
    public String getRankDisplayName(Rank rank) {
        int rankValue = convertRankToInt(rank);
        return rankManager.getRankName(rankValue);
    }
    
    @Override
    public String getRankPrefix(Rank rank) {
        int rankValue = convertRankToInt(rank);
        // Assuming there's a method to get rank color - would need to implement this
        return rankManager.getRankColor(rankValue) + getRankDisplayName(rank);
    }
    
    @Override
    public Rank getNextRank(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            int currentRank = rankManager.getPlayerRank(player);
            if (currentRank < BDRankManager.RANK_REBORN) {
                return convertIntToRank(currentRank + 1);
            }
        }
        return null; // No next rank if at max
    }
    
    @Override
    public double getProgressPercentage(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            int currentRank = rankManager.getPlayerRank(player);
            if (currentRank >= BDRankManager.RANK_REBORN) {
                return 100.0; // Already at max rank
            }
            
            int currentExp = rankManager.getPlayerExperience(player);
            int requiredExp = rankManager.getExperienceForNextRank(player);
            
            if (requiredExp <= 0) return 100.0;
            
            return (double) currentExp / requiredExp * 100.0;
        }
        return 0.0;
    }
    
    // Helper methods for conversion between int rank and enum rank
    private Rank convertIntToRank(int rankValue) {
        switch (rankValue) {
            case BDRankManager.RANK_NEWCOMER: return Rank.NEWCOMER;
            case BDRankManager.RANK_FARMER: return Rank.FARMER;
            case BDRankManager.RANK_EXPERT_FARMER: return Rank.EXPERT_FARMER;
            case BDRankManager.RANK_MASTER_FARMER: return Rank.MASTER_FARMER;
            case BDRankManager.RANK_AGRICULTURAL_EXPERT: return Rank.AGRICULTURAL_EXPERT;
            case BDRankManager.RANK_REBORN: return Rank.REBORN;
            default: return Rank.NEWCOMER;
        }
    }
    
    private int convertRankToInt(Rank rank) {
        switch (rank) {
            case NEWCOMER: return BDRankManager.RANK_NEWCOMER;
            case FARMER: return BDRankManager.RANK_FARMER;
            case EXPERT_FARMER: return BDRankManager.RANK_EXPERT_FARMER;
            case MASTER_FARMER: return BDRankManager.RANK_MASTER_FARMER;
            case AGRICULTURAL_EXPERT: return BDRankManager.RANK_AGRICULTURAL_EXPERT;
            case REBORN: return BDRankManager.RANK_REBORN;
            default: return BDRankManager.RANK_NEWCOMER;
        }
    }
    
    /**
     * Applies a blessing effect to a player.
     * 
     * @param player The player to bless
     */
    // Method removed to fix duplicate method declaration
    
    /**
     * Shows rank information to a player.
     * @param sender The command sender
     * @param player The player to show info for
     */
    private void showRankInfo(CommandSender sender, Player player) {
        int rank = rankManager.getPlayerRank(player);
        int experience = rankManager.getPlayerExperience(player);
        int rebirths = rankManager.getPlayerRebirths(player);
        
        // Get exp needed for next rank
        int expForNextRank = rankManager.getExperienceForNextRank(player);
        String nextRankName = rankManager.getRankName(rank + 1);
        
        sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.GOLD + player.getName() + "'s Rank Info" + ChatColor.GREEN + " =====");
        sender.sendMessage(ChatColor.GREEN + "Current Rank: " + rankManager.getPlayerColoredRankName(player));
        sender.sendMessage(ChatColor.GREEN + "Experience: " + ChatColor.GOLD + experience);
        
        if (rank < BDRankManager.RANK_MASTER_FARMER) {
            sender.sendMessage(ChatColor.GREEN + "Progress to " + nextRankName + ": " + 
                    ChatColor.GOLD + experience + "/" + expForNextRank);
            sender.sendMessage(rankManager.getProgressBar(player));
        }
        
        if (rebirths > 0) {
            sender.sendMessage(ChatColor.GREEN + "Rebirths: " + ChatColor.LIGHT_PURPLE + rebirths + 
                    ChatColor.GREEN + " (+" + (rebirths * 10) + "% exp gain)");
        }
    }
    
    /**
     * Registers event listeners for this module.
     */
    private void registerListeners() {
        // Register progression-related listeners
        plugin.getServer().getPluginManager().registerEvents(new ProgressionExperienceListener(plugin, this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerProgressionListener(plugin, rankManager), plugin);
        
        logger.info("Progression listeners registered.");
    }
    
    /**
     * Registers commands for this module.
     */
    private void registerCommands() {
        // Register deity commands
        plugin.getCommand("bdbless").setExecutor(new BDBlessCommand(plugin));
        plugin.getCommand("bdaura").setExecutor(new BDAuraCommand(plugin));
        plugin.getCommand("bdtouch").setExecutor(new BDTouchCommand(plugin));
        plugin.getCommand("bdinsight").setExecutor(new BDInsightCommand(plugin));
        
        // Register regular progression commands
        new CommandBase(plugin, "bdrank", "bdcraft.rank") {
            {
                // Check rank info
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "info";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows your rank information";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[player]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.info";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length == 0) {
                            // Show own rank info
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                                return true;
                            }
                            
                            Player player = (Player) sender;
                            showRankInfo(player, player);
                            return true;
                        } else {
                            // Show other player's rank info
                            if (!sender.hasPermission("bdcraft.rank.info.others")) {
                                sender.sendMessage(ChatColor.RED + "You don't have permission to view other players' ranks.");
                                return true;
                            }
                            
                            String playerName = args[0];
                            Player target = plugin.getServer().getPlayer(playerName);
                            
                            if (target == null) {
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                                return true;
                            }
                            
                            showRankInfo(sender, target);
                            return true;
                        }
                    }
                });
                
                // Aura toggle command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "aura";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Toggles your rebirth aura (requires rebirth level 3+)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.aura";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        int rebirthLevel = rankManager.getPlayerRebirths(player);
                        
                        if (rebirthLevel < 3) {
                            player.sendMessage(ChatColor.RED + "You need at least rebirth level 3 to use auras.");
                            return true;
                        }
                        
                        boolean newState = rebirthManager.togglePlayerAura(player);
                        return true;
                    }
                });
                
                // Rebirth command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "rebirth";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Resets your rank progression for enhanced future farming";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.rebirth";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        
                        int currentRank = rankManager.getPlayerRank(player);
                        
                        if (currentRank < BDRankManager.RANK_MASTER_FARMER) {
                            player.sendMessage(ChatColor.RED + "You need to be a Master Farmer to rebirth.");
                            player.sendMessage(ChatColor.RED + "Current rank: " + rankManager.getPlayerColoredRankName(player));
                            return true;
                        }
                        
                        // Confirm rebirth
                        if (args.length == 0) {
                            player.sendMessage(ChatColor.YELLOW + "Warning: Rebirth will reset your rank back to Newcomer!");
                            player.sendMessage(ChatColor.YELLOW + "But you'll gain a permanent 10% bonus to all future experience.");
                            player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GOLD + "/bdrank rebirth confirm" + 
                                    ChatColor.YELLOW + " to proceed.");
                            return true;
                        } else if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
                            int newRebirths = rankManager.addPlayerRebirth(player);
                            
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "You have been reborn!");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "Rank reset to Newcomer with a " + 
                                    (newRebirths * 10) + "% experience bonus.");
                            
                            // Broadcast to server
                            plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + 
                                    " has achieved rebirth status! (Total: " + newRebirths + ")");
                            
                            return true;
                        }
                        
                        return false;
                    }
                });
                
                // Admin commands
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "set";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Sets a player's rank (admin only)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "<player> <rank>";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.admin";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /bdrank set <player> <rank>");
                            return true;
                        }
                        
                        String playerName = args[0];
                        Player target = plugin.getServer().getPlayer(playerName);
                        
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        
                        try {
                            int rank = Integer.parseInt(args[1]);
                            
                            if (rank < BDRankManager.RANK_NEWCOMER || rank > BDRankManager.RANK_REBORN) {
                                sender.sendMessage(ChatColor.RED + "Invalid rank. Valid ranks are 0-5:");
                                sender.sendMessage(ChatColor.RED + "0: Newcomer, 1: Farmer, 2: Expert Farmer,");
                                sender.sendMessage(ChatColor.RED + "3: Agricultural Expert, 4: Master Farmer, 5: Reborn Farmer");
                                return true;
                            }
                            
                            rankManager.setPlayerRank(target, rank);
                            
                            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s rank to " + 
                                    rankManager.getPlayerColoredRankName(target) + ChatColor.GREEN + ".");
                            
                            if (sender != target) {
                                target.sendMessage(ChatColor.GREEN + "Your rank has been set to " + 
                                        rankManager.getPlayerColoredRankName(target) + ChatColor.GREEN + " by an admin.");
                            }
                            
                            return true;
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid rank. Please use a number between 0 and 5.");
                            return true;
                        }
                    }
                });
                
                // Add experience command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "addexp";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Adds experience to a player (admin only)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "<player> <amount>";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.admin";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /bdrank addexp <player> <amount>");
                            return true;
                        }
                        
                        String playerName = args[0];
                        Player target = plugin.getServer().getPlayer(playerName);
                        
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        
                        try {
                            int amount = Integer.parseInt(args[1]);
                            
                            if (amount <= 0) {
                                sender.sendMessage(ChatColor.RED + "Experience amount must be positive.");
                                return true;
                            }
                            
                            int newExperience = rankManager.addPlayerExperience(target, amount);
                            boolean didRankUp = rankManager.checkRankUp(target);
                            
                            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " experience to " + target.getName() + ".");
                            sender.sendMessage(ChatColor.GREEN + "New experience: " + newExperience);
                            
                            if (sender != target) {
                                target.sendMessage(ChatColor.GREEN + "You received " + amount + " experience from an admin.");
                                target.sendMessage(ChatColor.GREEN + "New experience: " + newExperience);
                            }
                            
                            return true;
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid amount. Please use a positive number.");
                            return true;
                        }
                    }
                });
                
                // Blessing command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "bless";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Blesses another player with temporary trading bonus (requires rebirth level 5+)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "<player>";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.bless";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        int rebirthLevel = rankManager.getPlayerRebirths(player);
                        
                        if (rebirthLevel < 5) {
                            player.sendMessage(ChatColor.RED + "You need at least rebirth level 5 to bless others.");
                            return true;
                        }
                        
                        if (args.length < 1) {
                            player.sendMessage(ChatColor.RED + "Usage: /bdrank bless <player>");
                            return true;
                        }
                        
                        String targetName = args[0];
                        Player target = plugin.getServer().getPlayer(targetName);
                        
                        if (target == null) {
                            player.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        
                        if (target.equals(player)) {
                            player.sendMessage(ChatColor.RED + "You cannot bless yourself.");
                            return true;
                        }
                        
                        // Check cooldown
                        long cooldownTime = rebirthManager.getBlessCooldown(player.getUniqueId());
                        long currentTime = System.currentTimeMillis();
                        
                        if (cooldownTime > currentTime) {
                            long remainingTime = (cooldownTime - currentTime) / 1000; // seconds
                            player.sendMessage(ChatColor.RED + "You must wait " + formatTime(remainingTime) + 
                                    " before blessing again.");
                            return true;
                        }
                        
                        // Apply blessing
                        applyBlessingEffect(target);
                        
                        // Set cooldown
                        int cooldownSeconds = plugin.getConfig().getInt("rebirth.bonuses.blessing_cooldown", 7200);
                        rebirthManager.setBlessCooldown(player.getUniqueId(), currentTime + (cooldownSeconds * 1000));
                        
                        // Messages
                        player.sendMessage(ChatColor.GREEN + "You have blessed " + target.getName() + " with improved trading for 30 minutes!");
                        target.sendMessage(ChatColor.GOLD + "You have been blessed by " + player.getName() + "!");
                        target.sendMessage(ChatColor.GOLD + "Your trading values are improved by 20% for 30 minutes!");
                        
                        // Effects
                        player.getWorld().strikeLightningEffect(target.getLocation());
                        
                        return true;
                    }
                    
                    /**
                     * Formats time in seconds to a readable string.
                     * 
                     * @param seconds The time in seconds
                     * @return The formatted time string
                     */
                    private String formatTime(long seconds) {
                        long minutes = seconds / 60;
                        long remainingSeconds = seconds % 60;
                        
                        if (minutes > 0) {
                            return minutes + " minutes and " + remainingSeconds + " seconds";
                        } else {
                            return seconds + " seconds";
                        }
                    }
                });
                
                // Predict seasonal items command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "predict";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Predicts next day's seasonal trader items (requires Agricultural Deity status)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.predict";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        
                        // Check if player has deity status
                        if (!rebirthManager.hasDeityStatus(player)) {
                            player.sendMessage(ChatColor.RED + "You need Agricultural Deity status (Rebirth 10+) to use this command.");
                            return true;
                        }
                        
                        // Check if player has seasonal insight
                        if (!rebirthManager.hasSeasonalInsight(player)) {
                            player.sendMessage(ChatColor.RED + "You need the Seasonal Insight ability to predict seasonal trader items.");
                            return true;
                        }
                        
                        // Predict seasonal items
                        boolean success = rebirthManager.predictSeasonalItems(player);
                        
                        if (!success) {
                            player.sendMessage(ChatColor.RED + "Failed to predict seasonal items. Please try again later.");
                        }
                        
                        return true;
                    }
                });
                
                // Aura command
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "aura";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Toggles your rebirth aura visibility (requires rebirth level 3+)";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[on|off]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.aura";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        int rebirthLevel = rankManager.getPlayerRebirths(player);
                        
                        if (rebirthLevel < 3) {
                            player.sendMessage(ChatColor.RED + "You need at least rebirth level 3 to use the aura command.");
                            return true;
                        }
                        
                        boolean newState;
                        
                        // Check if state specified
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("on")) {
                                newState = true;
                            } else if (args[0].equalsIgnoreCase("off")) {
                                newState = false;
                            } else {
                                player.sendMessage(ChatColor.RED + "Usage: /bdrank aura [on|off]");
                                return true;
                            }
                            
                            rebirthManager.setAuraEnabled(player, newState);
                        } else {
                            // Toggle current state
                            newState = rebirthManager.toggleAura(player);
                        }
                        
                        // Notify player
                        if (newState) {
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura is now visible to other players.");
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "Players within " + 
                                    rebirthManager.getAbundanceAuraRadius(player) + " blocks will receive your aura bonus.");
                            
                            // Apply particle effect to show activation
                            Location loc = player.getLocation();
                            World world = player.getWorld();
                            
                            for (int i = 0; i < 3; i++) {
                                final int iteration = i;
                                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                    for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 16) {
                                        double radius = 1.5 + (iteration * 0.5);
                                        double x = Math.cos(angle) * radius;
                                        double z = Math.sin(angle) * radius;
                                        
                                        Location particleLoc = loc.clone().add(x, iteration * 0.5, z);
                                        world.spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
                                    }
                                }, i * 5);
                            }
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Your rebirth aura is now hidden from other players.");
                        }
                        
                        return true;
                    }
                });
                
                // The CommandBase class will automatically show help if no arguments are provided
            }
        };
        
        logger.info("Progression commands registered.");
    }
    
    /**
     * Gets the rank manager.
     * @return The rank manager
     */
    public BDRankManager getRankManager() {
        return rankManager;
    }
    
    /**
     * Gets the rebirth manager.
     * @return The rebirth manager
     */
    public BDRebirthManager getRebirthManager() {
        return rebirthManager;
    }
    
    /**
     * Applies a blessing effect to a player from a deity player.
     * Temporary trading bonus for 30 minutes.
     * 
     * @param player The player to bless
     */
    public void applyBlessingEffect(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Store player in the blessed players list with expiration time
        long expirationTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30 minutes
        activeBlessings.put(uuid, expirationTime);
        
        // Apply visual effect with particles
        Location loc = player.getLocation();
        World world = player.getWorld();
        
        // Create a helix of gold particles around the player
        new BukkitRunnable() {
            double y = 0;
            int count = 0;
            
            @Override
            public void run() {
                if (count > 20) { // Run for 1 second (20 ticks)
                    this.cancel();
                    return;
                }
                
                double radius = 1.5;
                for (double i = 0; i < Math.PI * 2; i += Math.PI / 8) {
                    double x = Math.cos(i + (count * 0.5)) * radius;
                    double z = Math.sin(i + (count * 0.5)) * radius;
                    
                    Location particleLoc = loc.clone().add(x, y, z);
                    world.spawnParticle(Particle.CRIT, particleLoc, 1, 0, 0, 0, 0); 
                    world.spawnParticle(Particle.HEART, particleLoc, 1, 0, 0, 0, 0);
                }
                
                y += 0.1;
                count++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        // Apply sound effect
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.8f);
        
        // Add temporary bonus
        player.sendMessage(ChatColor.GOLD + "You feel the blessing take effect! For the next 30 minutes, your trades will be 20% more favorable.");
        
        // Schedule removal of the blessing
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Remove bonus
            activeBlessings.remove(uuid);
            player.sendMessage(ChatColor.GOLD + "Your trade blessing has worn off.");
            // Play sound to indicate end of blessing
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.5f);
        }, 20 * 60 * 30); // 30 minutes in ticks
    }
    
    /**
     * Checks if a player is currently blessed.
     * 
     * @param player The player to check
     * @return True if the player has an active blessing
     */
    public boolean isPlayerBlessed(Player player) {
        UUID uuid = player.getUniqueId();
        if (!activeBlessings.containsKey(uuid)) {
            return false;
        }
        
        long expirationTime = activeBlessings.get(uuid);
        long currentTime = System.currentTimeMillis();
        
        if (currentTime > expirationTime) {
            // Blessing has expired, remove it
            activeBlessings.remove(uuid);
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the blessing trade bonus percentage for a player.
     * 
     * @param player The player to check
     * @return The bonus percentage (20 if blessed, 0 if not)
     */
    public int getBlessingTradeBonus(Player player) {
        return isPlayerBlessed(player) ? 20 : 0;
    }
}