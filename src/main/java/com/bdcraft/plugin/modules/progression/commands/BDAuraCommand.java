package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for toggling rebirth aura.
 */
public class BDAuraCommand implements CommandExecutor {
    private final BDCraft plugin;
    
    /**
     * Creates a new aura command.
     *
     * @param plugin The plugin instance
     */
    public BDAuraCommand(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.rebirth.aura")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // Check if player has been reborn
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        
        if (rebirthLevel <= 0) {
            player.sendMessage(ChatColor.RED + "You must be reborn to use this command.");
            return true;
        }
        
        // Toggle aura
        boolean auraEnabled = plugin.getProgressionModule().getRebirthManager().togglePlayerAura(player);
        
        if (auraEnabled) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura has been " + ChatColor.GREEN + "enabled" + ChatColor.LIGHT_PURPLE + ".");
        } else {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura has been " + ChatColor.RED + "disabled" + ChatColor.LIGHT_PURPLE + ".");
        }
        
        return true;
    }
}