package com.bdcraft.plugin.modules.vital.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Commands for messaging.
 */
public class MessageCommands {
    private final BDCraft plugin;
    private final MessageManager messageManager;
    
    /**
     * Creates new message commands.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     */
    public MessageCommands(BDCraft plugin, BDVitalModule vitalModule) {
        this.plugin = plugin;
        this.messageManager = vitalModule.getMessageManager();
        
        // Register commands
        registerMsgCommand();
        registerReplyCommand();
        registerMailCommand();
    }
    
    /**
     * Registers the /bdmsg command.
     */
    private void registerMsgCommand() {
        plugin.getCommand("bdmsg").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.msg")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Please specify a player and a message.");
                    player.sendMessage(ChatColor.YELLOW + "Usage: /bdmsg <player> <message>");
                    return true;
                }
                
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);
                
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not online.");
                    return true;
                }
                
                // Build message
                StringBuilder message = new StringBuilder();
                
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                
                // Send message
                messageManager.sendMessage(player, target, message.toString());
                
                return true;
            }
        });
        
        plugin.getCommand("bdmsg").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Registers the /bdr command.
     */
    private void registerReplyCommand() {
        plugin.getCommand("bdr").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.msg")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "Please specify a message.");
                    player.sendMessage(ChatColor.YELLOW + "Usage: /bdr <message>");
                    return true;
                }
                
                // Get last message sender
                UUID lastSenderUuid = messageManager.getLastMessageSender(player.getUniqueId());
                
                if (lastSenderUuid == null) {
                    player.sendMessage(ChatColor.RED + "You have nobody to reply to.");
                    return true;
                }
                
                Player target = Bukkit.getPlayer(lastSenderUuid);
                
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "The player you're trying to reply to is no longer online.");
                    return true;
                }
                
                // Build message
                StringBuilder message = new StringBuilder();
                
                for (int i = 0; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                
                // Send message
                messageManager.sendMessage(player, target, message.toString());
                
                return true;
            }
        });
    }
    
    /**
     * Registers the /bdmail command.
     */
    private void registerMailCommand() {
        plugin.getCommand("bdmail").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.mail")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length == 0) {
                    // Show help
                    showMailHelp(player);
                    return true;
                }
                
                String subCommand = args[0].toLowerCase();
                
                switch (subCommand) {
                    case "send":
                        handleMailSend(player, args);
                        break;
                    case "read":
                        handleMailRead(player);
                        break;
                    case "clear":
                        handleMailClear(player);
                        break;
                    default:
                        showMailHelp(player);
                        break;
                }
                
                return true;
            }
        });
        
        plugin.getCommand("bdmail").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    List<String> subCommands = new ArrayList<>();
                    subCommands.add("send");
                    subCommands.add("read");
                    subCommands.add("clear");
                    
                    return subCommands.stream()
                            .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                } else if (args.length == 2 && args[0].equalsIgnoreCase("send")) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                }
                
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Shows mail help to a player.
     * @param player The player
     */
    private void showMailHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BD Mail Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bdmail send <player> <message>" + ChatColor.WHITE + " - Send mail to a player");
        player.sendMessage(ChatColor.YELLOW + "/bdmail read" + ChatColor.WHITE + " - Read your mail");
        player.sendMessage(ChatColor.YELLOW + "/bdmail clear" + ChatColor.WHITE + " - Clear your mail");
    }
    
    /**
     * Handles the mail send command.
     * @param player The player
     * @param args The command arguments
     */
    private void handleMailSend(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Please specify a player and a message.");
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdmail send <player> <message>");
            return;
        }
        
        String targetName = args[1];
        
        // Build message
        StringBuilder message = new StringBuilder();
        
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        
        // Send mail
        boolean success = messageManager.sendMail(player.getName(), targetName, message.toString());
        
        if (success) {
            player.sendMessage(ChatColor.GREEN + "Mail sent to " + targetName + ".");
            
            // Notify target if online
            Player target = Bukkit.getPlayer(targetName);
            
            if (target != null) {
                target.sendMessage(ChatColor.YELLOW + "You have new mail from " + player.getName() + ".");
                target.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdmail read" + 
                        ChatColor.YELLOW + " to read it.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Failed to send mail. The player may not exist or you've sent too many mails.");
        }
    }
    
    /**
     * Handles the mail read command.
     * @param player The player
     */
    private void handleMailRead(Player player) {
        List<MessageManager.Mail> mails = messageManager.getMail(player.getName());
        
        if (mails.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You don't have any mail.");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== Your Mail ===");
        
        for (int i = 0; i < mails.size(); i++) {
            MessageManager.Mail mail = mails.get(i);
            
            player.sendMessage(ChatColor.YELLOW + Integer.toString(i + 1) + ". " + ChatColor.GREEN + "From: " + 
                    mail.getSender() + ChatColor.WHITE + " - " + mail.getMessage());
        }
        
        player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdmail clear" + 
                ChatColor.YELLOW + " to clear your mail.");
    }
    
    /**
     * Handles the mail clear command.
     * @param player The player
     */
    private void handleMailClear(Player player) {
        messageManager.clearMail(player.getName());
        player.sendMessage(ChatColor.GREEN + "Your mail has been cleared.");
    }
}