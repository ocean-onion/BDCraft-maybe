package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.progression.BDRebirthManager;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for the Golden Touch deity ability.
 */
public class BDTouchCommand implements CommandExecutor {
    private final BDCraft plugin;
    private static final int MIN_REBIRTH_LEVEL = 5;
    private static final long COOLDOWN_TIME = 12 * 60 * 60 * 1000; // 12 hour cooldown

    /**
     * Creates a new BDTouchCommand instance.
     * 
     * @param plugin The plugin instance
     */
    public BDTouchCommand(BDCraft plugin) {
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
        if (!player.hasPermission("bdcraft.deity.touch")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check rebirth level
        BDRebirthManager rebirthManager = plugin.getProgressionModule().getRebirthManager();
        int rebirthLevel = rebirthManager.getPlayerRebirthLevel(player);
        if (rebirthLevel < MIN_REBIRTH_LEVEL) {
            player.sendMessage(ChatColor.RED + "You need rebirth level " + MIN_REBIRTH_LEVEL + " to use the Golden Touch.");
            return true;
        }

        // Check cooldown
        if (rebirthManager.isOnCommandCooldown(player.getUniqueId(), "touch", COOLDOWN_TIME)) {
            long timeLeft = rebirthManager.getRemainingCooldown(player.getUniqueId(), "touch");
            player.sendMessage(ChatColor.RED + "Golden Touch is on cooldown. Available in " + 
                    rebirthManager.formatTimeRemaining(timeLeft) + ".");
            return true;
        }

        // Activate the Golden Touch
        activateGoldenTouch(player, rebirthLevel);
        
        // Record command use
        rebirthManager.recordCommandUse(player.getUniqueId(), "touch");
        
        return true;
    }
    
    /**
     * Activates the Golden Touch ability.
     * 
     * @param player The player
     * @param rebirthLevel The player's rebirth level
     */
    private void activateGoldenTouch(Player player, int rebirthLevel) {
        // Calculate bonus based on rebirth level
        double baseBonus = 0.5; // 50% base bonus
        double levelBonus = rebirthLevel * 0.1; // 10% per rebirth level
        double totalBonus = baseBonus + levelBonus;
        
        // Cap at 200% (3x normal)
        totalBonus = Math.min(2.0, totalBonus);
        
        // Add temporary crop value boost
        long duration = 10 * 60 * 1000; // 10 minutes
        plugin.getProgressionModule().getRebirthManager().setPlayerExpBoost(player.getUniqueId(), totalBonus, duration);
        
        // Visual effects
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1.0, 0), 
                100, 0.5, 1.0, 0.5);
        
        // Sound effects
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.7f, 1.0f);
        
        // Messages
        player.sendMessage(ChatColor.GOLD + "✧✧✧ " + ChatColor.YELLOW + "Golden Touch Activated" + 
                ChatColor.GOLD + " ✧✧✧");
        player.sendMessage(ChatColor.YELLOW + "For the next 10 minutes, all crops you harvest will be worth " + 
                ChatColor.GOLD + (int)((1 + totalBonus) * 100) + "%" + ChatColor.YELLOW + " of their normal value!");
        player.sendMessage(ChatColor.GRAY + "This ability can be used again in 12 hours.");
        
        // Broadcast to nearby players
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby.equals(player) || nearby.getLocation().distance(player.getLocation()) > 20) {
                continue;
            }
            
            nearby.sendMessage(ChatColor.GOLD + player.getName() + "'s Golden Touch illuminates the area...");
            nearby.spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1.5, 0), 20, 0.5, 0.5, 0.5);
        }
    }
}