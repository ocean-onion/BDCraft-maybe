package com.bdcraft.plugin.commands.admin;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to give BD items to players.
 */
public class GiveItemCommand extends CommandBase {
    private final BDCraft plugin;
    private final BDItemManager itemManager;
    
    /**
     * Creates a new give item command.
     * @param plugin The plugin instance
     */
    public GiveItemCommand(BDCraft plugin) {
        super(plugin, "bdgive", "bdcraft.admin.give");
        this.plugin = plugin;
        
        // Get the item manager from the economy module
        BDEconomyModule economyModule = (BDEconomyModule) plugin.getModuleManager().getModule("economy");
        this.itemManager = economyModule.getItemManager();
        
        // Add item subcommand
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "item";
            }
            
            @Override
            public String getDescription() {
                return "Gives a BD item to a player";
            }
            
            @Override
            public String getUsage() {
                return "<player> <item_id> [amount] [value]";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.admin.give.item";
            }
            
            @Override
            public List<String> getAliases() {
                return Arrays.asList("i");
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bdgive item <player> <item_id> [amount] [value]");
                    return true;
                }
                
                // Get the target player
                String playerName = args[0];
                Player targetPlayer = Bukkit.getPlayerExact(playerName);
                
                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found: " + playerName);
                    return true;
                }
                
                // Get the item ID
                String itemId = args[1].toLowerCase();
                
                // Get the amount (default: 1)
                int amount = 1;
                if (args.length >= 3) {
                    try {
                        amount = Integer.parseInt(args[2]);
                        if (amount <= 0) {
                            throw new NumberFormatException("Amount must be positive");
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid amount: " + args[2]);
                        return true;
                    }
                }
                
                // Get the value (default: 0)
                int value = 0;
                if (args.length >= 4) {
                    try {
                        value = Integer.parseInt(args[3]);
                        if (value < 0) {
                            throw new NumberFormatException("Value cannot be negative");
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid value: " + args[3]);
                        return true;
                    }
                }
                
                // Give the item to the player
                boolean success = itemManager.giveItem(targetPlayer, itemId, amount, value);
                
                if (!success) {
                    sender.sendMessage(ChatColor.RED + "Unknown item ID: " + itemId);
                    return true;
                }
                
                // Notify the sender and target
                sender.sendMessage(ChatColor.GREEN + "Gave " + amount + "x " + itemId + " to " + targetPlayer.getName());
                
                if (sender != targetPlayer) {
                    targetPlayer.sendMessage(ChatColor.GREEN + "You received " + amount + "x " + itemId + " from " + 
                            (sender instanceof Player ? ((Player) sender).getName() : "Console"));
                }
                
                return true;
            }
            
            @Override
            public List<String> tabComplete(CommandSender sender, String[] args) {
                List<String> completions = new ArrayList<>();
                
                if (args.length == 1) {
                    // Complete player names
                    String partialName = args[0].toLowerCase();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(partialName)) {
                            completions.add(player.getName());
                        }
                    }
                } else if (args.length == 2) {
                    // Complete item IDs
                    // For now, just add the known item types
                    String partialId = args[1].toLowerCase();
                    List<String> itemIds = Arrays.asList(
                            "bd_stick", "market_token", "house_token", 
                            "regular_seeds", "green_seeds", "purple_seeds",
                            "harvester_tool", "fertilizer");
                    
                    for (String id : itemIds) {
                        if (id.startsWith(partialId)) {
                            completions.add(id);
                        }
                    }
                }
                
                return completions;
            }
        });
    }
    
    @Override
    protected void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDGive Help" + ChatColor.GOLD + " ===");
        sender.sendMessage(ChatColor.YELLOW + "/bdgive item <player> <item_id> [amount] [value]" + 
                ChatColor.GRAY + " - Gives a BD item to a player");
    }
}