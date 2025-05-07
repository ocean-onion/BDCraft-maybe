# BDCraft Internal API Documentation

**Note: BDCraft is designed as a fully self-contained plugin with no support for external integrations or third-party plugins.**

This documentation is provided for reference purposes only, primarily for server administrators and developers who wish to understand the internal workings of the plugin. The BDCraft API is not intended for external plugin integration.

## Internal Module Structure

The following information describes how BDCraft's internal modules interact:

### Internal API Reference

This documentation describes the internal API structure for reference purposes only. As a fully self-contained plugin, BDCraft does not expose its API for third-party plugin development. The following internal modules work together to provide the complete functionality:

## Core API

### Internal Class Structure

**Note: This is internal structure documentation only. BDCraft is fully self-contained and does not support third-party plugin integration.**

The following code examples demonstrate the internal architecture for reference purposes only:

```java
import com.bdcraft.api.BDCraftAPI;

// INTERNAL CODE - FOR DOCUMENTATION PURPOSES ONLY
// BDCraft does not support external plugin integration
public class InternalStructureExample {
    private BDCraftAPI bdApi;
    
    public void initializeComponents() {
        bdApi = BDCraftAPI.getInstance();
        // Internal module initialization
    }
}
```

## Economy API

The Economy API allows you to interact with the BD economy system.

### Currency Management

```java
// Get the BD Economy Module
BDEconomyAPI economyApi = bdApi.getEconomyAPI();

// Get player balance
double balance = economyApi.getBalance(player);

// Add currency to player
economyApi.addCurrency(player, 500);

// Remove currency from player
economyApi.removeCurrency(player, 200);

// Set player currency
economyApi.setCurrency(player, 1000);

// Check if player has enough currency
boolean hasEnough = economyApi.hasEnough(player, 300);
```

### Rank Management

```java
// Get player rank
int rank = economyApi.getRankManager().getPlayerRank(player);

// Set player rank
economyApi.getRankManager().setPlayerRank(player, 3);

// Get rank name
String rankName = economyApi.getRankManager().getRankName(3); // Returns "Expert Farmer"

// Check if player has particular rank
boolean hasRank = economyApi.getRankManager().hasRank(player, 2); // Checks if player is at least Farmer
```

### Rebirth System

```java
// Get player rebirth level
int rebirthLevel = economyApi.getRebirthManager().getRebirthLevel(player);

// Set player rebirth level
economyApi.getRebirthManager().setRebirthLevel(player, 2);

// Check if player is eligible for rebirth
boolean canRebirth = economyApi.getRebirthManager().canRebirth(player);

// Perform rebirth
boolean success = economyApi.getRebirthManager().performRebirth(player);
```

## BD Items API

The Items API allows you to create, check, and manipulate BD items.

### Creating BD Items

```java
// Get the BD Items API
BDItemsAPI itemsApi = bdApi.getItemsAPI();

// Create BD Seeds
ItemStack regularSeeds = itemsApi.createBDSeed(5); // Creates 5 regular BD seeds
ItemStack greenSeeds = itemsApi.createGreenBDSeed(3); // Creates 3 green BD seeds
ItemStack purpleSeeds = itemsApi.createPurpleBDSeed(1); // Creates 1 purple BD seed

// Create BD Crops
ItemStack regularCrops = itemsApi.createBDCrop(10); // Creates 10 regular BD crops
ItemStack greenCrops = itemsApi.createGreenBDCrop(5); // Creates 5 green BD crops
ItemStack purpleCrops = itemsApi.createPurpleBDCrop(2); // Creates 2 purple BD crops

// Create BD Tools
ItemStack bdStick = itemsApi.createBDStick(); // Creates a BD Stick
ItemStack harvester = itemsApi.createBDHarvester(); // Creates a BD Harvester
ItemStack ultimateHarvester = itemsApi.createUltimateBDHarvester(); // Creates an Ultimate BD Harvester
```

### Checking BD Items

```java
// Check if item is a BD item
boolean isBDItem = itemsApi.isBDItem(item);

// Check specific BD item types
boolean isBDSeed = itemsApi.isBDSeed(item);
boolean isGreenBDSeed = itemsApi.isGreenBDSeed(item);
boolean isPurpleBDSeed = itemsApi.isPurpleBDSeed(item);

boolean isBDCrop = itemsApi.isBDCrop(item);
boolean isGreenBDCrop = itemsApi.isGreenBDCrop(item);
boolean isPurpleBDCrop = itemsApi.isPurpleBDCrop(item);

boolean isBDStick = itemsApi.isBDStick(item);
boolean isBDHarvester = itemsApi.isBDHarvester(item);
boolean isUltimateBDHarvester = itemsApi.isUltimateBDHarvester(item);
```

## Villager API

The Villager API allows you to create and manage BD villagers.

### Creating Villagers

```java
// Get the BD Villager API
BDVillagerAPI villagerApi = bdApi.getVillagerAPI();

// Create BD villagers
Villager dealer = villagerApi.createDealer(location, villageId);
Villager collector = villagerApi.createCollector(location, villageId);
Villager seasonalTrader = villagerApi.createSeasonalTrader(location, villageId);
```

