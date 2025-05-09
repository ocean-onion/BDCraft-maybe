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
 * Command for predicting future market trends (high-level rebirth feature).
 */
public class BDPredictCommand implements CommandExecutor {
    private final BDCraft plugin;
    private static final int MIN_REBIRTH_LEVEL = 4;

    /**
     * Creates a new BDPredictCommand instance.
     * 
     * @param plugin The plugin instance
     */
    public BDPredictCommand(BDCraft plugin) {
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
        if (!player.hasPermission("bdcraft.deity.predict")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check rebirth level
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        if (rebirthLevel < MIN_REBIRTH_LEVEL) {
            player.sendMessage(ChatColor.RED + "You need rebirth level " + MIN_REBIRTH_LEVEL + " to predict market trends.");
            return true;
        }

        // Perform prediction using the BDRebirthManager
        boolean success = plugin.getProgressionModule().getRebirthManager().predictSeasonalItems(player);
        
        if (success) {
            // Create a mystical effect around the player
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1, 0), 
                    50, 0.5, 0.5, 0.5);
            
            // Play a mystical sound
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
        }
        
        return true;
    }
}