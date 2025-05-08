package com.bdcraft.plugin.modules.vital.message;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
 * Manages messages and mail.
 */
public class MessageManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final File mailFile;
    private FileConfiguration mailConfig;
    
    // Maps player UUIDs to their last message sender
    private final Map<UUID, UUID> lastMessageSenders;
    
    // Maps player names to their mail
    private final Map<String, List<Mail>> playerMail;
    
    // Maximum number of mail messages a player can have
    private static final int MAX_MAIL = 10;
    
    /**
     * Creates a new message manager.
     * @param plugin The plugin instance
     */
    public MessageManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.mailFile = new File(plugin.getDataFolder(), "mail.yml");
        this.lastMessageSenders = new HashMap<>();
        this.playerMail = new HashMap<>();
        
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
        
        // Load mail
        loadMail();
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
     * Sends a message to a player.
     * @param sender The sender
     * @param recipient The recipient
     * @param message The message
     */
    public void sendMessage(Player sender, Player recipient, String message) {
        // Store last message sender
        lastMessageSenders.put(recipient.getUniqueId(), sender.getUniqueId());
        lastMessageSenders.put(sender.getUniqueId(), recipient.getUniqueId());
        
        // Format message
        String formattedMessage = ChatColor.GRAY + "[" + ChatColor.GREEN + sender.getName() + 
                ChatColor.GRAY + " -> " + ChatColor.GREEN + "You" + ChatColor.GRAY + "] " + 
                ChatColor.WHITE + message;
        
        // Send to recipient
        recipient.sendMessage(formattedMessage);
        
        // Send confirmation to sender
        formattedMessage = ChatColor.GRAY + "[" + ChatColor.GREEN + "You" + 
                ChatColor.GRAY + " -> " + ChatColor.GREEN + recipient.getName() + 
                ChatColor.GRAY + "] " + ChatColor.WHITE + message;
        
        sender.sendMessage(formattedMessage);
    }
    
    /**
     * Gets the last message sender for a player.
     * @param playerUuid The player UUID
     * @return The last message sender UUID
     */
    public UUID getLastMessageSender(UUID playerUuid) {
        return lastMessageSenders.get(playerUuid);
    }
    
    /**
     * Sends mail to a player.
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