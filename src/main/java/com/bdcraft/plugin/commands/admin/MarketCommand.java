package com.bdcraft.plugin.commands.admin;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Command for managing BD Markets.
 */
public class MarketCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    
    /**
     * Creates a new market command.
     * @param plugin The plugin instance
     */
    public MarketCommand(BDCraft plugin) {
        this.plugin = plugin;
        plugin.getCommand("bdmarket").setExecutor(this);
        plugin.getCommand("bdmarket").setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check for basic market permission, specific actions will check their own permissions
        if (!player.hasPermission("bdcraft.market.check") && 
            !player.hasPermission("bdcraft.market.info") && 
            !player.hasPermission("bdcraft.market.create") && 
            !player.hasPermission("bdcraft.market.associate") && 
            !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }
        
        MarketManager marketManager = plugin.getEconomyModule().getMarketManager();
        
        switch (args[0].toLowerCase()) {
            case "create":
                createMarket(player, marketManager);
                break;
            case "upgrade":
                upgradeMarket(player, args, marketManager);
                break;
            case "info":
                getMarketInfo(player, marketManager);
                break;
            case "list":
                listMarkets(player, marketManager);
                break;
            case "delete":
                deleteMarket(player, args, marketManager);
                break;
            case "check":
                checkMarketBoundary(player, marketManager);
                break;
            case "associate":
                handleAssociateCommand(player, args, marketManager);
                break;
            default:
                sendUsage(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Creates a market at the player's location.
     * @param player The player
     * @param marketManager The market manager
     */
    private void createMarket(Player player, MarketManager marketManager) {
        // Check permission (bdcraft.market.create according to documentation)
        if (!player.hasPermission("bdcraft.market.create") && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create markets.");
            return;
        }
        
        Location location = player.getLocation();
        
        // Check if near existing market
        if (marketManager.getMarketAt(location) != null) {
            player.sendMessage(ChatColor.RED + "There is already a market in this area.");
            return;
        }
        
        // Create market
        BDMarket market = marketManager.createMarket(location, player);
        
        if (market != null) {
            player.sendMessage(ChatColor.GREEN + "Market created successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create market.");
        }
    }
    
    /**
     * Upgrades a market.
     * @param player The player
     * @param args The command arguments
     * @param marketManager The market manager
     */
    private void upgradeMarket(Player player, String[] args, MarketManager marketManager) {
        // Check permission (bdcraft.market.upgrade according to documentation)
        if (!player.hasPermission("bdcraft.market.upgrade") && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to upgrade markets.");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Check if player is the founder or has admin permissions
        if (!player.getUniqueId().equals(market.getFounderId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "Only the market founder can upgrade this market.");
            return;
        }
        
        // Check if already max level
        if (market.getLevel() >= 4) {
            player.sendMessage(ChatColor.RED + "This market is already at maximum level.");
            return;
        }
        
        // Upgrade market
        if (marketManager.upgradeMarket(market)) {
            player.sendMessage(ChatColor.GREEN + "Market upgraded to level " + market.getLevel() + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to upgrade market.");
        }
    }
    
    /**
     * Gets information about a market.
     * @param player The player
     * @param marketManager The market manager
     */
    private void getMarketInfo(Player player, MarketManager marketManager) {
        // Check permission (bdcraft.market.info according to documentation)
        if (!player.hasPermission("bdcraft.market.info") && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to view market information.");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Send info
        player.sendMessage(ChatColor.GOLD + "=== Market Information ===");
        player.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + market.getId());
        player.sendMessage(ChatColor.YELLOW + "Founder: " + ChatColor.WHITE + market.getFounderName());
        player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Traders: " + ChatColor.WHITE + market.getTraders().size());
        player.sendMessage(ChatColor.YELLOW + "Collectors: " + ChatColor.WHITE + market.getTraderCount("COLLECTOR") + 
                "/" + getMaxCollectors(market));
        player.sendMessage(ChatColor.YELLOW + "Price Modifier: " + ChatColor.WHITE + 
                String.format("%.2fx", market.getPriceModifier()));
        player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.WHITE + 
                formatLocation(market.getCenter()));
    }
    
    /**
     * Lists all markets.
     * @param player The player
     * @param marketManager The market manager
     */
    private void listMarkets(Player player, MarketManager marketManager) {
        // Check permission (bdcraft.market.create according to documentation)
        if (!player.hasPermission("bdcraft.market.create") && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to list markets.");
            return;
        }
        
        List<BDMarket> markets = marketManager.getMarketsInWorld(player.getWorld().getName());
        
        if (markets.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no markets in this world.");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== Markets in " + player.getWorld().getName() + " ===");
        
        for (BDMarket market : markets) {
            player.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + market.getId() + 
                    ChatColor.YELLOW + " | Founder: " + ChatColor.WHITE + market.getFounderName() + 
                    ChatColor.YELLOW + " | Level: " + ChatColor.WHITE + market.getLevel() + 
                    ChatColor.YELLOW + " | Location: " + ChatColor.WHITE + 
                    formatLocation(market.getCenter()));
        }
    }
    
    /**
     * Deletes a market.
     * @param player The player
     * @param args The command arguments
     * @param marketManager The market manager
     */
    private void deleteMarket(Player player, String[] args, MarketManager marketManager) {
        // Check permission (bdcraft.admin.market according to documentation)
        if (!player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to delete markets.");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Confirm deletion
        if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
            player.sendMessage(ChatColor.RED + "WARNING: This will permanently delete this market and remove all associated villagers.");
            player.sendMessage(ChatColor.RED + "To confirm, type: /bdmarket delete confirm");
            return;
        }
        
        // Delete market
        marketManager.removeMarket(market);
        player.sendMessage(ChatColor.GREEN + "Market deleted successfully!");
    }
    
    /**
     * Gets the maximum number of collectors for a market.
     * @param market The market
     * @return The maximum number of collectors
     */
    private int getMaxCollectors(BDMarket market) {
        int level = market.getLevel();
        
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Formats a location for display.
     * @param location The location
     * @return The formatted location
     */
    private String formatLocation(Location location) {
        return location.getBlockX() + ", " + 
                location.getBlockY() + ", " + 
                location.getBlockZ();
    }
    
    /**
     * Shows the boundary of a market using wool blocks.
     * @param player The player
     * @param marketManager The market manager
     */
    private void checkMarketBoundary(Player player, MarketManager marketManager) {
        // Check permission (bdcraft.market.check according to documentation)
        if (!player.hasPermission("bdcraft.market.check") && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to check market boundaries.");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Show boundary using wool blocks as specified in docs
        market.showBoundary(player, plugin);
    }
    
    /**
     * Handles the associate commands for adding/removing market associates.
     * 
     * @param player The player executing the command
     * @param args The command arguments
     * @param marketManager The market manager
     */
    private void handleAssociateCommand(Player player, String[] args, MarketManager marketManager) {
        // Check permissions
        if (!player.hasPermission("bdcraft.market.associate")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to manage market associates.");
            return;
        }
        
        // Check if enough arguments
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage:");
            player.sendMessage(ChatColor.RED + "/bdmarket associate add <player> - Add a player as a market associate");
            player.sendMessage(ChatColor.RED + "/bdmarket associate remove <player> - Remove a player as a market associate");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Check if player is the founder or has admin permissions
        if (!player.getUniqueId().equals(market.getFounderId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "Only the market founder can manage associates.");
            return;
        }
        
        // Get the target player
        String targetName = args[2];
        Player targetPlayer = plugin.getServer().getPlayer(targetName);
        
        // Add or remove associate
        if (args[1].equalsIgnoreCase("add")) {
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not online. Associates must be online when added.");
                return;
            }
            
            // Check if target is founder
            if (targetPlayer.getUniqueId().equals(market.getFounderId())) {
                player.sendMessage(ChatColor.RED + "You can't add the market founder as an associate.");
                return;
            }
            
            // Add associate
            if (market.addAssociate(targetPlayer.getUniqueId(), targetPlayer.getName())) {
                player.sendMessage(ChatColor.GREEN + "Added " + targetPlayer.getName() + " as an associate to your market.");
                targetPlayer.sendMessage(ChatColor.GREEN + "You have been added as an associate to " +
                        market.getFounderName() + "'s market.");
            } else {
                if (market.isAssociate(targetPlayer.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + targetPlayer.getName() + " is already an associate of this market.");
                } else {
                    player.sendMessage(ChatColor.RED + "Market already has the maximum of 5 associates.");
                }
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            // Try to get player by name if online
            UUID targetId = null;
            if (targetPlayer != null) {
                targetId = targetPlayer.getUniqueId();
            } else {
                // Check existing associates by name (if they're offline)
                for (UUID associateId : market.getAssociates()) {
                    String associateName = null;
                    
                    // Try to get the player's name from the server
                    Player associatePlayer = plugin.getServer().getPlayer(associateId);
                    if (associatePlayer != null) {
                        associateName = associatePlayer.getName();
                    } else {
                        // Try to get from offline player data
                        associateName = plugin.getServer().getOfflinePlayer(associateId).getName();
                    }
                    
                    if (associateName != null && associateName.equalsIgnoreCase(targetName)) {
                        targetId = associateId;
                        break;
                    }
                }
            }
            
            // Check if target was found
            if (targetId == null) {
                player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not an associate of this market.");
                return;
            }
            
            // Remove associate
            if (market.removeAssociate(targetId, targetName)) {
                player.sendMessage(ChatColor.GREEN + "Removed " + targetName + " as an associate from your market.");
                
                // Notify the target player if they're online
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(ChatColor.YELLOW + "You have been removed as an associate from " +
                            market.getFounderName() + "'s market.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not an associate of this market.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Unknown associate command: " + args[1]);
            player.sendMessage(ChatColor.RED + "Available commands: add, remove");
        }
    }
    
    /**
     * Sends usage information to a player.
     * @param player The player
     */
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BD Market Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket create" + ChatColor.WHITE + " - Creates a market at your location");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket upgrade" + ChatColor.WHITE + " - Upgrades the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket info" + ChatColor.WHITE + " - Shows information about the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket list" + ChatColor.WHITE + " - Lists all markets in the current world");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket delete" + ChatColor.WHITE + " - Deletes the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket check" + ChatColor.WHITE + " - Visualize market boundaries with wool blocks");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket associate add <player>" + ChatColor.WHITE + " - Add a player as a market associate");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket associate remove <player>" + ChatColor.WHITE + " - Remove a player as a market associate");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = Arrays.asList("create", "upgrade", "info", "list", "delete", "check", "associate");
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                return Arrays.asList("confirm");
            } else if (args[0].equalsIgnoreCase("associate")) {
                return Arrays.asList("add", "remove").stream()
                        .filter(s -> s.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("associate")) {
            // Return online player names for tab completion of associate commands
            if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
                return plugin.getServer().getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        
        return new ArrayList<>();
    }
}