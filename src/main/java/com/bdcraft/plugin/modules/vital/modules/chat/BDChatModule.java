package com.bdcraft.plugin.modules.vital.modules.chat;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.modules.VitalSubmodule;
import com.bdcraft.plugin.modules.progression.ProgressionManager;
import com.bdcraft.plugin.modules.perms.BDPermsModule;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Submodule for chat formatting and features.
 */
public class BDChatModule implements VitalSubmodule, Listener, CommandExecutor {
    private final BDCraft plugin;
    private BDVitalModule vitalModule;
    private boolean enabled = false;
    
    private final Pattern hexColorPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private boolean chatFormattingEnabled;
    private String chatFormat;
    private boolean rankInChat;
    private boolean colorPermEnabled;
    private final Map<String, ChatColor> colorNameMap;
    
    /**
     * Creates a new chat module.
     * 
     * @param plugin The plugin instance
     */
    public BDChatModule(BDCraft plugin) {
        this.plugin = plugin;
        this.colorNameMap = new HashMap<>();
        initColorMap();
    }
    
    @Override
    public String getName() {
        return "Chat";
    }
    
    @Override
    public void enable(BDVitalModule vitalModule) {
        if (enabled) {
            return;
        }
        
        this.vitalModule = vitalModule;
        
        plugin.getLogger().info("Enabling BDChat module");
        
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
        
        plugin.getLogger().info("Disabling BDChat module");
        
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
        chatFormattingEnabled = config.getBoolean("chat.formatting.enabled", true);
        chatFormat = config.getString("chat.formatting.format", "&7[&r{rank}&7] {displayname}&f: {message}");
        rankInChat = config.getBoolean("chat.formatting.show-rank", true);
        colorPermEnabled = config.getBoolean("chat.colors.permission-based", true);
    }
    
    /**
     * Initializes the color name map.
     */
    private void initColorMap() {
        colorNameMap.put("black", ChatColor.BLACK);
        colorNameMap.put("dark_blue", ChatColor.DARK_BLUE);
        colorNameMap.put("dark_green", ChatColor.DARK_GREEN);
        colorNameMap.put("dark_aqua", ChatColor.DARK_AQUA);
        colorNameMap.put("dark_red", ChatColor.DARK_RED);
        colorNameMap.put("dark_purple", ChatColor.DARK_PURPLE);
        colorNameMap.put("gold", ChatColor.GOLD);
        colorNameMap.put("gray", ChatColor.GRAY);
        colorNameMap.put("dark_gray", ChatColor.DARK_GRAY);
        colorNameMap.put("blue", ChatColor.BLUE);
        colorNameMap.put("green", ChatColor.GREEN);
        colorNameMap.put("aqua", ChatColor.AQUA);
        colorNameMap.put("red", ChatColor.RED);
        colorNameMap.put("light_purple", ChatColor.LIGHT_PURPLE);
        colorNameMap.put("yellow", ChatColor.YELLOW);
        colorNameMap.put("white", ChatColor.WHITE);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register color command
        PluginCommand colorCommand = plugin.getCommand("bdcolor");
        if (colorCommand != null) {
            colorCommand.setExecutor(this);
        }
        
        // Register me command
        PluginCommand meCommand = plugin.getCommand("bdme");
        if (meCommand != null) {
            meCommand.setExecutor(this);
        }
    }
    
    /**
     * Translates color codes in a string.
     * 
     * @param message The message
     * @param player The player (for permission checks)
     * @return The colorized message
     */
    public String colorize(String message, Player player) {
        if (message == null) {
            return "";
        }
        
        // Check permission for color codes
        if (colorPermEnabled && !player.hasPermission("bdvital.chat.color")) {
            return message;
        }
        
        // Regular color codes
        message = ChatColor.translateAlternateColorCodes('&', message);
        
        // Hex color codes (for 1.16+)
        Matcher matcher = hexColorPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();
        
        while (matcher.find()) {
            if (!player.hasPermission("bdvital.chat.hex")) {
                continue;
            }
            
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + group).toString());
        }
        
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    
    /**
     * Formats a chat message.
     * 
     * @param player The player
     * @param message The message
     * @return The formatted message
     */
    public String formatChat(Player player, String message) {
        if (!chatFormattingEnabled) {
            return message;
        }
        
        String formatted = chatFormat;
        
        // Apply placeholders
        formatted = formatted.replace("{displayname}", player.getDisplayName());
        formatted = formatted.replace("{name}", player.getName());
        formatted = formatted.replace("{message}", colorize(message, player));
        
        // Get rank if enabled
        if (rankInChat) {
            String rank = "Player"; // Default rank
            
            try {
                // Try to get rank from ProgressionModule
                ProgressionManager progressionManager = (ProgressionManager) plugin.getModule("Progression");
                if (progressionManager != null) {
                    rank = progressionManager.getPlayerRank(player.getUniqueId()).getName();
                }
                
                // If no rank from progression, try to get from perms
                if (rank.equals("Player")) {
                    BDPermsModule permsModule = (BDPermsModule) plugin.getModule("Perms");
                    if (permsModule != null) {
                        String group = permsModule.getPlayerGroup(player);
                        if (group != null && !group.isEmpty()) {
                            rank = group;
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to get player rank for chat formatting: " + e.getMessage());
            }
            
            formatted = formatted.replace("{rank}", rank);
        }
        
        // Translate color codes
        return ChatColor.translateAlternateColorCodes('&', formatted);
    }
    
    /**
     * Handles chat events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!chatFormattingEnabled) {
            return;
        }
        
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        // Format the message
        String formattedMessage = formatChat(player, message);
        
        // Set the formatted message
        event.setFormat(formattedMessage);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdcolor")) {
            return handleColorCommand(player, args);
        } else if (command.getName().equalsIgnoreCase("bdme")) {
            return handleMeCommand(player, args);
        }
        
        return false;
    }
    
    /**
     * Handles the color command.
     * 
     * @param player The player
     * @param args The arguments
     * @return Whether the command was handled
     */
    private boolean handleColorCommand(Player player, String[] args) {
        if (!player.hasPermission("bdvital.chat.color")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use color codes.");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Available colors:");
            StringBuilder colors = new StringBuilder();
            
            for (Map.Entry<String, ChatColor> entry : colorNameMap.entrySet()) {
                colors.append(entry.getValue()).append(entry.getKey()).append(" ");
            }
            
            player.sendMessage(colors.toString());
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdcolor <message>");
            return true;
        }
        
        // Combine args
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        
        // Colorize and send
        String colorized = colorize(message.toString(), player);
        player.chat(colorized);
        
        return true;
    }
    
    /**
     * Handles the me command.
     * 
     * @param player The player
     * @param args The arguments
     * @return Whether the command was handled
     */
    private boolean handleMeCommand(Player player, String[] args) {
        if (!player.hasPermission("bdvital.chat.me")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use the me command.");
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdme <action>");
            return true;
        }
        
        // Combine args
        StringBuilder action = new StringBuilder();
        for (String arg : args) {
            action.append(arg).append(" ");
        }
        
        // Send action message
        plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "* " + player.getDisplayName() + " " + action.toString().trim());
        
        return true;
    }
}