### Managing Villages

```java
// Create a new village
String villageId = villagerApi.createVillage("farming_village1", centerLocation);

// Set village center
villagerApi.setVillageCenter(villageId, newCenterLocation);

// Get all villages
List<String> villages = villagerApi.getVillages();

// Get village center
Location center = villagerApi.getVillageCenter(villageId);

// Delete a village
villagerApi.deleteVillage(villageId);
```

### Managing Reputation

```java
// Get player reputation in a village
int reputation = villagerApi.getReputation(player, villageId);

// Add reputation
villagerApi.addReputation(player, villageId, 5);

// Remove reputation
villagerApi.removeReputation(player, villageId, 2);

// Set reputation
villagerApi.setReputation(player, villageId, 50);
```

## Permissions API

The Permissions API allows you to interact with the BDCraft permission system.

### Permission Management

```java
// Get the BD Permissions API
BDPermissionsAPI permsApi = bdApi.getPermissionsAPI();

// Check if player has permission
boolean hasPermission = permsApi.hasPermission(player, "bdcraft.economy.admin");

// Add permission to player
permsApi.addPermission(player, "bdcraft.special.ability");

// Remove permission from player
permsApi.removePermission(player, "bdcraft.special.ability");

// Add player to group
permsApi.addToGroup(player, "vip");

// Remove player from group
permsApi.removeFromGroup(player, "vip");
```

### Group Management

```java
// Create a new permission group
permsApi.createGroup("vip_plus");

// Delete a permission group
permsApi.deleteGroup("old_group");

// Add permission to group
permsApi.addGroupPermission("vip", "bdcraft.vip.benefits");

// Remove permission from group
permsApi.removeGroupPermission("vip", "bdcraft.vip.benefits");

// Get all groups
List<String> groups = permsApi.getGroups();

// Get group permissions
List<String> permissions = permsApi.getGroupPermissions("vip");
```

## Market API

The Market API allows you to interact with the player market system.

### Market Management

```java
// Get the BD Market API
BDMarketAPI marketApi = bdApi.getMarketAPI();

// Get all markets
List<String> markets = marketApi.getMarkets();

// Get market by ID
BDMarket market = marketApi.getMarket("market_id");

// Get markets owned by player
List<BDMarket> playerMarkets = marketApi.getPlayerMarkets(player);

// Check if location is in market
boolean isInMarket = marketApi.isInMarket(location);

// Get market at location
BDMarket marketAtLocation = marketApi.getMarketAt(location);
```

### Market Properties

```java
// Get market owner
UUID owner = market.getOwner();
Player ownerPlayer = Bukkit.getPlayer(owner);

// Get market level
int level = market.getLevel();

// Get market center
Location center = market.getCenter();

// Get market radius
int radius = market.getRadius();

// Get market creation date
Date creationDate = market.getCreationDate();

// Get market upgrade history
Map<Integer, Date> upgradeHistory = market.getUpgradeHistory();
```

### Market Associates

```java
// Check if player is an associate
boolean isAssociate = market.isAssociate(player);

// Add market associate
boolean success = market.addAssociate(player);

// Remove market associate
boolean removed = market.removeAssociate(player);

// Get market associates
List<UUID> associates = market.getAssociates();
```

## Event API

BDCraft provides custom events that you can listen to in your plugin.

### Listening to BD Events

```java
// BD Economy Events
@EventHandler
public void onCurrencyChange(BDCurrencyChangeEvent event) {
    Player player = event.getPlayer();
    double oldAmount = event.getOldAmount();
    double newAmount = event.getNewAmount();
    // Your code here
}

@EventHandler
public void onRankChange(BDRankChangeEvent event) {
    Player player = event.getPlayer();
    int oldRank = event.getOldRank();
    int newRank = event.getNewRank();
    // Your code here
}

@EventHandler
public void onRebirth(BDRebirthEvent event) {
    Player player = event.getPlayer();
    int newRebirthLevel = event.getNewRebirthLevel();
    // Your code here
}

// BD Villager Events
@EventHandler
public void onVillagerTrade(BDVillagerTradeEvent event) {
    Player player = event.getPlayer();
    String villageId = event.getVillageId();
    String villagerType = event.getVillagerType();
    // Your code here
}

@EventHandler
public void onReputationChange(BDReputationChangeEvent event) {
    Player player = event.getPlayer();
    String villageId = event.getVillageId();
    int oldReputation = event.getOldReputation();
    int newReputation = event.getNewReputation();
    // Your code here
}

// BD Crop Events
@EventHandler
public void onCropHarvest(BDCropHarvestEvent event) {
    Player player = event.getPlayer();
    Location location = event.getLocation();
    String cropType = event.getCropType();
    int amount = event.getAmount();
    // Your code here
}

// BD Market Events
@EventHandler
public void onMarketCreate(BDMarketCreateEvent event) {
    Player player = event.getPlayer();
    String marketId = event.getMarketId();
    Location center = event.getCenter();
    // Your code here
}

@EventHandler
public void onMarketUpgrade(BDMarketUpgradeEvent event) {
    Player player = event.getPlayer();
    String marketId = event.getMarketId();
    int oldLevel = event.getOldLevel();
    int newLevel = event.getNewLevel();
    // Your code here
}

@EventHandler
public void onMarketAssociateChange(BDMarketAssociateEvent event) {
    Player player = event.getPlayer();
    String marketId = event.getMarketId();
    UUID associate = event.getAssociate();
    boolean added = event.isAdded(); // true if added, false if removed
    // Your code here
}
```

