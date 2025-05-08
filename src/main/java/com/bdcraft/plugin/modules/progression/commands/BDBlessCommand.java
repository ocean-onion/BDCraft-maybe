package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Command that allows high-level reborn players to bless another player with temporary bonuses.
 */
public class BDBlessCommand implements CommandExecutor {
    private final BDCraft plugin;
    private static final long COOLDOWN_TIME = 24 * 60 * 60 * 1000; // 24 hour cooldown
    private static final int MIN_REBIRTH_LEVEL = 4;
    private static final int BLESSING_DURATION = 20 * 60 * 20; // 20 minutes in ticks

    /**
     * Creates a new BDBlessCommand instance.
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

        // Check permission
        if (!player.hasPermission("bdcraft.deity.bless")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check rebirth level
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        if (rebirthLevel < MIN_REBIRTH_LEVEL) {
            player.sendMessage(ChatColor.RED + "You need rebirth level " + MIN_REBIRTH_LEVEL + " to bless others.");
            return true;
        }

        // Check cooldown
        long lastUseTime = plugin.getProgressionModule().getRebirthManager().getLastCommandUse(player.getUniqueId(), "bdbless");
        long currentTime = System.currentTimeMillis();
        
        if (lastUseTime > 0 && (currentTime - lastUseTime) < COOLDOWN_TIME) {
            long timeLeft = (lastUseTime + COOLDOWN_TIME - currentTime) / 1000;
            int hours = (int) (timeLeft / 3600);
            int minutes = (int) ((timeLeft % 3600) / 60);
            
            player.sendMessage(ChatColor.RED + "Blessing is on cooldown. Available in " + 
                    hours + "h " + minutes + "m.");
            return true;
        }

        // Check for target player
        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdbless <player>");
            return true;
        }

        String targetName = args[0];
        Player target = plugin.getServer().getPlayer(targetName);
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        
        // Can't bless yourself
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot bless yourself.");
            return true;
        }

        // Record the use time
        plugin.getProgressionModule().getRebirthManager().recordCommandUse(player.getUniqueId(), "bdbless");

        // Apply the blessing
        blessPlayer(player, target, rebirthLevel);
        
        return true;
    }

    /**
     * Blesses a player with temporary bonuses.
     * 
     * @param blesser The player giving the blessing
     * @param target The player receiving the blessing
     * @param rebirthLevel The blesser's rebirth level
     */
    private void blessPlayer(Player blesser, Player target, int rebirthLevel) {
        // Calculate blessing strength based on rebirth level
        int strengthBonus = Math.min(3, (rebirthLevel - 3) / 2); // 0-3 amplifier based on rebirth level
        int hasteBonus = Math.min(2, (rebirthLevel - 3) / 3);    // 0-2 amplifier
        int luckBonus = Math.min(2, (rebirthLevel - 4) / 3);     // 0-2 amplifier
        
        // Apply potion effects
        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, BLESSING_DURATION, hasteBonus, false, true, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, BLESSING_DURATION, luckBonus, false, true, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, BLESSING_DURATION, strengthBonus, false, true, true));
        
        // Add any other blessing effects (this is a placeholder for more complex blessings)
        long blessingEndTime = System.currentTimeMillis() + BLESSING_DURATION * 50;
        plugin.getProgressionModule().getRebirthManager().addBlessingEffect(target.getUniqueId(), blessingEndTime);
        
        // Add temporary experience boost
        double expBoost = 0.1 + (rebirthLevel * 0.05); // 15-50% boost based on rebirth level
        plugin.getProgressionModule().getRebirthManager().setPlayerExpBoost(target.getUniqueId(), expBoost, BLESSING_DURATION * 50);
        
        // Apply visual effects
        blesser.getWorld().strikeLightningEffect(target.getLocation());
        target.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, target.getLocation().add(0, 1, 0), 
                100, 0.5, 1.0, 0.5, 0.2);
        
        // Play sound effects
        target.playSound(target.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
        blesser.playSound(blesser.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.7f, 1.2f);
        
        // Send messages
        target.sendMessage(ChatColor.LIGHT_PURPLE + "✧✧✧ " + ChatColor.GOLD + "You have been blessed by " + 
                blesser.getName() + ChatColor.LIGHT_PURPLE + " ✧✧✧");
        target.sendMessage(ChatColor.GOLD + "You feel invigorated and more productive!");
        target.sendMessage(ChatColor.YELLOW + "• Mining/farming speed increased");
        target.sendMessage(ChatColor.YELLOW + "• Luck increased");
        target.sendMessage(ChatColor.YELLOW + "• Strength increased");
        target.sendMessage(ChatColor.YELLOW + "• Experience gain +" + (int)(expBoost * 100) + "%");
        target.sendMessage(ChatColor.GRAY + "These effects will last for 20 minutes.");
        
        blesser.sendMessage(ChatColor.LIGHT_PURPLE + "You have blessed " + ChatColor.GOLD + target.getName() + 
                ChatColor.LIGHT_PURPLE + " with your divine favor.");
        blesser.sendMessage(ChatColor.GRAY + "You can bless another player in 24 hours.");
    }
}