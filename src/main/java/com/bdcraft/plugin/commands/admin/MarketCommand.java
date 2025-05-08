package com.bdcraft.plugin.commands.admin;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.economy.market.Market;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Command class for market administration.
 */
public class MarketCommand extends CommandBase {
    private final BDCraft plugin;
    private final MarketManager marketManager;

    /**
     * Creates a new market command.
     * 
     * @param plugin The plugin instance
     */
    public MarketCommand(BDCraft plugin) {
        super(plugin, "bdmarket", "bdcraft.admin.market");
        this.plugin = plugin;
        this.marketManager = plugin.getEconomyModule().getMarketManager();
        
        // Create market
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "create";
            }
            
            @Override
            public String getDescription() {
                return "Creates a new market at player's location";
            }
            
            @Override
            public String getUsage() {
                return "<name> [player]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.create";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bdmarket create <name> [player]");
                    return true;
                }
                
                String marketName = args[0];
                Player owner;
                
                if (args.length >= 2) {
                    // Creating market for another player
                    String playerName = args[1];
                    owner = plugin.getServer().getPlayer(playerName);
                    
                    if (owner == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
                        return true;
                    }
                } else {
                    // Creating market for command sender
                    owner = (Player) sender;
                }
                
                Player player = (Player) sender;
                Location center = player.getLocation();
                
                // Create the market
                Market market = marketManager.createMarket(owner, marketName, center);
                
                if (market != null) {
                    sender.sendMessage(ChatColor.GREEN + "Market '" + marketName + "' created for " + owner.getName() + 
                            " at " + formatLocation(center));
                } else {
                    sender.sendMessage(ChatColor.RED + "Failed to create market. Check for overlap with existing markets.");
                }
                
