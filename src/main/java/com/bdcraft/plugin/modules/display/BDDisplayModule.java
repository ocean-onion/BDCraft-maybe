package com.bdcraft.plugin.modules.display;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.display.command.ScoreboardCommand;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.progression.BDProgressionModule;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Module for managing display elements such as scoreboards.
 */
public class BDDisplayModule extends BDModule {
    private final BDEconomyModule economyModule;
    private final BDProgressionModule progressionModule;
    private BDScoreboardManager scoreboardManager;
    
    /**
     * Creates a new display module.
     *
     * @param plugin The plugin instance
     * @param economyModule The economy module
     * @param progressionModule The progression module
     */
    public BDDisplayModule(BDCraft plugin, BDEconomyModule economyModule, BDProgressionModule progressionModule) {
        super(plugin, "display");
        this.economyModule = economyModule;
        this.progressionModule = progressionModule;
    }
    
    @Override
    public void onEnable() {
        // Initialize scoreboard manager
        scoreboardManager = new BDScoreboardManager(
                plugin, 
                economyModule, 
                progressionModule, 
                economyModule.getMarketManager()
        );
        
        // Register commands
        ScoreboardCommand scoreboardCommand = new ScoreboardCommand(plugin, scoreboardManager);
        plugin.getCommand("bdscoreboard").setExecutor(scoreboardCommand);
        plugin.getCommand("bdscoreboard").setTabCompleter(scoreboardCommand);
        
        plugin.getLogger().info("Display module enabled.");
    }
    
    @Override
    public void onDisable() {
        // Clean up scoreboard manager
        if (scoreboardManager != null) {
            scoreboardManager.disable();
        }
        
        plugin.getLogger().info("Display module disabled.");
    }
    
    /**
     * Gets the scoreboard manager.
     *
     * @return The scoreboard manager
     */
    public BDScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    
    @Override
    public void reload() {
        // Reload scoreboard manager
        if (scoreboardManager != null) {
            scoreboardManager.disable();
            
            // Recreate the scoreboard manager
            scoreboardManager = new BDScoreboardManager(
                    plugin, 
                    economyModule, 
                    progressionModule, 
                    economyModule.getMarketManager()
            );
        }
        
        plugin.getLogger().info("Display module reloaded.");
    }
}