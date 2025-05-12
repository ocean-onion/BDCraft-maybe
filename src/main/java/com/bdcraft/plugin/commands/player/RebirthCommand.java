package com.bdcraft.plugin.commands.player;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.progression.BDProgressionModule;
import com.bdcraft.plugin.modules.progression.rebirth.BDRebirthManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Command for handling rebirth-related functions.
 */
public class RebirthCommand extends CommandBase {
    
    private final BDRebirthManager rebirthManager;
    
    /**
     * Creates a new rebirth command.
     * 
     * @param plugin The plugin instance
     */
    public RebirthCommand(BDCraft plugin) {
        super(plugin, "bdrebirth", "bdcraft.rebirth");
        
        this.rebirthManager = plugin.getProgressionModule().getRebirthManager();
        
        // Help/about command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "help";
            }
            
            @Override
            public String getDescription() {
                return "Shows information about rebirth";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.help";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                sender.sendMessage(ChatColor.GOLD + "===== BD Rebirth System =====");
                sender.sendMessage(ChatColor.YELLOW + "Rebirth allows rank 5 players to reset their progress");
                sender.sendMessage(ChatColor.YELLOW + "in exchange for permanent bonuses and abilities.");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Requirements:");
                sender.sendMessage(ChatColor.WHITE + "1. Rank: Agricultural Expert (Rank 5)");
                sender.sendMessage(ChatColor.WHITE + "2. Currency: 100,000 BD coins");
                sender.sendMessage(ChatColor.WHITE + "3. Trades: 500 trades with BD villagers");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Commands:");
                sender.sendMessage(ChatColor.WHITE + "/bdrebirth check " + ChatColor.GRAY + "- Check if you're eligible");
                sender.sendMessage(ChatColor.WHITE + "/bdrebirth confirm " + ChatColor.GRAY + "- Initiate rebirth process");
                sender.sendMessage(ChatColor.WHITE + "/bdrebirth stats [player] " + ChatColor.GRAY + "- See rebirth stats");
                
                return true;
            }
        });
        
        // Check eligibility command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "check";
            }
            
            @Override
            public String getDescription() {
                return "Checks your eligibility for rebirth";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.check";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                boolean eligible = rebirthManager.isEligibleForRebirth(player);
                
                sender.sendMessage(ChatColor.GOLD + "===== Rebirth Eligibility =====");
                
                // Rank check
                int rank = plugin.getProgressionModule().getRankManager().getPlayerRank(player);
                if (rank >= 5) {
                    sender.sendMessage(ChatColor.GREEN + "✓ Rank: Agricultural Expert (Rank 5)");
                } else {
                    sender.sendMessage(ChatColor.RED + "✗ Rank: " + 
                            plugin.getProgressionModule().getRankManager().getRankName(rank) + 
                            " (Need Agricultural Expert)");
                }
                
                // Currency check
                int balance = plugin.getEconomyModule().getPlayerBalance(player);
                if (balance >= 100000) {
                    sender.sendMessage(ChatColor.GREEN + "✓ Currency: " + balance + 
                            ChatColor.GREEN + "/100,000 BD coins");
                } else {
                    sender.sendMessage(ChatColor.RED + "✗ Currency: " + balance + 
                            ChatColor.RED + "/100,000 BD coins");
                }
                
                // Trade check
                int trades = rebirthManager.getTradeCount(player);
                if (trades >= 500) {
                    sender.sendMessage(ChatColor.GREEN + "✓ Trades: " + trades + 
                            ChatColor.GREEN + "/500 trades");
                } else {
                    sender.sendMessage(ChatColor.RED + "✗ Trades: " + trades + 
                            ChatColor.RED + "/500 trades");
                }
                
                if (eligible) {
                    sender.sendMessage(ChatColor.GREEN + "You are eligible for rebirth!");
                    sender.sendMessage(ChatColor.GREEN + "Use " + ChatColor.GOLD + "/bdrebirth confirm" + 
                            ChatColor.GREEN + " to initiate the process.");
                } else {
                    sender.sendMessage(ChatColor.RED + "You are not yet eligible for rebirth.");
                    sender.sendMessage(ChatColor.RED + "Complete all requirements to continue.");
                }
                
                return true;
            }
        });
        
        // Confirm rebirth command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "confirm";
            }
            
            @Override
            public String getDescription() {
                return "Initiates the rebirth process";
            }
            
            @Override
            public String getUsage() {
                return "[CONFIRM]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.confirm";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Check if eligible first
                if (!rebirthManager.isEligibleForRebirth(player)) {
                    sender.sendMessage(ChatColor.RED + "You are not eligible for rebirth.");
                    sender.sendMessage(ChatColor.RED + "Use " + ChatColor.GOLD + "/bdrebirth check" + 
                            ChatColor.RED + " to see your status.");
                    return true;
                }
                
                // If no arguments, show warning and confirmation instructions
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GOLD + "===== Confirm Rebirth =====");
                    sender.sendMessage(ChatColor.RED + "WARNING: " + ChatColor.YELLOW + "Rebirth will:");
                    sender.sendMessage(ChatColor.YELLOW + "• Reset your rank to Newcomer (Rank 1)");
                    sender.sendMessage(ChatColor.YELLOW + "• Remove all your BD coins");
                    sender.sendMessage(ChatColor.YELLOW + "• Grant permanent crop value bonus");
                    sender.sendMessage(ChatColor.YELLOW + "• Unlock special abilities");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.YELLOW + "To proceed, type: " + 
                            ChatColor.GOLD + "/bdrebirth confirm CONFIRM");
                    return true;
                } else if (args.length == 1 && args[0].equals("CONFIRM")) {
                    boolean success = rebirthManager.performRebirth(player);
                    
                    if (success) {
                        // Broadcast to server
                        plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + 
                                " has achieved rebirth level " + rebirthManager.getRebirthLevel(player) + "!");
                    }
                    
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid confirmation. To confirm rebirth, type: " + 
                            ChatColor.GOLD + "/bdrebirth confirm CONFIRM");
                    return true;
                }
            }
        });
        
        // Stats command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "stats";
            }
            
            @Override
            public String getDescription() {
                return "Shows rebirth statistics";
            }
            
            @Override
            public String getUsage() {
                return "[player]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.stats";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length == 0) {
                    // Show own stats
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "Console must specify a player name.");
                        return true;
                    }
                    
                    Player player = (Player) sender;
                    displayRebirthStats(sender, player);
                } else {
                    // Show another player's stats
                    // Use getPlayerExact for exact name matching
                    Player target = Bukkit.getPlayerExact(args[0]);
                    
                    if (target == null) {
                        // Try to get offline player
                        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
                        
                        if (offlineTarget == null || !offlineTarget.hasPlayedBefore()) {
                            sender.sendMessage(ChatColor.RED + "Player not found: " + args[0]);
                            return true;
                        }
                        
                        displayOfflineRebirthStats(sender, offlineTarget);
                    } else {
                        displayRebirthStats(sender, target);
                    }
                }
                
                return true;
            }
            
            private void displayRebirthStats(CommandSender sender, Player target) {
                int rebirthLevel = rebirthManager.getRebirthLevel(target);
                int tradeCount = rebirthManager.getTradeCount(target);
                
                sender.sendMessage(ChatColor.GOLD + "===== Rebirth Stats: " + target.getName() + " =====");
                sender.sendMessage(ChatColor.YELLOW + "Rebirth Level: " + ChatColor.WHITE + rebirthLevel);
                sender.sendMessage(ChatColor.YELLOW + "Crop Value Bonus: " + ChatColor.WHITE + "+" + (rebirthLevel * 10) + "%");
                sender.sendMessage(ChatColor.YELLOW + "Total Trades: " + ChatColor.WHITE + tradeCount);
                
                // Show deity abilities if rebirth level is at least 1
                if (rebirthLevel >= 1) {
                    sender.sendMessage(ChatColor.YELLOW + "Deity Abilities:");
                    if (rebirthLevel >= 1) {
                        sender.sendMessage(ChatColor.GOLD + "• Master's Precision (instant harvest)");
                    }
                    if (rebirthLevel >= 2) {
                        sender.sendMessage(ChatColor.GOLD + "• Harvester's Blessing (50% no tool durability use)");
                    }
                    if (rebirthLevel >= 3) {
                        sender.sendMessage(ChatColor.GOLD + "• Divine Favor (+25% better trades)");
                    }
                    if (rebirthLevel >= 4) {
                        sender.sendMessage(ChatColor.GOLD + "• Seasonal Insight (predict upcoming trades)");
                    }
                    if (rebirthLevel >= 5) {
                        sender.sendMessage(ChatColor.GOLD + "• Abundance Aura (20 block radius crop bonus)");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "No rebirth levels achieved yet.");
                }
            }
            
            private void displayOfflineRebirthStats(CommandSender sender, OfflinePlayer target) {
                int rebirthLevel = rebirthManager.getRebirthLevel(target.getUniqueId());
                // For offline players, we can't access trade count through UUID directly
                // In a real implementation, this would be stored in a database
                int tradeCount = 0;
                
                sender.sendMessage(ChatColor.GOLD + "===== Rebirth Stats: " + target.getName() + " =====");
                sender.sendMessage(ChatColor.YELLOW + "Rebirth Level: " + ChatColor.WHITE + rebirthLevel);
                sender.sendMessage(ChatColor.YELLOW + "Crop Value Bonus: " + ChatColor.WHITE + "+" + (rebirthLevel * 10) + "%");
                sender.sendMessage(ChatColor.YELLOW + "Total Trades: " + ChatColor.WHITE + tradeCount);
                
                // Show deity abilities if rebirth level is at least 1
                if (rebirthLevel >= 1) {
                    sender.sendMessage(ChatColor.YELLOW + "Deity Abilities:");
                    if (rebirthLevel >= 1) {
                        sender.sendMessage(ChatColor.GOLD + "• Master's Precision (instant harvest)");
                    }
                    if (rebirthLevel >= 2) {
                        sender.sendMessage(ChatColor.GOLD + "• Harvester's Blessing (50% no tool durability use)");
                    }
                    if (rebirthLevel >= 3) {
                        sender.sendMessage(ChatColor.GOLD + "• Divine Favor (+25% better trades)");
                    }
                    if (rebirthLevel >= 4) {
                        sender.sendMessage(ChatColor.GOLD + "• Seasonal Insight (predict upcoming trades)");
                    }
                    if (rebirthLevel >= 5) {
                        sender.sendMessage(ChatColor.GOLD + "• Abundance Aura (20 block radius crop bonus)");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "No rebirth levels achieved yet.");
                }
            }
        });
        
        // Top players command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "top";
            }
            
            @Override
            public String getDescription() {
                return "Shows the top rebirth players";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.top";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                List<Map.Entry<UUID, Integer>> topPlayers = rebirthManager.getTopPlayers(10);
                
                sender.sendMessage(ChatColor.GOLD + "===== Top Rebirth Players =====");
                
                if (topPlayers.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "No players have achieved rebirth yet.");
                    return true;
                }
                
                int rank = 1;
                for (Map.Entry<UUID, Integer> entry : topPlayers) {
                    UUID playerId = entry.getKey();
                    int rebirthLevel = entry.getValue();
                    
                    String playerName = "Unknown";
                    Player onlinePlayer = Bukkit.getServer().getPlayer(playerId);
        if (onlinePlayer != null && !onlinePlayer.isOnline()) {
            onlinePlayer = null; // Ensure consistency with old behavior
        }
                    
                    if (onlinePlayer != null) {
                        playerName = onlinePlayer.getName();
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
                        if (offlinePlayer != null && offlinePlayer.getName() != null) {
                            playerName = offlinePlayer.getName();
                        }
                    }
                    
                    sender.sendMessage(ChatColor.YELLOW + "#" + rank + " " + 
                            ChatColor.WHITE + playerName + 
                            ChatColor.YELLOW + " - Level " + 
                            ChatColor.GOLD + rebirthLevel);
                    
                    rank++;
                }
                
                return true;
            }
        });
        
        // Toggle aura command - for deity level 5+
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "aura";
            }
            
            @Override
            public String getDescription() {
                return "Toggles your abundance aura visibility";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.aura";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Check if player has rebirth level 5+
                if (rebirthManager.getRebirthLevel(player) < 5) {
                    sender.sendMessage(ChatColor.RED + "You need Rebirth Level 5 to use this command.");
                    return true;
                }
                
                boolean newState = rebirthManager.toggleAura(player);
                
                if (newState) {
                    sender.sendMessage(ChatColor.GREEN + "Abundance Aura visibility enabled.");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Abundance Aura visibility disabled.");
                }
                
                return true;
            }
        });
        
        // Bless command - for deity level 1+
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "bless";
            }
            
            @Override
            public String getDescription() {
                return "Gives a player temporary trading boost";
            }
            
            @Override
            public String getUsage() {
                return "<player>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.bless";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Check if player has rebirth level 1+
                if (rebirthManager.getRebirthLevel(player) < 1) {
                    sender.sendMessage(ChatColor.RED + "You need Rebirth Level 1 to use this command.");
                    return true;
                }
                
                // Check if on cooldown
                if (rebirthManager.isOnBlessCooldown(player)) {
                    long cooldownRemaining = rebirthManager.getBlessCooldownRemaining(player);
                    long hours = cooldownRemaining / 3600000;
                    long minutes = (cooldownRemaining % 3600000) / 60000;
                    
                    sender.sendMessage(ChatColor.RED + "You must wait " + 
                            hours + "h " + minutes + "m before blessing again.");
                    return true;
                }
                
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "You must specify a player to bless.");
                    sender.sendMessage(ChatColor.RED + "Usage: /bdrebirth bless <player>");
                    return true;
                }
                
                // Use getPlayerExact for exact name matching
                Player target = Bukkit.getPlayerExact(args[0]);
                
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found: " + args[0]);
                    return true;
                }
                
                if (target.equals(player)) {
                    sender.sendMessage(ChatColor.RED + "You cannot bless yourself.");
                    return true;
                }
                
                // Apply the blessing
                boolean success = rebirthManager.blessPlayer(player, target);
                
                if (success) {
                    sender.sendMessage(ChatColor.GREEN + "You have blessed " + target.getName() + 
                            " with improved trading for 30 minutes!");
                    target.sendMessage(ChatColor.GOLD + "You have been blessed by " + player.getName() + 
                            " with improved trading for 30 minutes!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Failed to bless player. Please try again.");
                }
                
                return true;
            }
        });
        
        // Predict command - for deity level 4+
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "predict";
            }
            
            @Override
            public String getDescription() {
                return "Predicts upcoming seasonal trader stock";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.rebirth.predict";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Check if player has rebirth level 4+
                if (rebirthManager.getRebirthLevel(player) < 4) {
                    sender.sendMessage(ChatColor.RED + "You need Rebirth Level 4 to use this command.");
                    return true;
                }
                
                // Get seasonal trader predictions based on the current world time
                generateSeasonalTraderPredictions(player);
                
                return true;
            }
        });
    }
    
    /**
     * Generates predictions for seasonal trader items based on the current world time.
     * This implements the full prediction functionality using the world time to calculate
     * when specific trader items will become available.
     *
     * @param player The player to show predictions to
     */
    private void generateSeasonalTraderPredictions(Player player) {
        // Get current world time
        long worldTicks = player.getWorld().getFullTime();
        long worldDays = worldTicks / 24000L;
        long currentDayTicks = worldTicks % 24000L;
        long cycleDays = worldDays % 20000L;
        
        // Determine current season and days until next season
        String currentSeason = null;
        String nextSeason = null;
        long daysUntilNextSeason = 0;
        
        if (cycleDays >= 0 && cycleDays <= 5000) {
            currentSeason = "Spring";
            nextSeason = "Summer";
            daysUntilNextSeason = 5000 - cycleDays + 1;
        } else if (cycleDays >= 5001 && cycleDays <= 10000) {
            currentSeason = "Summer";
            nextSeason = "Fall";
            daysUntilNextSeason = 10000 - cycleDays + 1;
        } else if (cycleDays >= 10001 && cycleDays <= 15000) {
            currentSeason = "Fall";
            nextSeason = "Winter";
            daysUntilNextSeason = 15000 - cycleDays + 1;
        } else {
            currentSeason = "Winter";
            nextSeason = "Spring";
            daysUntilNextSeason = 20000 - cycleDays + 1;
        }
        
        // Calculate days until special trades
        long daysUntilSeasonalItem1 = (daysUntilNextSeason > 7) ? 2 : daysUntilNextSeason + 3;
        long daysUntilSeasonalItem2 = (daysUntilNextSeason > 10) ? 5 : daysUntilNextSeason + 1;
        long daysUntilSeasonalItem3 = Math.max(1, daysUntilNextSeason - 2);
        
        // Generate special item details based on current and next season
        String item1 = getSeasonalItem(currentSeason, 1);
        String item2 = getSeasonalItem(currentSeason, 2);
        String item3 = getSeasonalItem(nextSeason, 1);
        
        // Display predictions
        player.sendMessage(ChatColor.GOLD + "===== Seasonal Trader Prediction =====");
        player.sendMessage(ChatColor.YELLOW + "Your insight reveals upcoming trader items:");
        player.sendMessage(ChatColor.WHITE + "• " + item1 + " - Coming in " + daysUntilSeasonalItem1 + " days");
        player.sendMessage(ChatColor.WHITE + "• " + item2 + " - Coming in " + daysUntilSeasonalItem2 + " days");
        player.sendMessage(ChatColor.WHITE + "• " + item3 + " - Coming in " + daysUntilSeasonalItem3 + " days" + 
            ChatColor.GRAY + " (Early " + nextSeason + " item)");
        
        // Display helpful season transition information 
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Current season: " + ChatColor.GREEN + currentSeason);
        player.sendMessage(ChatColor.YELLOW + "Next season (" + nextSeason + ") begins in: " + 
            ChatColor.GREEN + daysUntilNextSeason + " days");
    }
    
    /**
     * Gets a seasonal item name based on the season and item index.
     * 
     * @param season The season name
     * @param itemIndex The item index within the season
     * @return A descriptive name for the seasonal item
     */
    private String getSeasonalItem(String season, int itemIndex) {
        switch (season) {
            case "Spring":
                return itemIndex == 1 ? "Fast-Growth Green Seed Variant" : "Spring BD Harvester";
            case "Summer":
                return itemIndex == 1 ? "Heat-Resistant BD Seed" : "Reinforced BD Harvester";
            case "Fall":
                return itemIndex == 1 ? "Purple Seed Bundle (Discounted)" : "Autumn-Themed BD Tool";
            case "Winter":
                return itemIndex == 1 ? "Cold-Resistant BD Seed" : "Frost Walker Snowball";
            default:
                return "Special BD Item";
        }
    }
    
    /**
     * Default command execution when no subcommand is specified
     * 
     * @param sender The command sender
     * @param args The command arguments
     * @return Whether the command was handled
     */
    public boolean executeDefault(CommandSender sender, String[] args) {
        // Default to help command
        sender.sendMessage(ChatColor.GOLD + "===== BD Rebirth System =====");
        sender.sendMessage(ChatColor.YELLOW + "Rebirth allows rank 5 players to reset their progress");
        sender.sendMessage(ChatColor.YELLOW + "in exchange for permanent bonuses and abilities.");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Commands:");
        sender.sendMessage(ChatColor.WHITE + "/bdrebirth check " + ChatColor.GRAY + "- Check if you're eligible");
        sender.sendMessage(ChatColor.WHITE + "/bdrebirth confirm " + ChatColor.GRAY + "- Initiate rebirth process");
        sender.sendMessage(ChatColor.WHITE + "/bdrebirth stats [player] " + ChatColor.GRAY + "- See rebirth stats");
        sender.sendMessage(ChatColor.WHITE + "/bdrebirth top " + ChatColor.GRAY + "- View rebirth leaderboard");
        
        // Show deity commands if applicable
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int rebirthLevel = rebirthManager.getRebirthLevel(player);
            
            if (rebirthLevel >= 1) {
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Deity Commands:");
                
                if (rebirthLevel >= 1) {
                    sender.sendMessage(ChatColor.WHITE + "/bdrebirth bless <player> " + 
                            ChatColor.GRAY + "- Give player trading boost");
                }
                
                if (rebirthLevel >= 4) {
                    sender.sendMessage(ChatColor.WHITE + "/bdrebirth predict " + 
                            ChatColor.GRAY + "- See upcoming trader stock");
                }
                
                if (rebirthLevel >= 5) {
                    sender.sendMessage(ChatColor.WHITE + "/bdrebirth aura " + 
                            ChatColor.GRAY + "- Toggle abundance aura visibility");
                }
            }
        }
        
        return true;
    }
}