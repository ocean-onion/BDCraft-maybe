package com.bdcraft.plugin.modules.economy.market.gui;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GUI for managing BD Markets.
 * This provides a custom interface for market management functions.
 */
public class MarketManagementGUI {
    private final BDCraft plugin;
    private final MarketManager marketManager;
    
    // Track which players have which menu open and what market they're viewing
    private final Map<UUID, BDMarket> playerCurrentMarket = new HashMap<>();
    private final Map<UUID, GUIType> playerCurrentMenu = new HashMap<>();
    
    // Track which associates are in which slots in the associate menu
    private final Map<UUID, Map<Integer, UUID>> playerAssociateSlots = new HashMap<>();
    
    // GUI Types
    public enum GUIType {
        MAIN_MENU,
        ASSOCIATE_MANAGEMENT,
        PERMISSIONS_MANAGEMENT,
        SECURITY_SETTINGS,
        MARKET_UPGRADES,
        MARKET_SETTINGS,
        MARKET_LEADERBOARD
    }
    
    /**
     * Creates a new market management GUI.
     * 
     * @param plugin The plugin instance
     * @param marketManager The market manager
     */
    public MarketManagementGUI(BDCraft plugin, MarketManager marketManager) {
        this.plugin = plugin;
        this.marketManager = marketManager;
    }
    
