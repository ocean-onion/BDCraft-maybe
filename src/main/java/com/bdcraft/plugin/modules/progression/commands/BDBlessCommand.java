package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command for blessing other players with rebirth bonuses.
 */
public class BDBlessCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    
    /**
     * Creates a new bless command.
     *
     * @param plugin The plugin instance
     */
    public BDBlessCommand(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.rebirth.bless")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // Check if player has been reborn
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        
        if (rebirthLevel <= 0) {
            player.sendMessage(ChatColor.RED + "You must be reborn to use this command.");
            return true;
        }
        
        // Check cooldown
        long lastUseTime = plugin.getProgressionModule().getRebirthManager().getLastCommandUse(player.getUniqueId(), "bdbless");
        long currentTime = System.currentTimeMillis();
        long cooldown = 6 * 60 * 60 * 1000; // 6 hours
        
        if (lastUseTime > 0 && currentTime - lastUseTime < cooldown) {
            long remainingTime = cooldown - (currentTime - lastUseTime);
            long hours = remainingTime / (60 * 60 * 1000);
            long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
            
            player.sendMessage(ChatColor.RED + "You must wait " + hours + " hours and " + 
                    minutes + " minutes before using this command again.");
            return true;
        }
        
        // Check for target player
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bdbless <player>");
            return true;
        }
        
        // Use getPlayerExact for exact name matching
        Player target = Bukkit.getPlayerExact(args[0]);
        
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player not found or is offline.");
            return true;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot bless yourself.");
            return true;
        }
        
        // Record command use
        plugin.getProgressionModule().getRebirthManager().recordCommandUse(player.getUniqueId(), "bdbless");
        
        // Calculate blessing duration
        int blessingHours = 1 + rebirthLevel; // 1 hour + 1 per rebirth level
        long blessingDuration = blessingHours * 60 * 60 * 1000; // Convert to milliseconds
        
        // Calculate blessing end time
        long blessingEndTime = System.currentTimeMillis() + blessingDuration;
        
        // Apply blessing
        plugin.getProgressionModule().getRebirthManager().addBlessingEffect(target.getUniqueId(), blessingEndTime);
        
        // Send messages and effects
        player.sendMessage(ChatColor.GREEN + "You have blessed " + target.getName() + " for " + blessingHours + " hours!");
        
        target.sendMessage(ChatColor.LIGHT_PURPLE + "You have been blessed by " + player.getName() + 
                " for " + blessingHours + " hours!");
        target.sendMessage(ChatColor.YELLOW + "Your rewards from farming and trading will be increased!");
        
        // Play sound effect
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}