                return true;
            }
        });
        
        // Delete market
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "delete";
            }
            
            @Override
            public String getDescription() {
                return "Deletes a market by ID or at player's location";
            }
            
            @Override
            public String getUsage() {
                return "[market_id]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.delete";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                if (args.length >= 1) {
                    // Try to parse as UUID
                    try {
                        UUID marketId = UUID.fromString(args[0]);
                        Market market = marketManager.getMarket(marketId);
                        
                        if (market == null) {
                            sender.sendMessage(ChatColor.RED + "Market not found with ID: " + marketId);
                            return true;
                        }
                        
                        boolean removed = marketManager.removeMarket(marketId);
                        
                        if (removed) {
                            sender.sendMessage(ChatColor.GREEN + "Market '" + market.getName() + "' deleted.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Failed to delete market.");
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid market ID format. Use the market ID or stand inside a market.");
                    }
                } else {
                    // Try to get market at player location
                    Market market = marketManager.getMarketAt(player.getLocation());
                    
                    if (market == null) {
                        sender.sendMessage(ChatColor.RED + "No market found at your location.");
                        return true;
                    }
                    
                    boolean removed = marketManager.removeMarket(market.getId());
                    
                    if (removed) {
                        sender.sendMessage(ChatColor.GREEN + "Market '" + market.getName() + "' deleted.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to delete market.");
                    }
                }
                
                return true;
            }
        });
        
        // Info command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "info";
            }
            
            @Override
            public String getDescription() {
                return "Shows information about a market";
            }
            
            @Override
            public String getUsage() {
                return "[market_id]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.info";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                Market market;
                
                if (args.length >= 1) {
                    // Try to parse as UUID
                    try {
                        UUID marketId = UUID.fromString(args[0]);
                        market = marketManager.getMarket(marketId);
                        
                        if (market == null) {
                            sender.sendMessage(ChatColor.RED + "Market not found with ID: " + marketId);
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid market ID format. Use the market ID or stand inside a market.");
                        return true;
                    }
                } else {
                    // Try to get market at player location
                    market = marketManager.getMarketAt(player.getLocation());
                    
                    if (market == null) {
                        sender.sendMessage(ChatColor.RED + "No market found at your location.");
                        return true;
                    }
                }
                
                // Display market info
                sender.sendMessage(ChatColor.GOLD + "=== Market Information ===");
                sender.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + market.getId());
                sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + market.getName());
                sender.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + market.getOwnerName() + 
                        " (" + market.getOwnerId() + ")");
                sender.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.WHITE + 
                        market.getWorldName() + " at " + market.getCenterX() + ", " + 
                        market.getCenterY() + ", " + market.getCenterZ());
                sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel());
                sender.sendMessage(ChatColor.YELLOW + "Total Sales: " + ChatColor.WHITE + market.getTotalSales());
                
                // List associates
                List<UUID> associates = market.getAssociates();
                sender.sendMessage(ChatColor.YELLOW + "Associates (" + associates.size() + "/5):");
                
                if (associates.isEmpty()) {
                    sender.sendMessage(ChatColor.GRAY + "  No associates");
                } else {
                    for (UUID associateId : associates) {
                        String associateName = plugin.getServer().getOfflinePlayer(associateId).getName();
                        if (associateName == null) {
                            associateName = "Unknown";
                        }
                        sender.sendMessage(ChatColor.GRAY + "  - " + associateName + " (" + associateId + ")");
                    }
                }
                
                return true;
            }
        });
        
        // List command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "list";
            }
            
            @Override
            public String getDescription() {
                return "Lists all markets or markets in current world";
            }
            
            @Override
            public String getUsage() {
                return "[world|all]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.list";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                boolean allWorlds = false;
                String worldName = null;
                
                if (sender instanceof Player && args.length == 0) {
                    // Default to current world if player and no args
                    worldName = ((Player) sender).getWorld().getName();
                } else if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
                    // List all worlds
                    allWorlds = true;
                } else if (args.length > 0) {
                    // Specific world name
                    worldName = args[0];
                    World world = plugin.getServer().getWorld(worldName);
                    
                    if (world == null) {
                        sender.sendMessage(ChatColor.RED + "World not found: " + worldName);
                        return true;
                    }
                } else {
                    // Console with no args defaults to all worlds
                    allWorlds = true;
                }
                
                // Get all markets
                List<Market> markets = marketManager.getMarkets();
                
                // Filter by world if needed
                if (!allWorlds && worldName != null) {
                    final String targetWorld = worldName;
                    markets = markets.stream()
                            .filter(m -> m.getWorldName().equals(targetWorld))
                            .collect(java.util.stream.Collectors.toList());
                }
                
                // Display market list
                if (markets.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "No markets found" + 
                            (allWorlds ? "" : " in world '" + worldName + "'") + ".");
                    return true;
                }
                
                sender.sendMessage(ChatColor.GOLD + "=== Markets (" + markets.size() + ") ===");
                
                for (Market market : markets) {
                    sender.sendMessage(
                            ChatColor.YELLOW + market.getName() + 
                            ChatColor.GRAY + " (" + market.getOwnerName() + ") - " + 
                            ChatColor.WHITE + market.getWorldName() + " at " + 
                            market.getCenterX() + ", " + market.getCenterY() + ", " + market.getCenterZ());
                }
                
                return true;
            }
        });
        
        // Add associate command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "addassociate";
            }
            
            @Override
            public String getDescription() {
                return "Adds an associate to a market";
            }
            
            @Override
            public String getUsage() {
                return "<player>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.associate";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bdmarket addassociate <player>");
                    return true;
                }
                
                Player player = (Player) sender;
                String targetName = args[0];
                Player target = plugin.getServer().getPlayer(targetName);
                
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found: " + targetName);
                    return true;
                }
                
                // Get market at location
                Market market = marketManager.getMarketAt(player.getLocation());
                
                if (market == null) {
                    sender.sendMessage(ChatColor.RED + "No market found at your location.");
                    return true;
                }
                
                // Add associate
                boolean added = market.addAssociate(target.getUniqueId());
                
                if (added) {
                    sender.sendMessage(ChatColor.GREEN + "Added " + target.getName() + " as an associate to market '" + 
                            market.getName() + "'.");
                    target.sendMessage(ChatColor.GREEN + "You have been added as an associate to market '" + 
                            market.getName() + "' owned by " + market.getOwnerName() + ".");
                } else {
                    sender.sendMessage(ChatColor.RED + "Failed to add associate. Market may already have 5 associates " + 
                            "or player is already an associate.");
                }
                
                return true;
            }
        });
        
        // Remove associate command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "removeassociate";
            }
            
            @Override
            public String getDescription() {
                return "Removes an associate from a market";
            }
            
            @Override
            public String getUsage() {
                return "<player>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.associate";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bdmarket removeassociate <player>");
                    return true;
                }
                
                Player player = (Player) sender;
                String targetName = args[0];
                
                // Try to get UUID from username
                UUID targetUuid = null;
                Player target = plugin.getServer().getPlayer(targetName);
                
                if (target != null) {
                    targetUuid = target.getUniqueId();
                } else {
                    // Try to get offline player
                    targetUuid = plugin.getServer().getOfflinePlayer(targetName).getUniqueId();
                }
                
                if (targetUuid == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found: " + targetName);
                    return true;
                }
                
                // Get market at location
                Market market = marketManager.getMarketAt(player.getLocation());
                
                if (market == null) {
                    sender.sendMessage(ChatColor.RED + "No market found at your location.");
                    return true;
                }
                
                // Remove associate
                boolean removed = market.removeAssociate(targetUuid);
                
                if (removed) {
                    sender.sendMessage(ChatColor.GREEN + "Removed " + targetName + " as an associate from market '" + 
                            market.getName() + "'.");
                    
                    if (target != null) {
                        target.sendMessage(ChatColor.YELLOW + "You have been removed as an associate from market '" + 
                                market.getName() + "'.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Failed to remove associate. Player may not be an associate of this market.");
                }
                
                return true;
            }
        });
        
        // Set level command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "setlevel";
            }
            
            @Override
            public String getDescription() {
                return "Sets the level of a market";
            }
            
            @Override
            public String getUsage() {
                return "<level>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.market.setlevel";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bdmarket setlevel <level>");
                    return true;
                }
                
                Player player = (Player) sender;
                
                // Parse level
                int level;
                try {
                    level = Integer.parseInt(args[0]);
                    
                    if (level < 1 || level > 5) {
                        sender.sendMessage(ChatColor.RED + "Level must be between 1 and 5.");
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid level. Must be a number between 1 and 5.");
                    return true;
                }
                
                // Get market at location
                Market market = marketManager.getMarketAt(player.getLocation());
                
                if (market == null) {
                    sender.sendMessage(ChatColor.RED + "No market found at your location.");
                    return true;
                }
                
                // Set level
                market.setLevel(level);
                
                sender.sendMessage(ChatColor.GREEN + "Set market '" + market.getName() + "' to level " + level + ".");
                
                return true;
            }
        });
    }
    
    /**
     * Formats a location as a string.
     * 
     * @param location The location
     * @return The formatted string
     */
    private String formatLocation(Location location) {
        return location.getWorld().getName() + " (" + 
                location.getBlockX() + ", " + 
                location.getBlockY() + ", " + 
                location.getBlockZ() + ")";
    }
}