    /**
     * Opens the market management GUI for a player.
     * 
     * @param player The player
     * @param market The market
     */
    public void openMainMenu(Player player, BDMarket market) {
        // Store which market the player is viewing
        playerCurrentMarket.put(player.getUniqueId(), market);
        playerCurrentMenu.put(player.getUniqueId(), GUIType.MAIN_MENU);
        
        Inventory menu = Bukkit.createInventory(player, 36, "Market Management");
        
        // Market Info at the top
        ItemStack infoItem = createGuiItem(Material.EMERALD, 
                ChatColor.GOLD + "Market Information",
                ChatColor.YELLOW + "Founder: " + ChatColor.WHITE + market.getFounderName(),
                ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel(),
                ChatColor.YELLOW + "Collectors: " + ChatColor.WHITE + market.getTraderCount("COLLECTOR") + "/" + getMaxCollectors(market),
                ChatColor.YELLOW + "Price Modifier: " + ChatColor.WHITE + String.format("%.2fx", market.getPriceModifier()));
        menu.setItem(4, infoItem);
        
        // Associate Management
        ItemStack associateItem = createGuiItem(Material.PLAYER_HEAD, 
                ChatColor.AQUA + "Market Associates",
                ChatColor.GRAY + "Manage who can help run your market",
                ChatColor.GRAY + "Current: " + market.getAssociates().size() + "/5");
        menu.setItem(10, associateItem);
        
        // Permissions Management
        ItemStack permissionsItem = createGuiItem(Material.PAPER, 
                ChatColor.GREEN + "Market Permissions",
                ChatColor.GRAY + "Control who can trade with your collectors",
                ChatColor.GRAY + "Current: " + (market.isPublicAccess() ? "Public" : "Private"));
        menu.setItem(12, permissionsItem);
        
        // Security Settings
        ItemStack securityItem = createGuiItem(Material.IRON_DOOR, 
                ChatColor.RED + "Security Settings",
                ChatColor.GRAY + "Manage building permissions in market area");
        menu.setItem(14, securityItem);
        
        // Market Upgrades
        ItemStack upgradeItem = createGuiItem(Material.EXPERIENCE_BOTTLE, 
                ChatColor.GOLD + "Market Upgrades",
                ChatColor.GRAY + "Improve your market's capabilities",
                market.getLevel() >= 4 ? 
                    ChatColor.GREEN + "Your market is at maximum level!" : 
                    ChatColor.YELLOW + "Current level: " + market.getLevel() + "/4");
        menu.setItem(16, upgradeItem);
        
        // Market Settings
        ItemStack settingsItem = createGuiItem(Material.REDSTONE_TORCH, 
                ChatColor.LIGHT_PURPLE + "Market Settings",
                ChatColor.GRAY + "Configure market appearance and effects");
        menu.setItem(28, settingsItem);
        
        // Market Leaderboard
        ItemStack leaderboardItem = createGuiItem(Material.OAK_SIGN, 
                ChatColor.AQUA + "Market Leaderboard",
                ChatColor.GRAY + "See how your market ranks against others",
                ChatColor.GRAY + "Based on total net worth and activity");
        menu.setItem(30, leaderboardItem);
        
        // Exit Button
        ItemStack exitItem = createGuiItem(Material.BARRIER, 
                ChatColor.RED + "Exit",
                ChatColor.GRAY + "Close the market management menu");
        menu.setItem(32, exitItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the associate management menu.
     * 
     * @param player The player
     */
    public void openAssociateMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.ASSOCIATE_MANAGEMENT);
        Map<Integer, UUID> slotMap = new HashMap<>();
        playerAssociateSlots.put(player.getUniqueId(), slotMap);
        
        Inventory menu = Bukkit.createInventory(player, 54, "Market Associates");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.BOOK, 
                ChatColor.GOLD + "Associate Information",
                ChatColor.GRAY + "Associates can help manage your market",
                ChatColor.GRAY + "They can build collector houses and more",
                ChatColor.YELLOW + "Current: " + market.getAssociates().size() + "/5");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Current associates
        int slot = 18;
        for (UUID associateId : market.getAssociates()) {
            String associateName = "Unknown";
            
            // Try to get player name
            Player associatePlayer = plugin.getServer().getPlayer(associateId);
            if (associatePlayer != null) {
                associateName = associatePlayer.getName();
            } else {
                // Try to get from offline player data
                associateName = plugin.getServer().getOfflinePlayer(associateId).getName();
            }
            
            ItemStack associateHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) associateHead.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + associateName);
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Click to remove as associate"
                ));
                
                // Try to set owner if online
                if (associatePlayer != null) {
                    meta.setOwningPlayer(associatePlayer);
                }
                
                associateHead.setItemMeta(meta);
            }
            
            menu.setItem(slot, associateHead);
            slotMap.put(slot, associateId);
            slot++;
        }
        
        // Add new associate
        if (market.getAssociates().size() < 5) {
            ItemStack addItem = createGuiItem(Material.EMERALD, 
                    ChatColor.GREEN + "Add Associate",
                    ChatColor.GRAY + "Click to add a new associate",
                    ChatColor.GRAY + "Associates must be online when added");
            menu.setItem(49, addItem);
        }
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(45, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the permissions management menu.
     * 
     * @param player The player
     */
    public void openPermissionsMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.PERMISSIONS_MANAGEMENT);
        
        Inventory menu = Bukkit.createInventory(player, 36, "Market Permissions");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.BOOK, 
                ChatColor.GOLD + "Permissions Information",
                ChatColor.GRAY + "Control who can trade with your collectors",
                ChatColor.GRAY + "and interact with your market");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Public/Private toggle
        Material accessMaterial = market.isPublicAccess() ? Material.LIME_WOOL : Material.RED_WOOL;
        String accessStatus = market.isPublicAccess() ? "Public" : "Private";
        ItemStack accessItem = createGuiItem(accessMaterial, 
                ChatColor.YELLOW + "Market Access: " + ChatColor.GREEN + accessStatus,
                ChatColor.GRAY + "Click to toggle market access mode",
                market.isPublicAccess() ? 
                        ChatColor.GREEN + "Anyone can trade with your collectors" : 
                        ChatColor.RED + "Only you and associates can trade with collectors");
        menu.setItem(22, accessItem);
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(27, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the security settings menu.
     * 
     * @param player The player
     */
    public void openSecurityMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.SECURITY_SETTINGS);
        
        Inventory menu = Bukkit.createInventory(player, 36, "Market Security");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.BOOK, 
                ChatColor.GOLD + "Security Information",
                ChatColor.GRAY + "Control who can build and break blocks",
                ChatColor.GRAY + "within your market boundaries");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Building permissions
        Material buildMaterial = market.isOpenBuild() ? Material.LIME_WOOL : Material.RED_WOOL;
        String buildStatus = market.isOpenBuild() ? "Open" : "Restricted";
        ItemStack buildItem = createGuiItem(buildMaterial, 
                ChatColor.YELLOW + "Building Access: " + ChatColor.GREEN + buildStatus,
                ChatColor.GRAY + "Click to toggle building permissions",
                market.isOpenBuild() ? 
                        ChatColor.GREEN + "Anyone can build within market boundaries" : 
                        ChatColor.RED + "Only you and associates can build here");
        menu.setItem(21, buildItem);
        
        // Breaking permissions
        Material breakMaterial = market.isOpenBreak() ? Material.LIME_WOOL : Material.RED_WOOL;
        String breakStatus = market.isOpenBreak() ? "Open" : "Restricted";
        ItemStack breakItem = createGuiItem(breakMaterial, 
                ChatColor.YELLOW + "Breaking Access: " + ChatColor.GREEN + breakStatus,
                ChatColor.GRAY + "Click to toggle breaking permissions",
                market.isOpenBreak() ? 
                        ChatColor.GREEN + "Anyone can break blocks within market boundaries" : 
                        ChatColor.RED + "Only you and associates can break blocks here");
        menu.setItem(23, breakItem);
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(27, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the market upgrades menu.
     * 
     * @param player The player
     */
    public void openUpgradesMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.MARKET_UPGRADES);
        
        Inventory menu = Bukkit.createInventory(player, 36, "Market Upgrades");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.BOOK, 
                ChatColor.GOLD + "Upgrade Information",
                ChatColor.GRAY + "Upgrade your market to gain benefits",
                ChatColor.YELLOW + "Current level: " + market.getLevel() + "/4");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Level indicator
        ItemStack levelItem = createGuiItem(Material.EXPERIENCE_BOTTLE, 
                ChatColor.GOLD + "Market Level: " + market.getLevel(),
                ChatColor.GRAY + "Your market's current level");
        menu.setItem(19, levelItem);
        
        // Current benefits
        List<String> currentBenefits = new ArrayList<>();
        currentBenefits.add(ChatColor.YELLOW + "Current Benefits:");
        currentBenefits.add(ChatColor.AQUA + "• Max Collectors: " + getMaxCollectors(market));
        currentBenefits.add(ChatColor.AQUA + "• Price Modifier: " + String.format("%.2fx", market.getPriceModifier()));
        
        ItemStack currentItem = createGuiItem(Material.GOLD_INGOT, 
                ChatColor.YELLOW + "Current Level Benefits",
                currentBenefits.toArray(new String[0]));
        menu.setItem(21, currentItem);
        
        // Upgrade button - only show if not at max level
        if (market.getLevel() < 4) {
            int nextLevel = market.getLevel() + 1;
            double nextModifier = getModifierForLevel(nextLevel);
            
            List<String> nextBenefits = new ArrayList<>();
            nextBenefits.add(ChatColor.YELLOW + "Next Level Benefits:");
            nextBenefits.add(ChatColor.AQUA + "• Max Collectors: " + getMaxCollectorsForLevel(nextLevel));
            nextBenefits.add(ChatColor.AQUA + "• Price Modifier: " + String.format("%.2fx", nextModifier));
            
            ItemStack nextItem = createGuiItem(Material.DIAMOND, 
                    ChatColor.GREEN + "Next Level Benefits",
                    nextBenefits.toArray(new String[0]));
            menu.setItem(23, nextItem);
            
            List<String> requirements = getUpgradeRequirements(market.getLevel());
            
            String requirementsDescription = ChatColor.YELLOW + "Requirements:";
            ItemStack upgradeItem = createGuiItem(Material.EMERALD_BLOCK, 
                    ChatColor.GREEN + "Upgrade to Level " + nextLevel,
                    requirementsDescription, requirements.toArray(new String[0]));
            menu.setItem(25, upgradeItem);
        } else {
            ItemStack maxLevelItem = createGuiItem(Material.GOLDEN_APPLE, 
                    ChatColor.GOLD + "Maximum Level Reached!",
                    ChatColor.GREEN + "Your market is at the maximum level",
                    ChatColor.GREEN + "Enjoy all the benefits of a level 4 market!");
            menu.setItem(25, maxLevelItem);
        }
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(27, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the market settings menu.
     * 
     * @param player The player
     */
    public void openSettingsMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.MARKET_SETTINGS);
        
        Inventory menu = Bukkit.createInventory(player, 36, "Market Settings");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.BOOK, 
                ChatColor.GOLD + "Settings Information",
                ChatColor.GRAY + "Configure visual settings for your market");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Market Particles toggle
        Material particleMaterial = market.hasParticles() ? Material.LIME_WOOL : Material.RED_WOOL;
        String particleStatus = market.hasParticles() ? "Enabled" : "Disabled";
        ItemStack particleItem = createGuiItem(particleMaterial, 
                ChatColor.YELLOW + "Market Particles: " + ChatColor.GREEN + particleStatus,
                ChatColor.GRAY + "Click to toggle particle effects",
                market.hasParticles() ? 
                        ChatColor.GREEN + "Villagers display special particles" : 
                        ChatColor.RED + "No special particles shown");
        menu.setItem(20, particleItem);
        
        // Market Sound Effects toggle
        Material soundMaterial = market.hasSounds() ? Material.LIME_WOOL : Material.RED_WOOL;
        String soundStatus = market.hasSounds() ? "Enabled" : "Disabled";
        ItemStack soundItem = createGuiItem(soundMaterial, 
                ChatColor.YELLOW + "Market Sounds: " + ChatColor.GREEN + soundStatus,
                ChatColor.GRAY + "Click to toggle sound effects",
                market.hasSounds() ? 
                        ChatColor.GREEN + "Special market sounds are played" : 
                        ChatColor.RED + "No special sounds played");
        menu.setItem(24, soundItem);
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(27, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the market leaderboard.
     * 
     * @param player The player
     */
    public void openLeaderboardMenu(Player player) {
        BDMarket market = playerCurrentMarket.get(player.getUniqueId());
        if (market == null) {
            return;
        }
        
        playerCurrentMenu.put(player.getUniqueId(), GUIType.MARKET_LEADERBOARD);
        
        Inventory menu = Bukkit.createInventory(player, 54, "Market Leaderboard");
        
        // Title and info
        ItemStack infoItem = createGuiItem(Material.GOLDEN_APPLE, 
                ChatColor.GOLD + "Market Leaderboard",
                ChatColor.GRAY + "Top markets by net worth and activity");
        menu.setItem(4, infoItem);
        
        // Divider
        for (int i = 9; i < 18; i++) {
            menu.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        
        // Get top 10 markets by net worth
        List<BDMarket> topMarkets = getTopMarkets(10);
        int rank = 1;
        int slot = 19;
        
        for (BDMarket topMarket : topMarkets) {
            Material rankMaterial;
            if (rank == 1) rankMaterial = Material.GOLD_BLOCK;
            else if (rank == 2) rankMaterial = Material.IRON_BLOCK;
            else if (rank == 3) rankMaterial = Material.COPPER_BLOCK;
            else rankMaterial = Material.EMERALD;
            
            boolean isPlayerMarket = topMarket.getId().equals(market.getId());
            
            ItemStack marketItem = createGuiItem(rankMaterial, 
                    (isPlayerMarket ? ChatColor.GREEN : ChatColor.YELLOW) + "#" + rank + ": " + topMarket.getFounderName() + "'s Market",
                    ChatColor.YELLOW + "Level: " + ChatColor.WHITE + topMarket.getLevel(),
                    ChatColor.YELLOW + "Net Worth: " + ChatColor.GOLD + formatNetWorth(topMarket.getNetWorth()) + " BD coins",
                    ChatColor.YELLOW + "Weekly Trades: " + ChatColor.WHITE + topMarket.getWeeklyTradeCount(),
                    "",
                    isPlayerMarket ? ChatColor.GREEN + "This is your market!" : "");
            
            menu.setItem(slot, marketItem);
            slot++;
            rank++;
        }
        
        // Your market rank (if not in top 10)
        boolean inTop10 = false;
        for (BDMarket topMarket : topMarkets) {
            if (topMarket.getId().equals(market.getId())) {
                inTop10 = true;
                break;
            }
        }
        
        if (!inTop10) {
            int yourRank = getMarketRank(market);
            ItemStack yourMarketItem = createGuiItem(Material.EMERALD, 
                    ChatColor.GREEN + "Your Market",
                    ChatColor.YELLOW + "Rank: " + ChatColor.WHITE + "#" + yourRank,
                    ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel(),
                    ChatColor.YELLOW + "Net Worth: " + ChatColor.GOLD + formatNetWorth(market.getNetWorth()) + " BD coins",
                    ChatColor.YELLOW + "Weekly Trades: " + ChatColor.WHITE + market.getWeeklyTradeCount());
            
            menu.setItem(40, yourMarketItem);
        }
        
        // Back button
        ItemStack backItem = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu",
                ChatColor.GRAY + "Return to the market management menu");
        menu.setItem(45, backItem);
        
        player.openInventory(menu);
    }
    
    /**
     * Processes a click in the market management GUI.
     * 
     * @param player The player
     * @param inventory The inventory
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    public boolean handleClick(Player player, Inventory inventory, int slot) {
        UUID playerId = player.getUniqueId();
        
        if (!playerCurrentMarket.containsKey(playerId) || !playerCurrentMenu.containsKey(playerId)) {
            return false;
        }
        
        BDMarket market = playerCurrentMarket.get(playerId);
        GUIType currentMenu = playerCurrentMenu.get(playerId);
        
        // Process based on current menu
        switch (currentMenu) {
            case MAIN_MENU:
                return handleMainMenuClick(player, market, slot);
            case ASSOCIATE_MANAGEMENT:
                return handleAssociateMenuClick(player, market, slot);
            case PERMISSIONS_MANAGEMENT:
                return handlePermissionsMenuClick(player, market, slot);
            case SECURITY_SETTINGS:
                return handleSecurityMenuClick(player, market, slot);
            case MARKET_UPGRADES:
                return handleUpgradesMenuClick(player, market, slot);
            case MARKET_SETTINGS:
                return handleSettingsMenuClick(player, market, slot);
            case MARKET_LEADERBOARD:
                return handleLeaderboardMenuClick(player, market, slot);
            default:
                return false;
        }
    }
    
    /**
     * Processes a click in the main menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleMainMenuClick(Player player, BDMarket market, int slot) {
        switch (slot) {
            case 10: // Associates
                openAssociateMenu(player);
                return true;
            case 12: // Permissions
                openPermissionsMenu(player);
                return true;
            case 14: // Security
                openSecurityMenu(player);
                return true;
            case 16: // Upgrades
                openUpgradesMenu(player);
                return true;
            case 28: // Settings
                openSettingsMenu(player);
                return true;
            case 30: // Leaderboard
                openLeaderboardMenu(player);
                return true;
            case 32: // Exit
                cleanupPlayer(player);
                player.closeInventory();
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Processes a click in the associate menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleAssociateMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 45) {
            openMainMenu(player, market);
            return true;
        }
        
        // Add new associate
        if (slot == 49 && market.getAssociates().size() < 5) {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Type the name of the player you want to add as an associate.");
            player.sendMessage(ChatColor.GREEN + "Use " + ChatColor.GOLD + "/bdmarket associate add <player>" + 
                    ChatColor.GREEN + " to add them.");
            cleanupPlayer(player);
            return true;
        }
        
        // Check if clicking on an associate
        Map<Integer, UUID> slotMap = playerAssociateSlots.get(player.getUniqueId());
        if (slotMap != null && slotMap.containsKey(slot)) {
            // Get associate info
            UUID associateId = slotMap.get(slot);
            String associateName = "Unknown";
            
            // Try to get player name
            Player associatePlayer = plugin.getServer().getPlayer(associateId);
            if (associatePlayer != null) {
                associateName = associatePlayer.getName();
            } else {
                // Try to get from offline player data
                associateName = plugin.getServer().getOfflinePlayer(associateId).getName();
            }
            
            // Confirm removal
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Are you sure you want to remove " + associateName + 
                    " as an associate?");
            player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GOLD + "/bdmarket associate remove " + 
                    associateName + ChatColor.YELLOW + " to confirm.");
            cleanupPlayer(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes a click in the permissions menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handlePermissionsMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 27) {
            openMainMenu(player, market);
            return true;
        }
        
        // Public/Private toggle
        if (slot == 22) {
            boolean newState = !market.isPublicAccess();
            market.setPublicAccess(newState);
            
            String accessText = newState ? "public" : "private";
            player.sendMessage(ChatColor.GREEN + "Market access set to " + accessText + ".");
            
            // Refresh menu
            openPermissionsMenu(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes a click in the security menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleSecurityMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 27) {
            openMainMenu(player, market);
            return true;
        }
        
        // Building toggle
        if (slot == 21) {
            boolean newState = !market.isOpenBuild();
            market.setOpenBuild(newState);
            
            String buildText = newState ? "open" : "restricted";
            player.sendMessage(ChatColor.GREEN + "Building permissions set to " + buildText + ".");
            
            // Refresh menu
            openSecurityMenu(player);
            return true;
        }
        
        // Breaking toggle
        if (slot == 23) {
            boolean newState = !market.isOpenBreak();
            market.setOpenBreak(newState);
            
            String breakText = newState ? "open" : "restricted";
            player.sendMessage(ChatColor.GREEN + "Breaking permissions set to " + breakText + ".");
            
            // Refresh menu
            openSecurityMenu(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes a click in the upgrades menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleUpgradesMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 27) {
            openMainMenu(player, market);
            return true;
        }
        
        // Upgrade button
        if (slot == 25 && market.getLevel() < 4) {
            // Attempt to upgrade the market
            if (attemptUpgrade(player, market)) {
                player.sendMessage(ChatColor.GREEN + "Market upgraded to level " + market.getLevel() + "!");
                // Refresh menu to show new level
                openUpgradesMenu(player);
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes a click in the settings menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleSettingsMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 27) {
            openMainMenu(player, market);
            return true;
        }
        
        // Particles toggle
        if (slot == 20) {
            boolean newState = !market.hasParticles();
            market.setParticles(newState);
            
            String particleText = newState ? "enabled" : "disabled";
            player.sendMessage(ChatColor.GREEN + "Market particles " + particleText + ".");
            
            // Refresh menu
            openSettingsMenu(player);
            return true;
        }
        
        // Sounds toggle
        if (slot == 24) {
            boolean newState = !market.hasSounds();
            market.setSounds(newState);
            
            String soundText = newState ? "enabled" : "disabled";
            player.sendMessage(ChatColor.GREEN + "Market sounds " + soundText + ".");
            
            // Refresh menu
            openSettingsMenu(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Processes a click in the leaderboard menu.
     * 
     * @param player The player
     * @param market The market
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    private boolean handleLeaderboardMenuClick(Player player, BDMarket market, int slot) {
        // Back button
        if (slot == 45) {
            openMainMenu(player, market);
            return true;
        }
        
        return false;
    }
    
    /**
     * Attempts to upgrade a market.
     * 
     * @param player The player
     * @param market The market
     * @return True if the market was upgraded
     */
    private boolean attemptUpgrade(Player player, BDMarket market) {
        // Admin bypass
        if (player.hasPermission("bdcraft.admin.market")) {
            return marketManager.upgradeMarket(market);
        }
        
        // Check if player has necessary items
        List<ItemStack> requiredItems = getUpgradeItemRequirements(market.getLevel());
        
        // Check if player has all required items
        for (ItemStack requiredItem : requiredItems) {
            if (!playerHasItem(player, requiredItem)) {
                player.sendMessage(ChatColor.RED + "You need " + formatItemRequirement(requiredItem) + 
                        " to upgrade your market.");
                return false;
            }
        }
        
        // Remove required items
        for (ItemStack requiredItem : requiredItems) {
            removeItemFromPlayer(player, requiredItem);
        }
        
        // Upgrade market
        return marketManager.upgradeMarket(market);
    }
    
    /**
     * Checks if a player has a specific item in their inventory.
     * 
     * @param player The player
     * @param requiredItem The required item
     * @return True if the player has the item
     */
    private boolean playerHasItem(Player player, ItemStack requiredItem) {
        int totalAmount = 0;
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == requiredItem.getType()) {
                totalAmount += item.getAmount();
            }
        }
        
        return totalAmount >= requiredItem.getAmount();
    }
    
    /**
     * Removes items from a player's inventory.
     * 
     * @param player The player
     * @param itemToRemove The item to remove
     */
    private void removeItemFromPlayer(Player player, ItemStack itemToRemove) {
        int amountToRemove = itemToRemove.getAmount();
        Material material = itemToRemove.getType();
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                if (item.getAmount() <= amountToRemove) {
                    amountToRemove -= item.getAmount();
                    item.setAmount(0);
                } else {
                    item.setAmount(item.getAmount() - amountToRemove);
                    amountToRemove = 0;
                }
                
                if (amountToRemove <= 0) {
                    break;
                }
            }
        }
    }
    
    /**
     * Formats an item requirement for display.
     * 
     * @param item The item
     * @return The formatted requirement
     */
    private String formatItemRequirement(ItemStack item) {
        String itemName = item.getType().toString().toLowerCase().replace('_', ' ');
        return item.getAmount() + " " + itemName;
    }
    
    /**
     * Gets the required items for an upgrade based on the current level.
     * 
     * @param currentLevel The current level
     * @return The required items
     */
    private List<ItemStack> getUpgradeItemRequirements(int currentLevel) {
        List<ItemStack> requirements = new ArrayList<>();
        
        switch (currentLevel) {
            case 1: // Level 1 to 2
                requirements.add(new ItemStack(Material.EMERALD, 16));
                requirements.add(new ItemStack(Material.WHEAT, 64));
                break;
            case 2: // Level 2 to 3
                requirements.add(new ItemStack(Material.EMERALD, 32));
                requirements.add(new ItemStack(Material.GOLD_INGOT, 16));
                requirements.add(new ItemStack(Material.WHEAT, 128));
                break;
            case 3: // Level 3 to 4
                requirements.add(new ItemStack(Material.EMERALD, 64));
                requirements.add(new ItemStack(Material.DIAMOND, 8));
                requirements.add(new ItemStack(Material.WHEAT, 192));
                break;
        }
        
        return requirements;
    }
    
    /**
     * Gets the upgrade requirements as lore strings.
     * 
     * @param currentLevel The current level
     * @return The requirement strings
     */
    private List<String> getUpgradeRequirements(int currentLevel) {
        List<String> requirements = new ArrayList<>();
        
        switch (currentLevel) {
            case 1: // Level 1 to 2
                requirements.add(ChatColor.AQUA + "• 16 Emeralds");
                requirements.add(ChatColor.AQUA + "• 64 Wheat");
                break;
            case 2: // Level 2 to 3
                requirements.add(ChatColor.AQUA + "• 32 Emeralds");
                requirements.add(ChatColor.AQUA + "• 16 Gold Ingots");
                requirements.add(ChatColor.AQUA + "• 128 Wheat");
                break;
            case 3: // Level 3 to 4
                requirements.add(ChatColor.AQUA + "• 64 Emeralds");
                requirements.add(ChatColor.AQUA + "• 8 Diamonds");
                requirements.add(ChatColor.AQUA + "• 192 Wheat");
                break;
        }
        
        return requirements;
    }
    
    /**
     * Gets the top markets by net worth.
     * 
     * @param limit The maximum number of markets to return
     * @return The top markets
     */
    private List<BDMarket> getTopMarkets(int limit) {
        // Get all markets
        List<BDMarket> allMarkets = new ArrayList<>();
        for (String worldName : marketManager.getMarketWorlds()) {
            allMarkets.addAll(marketManager.getMarketsInWorld(worldName));
        }
        
        // Sort by net worth (descending)
        allMarkets.sort((m1, m2) -> Double.compare(m2.getNetWorth(), m1.getNetWorth()));
        
        // Limit to top markets
        if (allMarkets.size() > limit) {
            return allMarkets.subList(0, limit);
        }
        
        return allMarkets;
    }
    
    /**
     * Gets a market's rank in the leaderboard.
     * 
     * @param market The market
     * @return The market's rank
     */
    private int getMarketRank(BDMarket market) {
        // Get all markets
        List<BDMarket> allMarkets = new ArrayList<>();
        for (String worldName : marketManager.getMarketWorlds()) {
            allMarkets.addAll(marketManager.getMarketsInWorld(worldName));
        }
        
        // Sort by net worth (descending)
        allMarkets.sort((m1, m2) -> Double.compare(m2.getNetWorth(), m1.getNetWorth()));
        
        // Find market rank
        for (int i = 0; i < allMarkets.size(); i++) {
            if (allMarkets.get(i).getId().equals(market.getId())) {
                return i + 1;
            }
        }
        
        return -1;
    }
    
    /**
     * Formats a net worth value for display.
     * 
     * @param netWorth The net worth
     * @return The formatted net worth
     */
    private String formatNetWorth(double netWorth) {
        if (netWorth >= 1000000) {
            return String.format("%.1fM", netWorth / 1000000);
        } else if (netWorth >= 1000) {
            return String.format("%.1fK", netWorth / 1000);
        } else {
            return String.format("%.0f", netWorth);
        }
    }
    
    /**
     * Gets the maximum number of collectors for a market.
     * 
     * @param market The market
     * @return The maximum number of collectors
     */
    private int getMaxCollectors(BDMarket market) {
        return getMaxCollectorsForLevel(market.getLevel());
    }
    
    /**
     * Gets the maximum number of collectors for a specific level.
     * 
     * @param level The level
     * @return The maximum number of collectors
     */
    private int getMaxCollectorsForLevel(int level) {
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Gets the price modifier for a specific level.
     * 
     * @param level The level
     * @return The price modifier
     */
    private double getModifierForLevel(int level) {
        switch (level) {
            case 1:
                return 1.0;
            case 2:
                return 1.1;
            case 3:
                return 1.2;
            case 4:
                return 1.3;
            default:
                return 1.0;
        }
    }
    
    /**
     * Creates a GUI item with custom name and lore.
     * 
     * @param material The material
     * @param name The name
     * @param lore The lore
     * @return The item stack
     */
    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(name);
            
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Removes a player from tracking when they disconnect.
     * 
     * @param player The player
     */
    public void cleanupPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        playerCurrentMarket.remove(playerId);
        playerCurrentMenu.remove(playerId);
        playerAssociateSlots.remove(playerId);
    }
}