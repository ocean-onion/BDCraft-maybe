package com.bdcraft.plugin.modules.vital.modules.teleport;

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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages teleportation as a submodule of BDVital.
 */
public class TeleportModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Teleport settings
    private int tpaCooldown;
    private int tpaTimeout;
    private int backCooldown;
    private boolean enableBack;
    
    // Player data
    private final Map<UUID, UUID> tpaRequests = new HashMap<>();
    private final Map<UUID, Long> tpaRequestTimes = new HashMap<>();
    private final Map<UUID, BukkitTask> tpaTimeoutTasks = new HashMap<>();
    private final Map<UUID, Long> tpaCooldowns = new HashMap<>();
    private final Map<UUID, Location> lastLocations = new HashMap<>();
    private final Map<UUID, Long> backCooldowns = new HashMap<>();
    
    /**
     * Creates a new teleport module.
     * 
     * @param plugin The plugin instance
     */
    public TeleportModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Teleport";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Teleport submodule");
        
        // Load config
        loadConfig();
        
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
        
        plugin.getLogger().info("Disabling Teleport submodule");
        
        // Cancel all timeout tasks
        for (BukkitTask task : tpaTimeoutTasks.values()) {
            task.cancel();
        }
        tpaTimeoutTasks.clear();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
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
        tpaCooldown = config.getInt("teleport.tpa.cooldown", 60);
        tpaTimeout = config.getInt("teleport.tpa.timeout", 60);
        backCooldown = config.getInt("teleport.back.cooldown", 60);
        enableBack = config.getBoolean("teleport.back.enabled", true);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register tpa command
        PluginCommand tpaCommand = plugin.getCommand("bdtpa");
        if (tpaCommand != null) {
            tpaCommand.setExecutor(this);
        }
        
        // Register tpaccept command
        PluginCommand tpacceptCommand = plugin.getCommand("bdtpaccept");
        if (tpacceptCommand != null) {
            tpacceptCommand.setExecutor(this);
        }
        
        // Register tpdeny command
        PluginCommand tpdenyCommand = plugin.getCommand("bdtpdeny");
        if (tpdenyCommand != null) {
            tpdenyCommand.setExecutor(this);
        }
        
        // Register back command
        PluginCommand backCommand = plugin.getCommand("bdback");
        if (backCommand != null) {
            backCommand.setExecutor(this);
        }
    }
    
    /**
     * Records a player's last location before teleporting.
     * 
     * @param player The player
     */
    public void recordLastLocation(Player player) {
        if (enableBack && player.hasPermission("bdvital.teleport.back")) {
            lastLocations.put(player.getUniqueId(), player.getLocation());
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdtpa")) {
            handleTpaCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdtpaccept")) {
            handleTpAcceptCommand(player);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdtpdeny")) {
            handleTpDenyCommand(player);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdback")) {
            handleBackCommand(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Handles the tpa command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleTpaCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bdtpa <player>");
            return;
        }
        
        // Check cooldown
        if (tpaCooldowns.containsKey(player.getUniqueId())) {
            long lastTpa = tpaCooldowns.get(player.getUniqueId());
            long now = System.currentTimeMillis();
            long cooldownTime = tpaCooldown * 1000L;
            
            if (now - lastTpa < cooldownTime && !player.hasPermission("bdvital.teleport.bypass-cooldown")) {
                long remainingSeconds = (cooldownTime - (now - lastTpa)) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before sending another teleport request.");
                return;
            }
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot teleport to yourself.");
            return;
        }
        
        // Send request
        tpaRequests.put(target.getUniqueId(), player.getUniqueId());
        tpaRequestTimes.put(target.getUniqueId(), System.currentTimeMillis());
        
        // Set timeout
        BukkitTask timeoutTask = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (tpaRequests.containsKey(target.getUniqueId()) && tpaRequests.get(target.getUniqueId()).equals(player.getUniqueId())) {
                tpaRequests.remove(target.getUniqueId());
                tpaRequestTimes.remove(target.getUniqueId());
                
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Your teleport request to " + target.getName() + " has expired.");
                }
                
                if (target.isOnline()) {
                    target.sendMessage(ChatColor.RED + "Teleport request from " + player.getName() + " has expired.");
                }
            }
            
            tpaTimeoutTasks.remove(target.getUniqueId());
        }, tpaTimeout * 20L);
        
        tpaTimeoutTasks.put(target.getUniqueId(), timeoutTask);
        
        // Set cooldown
        tpaCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        
        // Send messages
        player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName() + ".");
        target.sendMessage(ChatColor.YELLOW + player.getName() + " has requested to teleport to you.");
        target.sendMessage(ChatColor.YELLOW + "Type /bdtpaccept to accept or /bdtpdeny to deny.");
    }
    
    /**
     * Handles the tpaccept command.
     * 
     * @param player The player
     */
    private void handleTpAcceptCommand(Player player) {
        if (!tpaRequests.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return;
        }
        
        UUID requesterId = tpaRequests.get(player.getUniqueId());
        Player requester = plugin.getServer().getPlayer(requesterId);
        
        if (requester == null || !requester.isOnline()) {
            player.sendMessage(ChatColor.RED + "The player who requested to teleport is no longer online.");
            tpaRequests.remove(player.getUniqueId());
            tpaRequestTimes.remove(player.getUniqueId());
            
            if (tpaTimeoutTasks.containsKey(player.getUniqueId())) {
                tpaTimeoutTasks.get(player.getUniqueId()).cancel();
                tpaTimeoutTasks.remove(player.getUniqueId());
            }
            
            return;
        }
        
        // Record last location for the requester
        recordLastLocation(requester);
        
        // Teleport requester to player
        requester.teleport(player.getLocation());
        
        // Send messages
        player.sendMessage(ChatColor.GREEN + "Teleport request accepted.");
        requester.sendMessage(ChatColor.GREEN + player.getName() + " accepted your teleport request.");
        
        // Clean up
        tpaRequests.remove(player.getUniqueId());
        tpaRequestTimes.remove(player.getUniqueId());
        
        if (tpaTimeoutTasks.containsKey(player.getUniqueId())) {
            tpaTimeoutTasks.get(player.getUniqueId()).cancel();
            tpaTimeoutTasks.remove(player.getUniqueId());
        }
    }
    
    /**
     * Handles the tpdeny command.
     * 
     * @param player The player
     */
    private void handleTpDenyCommand(Player player) {
        if (!tpaRequests.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return;
        }
        
        UUID requesterId = tpaRequests.get(player.getUniqueId());
        Player requester = plugin.getServer().getPlayer(requesterId);
        
        // Send messages
        player.sendMessage(ChatColor.GREEN + "Teleport request denied.");
        
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(ChatColor.RED + player.getName() + " denied your teleport request.");
        }
        
        // Clean up
        tpaRequests.remove(player.getUniqueId());
        tpaRequestTimes.remove(player.getUniqueId());
        
        if (tpaTimeoutTasks.containsKey(player.getUniqueId())) {
            tpaTimeoutTasks.get(player.getUniqueId()).cancel();
            tpaTimeoutTasks.remove(player.getUniqueId());
        }
    }
    
    /**
     * Handles the back command.
     * 
     * @param player The player
     */
    private void handleBackCommand(Player player) {
        if (!enableBack) {
            player.sendMessage(ChatColor.RED + "The back command is disabled.");
            return;
        }
        
        if (!player.hasPermission("bdvital.teleport.back")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use the back command.");
            return;
        }
        
        if (!lastLocations.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have no previous location to teleport to.");
            return;
        }
        
        // Check cooldown
        if (backCooldowns.containsKey(player.getUniqueId())) {
            long lastBack = backCooldowns.get(player.getUniqueId());
            long now = System.currentTimeMillis();
            long cooldownTime = backCooldown * 1000L;
            
            if (now - lastBack < cooldownTime && !player.hasPermission("bdvital.teleport.bypass-cooldown")) {
                long remainingSeconds = (cooldownTime - (now - lastBack)) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before using the back command again.");
                return;
            }
        }
        
        // Get last location
        Location lastLocation = lastLocations.get(player.getUniqueId());
        
        // Record current location before teleporting
        Location currentLocation = player.getLocation();
        
        // Teleport player
        player.teleport(lastLocation);
        player.sendMessage(ChatColor.GREEN + "Teleported to your previous location.");
        
        // Update last location
        lastLocations.put(player.getUniqueId(), currentLocation);
        
        // Set cooldown
        backCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Clean up any teleport requests to or from this player
        tpaRequests.entrySet().removeIf(entry -> 
            entry.getKey().equals(player.getUniqueId()) || entry.getValue().equals(player.getUniqueId())
        );
        
        tpaRequestTimes.remove(player.getUniqueId());
        
        if (tpaTimeoutTasks.containsKey(player.getUniqueId())) {
            tpaTimeoutTasks.get(player.getUniqueId()).cancel();
            tpaTimeoutTasks.remove(player.getUniqueId());
        }
    }
}