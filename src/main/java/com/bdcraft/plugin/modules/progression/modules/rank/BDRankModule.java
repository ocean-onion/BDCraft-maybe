package com.bdcraft.plugin.modules.progression.modules.rank;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.progression.Rank;
import com.bdcraft.plugin.modules.progression.modules.rebirth.BDRebirthModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles player ranks, including progression and permissions.
 * Based on the original BDRankManager implementation.
 */
public class BDRankModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Rank progression levels (as per documentation - do not modify)
    public static final int RANK_NEWCOMER = 1;
    public static final int RANK_FARMER = 2;
    public static final int RANK_EXPERT_FARMER = 3;
    public static final int RANK_MASTER_FARMER = 4;
    public static final int RANK_AGRICULTURAL_EXPERT = 5;
    public static final int RANK_REBORN = 6; // Special rebirthable state
    
    // Currency costs for each rank as per documentation
    private final int[] rankCosts = new int[]{
        0,      // Newcomer (starting rank)
        5000,   // Farmer
        15000,  // Expert Farmer
        30000,  // Master Farmer
        60000,  // Agricultural Expert
        0       // Reborn status (uses special rebirth system)
    };
    
    // Experience points required for each rank (now based on currency costs)
    private final int[] rankExperienceRequirements = new int[]{
        0,      // Rank 0 (unused)
        0,      // Newcomer (starting rank)
        5000,   // Farmer
        15000,  // Expert Farmer
        30000,  // Master Farmer
        60000,  // Agricultural Expert
        100000  // Reborn status
    };
    
    // Rank names
    private final String[] rankNames = new String[]{
        "", // Index 0 is unused
        "Newcomer",
        "Farmer",
        "Expert Farmer",
        "Master Farmer",
        "Agricultural Expert",
        "Reborn Farmer"
    };
    
    // Rank display colors
    private final ChatColor[] rankColors = new ChatColor[]{
        ChatColor.WHITE,      // Unused
        ChatColor.GRAY,       // Newcomer
        ChatColor.GREEN,      // Farmer
        ChatColor.BLUE,       // Expert Farmer
        ChatColor.GOLD,       // Master Farmer
        ChatColor.AQUA,       // Agricultural Expert
        ChatColor.LIGHT_PURPLE// Reborn status
    };
    
    // Player data
    private final Map<UUID, Integer> playerRanks = new HashMap<>();
    private final Map<UUID, Integer> playerExperience = new HashMap<>();
    
    /**
     * Creates a new rank module.
     * 
     * @param plugin The plugin instance
     */
    public BDRankModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public String getName() {
        return "Rank";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Rank submodule");
        
        // Load player ranks
        loadPlayerRanks();
        
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
        
        plugin.getLogger().info("Disabling Rank submodule");
        
        // Save player ranks
        savePlayerRanks();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadPlayerRanks();
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Loads player ranks.
     */
    private void loadPlayerRanks() {
        // Clear existing data
        playerRanks.clear();
        playerExperience.clear();
        
        // TODO: Implement loading player data from database or file
        logger.info("Loaded rank data from storage");
    }
    
    /**
     * Saves player ranks.
     */
    private void savePlayerRanks() {
        // TODO: Implement saving player data to database or file
        logger.info("Saved rank data to storage");
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register rank command
        PluginCommand rankCommand = plugin.getCommand("bdrank");
        if (rankCommand != null) {
            rankCommand.setExecutor(this);
        }
    }
    
    /**
     * Gets a player's current rank as an integer.
     * 
     * @param player The player
     * @return The player's rank
     */
    public int getPlayerRank(Player player) {
        return playerRanks.getOrDefault(player.getUniqueId(), RANK_NEWCOMER);
    }
    
    /**
     * Gets a player's rank based on UUID.
     * 
     * @param playerId The player's UUID
     * @return The player's rank
     */
    public Rank getPlayerRank(UUID playerId) {
        int rank = playerRanks.getOrDefault(playerId, RANK_NEWCOMER);
        
        // Convert internal rank to Rank enum
        switch (rank) {
            case RANK_NEWCOMER:
                return Rank.DEFAULT;
            case RANK_FARMER:
                return Rank.NOVICE;
            case RANK_EXPERT_FARMER:
                return Rank.APPRENTICE;
            case RANK_MASTER_FARMER:
                return Rank.EXPERT;
            case RANK_AGRICULTURAL_EXPERT:
                return Rank.MASTER;
            case RANK_REBORN:
                return Rank.LEGEND;
            default:
                return Rank.DEFAULT;
        }
    }
    
    /**
     * Sets a player's rank.
     * 
     * @param player The player
     * @param rank The new rank
     */
    public void setPlayerRank(Player player, int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return;
        }
        
        playerRanks.put(player.getUniqueId(), rank);
        updatePlayerRankDisplayName(player);
    }
    
    /**
     * Sets a player's rank based on UUID and Rank enum.
     * 
     * @param playerId The player's UUID
     * @param rank The rank
     * @return Whether the rank was set
     */
    public boolean setPlayerRank(UUID playerId, Rank rank) {
        int internalRank;
        
        // Convert Rank enum to internal rank
        switch (rank) {
            case DEFAULT:
                internalRank = RANK_NEWCOMER;
                break;
            case NOVICE:
                internalRank = RANK_FARMER;
                break;
            case APPRENTICE:
                internalRank = RANK_EXPERT_FARMER;
                break;
            case EXPERT:
                internalRank = RANK_MASTER_FARMER;
                break;
            case MASTER:
                internalRank = RANK_AGRICULTURAL_EXPERT;
                break;
            case LEGEND:
                internalRank = RANK_REBORN;
                break;
            default:
                internalRank = RANK_NEWCOMER;
                break;
        }
        
        playerRanks.put(playerId, internalRank);
        
        // Update player if online
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null && player.isOnline()) {
            updatePlayerRankDisplayName(player);
        }
        
        return true;
    }
    
    /**
     * Gets a player's current experience points by UUID.
     * 
     * @param playerId The player's UUID
     * @return The player's experience points
     */
    public int getPlayerExperience(UUID playerId) {
        return playerExperience.getOrDefault(playerId, 0);
    }
    
    /**
     * Gets a player's current experience points.
     * 
     * @param player The player
     * @return The player's experience points
     */
    public int getPlayerExperience(Player player) {
        return getPlayerExperience(player.getUniqueId());
    }
    
    /**
     * Gets a player's rebirth count by UUID.
     * 
     * @param playerId The player's UUID
     * @return The player's rebirth count
     */
    public int getPlayerRebirths(UUID playerId) {
        BDRebirthModule rebirthModule = (BDRebirthModule) parentModule.getSubmodule("Rebirth");
        if (rebirthModule != null && rebirthModule.isEnabled()) {
            return rebirthModule.getRebirthLevel(playerId);
        }
        return 0;
    }
    
    /**
     * Gets a player's rebirth count.
     * 
     * @param player The player
     * @return The player's rebirth count
     */
    public int getPlayerRebirths(Player player) {
        return getPlayerRebirths(player.getUniqueId());
    }
    
    /**
     * Sets a player's experience points by UUID.
     * 
     * @param playerId The player's UUID
     * @param experience The new experience points
     */
    public void setPlayerExperience(UUID playerId, int experience) {
        if (experience < 0) {
            experience = 0;
        }
        
        playerExperience.put(playerId, experience);
        
        // We can't check for rank up directly with just a UUID
        // Client code should call checkRankUp if a Player object is available
    }
    
    /**
     * Sets a player's experience points.
     * 
     * @param player The player
     * @param experience The new experience points
     */
    public void setPlayerExperience(Player player, int experience) {
        setPlayerExperience(player.getUniqueId(), experience);
        
        // Check for rank up
        checkRankUp(player);
    }
    
    /**
     * Adds experience points to a player by UUID.
     * 
     * @param playerId The player's UUID
     * @param experienceToAdd The experience points to add
     * @return The player's new experience points
     */
    public int addPlayerExperience(UUID playerId, int experienceToAdd) {
        if (experienceToAdd <= 0) {
            return getPlayerExperience(playerId);
        }
        
        // Apply rebirth bonus if applicable
        int rebirths = getPlayerRebirths(playerId);
        if (rebirths > 0) {
            // 10% bonus per rebirth
            experienceToAdd += (int)(experienceToAdd * (rebirths * 0.10));
        }
        
        int currentExperience = getPlayerExperience(playerId);
        int newExperience = currentExperience + experienceToAdd;
        
        setPlayerExperience(playerId, newExperience);
        
        return newExperience;
    }
    
    /**
     * Adds experience points to a player.
     * 
     * @param player The player
     * @param experienceToAdd The experience points to add
     * @return The player's new experience points
     */
    public int addPlayerExperience(Player player, int experienceToAdd) {
        return addPlayerExperience(player.getUniqueId(), experienceToAdd);
    }
    
    /**
     * Checks if a player can rank up and performs the rank up if possible.
     * 
     * @param player The player
     * @return Whether the player ranked up
     */
    public boolean checkRankUp(Player player) {
        int currentRank = getPlayerRank(player);
        int currentExperience = getPlayerExperience(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return false;
        }
        
        // Check if player has enough experience to rank up
        int nextRank = currentRank + 1;
        int requiredExperience = rankExperienceRequirements[nextRank];
        
        if (currentExperience >= requiredExperience) {
            // Rank up the player
            setPlayerRank(player, nextRank);
            
            // Notify player
            player.sendMessage(ChatColor.GREEN + "Congratulations! You have ranked up to " + 
                    rankColors[nextRank] + rankNames[nextRank] + ChatColor.GREEN + "!");
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a player's rank name.
     * 
     * @param player The player
     * @return The rank name
     */
    public String getPlayerRankName(Player player) {
        int rank = getPlayerRank(player);
        return rankNames[rank];
    }
    
    /**
     * Gets a player's colored rank name.
     * 
     * @param player The player
     * @return The colored rank name
     */
    public String getPlayerColoredRankName(Player player) {
        int rank = getPlayerRank(player);
        return rankColors[rank] + rankNames[rank];
    }
    
    /**
     * Gets the display format for a player's rank and name.
     * 
     * @param player The player
     * @return The formatted display
     */
    public String getPlayerDisplayName(Player player) {
        int rank = getPlayerRank(player);
        int rebirths = getPlayerRebirths(player);
        
        StringBuilder display = new StringBuilder();
        
        // Add rebirth stars if applicable
        if (rebirths > 0) {
            for (int i = 0; i < Math.min(rebirths, 5); i++) {
                display.append(ChatColor.YELLOW).append("★");
            }
            
            if (rebirths > 5) {
                display.append(ChatColor.YELLOW).append("[").append(rebirths).append("]");
            }
            
            display.append(" ");
        }
        
        // Add rank and name
        display.append(rankColors[rank]).append("[").append(rankNames[rank]).append("] ")
               .append(ChatColor.WHITE).append(player.getName());
        
        return display.toString();
    }
    
    /**
     * Updates a player's display name based on their rank.
     * 
     * @param player The player
     */
    private void updatePlayerRankDisplayName(Player player) {
        int rank = getPlayerRank(player);
        String displayName = rankColors[rank] + player.getName();
        player.setDisplayName(displayName);
    }
    
    /**
     * Gets the experience required to reach the next rank.
     * 
     * @param player The player
     * @return The experience required, or -1 if already at max rank
     */
    public int getExperienceRequiredForNextRank(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return -1;
        }
        
        int nextRank = currentRank + 1;
        return rankExperienceRequirements[nextRank];
    }
    
    /**
     * Gets the progress percentage towards the next rank.
     * 
     * @param player The player
     * @return The progress percentage (0-100), or 100 if already at max rank
     */
    public int getProgressPercentage(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return 100;
        }
        
        int currentExperience = getPlayerExperience(player);
        int nextRankExperience = rankExperienceRequirements[currentRank + 1];
        int currentRankExperience = rankExperienceRequirements[currentRank];
        
        int expNeeded = nextRankExperience - currentRankExperience;
        int expGained = currentExperience - currentRankExperience;
        
        return (int)(((double)expGained / expNeeded) * 100);
    }
    
    /**
     * Gets a progress bar for display in chat.
     * 
     * @param player The player
     * @return The progress bar
     */
    public String getProgressBar(Player player) {
        int percentage = getProgressPercentage(player);
        int bars = percentage / 5; // 20 bars total for 100%
        
        StringBuilder progressBar = new StringBuilder(ChatColor.GRAY + "[");
        
        for (int i = 0; i < 20; i++) {
            if (i < bars) {
                progressBar.append(ChatColor.GREEN).append("|");
            } else {
                progressBar.append(ChatColor.RED).append("|");
            }
        }
        
        progressBar.append(ChatColor.GRAY).append("] ").append(percentage).append("%");
        
        return progressBar.toString();
    }
    
    /**
     * Checks if a player has a specific minimum rank.
     * 
     * @param player The player
     * @param requiredRank The minimum required rank
     * @return Whether the player has the required rank
     */
    public boolean hasRank(Player player, int requiredRank) {
        return getPlayerRank(player) >= requiredRank;
    }
    
    /**
     * Gets the experience bonus multiplier based on rebirth count.
     * 
     * @param player The player
     * @return The experience multiplier (1.0 = no bonus)
     */
    public double getExperienceMultiplier(Player player) {
        int rebirths = getPlayerRebirths(player);
        return 1.0 + (rebirths * 0.10); // 10% bonus per rebirth
    }
    
    /**
     * Gets the experience needed for the next rank.
     * 
     * @param player The player
     * @return The experience needed for the next rank
     */
    public int getExperienceForNextRank(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return -1;
        }
        
        int nextRank = currentRank + 1;
        return rankExperienceRequirements[nextRank];
    }
    
    /**
     * Gets the name of a rank.
     * 
     * @param rank The rank
     * @return The rank name
     */
    public String getRankName(int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return "Unknown Rank";
        }
        
        return rankNames[rank];
    }
    
    /**
     * Gets the experience required for a specific rank.
     * 
     * @param rank The rank
     * @return The experience required for this rank
     */
    public int getExperienceForRank(int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return 0;
        }
        
        return rankExperienceRequirements[rank];
    }
    
    /**
     * Gets the color for a specific rank.
     * 
     * @param rank The rank
     * @return The ChatColor for this rank
     */
    public ChatColor getRankColor(int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return ChatColor.WHITE;
        }
        
        return rankColors[rank];
    }
    
    /**
     * Checks if a player's rank is at least a certain rank.
     * 
     * @param playerRank The player's rank
     * @param requiredRank The required rank
     * @return Whether the player's rank is at least the required rank
     */
    public boolean isRankAtLeast(String playerRank, String requiredRank) {
        int playerRankValue = -1;
        int requiredRankValue = -1;
        
        // Find player rank value
        for (int i = 0; i < rankNames.length; i++) {
            if (rankNames[i].equalsIgnoreCase(playerRank)) {
                playerRankValue = i;
                break;
            }
        }
        
        // Find required rank value
        for (int i = 0; i < rankNames.length; i++) {
            if (rankNames[i].equalsIgnoreCase(requiredRank)) {
                requiredRankValue = i;
                break;
            }
        }
        
        // If ranks not found, return false
        if (playerRankValue == -1 || requiredRankValue == -1) {
            return false;
        }
        
        return playerRankValue >= requiredRankValue;
    }
    
    /**
     * Adds experience to a player.
     * 
     * @param player The player
     * @param experience The experience to add
     */
    public void addExperience(Player player, int experience) {
        addPlayerExperience(player, experience);
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Make sure this player has a rank
        if (!playerRanks.containsKey(player.getUniqueId())) {
            playerRanks.put(player.getUniqueId(), RANK_NEWCOMER);
        }
        
        // Update display name
        updatePlayerRankDisplayName(player);
    }
    
    /**
     * Gets a comma-separated list of all ranks.
     * 
     * @return The list of ranks
     */
    private String getRanksList() {
        StringBuilder ranks = new StringBuilder();
        for (Rank rank : Rank.values()) {
            ranks.append(rank.name()).append(", ");
        }
        
        if (ranks.length() > 2) {
            ranks.setLength(ranks.length() - 2);
        }
        
        return ranks.toString();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bdrank")) {
            if (args.length == 0) {
                // Show rank info
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    int rank = getPlayerRank(player);
                    int exp = getPlayerExperience(player);
                    
                    sender.sendMessage(ChatColor.YELLOW + "==== Your Rank Info ====");
                    sender.sendMessage(ChatColor.YELLOW + "Rank: " + rankColors[rank] + rankNames[rank]);
                    sender.sendMessage(ChatColor.YELLOW + "Experience: " + ChatColor.WHITE + exp);
                    
                    // Show progress to next rank
                    if (rank < RANK_AGRICULTURAL_EXPERT) {
                        int nextRankExp = rankExperienceRequirements[rank + 1];
                        int expNeeded = nextRankExp - exp;
                        sender.sendMessage(ChatColor.YELLOW + "Next Rank: " + 
                                rankColors[rank + 1] + rankNames[rank + 1] + 
                                ChatColor.YELLOW + " (" + expNeeded + " exp needed)");
                        sender.sendMessage(getProgressBar(player));
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "You have reached the maximum rank!");
                    }
                    
                    // Show rebirth info
                    int rebirths = getPlayerRebirths(player);
                    if (rebirths > 0) {
                        sender.sendMessage(ChatColor.YELLOW + "Rebirth Level: " + ChatColor.LIGHT_PURPLE + rebirths);
                        sender.sendMessage(ChatColor.YELLOW + "EXP Multiplier: " + ChatColor.GREEN + 
                                String.format("%.2fx", getExperienceMultiplier(player)));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Console does not have a rank.");
                }
                return true;
            }
            
            String subCommand = args[0].toLowerCase();
            
            if (subCommand.equals("set") && args.length >= 3) {
                // Admin command to set rank
                if (!sender.hasPermission("bdprogression.rank.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to set ranks.");
                    return true;
                }
                
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                try {
                    Rank rank = Rank.valueOf(args[2].toUpperCase());
                    setPlayerRank(target.getUniqueId(), rank);
                    sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s rank to " + rank.getFormattedName() + ".");
                    target.sendMessage(ChatColor.GREEN + "Your rank was set to " + rank.getFormattedName() + ".");
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid rank. Valid ranks: " + getRanksList());
                }
                return true;
            } else if (subCommand.equals("exp") && args.length >= 3) {
                // Admin command to set experience
                if (!sender.hasPermission("bdprogression.rank.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to set experience.");
                    return true;
                }
                
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                try {
                    int exp = Integer.parseInt(args[2]);
                    if (exp < 0) {
                        exp = 0;
                    }
                    
                    setPlayerExperience(target, exp);
                    sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s experience to " + exp + ".");
                    target.sendMessage(ChatColor.GREEN + "Your experience was set to " + exp + ".");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid experience value. Must be a number.");
                }
                return true;
            } else if (subCommand.equals("add") && args.length >= 3) {
                // Admin command to add experience
                if (!sender.hasPermission("bdprogression.rank.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to add experience.");
                    return true;
                }
                
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                
                try {
                    int expToAdd = Integer.parseInt(args[2]);
                    if (expToAdd <= 0) {
                        sender.sendMessage(ChatColor.RED + "Experience to add must be positive.");
                        return true;
                    }
                    
                    int newExp = addPlayerExperience(target, expToAdd);
                    sender.sendMessage(ChatColor.GREEN + "Added " + expToAdd + " experience to " + target.getName() + ". New total: " + newExp + ".");
                    target.sendMessage(ChatColor.GREEN + "You gained " + expToAdd + " experience. New total: " + newExp + ".");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid experience value. Must be a number.");
                }
                return true;
            } else if (subCommand.equals("help")) {
                // Show help
                sender.sendMessage(ChatColor.YELLOW + "==== BD Rank Commands ====");
                sender.sendMessage(ChatColor.YELLOW + "/bdrank - Show your rank info");
                
                if (sender.hasPermission("bdprogression.rank.admin")) {
                    sender.sendMessage(ChatColor.YELLOW + "/bdrank set <player> <rank> - Set a player's rank");
                    sender.sendMessage(ChatColor.YELLOW + "/bdrank exp <player> <amount> - Set a player's experience");
                    sender.sendMessage(ChatColor.YELLOW + "/bdrank add <player> <amount> - Add experience to a player");
                }
                
                return true;
            }
        }
        
        return false;
    }
}