package com.bdcraft.modules.economy.commands;

import com.bdcraft.BDCraftPlugin;
import com.bdcraft.modules.economy.EconomyModule;
import com.bdcraft.modules.economy.TransactionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles all economy-related commands for the BDCraft plugin.
 */
public class EconomyCommands implements CommandExecutor, TabCompleter {

    private final BDCraftPlugin plugin;
    private final EconomyModule economyModule;

    /**
     * Creates a new EconomyCommands instance.
     *
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public EconomyCommands(BDCraftPlugin plugin, EconomyModule economyModule) {
        this.plugin = plugin;
        this.economyModule = economyModule;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String cmdName = command.getName().toLowerCase();

        switch (cmdName) {
            case "bdbal":
                return handleBalanceCommand(sender, args);
            case "bdpay":
                return handlePayCommand(sender, args);
            case "bdtop":
                return handleTopCommand(sender, args);
            case "bdeco":
                return handleEcoCommand(sender, args);
            default:
                return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String cmdName = command.getName().toLowerCase();

        switch (cmdName) {
            case "bdbal":
                if (args.length == 1 && sender.hasPermission("bdcraft.balance.others")) {
                    return getOnlinePlayerNames(args[0]);
                }
                break;
            case "bdpay":
                if (args.length == 1) {
                    return getOnlinePlayerNames(args[0]);
                }
                break;
            case "bdeco":
                if (args.length == 1 && sender.hasPermission("bdcraft.admin.economy")) {
                    return Arrays.asList("give", "take", "set", "reset");
                } else if (args.length == 2 && sender.hasPermission("bdcraft.admin.economy")) {
                    return getOnlinePlayerNames(args[1]);
                }
                break;
        }
        return null;
    }

    /**
     * Handles the balance command.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return True if the command was executed successfully
     */
    private boolean handleBalanceCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage("Console must specify a player name.");
            return true;
        }

        // Check if the sender is trying to check someone else's balance
        if (args.length >= 1) {
            if (!sender.hasPermission("bdcraft.balance.others")) {
                sender.sendMessage(Component.text("You don't have permission to check other players' balances.")
                        .color(NamedTextColor.RED));
                return true;
            }

            String targetName = args[0];
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);

            if (target == null || !target.hasPlayedBefore()) {
                sender.sendMessage(Component.text("Player not found: " + targetName)
                        .color(NamedTextColor.RED));
                return true;
            }

            // Display the target's balance
            double credits = economyModule.getBalance(target);
            sender.sendMessage(Component.text(target.getName() + "'s balance: ")
                    .color(NamedTextColor.GOLD)
                    .append(Component.text(economyModule.formatCredits(credits))
                            .color(NamedTextColor.YELLOW)));
            return true;
        }

        // Display the sender's balance
        Player player = (Player) sender;
        double credits = economyModule.getBalance(player);
        sender.sendMessage(Component.text("Your balance: ")
                .color(NamedTextColor.GOLD)
                .append(Component.text(economyModule.formatCredits(credits))
                        .color(NamedTextColor.YELLOW)));
        return true;
    }

    /**
     * Handles the pay command.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return True if the command was executed successfully
     */
    private boolean handlePayCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /bdpay <player> <amount>")
                    .color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);

        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(Component.text("Player not found: " + targetName)
                    .color(NamedTextColor.RED));
            return true;
        }

        // Check if the player is trying to pay themselves
        if (player.getUniqueId().equals(target.getUniqueId())) {
            sender.sendMessage(Component.text("You cannot pay yourself.")
                    .color(NamedTextColor.RED));
            return true;
        }

        // Parse the amount
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid amount: " + args[1])
                    .color(NamedTextColor.RED));
            return true;
        }

        // Validate the amount
        if (amount <= 0) {
            sender.sendMessage(Component.text("Amount must be positive.")
                    .color(NamedTextColor.RED));
            return true;
        }

        // Transfer the credits
        boolean success = economyModule.transferCredits(player, target, amount);

        if (success) {
            sender.sendMessage(Component.text("You paid ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(economyModule.formatCredits(amount))
                            .color(NamedTextColor.GOLD))
                    .append(Component.text(" to ")
                            .color(NamedTextColor.GREEN))
                    .append(Component.text(target.getName())
                            .color(NamedTextColor.GOLD)));

            // Notify the target if they're online
            if (target.isOnline()) {
                Player targetPlayer = target.getPlayer();
                targetPlayer.sendMessage(Component.text("You received ")
                        .color(NamedTextColor.GREEN)
                        .append(Component.text(economyModule.formatCredits(amount))
                                .color(NamedTextColor.GOLD))
                        .append(Component.text(" from ")
                                .color(NamedTextColor.GREEN))
                        .append(Component.text(player.getName())
                                .color(NamedTextColor.GOLD)));
            }
        } else {
            sender.sendMessage(Component.text("You don't have enough credits.")
                    .color(NamedTextColor.RED));
        }

        return true;
    }

    /**
     * Handles the top command.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return True if the command was executed successfully
     */
    private boolean handleTopCommand(CommandSender sender, String[] args) {
        int page = 1;
        int perPage = 10;

        // Parse the page number
        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid page number: " + args[0])
                        .color(NamedTextColor.RED));
                return true;
            }
        }

        // Get the top balances
        Map<UUID, Double> topBalances = economyModule.getTopBalances(page * perPage);

        // Calculate the starting index
        int startIndex = (page - 1) * perPage;

        // Convert to a list and get the subset for the current page
        List<Map.Entry<UUID, Double>> entries = new ArrayList<>(topBalances.entrySet());
        
        // Check if the page is valid
        if (startIndex >= entries.size()) {
            sender.sendMessage(Component.text("No players found on page " + page)
                    .color(NamedTextColor.RED));
            return true;
        }

        // Display the top balances
        sender.sendMessage(Component.text("Top Balances (Page " + page + ")")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD));

        for (int i = startIndex; i < Math.min(startIndex + perPage, entries.size()); i++) {
            Map.Entry<UUID, Double> entry = entries.get(i);
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : entry.getKey().toString();
            
            sender.sendMessage(Component.text((i + 1) + ". ")
                    .color(NamedTextColor.GRAY)
                    .append(Component.text(playerName)
                            .color(NamedTextColor.YELLOW))
                    .append(Component.text(": ")
                            .color(NamedTextColor.GRAY))
                    .append(Component.text(economyModule.formatCredits(entry.getValue()))
                            .color(NamedTextColor.GOLD)));
        }

        return true;
    }

    /**
     * Handles the eco admin command.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return True if the command was executed successfully
     */
    private boolean handleEcoCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("bdcraft.admin.economy")) {
            sender.sendMessage(Component.text("You don't have permission to use this command.")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /bdeco <give|take|set|reset> <player> [amount]")
                    .color(NamedTextColor.RED));
            return true;
        }

        String action = args[0].toLowerCase();
        String targetName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);

        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(Component.text("Player not found: " + targetName)
                    .color(NamedTextColor.RED));
            return true;
        }

        UUID adminId = sender instanceof Player ? ((Player) sender).getUniqueId() : new UUID(0, 0);
        String adminName = sender instanceof Player ? ((Player) sender).getName() : "Console";

        switch (action) {
            case "give":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /bdeco give <player> <amount>")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Parse the amount
                double giveAmount;
                try {
                    giveAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid amount: " + args[2])
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Validate the amount
                if (giveAmount <= 0) {
                    sender.sendMessage(Component.text("Amount must be positive.")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Add the credits
                double newBalance = economyModule.addCredits(
                    target, 
                    giveAmount, 
                    adminId, 
                    TransactionType.ADMIN_GIVE, 
                    "Given by " + adminName
                );

                sender.sendMessage(Component.text("Gave ")
                        .color(NamedTextColor.GREEN)
                        .append(Component.text(economyModule.formatCredits(giveAmount))
                                .color(NamedTextColor.GOLD))
                        .append(Component.text(" to ")
                                .color(NamedTextColor.GREEN))
                        .append(Component.text(target.getName())
                                .color(NamedTextColor.GOLD))
                        .append(Component.text(". New balance: ")
                                .color(NamedTextColor.GREEN))
                        .append(Component.text(economyModule.formatCredits(newBalance))
                                .color(NamedTextColor.GOLD)));

                // Notify the target if they're online
                if (target.isOnline()) {
                    Player targetPlayer = target.getPlayer();
                    targetPlayer.sendMessage(Component.text("You received ")
                            .color(NamedTextColor.GREEN)
                            .append(Component.text(economyModule.formatCredits(giveAmount))
                                    .color(NamedTextColor.GOLD))
                            .append(Component.text(" from an admin.")
                                    .color(NamedTextColor.GREEN)));
                }
                break;

            case "take":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /bdeco take <player> <amount>")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Parse the amount
                double takeAmount;
                try {
                    takeAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid amount: " + args[2])
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Validate the amount
                if (takeAmount <= 0) {
                    sender.sendMessage(Component.text("Amount must be positive.")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Remove the credits
                boolean takeSucess = economyModule.removeCredits(
                    target, 
                    takeAmount, 
                    adminId, 
                    TransactionType.ADMIN_TAKE, 
                    "Taken by " + adminName
                );

                if (takeSucess) {
                    double targetBalance = economyModule.getBalance(target);
                    sender.sendMessage(Component.text("Took ")
                            .color(NamedTextColor.GREEN)
                            .append(Component.text(economyModule.formatCredits(takeAmount))
                                    .color(NamedTextColor.GOLD))
                            .append(Component.text(" from ")
                                    .color(NamedTextColor.GREEN))
                            .append(Component.text(target.getName())
                                    .color(NamedTextColor.GOLD))
                            .append(Component.text(". New balance: ")
                                    .color(NamedTextColor.GREEN))
                            .append(Component.text(economyModule.formatCredits(targetBalance))
                                    .color(NamedTextColor.GOLD)));

                    // Notify the target if they're online
                    if (target.isOnline()) {
                        Player targetPlayer = target.getPlayer();
                        targetPlayer.sendMessage(Component.text("An admin took ")
                                .color(NamedTextColor.RED)
                                .append(Component.text(economyModule.formatCredits(takeAmount))
                                        .color(NamedTextColor.GOLD))
                                .append(Component.text(" from you.")
                                        .color(NamedTextColor.RED)));
                    }
                } else {
                    sender.sendMessage(Component.text("The player doesn't have enough credits.")
                            .color(NamedTextColor.RED));
                }
                break;

            case "set":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /bdeco set <player> <amount>")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Parse the amount
                double setAmount;
                try {
                    setAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid amount: " + args[2])
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Validate the amount
                if (setAmount < 0) {
                    sender.sendMessage(Component.text("Amount cannot be negative.")
                            .color(NamedTextColor.RED));
                    return true;
                }

                // Set the balance
                economyModule.setBalance(target, setAmount, "Set by " + adminName, adminId);

                sender.sendMessage(Component.text("Set ")
                        .color(NamedTextColor.GREEN)
                        .append(Component.text(target.getName() + "'s")
                                .color(NamedTextColor.GOLD))
                        .append(Component.text(" balance to ")
                                .color(NamedTextColor.GREEN))
                        .append(Component.text(economyModule.formatCredits(setAmount))
                                .color(NamedTextColor.GOLD)));

                // Notify the target if they're online
                if (target.isOnline()) {
                    Player targetPlayer = target.getPlayer();
                    targetPlayer.sendMessage(Component.text("An admin set your balance to ")
                            .color(NamedTextColor.YELLOW)
                            .append(Component.text(economyModule.formatCredits(setAmount))
                                    .color(NamedTextColor.GOLD)));
                }
                break;

            case "reset":
                // Reset the balance to 0
                economyModule.setBalance(target, 0, "Reset by " + adminName, adminId);

                sender.sendMessage(Component.text("Reset ")
                        .color(NamedTextColor.GREEN)
                        .append(Component.text(target.getName() + "'s")
                                .color(NamedTextColor.GOLD))
                        .append(Component.text(" balance to 0.")
                                .color(NamedTextColor.GREEN)));

                // Notify the target if they're online
                if (target.isOnline()) {
                    Player targetPlayer = target.getPlayer();
                    targetPlayer.sendMessage(Component.text("An admin reset your balance to 0.")
                            .color(NamedTextColor.YELLOW));
                }
                break;

            default:
                sender.sendMessage(Component.text("Unknown action: " + action)
                        .color(NamedTextColor.RED));
                sender.sendMessage(Component.text("Usage: /bdeco <give|take|set|reset> <player> [amount]")
                        .color(NamedTextColor.RED));
                return true;
        }

        return true;
    }

    /**
     * Gets a list of online player names that start with the given prefix.
     *
     * @param prefix The prefix to match
     * @return A list of matching player names
     */
    private List<String> getOnlinePlayerNames(String prefix) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}