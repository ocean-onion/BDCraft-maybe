package com.bdcraft.plugin.modules.vital.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.warp.MarketWarpManager;
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
 * Commands for managing market warps.
 */
public class MarketWarpCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    private final MarketWarpManager warpManager;
    
    /**
     * Creates a new market warp command.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     * @param warpManager The warp manager
     */
    public MarketWarpCommand(BDCraft plugin, BDVitalModule vitalModule, MarketWarpManager warpManager) {
        this.plugin = plugin;
        this.warpManager = warpManager;
        
        plugin.getCommand("bdmarketwarp").setExecutor(this);
        plugin.getCommand("bdmarketwarp").setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdvital.market.warp") && !player.hasPermission("bdvital.market.setwarp")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }
        
        BDMarketManager marketManager = plugin.getEconomyModule().getBDMarketManager();
        
        switch (args[0].toLowerCase()) {
            case "set":
                setMarketWarp(player, marketManager);
                break;
            case "remove":
                removeMarketWarp(player, marketManager);
                break;
            case "list":
                listMarketWarps(player, marketManager);
                break;
            default:
                // Assume it's a warp name/index
                teleportToMarketWarp(player, args[0], marketManager);
                break;
        }
        
        return true;
    }
    
    /**
     * Sends usage information to a player.
     * @param player The player
     */
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Market Warp Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bdmarketwarp <n>" + ChatColor.WHITE + " - Teleport to a market warp");
        
        if (player.hasPermission("bdvital.market.setwarp")) {
            player.sendMessage(ChatColor.YELLOW + "/bdmarketwarp set" + ChatColor.WHITE + " - Set your market warp at your current location");
            player.sendMessage(ChatColor.YELLOW + "/bdmarketwarp remove" + ChatColor.WHITE + " - Remove your market warp");
        }
        
        player.sendMessage(ChatColor.YELLOW + "/bdmarketwarp list" + ChatColor.WHITE + " - List all public market warps");
    }
    
    /**
     * Sets a market warp at the player's location.
     * @param player The player
     * @param marketManager The market manager
     */
    private void setMarketWarp(Player player, BDMarketManager marketManager) {
        if (!player.hasPermission("bdvital.market.setwarp")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to set market warps.");
            return;
        }
        
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You must be in your market to set a warp.");
            return;
        }
        
        // Check if player is the founder
        if (!player.getUniqueId().equals(market.getFounderId()) && !player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "Only the market founder can set a market warp.");
            return;
        }
        
        // Check if market level allows warps (level 2+)
        if (market.getLevel() < 2) {
            player.sendMessage(ChatColor.RED + "Your market must be at least level 2 to set a warp.");
            player.sendMessage(ChatColor.YELLOW + "Upgrade your market to enable warps.");
            return;
        }
        
        // Set warp
        if (warpManager.setWarp(market, player.getLocation())) {
            player.sendMessage(ChatColor.GREEN + "Market warp set successfully!");
            player.sendMessage(ChatColor.YELLOW + "Players can now use /bdmarketwarp to teleport to your market.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to set market warp.");
        }
    }
    
    /**
     * Removes a market warp.
     * @param player The player
     * @param marketManager The market manager
     */
    private void removeMarketWarp(Player player, BDMarketManager marketManager) {
        if (!player.hasPermission("bdvital.market.setwarp")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to remove market warps.");
            return;
        }
        
        // Get player's market
        BDMarket market = null;
        
        for (BDMarket m : marketManager.getPlayerMarkets(player.getUniqueId())) {
            if (m.getFounderId().equals(player.getUniqueId())) {
                market = m;
                break;
            }
        }
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You don't have a market.");
            return;
        }
        
        // Remove warp
        if (warpManager.removeWarp(market.getId())) {
            player.sendMessage(ChatColor.GREEN + "Market warp removed successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a market warp set.");
        }
    }
    
    /**
     * Lists all market warps.
     * @param player The player
     * @param marketManager The market manager
     */
    private void listMarketWarps(Player player, BDMarketManager marketManager) {
        if (!player.hasPermission("bdvital.market.warp")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to list market warps.");
            return;
        }
        
        List<MarketWarpManager.MarketWarp> warps = warpManager.getAllWarps();
        
        if (warps.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no market warps yet.");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== Market Warps ===");
        
        for (int i = 0; i < warps.size(); i++) {
            MarketWarpManager.MarketWarp warp = warps.get(i);
            BDMarket market = marketManager.getMarket(warp.getMarketId());
            
            if (market != null) {
                player.sendMessage(ChatColor.YELLOW + String.valueOf(i + 1) + ". " + ChatColor.WHITE + market.getFounderName() + "'s Market" + 
                        ChatColor.GRAY + " (Level " + market.getLevel() + ")");
            }
        }
        
        player.sendMessage(ChatColor.YELLOW + "Use /bdmarketwarp <number> to teleport to a market.");
    }
    
    /**
     * Teleports a player to a market warp.
     * @param player The player
     * @param arg The warp name or index
     * @param marketManager The market manager
     */
    private void teleportToMarketWarp(Player player, String arg, BDMarketManager marketManager) {
        if (!player.hasPermission("bdvital.market.warp")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use market warps.");
            return;
        }
        
        // Check if player is on cooldown
        if (warpManager.isOnCooldown(player.getUniqueId())) {
            int remainingTime = warpManager.getRemainingCooldown(player.getUniqueId());
            int minutes = remainingTime / 60;
            int seconds = remainingTime % 60;
            
            player.sendMessage(ChatColor.RED + "You must wait " + 
                    (minutes > 0 ? minutes + " minutes and " : "") + seconds + " seconds before using a market warp again.");
            return;
        }
        
        List<MarketWarpManager.MarketWarp> warps = warpManager.getAllWarps();
        
        if (warps.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no market warps available.");
            return;
        }
        
        // Try to parse as index
        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Please use a number to select a market warp.");
            player.sendMessage(ChatColor.YELLOW + "Use /bdmarketwarp list to see available warps.");
            return;
        }
        
        if (index < 0 || index >= warps.size()) {
            player.sendMessage(ChatColor.RED + "Invalid market warp number.");
            player.sendMessage(ChatColor.YELLOW + "Use /bdmarketwarp list to see available warps.");
            return;
        }
        
        MarketWarpManager.MarketWarp warp = warps.get(index);
        BDMarket market = marketManager.getMarket(warp.getMarketId());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "That market no longer exists.");
            return;
        }
        
        // Get warp location
        Location location = warp.getLocation(plugin);
        
        if (location == null) {
            player.sendMessage(ChatColor.RED + "Failed to teleport to market. The market's world doesn't exist.");
            return;
        }
        
        // Set cooldown
        warpManager.setCooldown(player.getUniqueId(), market.getLevel());
        
        // Teleport player
        player.teleport(location);
        
        player.sendMessage(ChatColor.GREEN + "Teleported to " + market.getFounderName() + "'s market.");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        Player player = (Player) sender;
        
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            
            if (player.hasPermission("bdvital.market.warp")) {
                completions.add("list");
                
                // Add numbers for available warps
                List<MarketWarpManager.MarketWarp> warps = warpManager.getAllWarps();
                for (int i = 1; i <= warps.size(); i++) {
                    completions.add(Integer.toString(i));
                }
            }
            
            if (player.hasPermission("bdvital.market.setwarp")) {
                completions.add("set");
                completions.add("remove");
            }
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}