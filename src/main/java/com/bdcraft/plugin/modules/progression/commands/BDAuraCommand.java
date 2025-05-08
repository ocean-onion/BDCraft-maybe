package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to toggle the rebirth aura for high-level reborn players.
 */
public class BDAuraCommand implements CommandExecutor {
    private final BDCraft plugin;
    private static final int MIN_REBIRTH_LEVEL = 3;

    /**
     * Creates a new BDAuraCommand instance.
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

        // Check permission
        if (!player.hasPermission("bdcraft.deity.aura")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check rebirth level
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        if (rebirthLevel < MIN_REBIRTH_LEVEL) {
            player.sendMessage(ChatColor.RED + "You need rebirth level " + MIN_REBIRTH_LEVEL + " to use auras.");
            return true;
        }

        // Toggle the aura
        boolean auraEnabled = plugin.getProgressionModule().getRebirthManager().togglePlayerAura(player);
        
        if (auraEnabled) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura has been " + ChatColor.GREEN + "activated" + 
                    ChatColor.LIGHT_PURPLE + "!");
            player.sendMessage(ChatColor.GRAY + "Nearby players will receive farming benefits.");
            
            // Create a visual effect around the player
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 1.5, 1, 1.5, 0.1);
        } else {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth aura has been " + ChatColor.RED + "deactivated" + 
                    ChatColor.LIGHT_PURPLE + ".");
            
            // Play a deactivation sound
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.5f, 1.0f);
        }
        
        return true;
    }
}