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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages player messaging as a submodule of BDVital.
 */
public class MessageModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Message settings
    private String messageFormat;
    private boolean allowFormatting;
    private boolean enableSocialSpy;
    
    // File storage
    private final File mailFile;
    private FileConfiguration mailConfig;
    
    // Player data
    private final Map<UUID, UUID> lastMessageSenders = new HashMap<>();
    private final Map<UUID, Boolean> socialSpyEnabled = new HashMap<>();
    private final Map<UUID, Boolean> messagingToggled = new HashMap<>();
    
    // Mail data
    private final Map<String, List<Mail>> playerMail = new HashMap<>();
    
    // Maximum number of mail messages a player can have
    private static final int MAX_MAIL = 10;
    
    /**
     * Creates a new message module.
     * 
     * @param plugin The plugin instance
     */
    public MessageModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.mailFile = new File(plugin.getDataFolder(), "mail.yml");
        
        // Ensure file exists
        if (!mailFile.exists()) {
            try {
                if (mailFile.createNewFile()) {
                    logger.info("Created mail.yml file");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not create mail.yml file", e);
            }
        }
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
        
        // Load mail
        loadMail();
        
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
        
        // Save mail
        saveMail();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
        loadMail();
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
     * Loads mail from file.
     */
    public void loadMail() {
        // Clear cache
        playerMail.clear();
        
        // Load config
        mailConfig = YamlConfiguration.loadConfiguration(mailFile);
        
        // Check for mail section
        if (!mailConfig.contains("mail")) {
            return;
        }
        
        // Load player mail
        ConfigurationSection mailSection = mailConfig.getConfigurationSection("mail");
        
        for (String playerName : mailSection.getKeys(false)) {
            List<Mail> mails = new ArrayList<>();
            ConfigurationSection playerSection = mailSection.getConfigurationSection(playerName);
            
            for (String mailId : playerSection.getKeys(false)) {
                String sender = playerSection.getString(mailId + ".sender");
                String message = playerSection.getString(mailId + ".message");
                long timestamp = playerSection.getLong(mailId + ".timestamp");
                
                mails.add(new Mail(sender, message, timestamp));
            }
            
            playerMail.put(playerName.toLowerCase(), mails);
        }
        
        logger.info("Loaded mail for " + playerMail.size() + " players");
    }
    
    /**
     * Saves mail to file.
     */
    public void saveMail() {
        // Clear existing mail
        mailConfig.set("mail", null);
        
        // Save player mail
        for (String playerName : playerMail.keySet()) {
            List<Mail> mails = playerMail.get(playerName);
            
            for (int i = 0; i < mails.size(); i++) {
                Mail mail = mails.get(i);
                
                mailConfig.set("mail." + playerName + "." + i + ".sender", mail.getSender());
                mailConfig.set("mail." + playerName + "." + i + ".message", mail.getMessage());
                mailConfig.set("mail." + playerName + "." + i + ".timestamp", mail.getTimestamp());
            }
        }
        
        // Save config
        try {
            mailConfig.save(mailFile);
            logger.info("Saved mail for " + playerMail.size() + " players");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save mail.yml file", e);
        }
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
        
        // Register mail command
        PluginCommand mailCommand = plugin.getCommand("bdmail");
        if (mailCommand != null) {
            mailCommand.setExecutor(this);
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
        lastMessageSenders.put(sender.getUniqueId(), receiver.getUniqueId());
        
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
    
    /**
     * Gets the last message sender for a player.
     * 
     * @param playerUuid The player UUID
     * @return The last message sender UUID
     */
    public UUID getLastMessageSender(UUID playerUuid) {
        return lastMessageSenders.get(playerUuid);
    }
    
    /**
     * Sends mail to a player.
     * 
     * @param sender The sender
     * @param recipient The recipient
     * @param message The message
     * @return Whether the mail was sent
     */
    public boolean sendMail(String sender, String recipient, String message) {
        // Normalize names
        sender = sender.toLowerCase();
        recipient = recipient.toLowerCase();
        
        // Check recipient mail count
        List<Mail> recipientMail = playerMail.computeIfAbsent(recipient, k -> new ArrayList<>());
        
        if (recipientMail.size() >= MAX_MAIL) {
            return false;
        }
        
        // Create mail
        Mail mail = new Mail(sender, message, System.currentTimeMillis());
        recipientMail.add(mail);
        
        // Save mail
        saveMail();
        
        return true;
    }
    
    /**
     * Gets mail for a player.
     * 
     * @param playerName The player name
     * @return The mail
     */
    public List<Mail> getMail(String playerName) {
        // Normalize name
        playerName = playerName.toLowerCase();
        
        // Get mail
        List<Mail> mail = playerMail.get(playerName);
        
        if (mail == null) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(mail);
    }
    
    /**
     * Clears mail for a player.
     * 
     * @param playerName The player name
     */
    public void clearMail(String playerName) {
        // Normalize name
        playerName = playerName.toLowerCase();
        
        // Clear mail
        playerMail.remove(playerName);
        
        // Save mail
        saveMail();
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
        } else if (command.getName().equalsIgnoreCase("bdmail")) {
            handleMailCommand(player, args);
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
     * Handles the mail command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleMailCommand(Player player, String[] args) {
        if (args.length == 0) {
            // Show mail
            List<Mail> mail = getMail(player.getName());
            
            if (mail.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + "You have no mail.");
                return;
            }
            
            player.sendMessage(ChatColor.YELLOW + "You have " + mail.size() + " mail messages:");
            
            for (int i = 0; i < mail.size(); i++) {
                Mail m = mail.get(i);
                player.sendMessage(ChatColor.YELLOW + (i + 1) + ". " + ChatColor.WHITE + "From: " + 
                        m.getSender() + ", Date: " + m.getDate());
                player.sendMessage(ChatColor.GRAY + "  " + m.getMessage());
            }
            
            return;
        }
        
        String subCommand = args[0].toLowerCase();
        
        if (subCommand.equals("send")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /bdmail send <player> <message>");
                return;
            }
            
            String recipient = args[1];
            
            // Build message
            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }
            
            // Send mail
            if (sendMail(player.getName(), recipient, message.toString().trim())) {
                player.sendMessage(ChatColor.GREEN + "Mail sent to " + recipient + ".");
            } else {
                player.sendMessage(ChatColor.RED + recipient + "'s mailbox is full.");
            }
        } else if (subCommand.equals("clear")) {
            clearMail(player.getName());
            player.sendMessage(ChatColor.GREEN + "Your mailbox has been cleared.");
        } else {
            player.sendMessage(ChatColor.RED + "Unknown subcommand. Use /bdmail, /bdmail send, or /bdmail clear.");
        }
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check for mail
        List<Mail> mail = getMail(player.getName());
        
        if (!mail.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You have " + mail.size() + " unread mail messages. Type /bdmail to read them.");
        }
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
    
    /**
     * Mail class.
     */
    public static class Mail {
        private final String sender;
        private final String message;
        private final long timestamp;
        
        /**
         * Creates a new mail.
         * @param sender The sender
         * @param message The message
         * @param timestamp The timestamp
         */
        public Mail(String sender, String message, long timestamp) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        /**
         * Gets the sender.
         * @return The sender
         */
        public String getSender() {
            return sender;
        }
        
        /**
         * Gets the message.
         * @return The message
         */
        public String getMessage() {
            return message;
        }
        
        /**
         * Gets the timestamp.
         * @return The timestamp
         */
        public long getTimestamp() {
            return timestamp;
        }
        
        /**
         * Gets the date.
         * @return The date
         */
        public Date getDate() {
            return new Date(timestamp);
        }
    }
}