## Self-Contained Architecture

**Important: BDCraft is a fully self-contained plugin that does not support external integrations.**

The plugin is designed to work independently without requiring or supporting any third-party plugins. This design choice provides several benefits:

1. **Stability**: No dependency conflicts or version compatibility issues
2. **Performance**: Optimized internal systems without external overhead
3. **Security**: Protected internal systems and data integrity
4. **Simplicity**: Easier installation and configuration for server administrators

## Internal Implementation Notes

The following internal structure is documented for reference purposes only:

```java
public class BDCraftIntegrationExample extends JavaPlugin implements Listener {
    private BDCraftAPI bdApi;
    
    @Override
    public void onEnable() {
        // Check if BDCraft is available
        if (Bukkit.getPluginManager().getPlugin("BDCraft") != null) {
            bdApi = BDCraftAPI.getInstance();
            getLogger().info("Successfully hooked into BDCraft!");
            
            // Register events
            Bukkit.getPluginManager().registerEvents(this, this);
            
            // Register command
            getCommand("bdcheck").setExecutor(this);
        } else {
            getLogger().warning("BDCraft not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bdcheck") && sender instanceof Player) {
            Player player = (Player) sender;
            BDEconomyAPI economyApi = bdApi.getEconomyAPI();
            BDMarketAPI marketApi = bdApi.getMarketAPI();
            
            // Display BD information
            player.sendMessage("Your BD Rank: " + economyApi.getRankManager().getRankName(
                economyApi.getRankManager().getPlayerRank(player)));
            player.sendMessage("Your BD Balance: " + economyApi.getBalance(player));
            player.sendMessage("Your Rebirth Level: " + economyApi.getRebirthManager().getRebirthLevel(player));
            
            // Display market information
            List<BDMarket> playerMarkets = marketApi.getPlayerMarkets(player);
            if (!playerMarkets.isEmpty()) {
                player.sendMessage("You own " + playerMarkets.size() + " market(s):");
                for (BDMarket market : playerMarkets) {
                    player.sendMessage(" - Market ID: " + market.getId() + ", Level: " + market.getLevel());
                }
            } else {
                player.sendMessage("You don't own any markets yet.");
            }
            
            // Check if player is in a market
            BDMarket currentMarket = marketApi.getMarketAt(player.getLocation());
            if (currentMarket != null) {
                player.sendMessage("You are currently in market: " + currentMarket.getId());
            }
            
            return true;
        }
        return false;
    }
    
    @EventHandler
    public void onRankChange(BDRankChangeEvent event) {
        Player player = event.getPlayer();
        int newRank = event.getNewRank();
        String rankName = bdApi.getEconomyAPI().getRankManager().getRankName(newRank);
        
        // Broadcast rank change
        Bukkit.broadcastMessage(player.getName() + " has reached BD rank: " + rankName);
        
        // Give reward for ranking up
        if (newRank == 5) { // Agricultural Expert
            player.getInventory().addItem(bdApi.getItemsAPI().createUltimateBDHarvester());
            player.sendMessage("You received an Ultimate BD Harvester as a reward!");
        }
    }
    
    @EventHandler
    public void onMarketCreate(BDMarketCreateEvent event) {
        Player player = event.getPlayer();
        String marketId = event.getMarketId();
        
        // Broadcast market creation
        Bukkit.broadcastMessage(player.getName() + " has established a new BD Market: " + marketId);
        
        // Add welcome message at market center
        Location center = event.getCenter();
        center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 50, 2, 2, 2, 0.1);
        
        // Log creation for stats
        getLogger().info("Market " + marketId + " created by " + player.getName() + 
                      " at " + center.getWorld().getName() + 
                      " " + center.getBlockX() + "," + 
                      center.getBlockY() + "," + 
                      center.getBlockZ());
    }
    
    @EventHandler
    public void onMarketUpgrade(BDMarketUpgradeEvent event) {
        Player player = event.getPlayer();
        String marketId = event.getMarketId();
        int newLevel = event.getNewLevel();
        
        // Congratulate on upgrade
        player.sendMessage("§aCongratulations! Your market has been upgraded to level " + newLevel);
        player.sendMessage("§aYou now have access to new market benefits!");
        
        // Give bonus rewards based on the new level
        if (newLevel == 4) { // Max level
            // Give a special reward for reaching max level
            player.getInventory().addItem(bdApi.getItemsAPI().createBDSeed(20));
            player.sendMessage("§6You received 20 BD Seeds as a reward for reaching maximum market level!");
        }
    }
}
```

This internal documentation is provided for server administrators and plugin maintainers as a reference to understand the architecture of BDCraft. As a fully self-contained plugin, BDCraft does not expose these systems for external integration.