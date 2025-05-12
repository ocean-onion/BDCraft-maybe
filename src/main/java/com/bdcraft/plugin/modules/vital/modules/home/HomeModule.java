package com.bdcraft.plugin.modules.vital.modules.home;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player homes as a submodule of BDVital.
 */
public class HomeModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Home settings
    private int maxHomes;
    private int homeCooldown;
    private boolean homePermissionBased;
    
    // Player data
    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();
    private final Map<UUID, Long> homeCooldowns = new HashMap<>();
    
    /**
     * Creates a new home module.
     * 
     * @param plugin The plugin instance
     */
    public HomeModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Home";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Home submodule");
        
        // Load config
        loadConfig();
        
        // Load homes
        loadHomes();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling Home submodule");
        
        // Save homes
        saveHomes();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
        loadHomes();
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
        maxHomes = config.getInt("homes.max", 3);
        homeCooldown = config.getInt("homes.cooldown", 60);
        homePermissionBased = config.getBoolean("homes.permission-based", false);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register home command
        PluginCommand homeCommand = plugin.getCommand("bdhome");
        if (homeCommand != null) {
            homeCommand.setExecutor(this);
        }
        
        // Register sethome command
        PluginCommand sethomeCommand = plugin.getCommand("bdsethome");
        if (sethomeCommand != null) {
            sethomeCommand.setExecutor(this);
        }
        
        // Register delhome command
        PluginCommand delhomeCommand = plugin.getCommand("bddelhome");
        if (delhomeCommand != null) {
            delhomeCommand.setExecutor(this);
        }
    }
    
    /**
     * Loads player homes.
     */
    private void loadHomes() {
        // Clear existing homes
        playerHomes.clear();
        
        // TODO: Implement loading homes from database or file
    }
    
    /**
     * Saves player homes.
     */
    private void saveHomes() {
        // TODO: Implement saving homes to database or file
    }
    
    /**
     * Gets the maximum number of homes a player can have.
     * 
     * @param player The player
     * @return The maximum number of homes
     */
    public int getMaxHomes(Player player) {
        if (!homePermissionBased) {
            return maxHomes;
        }
        
        // Check for permission-based home limits
        for (int i = 50; i > 0; i--) {
            if (player.hasPermission("bdvital.homes." + i)) {
                return i;
            }
        }
        
        return maxHomes;
    }
    
    /**
     * Gets a player's homes.
     * 
     * @param playerId The player's UUID
     * @return The player's homes
     */
    public Map<String, Location> getPlayerHomes(UUID playerId) {
        return playerHomes.computeIfAbsent(playerId, k -> new HashMap<>());
    }
    
    /**
     * Sets a player's home.
     * 
     * @param playerId The player's UUID
     * @param homeName The home name
     * @param location The location
     */
    public void setPlayerHome(UUID playerId, String homeName, Location location) {
        Map<String, Location> homes = getPlayerHomes(playerId);
        homes.put(homeName.toLowerCase(), location);
    }
    
    /**
     * Removes a player's home.
     * 
     * @param playerId The player's UUID
     * @param homeName The home name
     * @return Whether the home was removed
     */
    public boolean removePlayerHome(UUID playerId, String homeName) {
        Map<String, Location> homes = getPlayerHomes(playerId);
        return homes.remove(homeName.toLowerCase()) != null;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdhome")) {
            handleHomeCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdsethome")) {
            handleSetHomeCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bddelhome")) {
            handleDelHomeCommand(player, args);
            return true;
        }
        
        return false;
    }
    
    /**
     * Handles the home command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleHomeCommand(Player player, String[] args) {
        String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
        Map<String, Location> homes = getPlayerHomes(player.getUniqueId());
        
        if (!homes.containsKey(homeName)) {
            player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found.");
            return;
        }
        
        // Check cooldown
        if (homeCooldowns.containsKey(player.getUniqueId())) {
            long lastTeleport = homeCooldowns.get(player.getUniqueId());
            long now = System.currentTimeMillis();
            long cooldownTime = homeCooldown * 1000L;
            
            if (now - lastTeleport < cooldownTime && !player.hasPermission("bdvital.home.bypass-cooldown")) {
                long remainingSeconds = (cooldownTime - (now - lastTeleport)) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before teleporting home.");
                return;
            }
        }
        
        // Teleport player
        Location homeLocation = homes.get(homeName);
        player.teleport(homeLocation);
        player.sendMessage(ChatColor.GREEN + "Teleported to home '" + homeName + "'.");
        
        // Set cooldown
        homeCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Handles the sethome command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleSetHomeCommand(Player player, String[] args) {
        String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
        Map<String, Location> homes = getPlayerHomes(player.getUniqueId());
        
        // Check if player has reached the maximum number of homes
        int maxHomes = getMaxHomes(player);
        if (homes.size() >= maxHomes && !homes.containsKey(homeName)) {
            player.sendMessage(ChatColor.RED + "You have reached the maximum number of homes (" + maxHomes + ").");
            return;
        }
        
        // Set home
        setPlayerHome(player.getUniqueId(), homeName, player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' set.");
    }
    
    /**
     * Handles the delhome command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleDelHomeCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bddelhome <name>");
            return;
        }
        
        String homeName = args[0].toLowerCase();
        
        if (removePlayerHome(player.getUniqueId(), homeName)) {
            player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' deleted.");
        } else {
            player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found.");
        }
    }
}