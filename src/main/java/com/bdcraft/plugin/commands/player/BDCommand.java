package com.bdcraft.plugin.commands.player;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.economy.market.Market;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Main command for the BDCraft plugin.
 */
public class BDCommand extends CommandBase {
    private final BDCraft plugin;
    private final MarketManager marketManager;
    
    /**
     * Creates a new BD command.
     * 
     * @param plugin The plugin instance
     */
    public BDCommand(BDCraft plugin) {
        super(plugin, "bdcraft", "bdcraft.command");
        aliases.add("bd");
        
        this.plugin = plugin;
        this.marketManager = plugin.getEconomyModule().getMarketManager();
        
        // Version command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "version";
            }
            
            @Override
            public String getDescription() {
                return "Shows the plugin version";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.version";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                String version = plugin.getDescription().getVersion();
                sender.sendMessage(ChatColor.GREEN + "BDCraft v" + version);
                return true;
            }
        });
        
        // Status command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "status";
            }
            
            @Override
            public String getDescription() {
                return "Shows the plugin status";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.status";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                sender.sendMessage(ChatColor.GREEN + "BDCraft Status:");
                
                // Module status
                sender.sendMessage(ChatColor.YELLOW + "Modules:");
                sender.sendMessage(ChatColor.WHITE + "  Economy: " + ChatColor.GREEN + "Enabled");
                sender.sendMessage(ChatColor.WHITE + "  Permissions: " + ChatColor.GREEN + "Enabled");
                sender.sendMessage(ChatColor.WHITE + "  Vital: " + ChatColor.GREEN + "Enabled");
                sender.sendMessage(ChatColor.WHITE + "  Progression: " + ChatColor.GREEN + "Enabled");
                
                // Show market stats
                List<Market> markets = marketManager.getMarkets();
                sender.sendMessage(ChatColor.YELLOW + "Markets: " + ChatColor.WHITE + markets.size());
                
                return true;
            }
        });
        
        // Reload command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "reload";
            }
            
            @Override
            public String getDescription() {
                return "Reloads the plugin configuration";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.reload";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "BDCraft configuration reloaded.");
                return true;
            }
        });
        
        // Market check command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "marketcheck";
            }
            
            @Override
            public String getDescription() {
                return "Checks if you're standing in a market";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.market.check";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Check if player is in a market
                Market market = marketManager.getMarketAt(player.getLocation());
                
                if (market != null) {
                    sender.sendMessage(ChatColor.GREEN + "You are in market '" + market.getName() + 
                            "' owned by " + market.getOwnerName() + ".");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You are not in a market.");
                }
                
                return true;
            }
        });
        
        // Nearest market command
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "nearestmarket";
            }
            
            @Override
            public String getDescription() {
                return "Shows the nearest market";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.market.nearest";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                
                // Get all markets in this world
                List<Market> markets = marketManager.getMarkets();
                
                if (markets.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "No markets found.");
                    return true;
                }
                
                // Find the nearest market
                Market nearestMarket = null;
                double nearestDistance = Double.MAX_VALUE;
                Location playerLoc = player.getLocation();
                
                for (Market market : markets) {
                    if (!market.getWorldName().equals(player.getWorld().getName())) {
                        continue; // Skip markets in other worlds
                    }
                    
                    Location marketLoc = new Location(
                            player.getWorld(),
                            market.getCenterX(),
                            market.getCenterY(),
                            market.getCenterZ()
                    );
                    
                    double distance = playerLoc.distance(marketLoc);
                    
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestMarket = market;
                    }
                }
                
                if (nearestMarket == null) {
                    sender.sendMessage(ChatColor.YELLOW + "No markets found in this world.");
                } else {
                    sender.sendMessage(ChatColor.GREEN + "Nearest market: " + ChatColor.WHITE + 
                            nearestMarket.getName() + ChatColor.GREEN + " owned by " + 
                            ChatColor.WHITE + nearestMarket.getOwnerName());
                    
                    sender.sendMessage(ChatColor.GREEN + "Distance: " + ChatColor.WHITE + 
                            String.format("%.2f", nearestDistance) + " blocks");
                    
                    sender.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.WHITE + 
                            nearestMarket.getCenterX() + ", " + 
                            nearestMarket.getCenterY() + ", " + 
                            nearestMarket.getCenterZ());
                }
                
                return true;
            }
        });
    }
}