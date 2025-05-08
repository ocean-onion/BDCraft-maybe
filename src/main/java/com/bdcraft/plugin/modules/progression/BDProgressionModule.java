package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.ProgressionAPI;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
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
    public int getPlayerRank(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getPlayerRank(player);
        }
        // For offline players, we would need to implement data storage
        return 0; // Default to Newcomer
    }
    
    @Override
    public boolean hasRank(UUID uuid, int requiredRank) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.hasRank(player, requiredRank);
        }
        return false;
    }
    
    @Override
    public void addExperience(UUID uuid, int amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            rankManager.addPlayerExperience(player, amount);
            // Check for rank up
            boolean didRankUp = rankManager.checkRankUp(player);
            
            if (didRankUp) {
                player.sendMessage(ChatColor.GREEN + "You ranked up to " + 
                        rankManager.getPlayerColoredRankName(player) + ChatColor.GREEN + "!");
            }
        }
    }
    
    @Override
    public double getExperienceMultiplier(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getExperienceMultiplier(player);
        }
        return 1.0; // No bonus for offline players
    }
    
    @Override
    public int getRebirths(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getPlayerRebirths(player);
        }
        return 0;
    }
    
    @Override
    public String getRankName(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return rankManager.getPlayerRankName(player);
        }
        return "Unknown";
    }
    
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
        // Apply effect via particle system
        
        // Add temporary bonus
        // Implementation would depend on how trading bonuses are applied
        player.sendMessage(ChatColor.GOLD + "You feel the blessing take effect! For the next 30 minutes, your trades will be more favorable.");
        
        // Schedule removal of the blessing
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Remove bonus
            player.sendMessage(ChatColor.GOLD + "Your trade blessing has worn off.");
        }, 20 * 60 * 30); // 30 minutes in ticks
    }
}