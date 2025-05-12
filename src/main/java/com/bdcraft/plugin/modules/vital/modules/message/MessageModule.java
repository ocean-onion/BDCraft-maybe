package com.bdcraft.plugin.modules.vital.modules.message;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player messaging as a submodule of BDVital.
 */
public class MessageModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Message settings
    private String messageFormat;
    private boolean allowFormatting;
    private boolean enableSocialSpy;
    
    // Player data
    private final Map<UUID, UUID> lastMessageSenders = new HashMap<>();
    private final Map<UUID, Boolean> socialSpyEnabled = new HashMap<>();
    private final Map<UUID, Boolean> messagingToggled = new HashMap<>();
    
    /**
     * Creates a new message module.
     * 
     * @param plugin The plugin instance
     */
    public MessageModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Message";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Message submodule");
        
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
        
        plugin.getLogger().info("Disabling Message submodule");
        
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
        messageFormat = config.getString("message.format", "&7[&r{sender} &7-> &r{receiver}&7] &f{message}");
        allowFormatting = config.getBoolean("message.allow-formatting", true);
        enableSocialSpy = config.getBoolean("message.social-spy.enabled", true);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register msg/tell/w command
        PluginCommand msgCommand = plugin.getCommand("bdmsg");
        if (msgCommand != null) {
            msgCommand.setExecutor(this);
        }
        
        // Register r command
        PluginCommand rCommand = plugin.getCommand("bdr");
        if (rCommand != null) {
            rCommand.setExecutor(this);
        }
        
        // Register socialspy command
        PluginCommand socialspyCommand = plugin.getCommand("bdsocialspy");
        if (socialspyCommand != null) {
            socialspyCommand.setExecutor(this);
        }
        
        // Register msgtoggle command
        PluginCommand msgtoggleCommand = plugin.getCommand("bdmsgtoggle");
        if (msgtoggleCommand != null) {
            msgtoggleCommand.setExecutor(this);
        }
    }
    
    /**
     * Checks if a player has messaging toggled off.
     * 
     * @param playerId The player's UUID
     * @return Whether messaging is toggled off
     */
    public boolean isMessagingToggledOff(UUID playerId) {
        return messagingToggled.getOrDefault(playerId, false);
    }
    
    /**
     * Toggles messaging for a player.
     * 
     * @param playerId The player's UUID
     * @return The new state
     */
    public boolean toggleMessaging(UUID playerId) {
        boolean current = isMessagingToggledOff(playerId);
        messagingToggled.put(playerId, !current);
        return !current;
    }
    
    /**
     * Checks if a player has social spy enabled.
     * 
     * @param playerId The player's UUID
     * @return Whether social spy is enabled
     */
    public boolean hasSocialSpyEnabled(UUID playerId) {
        return socialSpyEnabled.getOrDefault(playerId, false);
    }
    
    /**
     * Toggles social spy for a player.
     * 
     * @param playerId The player's UUID
     * @return The new state
     */
    public boolean toggleSocialSpy(UUID playerId) {
        boolean current = hasSocialSpyEnabled(playerId);
        socialSpyEnabled.put(playerId, !current);
        return !current;
    }
    
    /**
     * Sends a private message from one player to another.
     * 
     * @param sender The sender
     * @param receiver The receiver
     * @param message The message
     */
    public void sendPrivateMessage(Player sender, Player receiver, String message) {
        // Format message
        String formattedMessage = messageFormat
                .replace("{sender}", sender.getDisplayName())
                .replace("{receiver}", receiver.getDisplayName())
                .replace("{message}", allowFormatting ? ChatColor.translateAlternateColorCodes('&', message) : message);
        
        // Translate color codes in the format itself
        formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        
        // Send to sender and receiver
        sender.sendMessage(formattedMessage);
        receiver.sendMessage(formattedMessage);
        
        // Update last message sender for reply
        lastMessageSenders.put(receiver.getUniqueId(), sender.getUniqueId());
        
        // Send to social spy
        if (enableSocialSpy) {
            String spyMessage = ChatColor.GRAY + "[SPY] " + formattedMessage;
            
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.equals(sender) || player.equals(receiver)) {
                    continue;
                }
                
                if (hasSocialSpyEnabled(player.getUniqueId()) && player.hasPermission("bdvital.message.socialspy")) {
                    player.sendMessage(spyMessage);
                }
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdmsg")) {
            handleMsgCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdr")) {
            handleReplyCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdsocialspy")) {
            handleSocialSpyCommand(player);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdmsgtoggle")) {
            handleMsgToggleCommand(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Handles the msg command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleMsgCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /bdmsg <player> <message>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot message yourself.");
            return;
        }
        
        // Check if player has messaging toggled off
        if (isMessagingToggledOff(target.getUniqueId()) && !player.hasPermission("bdvital.message.bypass-toggle")) {
            player.sendMessage(ChatColor.RED + target.getName() + " has private messaging disabled.");
            return;
        }
        
        // Build message
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        
        // Send message
        sendPrivateMessage(player, target, message.toString().trim());
    }
    
    /**
     * Handles the reply command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleReplyCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bdr <message>");
            return;
        }
        
        if (!lastMessageSenders.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have nobody to reply to.");
            return;
        }
        
        UUID lastSenderId = lastMessageSenders.get(player.getUniqueId());
        Player lastSender = plugin.getServer().getPlayer(lastSenderId);
        
        if (lastSender == null || !lastSender.isOnline()) {
            player.sendMessage(ChatColor.RED + "That player is no longer online.");
            return;
        }
        
        // Check if player has messaging toggled off
        if (isMessagingToggledOff(lastSender.getUniqueId()) && !player.hasPermission("bdvital.message.bypass-toggle")) {
            player.sendMessage(ChatColor.RED + lastSender.getName() + " has private messaging disabled.");
            return;
        }
        
        // Build message
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        
        // Send message
        sendPrivateMessage(player, lastSender, message.toString().trim());
    }
    
    /**
     * Handles the socialspy command.
     * 
     * @param player The player
     */
    private void handleSocialSpyCommand(Player player) {
        if (!player.hasPermission("bdvital.message.socialspy")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use social spy.");
            return;
        }
        
        boolean enabled = toggleSocialSpy(player.getUniqueId());
        
        if (enabled) {
            player.sendMessage(ChatColor.GREEN + "Social spy enabled.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Social spy disabled.");
        }
    }
    
    /**
     * Handles the msgtoggle command.
     * 
     * @param player The player
     */
    private void handleMsgToggleCommand(Player player) {
        boolean toggled = toggleMessaging(player.getUniqueId());
        
        if (toggled) {
            player.sendMessage(ChatColor.GREEN + "Private messaging disabled.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Private messaging enabled.");
        }
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Nothing to do here for now
    }
    
    /**
     * Handles player quit events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        
        // Clean up last message senders
        lastMessageSenders.entrySet().removeIf(entry -> 
            entry.getKey().equals(playerId) || entry.getValue().equals(playerId)
        );
    }
}