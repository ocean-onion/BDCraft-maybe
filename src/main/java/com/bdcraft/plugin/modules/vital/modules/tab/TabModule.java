package com.bdcraft.plugin.modules.vital.modules.tab;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.progression.ProgressionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Manages player tab list and related features as a submodule of BDVital.
 */
public class TabModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Tab settings
    private boolean tabFormattingEnabled;
    private String tabNameFormat;
    private boolean rankInTab;
    private final Map<UUID, String> customTabNames = new HashMap<>();
    private Scoreboard scoreboard;
    
    /**
     * Creates a new tab module.
     * 
     * @param plugin The plugin instance
     */
    public TabModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Tab";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Tab submodule");
        
        // Load config
        loadConfig();
        
        // Create scoreboard for tab list
        this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        enabled = true;
        
        // Update all online players
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updatePlayerTabName(player);
        }
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling Tab submodule");
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
        
        // Update all online players
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updatePlayerTabName(player);
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig(ConfigType.VITAL);
        tabFormattingEnabled = config.getBoolean("tab.formatting.enabled", true);
        tabNameFormat = config.getString("tab.formatting.format", "&7[&r{rank}&7] {displayname}");
        rankInTab = config.getBoolean("tab.formatting.show-rank", true);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register tab command
        PluginCommand tabCommand = plugin.getCommand("bdtab");
        if (tabCommand != null) {
            tabCommand.setExecutor(this);
        }
    }
    
    /**
     * Updates a player's tab name.
     * 
     * @param player The player
     */
    public void updatePlayerTabName(Player player) {
        if (!tabFormattingEnabled) {
            return;
        }
        
        String formattedName = tabNameFormat;
        
        // Apply placeholders
        formattedName = formattedName.replace("{displayname}", player.getDisplayName());
        formattedName = formattedName.replace("{name}", player.getName());
        
        // Get rank if enabled
        if (rankInTab) {
            String rank = "Player"; // Default rank
            
            try {
                // Try to get rank from ProgressionModule
                ProgressionManager progressionManager = (ProgressionManager) plugin.getModule("Progression");
                if (progressionManager != null) {
                    rank = progressionManager.getPlayerRank(player.getUniqueId()).getName();
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to get player rank for tab formatting: " + e.getMessage());
            }
            
            formattedName = formattedName.replace("{rank}", rank);
        }
        
        // Translate color codes
        formattedName = ChatColor.translateAlternateColorCodes('&', formattedName);
        
        // Store the custom tab name
        customTabNames.put(player.getUniqueId(), formattedName);
        
        // Update the player's tab name using scoreboard teams
        updateScoreboardTeam(player);
    }
    
    /**
     * Updates a player's scoreboard team.
     * 
     * @param player The player
     */
    private void updateScoreboardTeam(Player player) {
        try {
            String teamName = "bdtab_" + player.getName().substring(0, Math.min(14, player.getName().length()));
            
            // Get or create team
            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }
            
            // Set prefix and suffix
            String customName = customTabNames.get(player.getUniqueId());
            if (customName != null) {
                if (customName.length() <= 16) {
                    team.setPrefix(customName);
                    team.setSuffix("");
                } else {
                    team.setPrefix(customName.substring(0, 16));
                    if (customName.length() > 16) {
                        team.setSuffix(customName.substring(16));
                    }
                }
            }
            
            // Add player to team
            team.addEntry(player.getName());
            
            // Set player's scoreboard
            player.setScoreboard(scoreboard);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to update player tab name: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!tabFormattingEnabled) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Update player's tab name
        updatePlayerTabName(player);
    }
    
    /**
     * Sets a custom tab name for a player.
     * 
     * @param player The player
     * @param tabName The tab name
     */
    public void setCustomTabName(Player player, String tabName) {
        customTabNames.put(player.getUniqueId(), tabName);
        updateScoreboardTeam(player);
    }
    
    /**
     * Resets a player's tab name.
     * 
     * @param player The player
     */
    public void resetTabName(Player player) {
        customTabNames.remove(player.getUniqueId());
        updatePlayerTabName(player);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdvital.tab.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdtab <reset|set> [player] [name]");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reset":
                if (args.length < 2) {
                    resetTabName(player);
                    player.sendMessage(ChatColor.GREEN + "Reset your tab name.");
                } else {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                        return true;
                    }
                    
                    resetTabName(target);
                    player.sendMessage(ChatColor.GREEN + "Reset " + target.getName() + "'s tab name.");
                }
                break;
                
            case "set":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /bdtab set <player> <name>");
                    return true;
                }
                
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    name.append(args[i]).append(" ");
                }
                
                setCustomTabName(target, name.toString().trim());
                player.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s tab name.");
                break;
                
            default:
                player.sendMessage(ChatColor.YELLOW + "Usage: /bdtab <reset|set> [player] [name]");
                break;
        }
        
        return true;
    }
}