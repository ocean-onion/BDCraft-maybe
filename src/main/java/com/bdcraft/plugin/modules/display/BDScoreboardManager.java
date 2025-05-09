package com.bdcraft.plugin.modules.display;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import com.bdcraft.plugin.modules.progression.BDProgressionModule;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player scoreboards.
 */
public class BDScoreboardManager implements Listener {
    private final BDCraft plugin;
    private final BDEconomyModule economyModule;
    private final BDProgressionModule progressionModule;
    private final BDMarketManager marketManager;
    
    private final Map<UUID, Scoreboard> playerScoreboards;
    private BukkitTask updateTask;
    
    // Scoreboard configuration
    private final String title;
    private int updateInterval; // Will be loaded from config
    
    /**
     * Creates a new scoreboard manager.
     *
     * @param plugin The plugin instance
     * @param economyModule The economy module
     * @param progressionModule The progression module
     * @param marketManager The market manager
     */
    public BDScoreboardManager(BDCraft plugin, BDEconomyModule economyModule, 
                               BDProgressionModule progressionModule, BDMarketManager marketManager) {
        this.plugin = plugin;
        this.economyModule = economyModule;
        this.progressionModule = progressionModule;
        this.marketManager = marketManager;
        this.playerScoreboards = new HashMap<>();
        
        // Get scoreboard title from config
        String configTitle = plugin.getConfig().getString("display.scoreboard.title", "&6&lBD Economy");
        this.title = ChatColor.translateAlternateColorCodes('&', configTitle);
        
        // Get update interval from config
        this.updateInterval = plugin.getConfig().getInt("display.scoreboard.update-interval", 100);
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Start scoreboard update task
        startUpdateTask();
    }
    
    /**
     * Starts the scoreboard update task.
     */
    private void startUpdateTask() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateAllScoreboards, 20L, updateInterval);
    }
    
    /**
     * Creates and sets up a scoreboard for a player.
     *
     * @param player The player
     */
    public void setupScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("bdinfo", "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        playerScoreboards.put(player.getUniqueId(), scoreboard);
        updateScoreboard(player);
    }
    
    /**
     * Updates a player's scoreboard with current data.
     *
     * @param player The player
     */
    public void updateScoreboard(Player player) {
        if (!player.isOnline()) {
            return;
        }
        
        Scoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) {
            setupScoreboard(player);
            scoreboard = playerScoreboards.get(player.getUniqueId());
        }
        
        Objective objective = scoreboard.getObjective("bdinfo");
        if (objective == null) {
            return;
        }
        
        // Clear existing scores
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        
        // Get player data
        int balance = economyModule.getPlayerBalance(player);
        String rankName = progressionModule.getPlayerRankName(player);
        int rebirthLevel = progressionModule.getPlayerRebirthLevel(player);
        int marketsOwned = marketManager.getPlayerMarkets(player).size();
        
        // Add scores
        objective.getScore(ChatColor.WHITE + "").setScore(10);
        objective.getScore(ChatColor.YELLOW + "Balance: " + ChatColor.GREEN + balance + " BD").setScore(9);
        objective.getScore(ChatColor.WHITE + " ").setScore(8);
        objective.getScore(ChatColor.YELLOW + "Rank: " + ChatColor.AQUA + rankName).setScore(7);
        
        if (rebirthLevel > 0) {
            objective.getScore(ChatColor.YELLOW + "Rebirth: " + ChatColor.LIGHT_PURPLE + "Level " + rebirthLevel).setScore(6);
            objective.getScore(ChatColor.WHITE + "  ").setScore(5);
        } else {
            objective.getScore(ChatColor.WHITE + "  ").setScore(6);
        }
        
        objective.getScore(ChatColor.YELLOW + "Markets: " + ChatColor.GOLD + marketsOwned).setScore(4);
        objective.getScore(ChatColor.WHITE + "   ").setScore(3);
        objective.getScore(ChatColor.GREEN + "bdcraft.com").setScore(2);
        
        // Set the scoreboard for the player
        player.setScoreboard(scoreboard);
    }
    
    /**
     * Updates all player scoreboards.
     */
    public void updateAllScoreboards() {
        for (UUID uuid : playerScoreboards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                updateScoreboard(player);
            }
        }
    }
    
    /**
     * Removes a player's scoreboard.
     *
     * @param player The player
     */
    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        
        // Reset to main server scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        player.setScoreboard(manager.getMainScoreboard());
    }
    
    /**
     * Checks if a player has a scoreboard.
     *
     * @param player The player
     * @return True if the player has a scoreboard
     */
    public boolean hasScoreboard(Player player) {
        return playerScoreboards.containsKey(player.getUniqueId());
    }
    
    /**
     * Handles player join events.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Set up the scoreboard after a short delay to ensure all data is loaded
        Bukkit.getScheduler().runTaskLater(plugin, () -> setupScoreboard(player), 20L);
    }
    
    /**
     * Handles player quit events.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeScoreboard(player);
    }
    
    /**
     * Disables the scoreboard manager.
     */
    public void disable() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
        
        // Reset all player scoreboards
        for (UUID uuid : playerScoreboards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                removeScoreboard(player);
            }
        }
        
        playerScoreboards.clear();
    }
}