package com.bdcraft.plugin.commands.player;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.economy.auction.AuctionHouseGUI;
import com.bdcraft.plugin.modules.economy.auction.AuctionItem;
import com.bdcraft.plugin.modules.economy.auction.AuctionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Command for handling auction house functions.
 */
public class AuctionHouseCommand extends CommandBase {

    private final AuctionManager auctionManager;
    private final AuctionHouseGUI auctionHouseGUI;
    
    /**
     * Creates a new auction house command.
     * 
     * @param plugin The plugin instance
     * @param auctionManager The auction manager
     * @param auctionHouseGUI The auction house GUI
     */
    public AuctionHouseCommand(BDCraft plugin, AuctionManager auctionManager, AuctionHouseGUI auctionHouseGUI) {
        super(plugin, "bdauction", "bdcraft.auction");
        this.auctionManager = auctionManager;
        this.auctionHouseGUI = auctionHouseGUI;
        
        // Open auction house
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "open";
            }
            
            @Override
            public String getDescription() {
                return "Opens the auction house GUI";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.open";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                auctionHouseGUI.openMainMenu(player);
                return true;
            }
        });
        
        // List an item for sale
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "list";
            }
            
            @Override
            public String getDescription() {
                return "Lists an item for sale in the auction house";
            }
            
            @Override
            public String getUsage() {
                return "<price>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.list";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                AuctionManager auctionManager = plugin.getEconomyModule().getAuctionManager();
                
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "You must specify a price. Usage: /bdauction list <price>");
                    return true;
                }
                
                try {
                    int price = Integer.parseInt(args[0]);
                    
                    if (price <= 0) {
                        sender.sendMessage(ChatColor.RED + "Price must be greater than 0.");
                        return true;
                    }
                    
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    
                    if (heldItem == null || heldItem.getType().isAir()) {
                        sender.sendMessage(ChatColor.RED + "You must be holding an item to list it.");
                        return true;
                    }
                    
                    UUID itemId = auctionManager.createListing(player, heldItem.clone(), price);
                    
                    if (itemId != null) {
                        // Remove the item from player's hand
                        player.getInventory().setItemInMainHand(null);
                        
                        sender.sendMessage(ChatColor.GREEN + "Item listed for " + price + " BD coins!");
                        sender.sendMessage(ChatColor.GREEN + "Listing ID: " + itemId);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to create listing. Please try again.");
                    }
                    
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid price. Please enter a number.");
                    return true;
                }
            }
        });
        
        // Confirm a purchase
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "confirm";
            }
            
            @Override
            public String getDescription() {
                return "Confirms a purchase from the auction house";
            }
            
            @Override
            public String getUsage() {
                return "<listing-id>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.confirm";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                AuctionManager auctionManager = plugin.getEconomyModule().getAuctionManager();
                
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "You must specify a listing ID. Usage: /bdauction confirm <listing-id>");
                    return true;
                }
                
                try {
                    UUID itemId = UUID.fromString(args[0]);
                    AuctionItem item = auctionManager.getAuctionItem(itemId);
                    
                    if (item == null) {
                        sender.sendMessage(ChatColor.RED + "Listing not found.");
                        return true;
                    }
                    
                    if (item.isSold()) {
                        sender.sendMessage(ChatColor.RED + "This item has already been sold.");
                        return true;
                    }
                    
                    if (item.getSellerId().equals(player.getUniqueId())) {
                        sender.sendMessage(ChatColor.RED + "You cannot buy your own listing.");
                        return true;
                    }
                    
                    // Check if player has enough coins
                    int playerBalance = plugin.getEconomyModule().getPlayerBalance(player);
                    
                    if (playerBalance < item.getPrice()) {
                        sender.sendMessage(ChatColor.RED + "You don't have enough BD coins to make this purchase.");
                        sender.sendMessage(ChatColor.RED + "Required: " + item.getPrice() + ", You have: " + playerBalance);
                        return true;
                    }
                    
                    // Process the purchase
                    boolean success = auctionManager.purchaseItem(player, itemId);
                    
                    if (success) {
                        sender.sendMessage(ChatColor.GREEN + "Purchase successful! The item has been added to your inventory.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to complete purchase. The item may no longer be available.");
                    }
                    
                    return true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid listing ID format.");
                    return true;
                }
            }
        });
        
        // Cancel a listing
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "cancel";
            }
            
            @Override
            public String getDescription() {
                return "Cancels your auction listing";
            }
            
            @Override
            public String getUsage() {
                return "<listing-id>";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.cancel";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                AuctionManager auctionManager = plugin.getEconomyModule().getAuctionManager();
                
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "You must specify a listing ID. Usage: /bdauction cancel <listing-id>");
                    return true;
                }
                
                try {
                    UUID itemId = UUID.fromString(args[0]);
                    AuctionItem item = auctionManager.getAuctionItem(itemId);
                    
                    if (item == null) {
                        sender.sendMessage(ChatColor.RED + "Listing not found.");
                        return true;
                    }
                    
                    if (item.isSold()) {
                        sender.sendMessage(ChatColor.RED + "This item has already been sold and cannot be canceled.");
                        return true;
                    }
                    
                    if (!item.getSellerId().equals(player.getUniqueId())) {
                        sender.sendMessage(ChatColor.RED + "You can only cancel your own listings.");
                        return true;
                    }
                    
                    // Process the cancellation
                    boolean success = auctionManager.cancelListing(player, itemId);
                    
                    if (success) {
                        sender.sendMessage(ChatColor.GREEN + "Listing canceled! The item has been returned to your inventory.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to cancel listing. Please try again.");
                    }
                    
                    return true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid listing ID format.");
                    return true;
                }
            }
        });
        
        // Collect payments or returns
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "collect";
            }
            
            @Override
            public String getDescription() {
                return "Collects pending payments and returned items";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.collect";
            }
            
            @Override
            public boolean isPlayerOnly() {
                return true;
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                Player player = (Player) sender;
                AuctionManager auctionManager = plugin.getEconomyModule().getAuctionManager();
                
                boolean collectedPayments = false;
                boolean collectedReturns = false;
                
                // Check for pending payments
                int pendingPayment = auctionManager.getPendingPayment(player);
                if (pendingPayment > 0) {
                    auctionManager.collectPendingPayment(player);
                    collectedPayments = true;
                    
                    sender.sendMessage(ChatColor.GREEN + "Collected " + pendingPayment + " BD coins from your auction sales!");
                }
                
                // Check for pending returns
                if (auctionManager.hasPendingReturns(player)) {
                    auctionManager.collectPendingReturns(player);
                    collectedReturns = true;
                    
                    sender.sendMessage(ChatColor.GREEN + "Collected your expired auction items!");
                }
                
                if (!collectedPayments && !collectedReturns) {
                    sender.sendMessage(ChatColor.YELLOW + "You have no pending payments or returns to collect.");
                }
                
                return true;
            }
        });
        
        // Show auction help/about
        addSubCommand(new SubCommand() {
            @Override
            public String getName() {
                return "help";
            }
            
            @Override
            public String getDescription() {
                return "Shows information about the auction house";
            }
            
            @Override
            public String getUsage() {
                return "";
            }
            
            @Override
            public String getPermission() {
                return "bdcraft.auction.help";
            }
            
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                sender.sendMessage(ChatColor.GOLD + "===== BD Auction House =====");
                sender.sendMessage(ChatColor.YELLOW + "The auction house allows players to buy and sell items.");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Commands:");
                sender.sendMessage(ChatColor.WHITE + "/bdauction open " + 
                        ChatColor.GRAY + "- Opens the auction house GUI");
                sender.sendMessage(ChatColor.WHITE + "/bdauction list <price> " + 
                        ChatColor.GRAY + "- Lists the item in your hand for sale");
                sender.sendMessage(ChatColor.WHITE + "/bdauction confirm <id> " + 
                        ChatColor.GRAY + "- Confirms a purchase");
                sender.sendMessage(ChatColor.WHITE + "/bdauction cancel <id> " + 
                        ChatColor.GRAY + "- Cancels one of your listings");
                sender.sendMessage(ChatColor.WHITE + "/bdauction collect " + 
                        ChatColor.GRAY + "- Collects payments and returns");
                
                return true;
            }
        });
    }
}