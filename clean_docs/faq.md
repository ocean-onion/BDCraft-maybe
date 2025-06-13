# Frequently Asked Questions

## General Questions

### What is BDCraft?
BDCraft is a comprehensive Minecraft plugin that provides a complete economic and progression system for Paper 1.21.x servers. It includes player-built markets, custom villagers, auction houses, rank progression, rebirth systems, and essential utilities like teleportation and chat management - all integrated into one cohesive plugin.

### What server versions is BDCraft compatible with?
BDCraft is designed specifically for Paper 1.21.x servers. It is not compatible with older versions, Spigot, Bukkit, or other server implementations due to its use of modern Paper-specific features.

### Does BDCraft require any other plugins?
No. BDCraft is designed to be completely self-contained and does not require any other plugins to function. In fact, it will automatically block competing plugins to prevent conflicts and ensure stability.

### Can I use BDCraft alongside other plugins?
Yes, as long as they don't conflict with BDCraft's features. BDCraft automatically blocks competing plugins to maintain stability.

**Plugins That Will Be Blocked:**
- Economy plugins (Vault, EssentialsX Economy, CMI Economy, etc.)
- Home/teleport plugins (EssentialsX, CMI Homes, HomeSpawnPlus, etc.)
- Chat management plugins (EssentialsX Chat, ChatControl, DeluxeChat, etc.)
- Villager-related plugins (VillagerShop, VillagerGUIShop, CustomVillager, etc.)
- Custom item plugins that modify vanilla item behavior

**Compatible Plugins:**
- World editing (WorldEdit, WorldGuard, etc.)
- Protection plugins (GriefPrevention, Towny, etc.)
- Logging plugins (CoreProtect, LogBlock, etc.)
- Cosmetic plugins (GadgetsMenu, Hats, etc.)
- Mini-game plugins

The blocking mechanism works by checking for competing plugins at startup and disabling their conflicting features. You'll see notifications in the console indicating which plugin functions have been blocked.

### How do I update BDCraft?
Simply replace the BDCraft.jar file with the new version and restart your server. The plugin handles any necessary data migrations automatically. Always backup your `plugins/BDCraft` folder before updating.

### What are the benefits of being a donor?
Donors receive comprehensive perks across all systems:

**Economic Benefits:**
- Remote market access via `/bdmarket` command from anywhere
- Fixed 2% auction house fees (vs 5% for regular players)
- Auction listing priority (appear first on main page)
- 20% currency retention during rebirth (regular players lose all)
- Daily 1% balance increase when inactive

**Enhanced Limits:**
- 3 homes instead of 1
- Up to 5 markets instead of 1
- 128x128 market area instead of 98x98
- Access to all BD tools regardless of rank

**Progression Bonuses:**
- 10% boost to all rank perks
- 15% discount on rank upgrade costs
- Enhanced rebirth bonuses

## Economy System

### How do players earn money in BDCraft?
Players earn money through the dual currency system:

**Emeralds (Physical Currency):**
- Sell BD crops to Collector villagers
- Trade with other players
- Receive payments from other players

**Server Currency (Digital Currency):**
- Automatically earned alongside emeralds when trading with Collectors
- Used for rank purchases and major upgrades
- Enhanced by higher ranks and rebirth levels
- Tracked digitally by the plugin

### How does the village reputation system work?
Each market tracks individual player reputation, which affects trading prices:
- **BD Dealers**: +2 reputation per trade
- **Collectors**: +3 reputation per trade  
- **Seasonal Traders**: +4 reputation per trade
- Higher reputation = better trading prices and bonuses
- Reputation is market-specific and accumulates over time

### Can I customize the currency name and symbol?
Yes, you can customize the server currency name, plural form, and symbol in the `economy.yml` configuration file. The emerald currency cannot be changed as it uses Minecraft's emerald item.

### How do I give money to players?
As an administrator, you can use `/bdeco give <player> <amount>` for server currency. For emeralds, use standard Minecraft item commands or the `/bdadmin give` command.

## Market System

### How do players create markets?
Players need to follow this process:

1. **Build a Market Structure**:
   - Minimum 3x3 roof at same height
   - Walls with one doorway
   - One bed inside the structure
   - Item frame placed directly above the door (outside)

2. **Craft a BD Market Token**:
   - 8 diamonds around edges + BD Stick in center + 2 emeralds on sides
   - BD Stick crafted from Paper + BD Crop + Flint in horizontal row

3. **Place the Token**:
   - Place Market Token in the item frame above the door
   - Must be 50+ blocks from any existing market
   - Creates 98x98 block market area (128x128 for donors)
   - Spawns BD Dealer and Market Owner villagers automatically

### How many markets can a player have?
- **Regular Players**: 1 market maximum
- **Donors**: Up to 5 markets with `bdcraft.market.multiple` permission

### What are collector houses and how do they work?
Collector houses are additional structures within market areas that spawn Collector villagers who buy crops from players:

**Creation Process:**
- Build a house structure (same requirements as main market)
- Craft BD House Token (8 BD crops surrounding a bed)
- Place token in item frame above collector house door
- Must be within existing market boundaries

**Limitations:**
- Initial markets: 3 collector houses maximum
- Upgraded markets: Up to 15 collectors at level 5
- If collector is killed, token becomes unenchanted and must be replaced

### How do players upgrade their markets?
Market upgrades are purchased through the Market Owner villager:
- Right-click the Market Owner (CARTOGRAPHER with gold name)
- Select "Upgrades" tab in the trading GUI
- Pay required emeralds + server currency for each level
- 5 upgrade levels total, each providing more collectors and better bonuses

### What are Seasonal Traders?
Special villagers that appear in level 3+ markets:
- **Frequency**: 3 days every 2 weeks (more frequent at level 5)
- **Variants**: Spring, Summer, Fall, Winter themed traders
- **Items**: Exclusive seasonal decorations and themed items
- **Benefits**: Highest reputation gains (+4 per trade)
- **Announcements**: Server-wide notifications when they arrive

## Auction System

### How do players use the auction house?
Players access the auction house using `/bdauction` which opens a GUI where they can:
- Browse all available listings with category filters
- Buy items directly from other players
- List their own BD items for sale
- Manage their active listings and view sales history

### How do players sell items in the auction house?
To sell items:
1. Hold the BD item in your main hand
2. Use `/bdauction sell <price> [amount]`
3. Confirm the listing in the GUI
4. Pay the listing fee (automatically deducted)

**Allowed Items:** BD Seeds, BD Tools, Special/Seasonal items
**Prohibited Items:** BD Crops (must be sold to Collectors), regular Minecraft items

### What are the auction house fees?
**Regular Players:**
- Newcomer: 5% listing fee
- Farmer: 4.5% listing fee
- Expert Farmer: 4% listing fee
- Master Farmer: 3.5% listing fee
- Agricultural Expert: 3% listing fee

**Donors:**
- Fixed 2% fee regardless of rank
- Inflation protection for high-value listings

### How long do auction listings last?
Listing duration depends on rank:
- Newcomer/Farmer: 3 days
- Expert Farmer: 4 days
- Master Farmer: 5 days
- Agricultural Expert: 7 days

Expired items go to your item storage mailbox, accessible via `/bdmail storage`.

## Progression System

### How do players progress through ranks?
Players advance through 5 ranks by completing specific achievements and paying server currency:

1. **Newcomer** (Starting rank) - No requirements
2. **Farmer** (500 currency) - 100 crops harvested, 10 dealer trades
3. **Expert Farmer** (2,000 currency) - 500 crops, 300 seeds planted, 25 collector trades
4. **Master Farmer** (5,000 currency) - 2,000 crops, 800 seeds, 80 collector trades, 50 dealer trades, market ownership
5. **Agricultural Expert** (10,000 currency) - 10,000 crops, 3,500 seeds, 150 collector trades, 80 dealer trades, level 5 market, all BD tools, help 10 players

### What benefits do higher ranks provide?
Each rank provides cumulative benefits:
- **Yield Bonuses**: 5% to 30% increased crop harvests
- **Auction Benefits**: Reduced fees and extended listing durations  
- **Growth Speed**: 5% to 25% faster crop growth rates
- **Rare Crops**: Increased chance of higher-tier crop generation
- **Special Access**: Tool purchases, market features, permanent effects
- **Chat Recognition**: Custom rank prefixes in chat

### What is the rebirth system?
The rebirth system allows Agricultural Expert players to reset their progression for permanent bonuses:

**Reset Elements:**
- Rank returns to Newcomer
- Server currency reset to 0 (donors keep 20%)
- Achievement counters reset

**Preserved Elements:**
- All markets and structures remain
- BD items in inventory kept
- Market associates and permissions maintained

**Permanent Benefits (Per Rebirth Level):**
- +5% crop yield bonus (25% maximum at Rebirth 5)
- +3% better trading prices (15% maximum)
- Exclusive cosmetic effects and titles
- Special features and abilities

### How do players perform a rebirth?
Once eligible (Agricultural Expert rank):
1. Complete rebirth-specific achievements
2. Gather required resources (64-1024 emeralds + 16-256 diamonds)
3. Use `/bdrebirth confirm` to initiate
4. Complete multi-step confirmation process
5. Receive permanent bonuses and reset progression

## Vital Features

### How many homes can players set?
Home limits are based on status:
- **Regular Players**: 1 home
- **Donors**: 3 homes
- **Permission-Based**: Can be increased via `bdcraft.home.multiple.X` permissions

### How do teleport cooldowns work?
Teleportation has several timing mechanisms:
- **Warmup Delay**: 3 seconds before teleport completes (cancels if player moves)
- **Home Cooldown**: 30 seconds between home teleports
- **Random Teleport**: 5-minute cooldown for `/tpr`
- **Back Command**: 30-second cooldown for `/back`

### Can players use colored chat?
Chat formatting depends on permissions:
- **Basic Chat**: Available to all players
- **Color Chat**: Requires `bdcraft.chat.color` permission
- **Format Chat**: Requires `bdcraft.chat.format` permission
- **Rank-Based**: Higher ranks may have enhanced chat formatting

### What chat channels are available?
BDCraft includes four chat channels:
- **Global Chat** (`/g` or normal typing): Server-wide communication
- **Local Chat** (`/l`): 100-block radius communication
- **Market Chat** (`/m`): Only within market areas
- **Admin Chat** (`/a`): Administrator-only communication

### How does the mail system work?
The mail system has two functions:

**Messaging** (`/bdmail send/read/clear`):
- Send messages to online/offline players
- Read received messages
- Clear message inbox

**Item Storage** (`/bdmail storage`):
- Retrieve expired auction items
- 7-day storage limit for unclaimed items
- Automatic delivery from auction expiration

## Administration

### How do I give players admin access?
Grant the `bdcraft.admin` permission using BDCraft's built-in permission system for full access. For granular control, use specific permissions like:
- `bdcraft.economy.admin` - Economy management
- `bdcraft.market.admin` - Market administration
- `bdcraft.progression.admin` - Rank and rebirth management

### How do I make someone a donor?
Use the `/donorverified [username]` command (admin only) to manually verify donor status, or set up automatic verification through your server's donation system.

### How do I reset a player's data?
Use `/bdadmin player reset <player>` to reset all plugin-related data including balance, rank, homes, and progression. This action is irreversible, so ensure you have backups.

### Can I back up BDCraft data?
Yes, use `/bdadmin backup` to create a backup of all plugin data in the `plugins/BDCraft/backups` folder. The plugin also automatically creates backups before major operations like updates.

### How do I configure the plugin?
BDCraft uses multiple configuration files in the `plugins/BDCraft` folder:
- `config.yml` - Main settings and module control
- `economy.yml` - Currency, market, auction, villager settings
- `progression.yml` - Rank and rebirth configuration
- `vital.yml` - Chat, home, teleport, tab settings
- `permissions.yml` - Built-in permission system

See the [Configuration Guide](configuration/configuration.md) for detailed information.

## Technical Questions

### Does BDCraft use a database?
By default, BDCraft stores data in flat files for simplicity. However, it can be configured to use MySQL for more robust data storage, which is recommended for larger servers (20+ players).

### How resource-intensive is BDCraft?
BDCraft is designed to be efficient but includes many features. For smaller servers (under 20 players), default configuration works well. For larger servers, consider:
- Using MySQL instead of flat files
- Disabling unused modules in configuration
- Adjusting update intervals for performance

### Can I disable modules I don't need?
Yes, you can disable any module (Economy, Progression, Vital) or specific submodules in the main configuration file. This reduces resource usage if you don't need all features.

### Is BDCraft compatible with server proxies?
Yes, BDCraft works with BungeeCord and Velocity setups, but data is server-specific by default. To share data across multiple servers, configure BDCraft to use a shared MySQL database.

## Troubleshooting

### What do I do if I encounter an error?
1. Check the [Troubleshooting Guide](admin/troubleshooting.md) for common issues
2. Enable debug mode in configuration for detailed error messages
3. Check server console for specific error information
4. Ensure all requirements are met (Paper 1.21.x, Java 17+)

### My players can't create markets. What's wrong?
Common market creation issues:
1. **Structure Requirements**: Ensure 3x3 roof, walls with door, bed inside, item frame above door
2. **Token Crafting**: Verify BD Market Token recipe (8 diamonds + BD Stick + 2 emeralds)
3. **Distance Requirements**: Must be 50+ blocks from existing markets
4. **Permissions**: Check `bdcraft.market.use` permission
5. **BD Stick**: Ensure players can craft BD Stick (Paper + BD Crop + Flint)

### Players aren't earning money from trading. How do I fix this?
Check these common issues:
1. **Module Status**: Ensure Economy module is enabled in configuration
2. **Villager Types**: Only BD Collectors buy crops (not regular villagers)
3. **Item Types**: Only BD crops are accepted (ferns, double ferns with special metadata)
4. **Market Boundaries**: Collectors only function within market areas
5. **Permissions**: Verify `bdcraft.economy.use` and `bdcraft.villager.use` permissions

### How do I report a bug?
If you believe you've found a bug, provide detailed information including:
1. Server version and BDCraft version
2. Detailed description of the issue
3. Steps to reproduce the problem
4. Console error messages
5. Configuration details if relevant

### Can I request a feature?
Feature requests are welcome, but BDCraft is designed to be comprehensive and self-contained. Requests that require external plugin integration or significantly alter the core design philosophy may not be implemented.

## BD Crops Information

### How do BD crops work?
BD crops are special crops that form the foundation of the economy:

**Growth Mechanics:**
- Grow as melon stems during stages 1-3 (all types look identical while growing)
- Transform to their final form at stage 4 (fully mature)
- Growth times: Regular (12 min), Green (20 min), Purple (32 min)
- 5% faster growth per market level (25% maximum at level 5)

**Crop Types:**
- **Regular BD Crops**: Appear as ferns when mature and in inventory
- **Green BD Crops**: Appear as double ferns when mature and in inventory  
- **Purple BD Crops**: Appear as double ferns with purple particle effects when mature (double ferns in inventory)

**Seeds vs Crops:**
- **Seeds**: Different inventory appearances (wheat seeds, melon seeds, pumpkin seeds)
- **Growing**: All types grow identically as melon stems
- **Harvested**: Different final forms based on crop type

### How do BD tools work?
BD tools enhance crop harvesting:
- **Regular Harvesting**: 1 crop per plant
- **BD Harvester**: 2 crops per plant (16 diamonds from Expert Farmer+ dealers)
- **Ultimate BD Harvester**: 3 crops per plant (32 diamonds from Agricultural Expert dealers)

## Getting Help

If your issue isn't covered in this FAQ:
1. Consult the [Troubleshooting Guide](admin/troubleshooting.md)
2. Check the relevant module documentation
3. Enable debug mode for detailed error information
4. Contact support with complete error details and server information

---

This FAQ covers the most common questions about BDCraft. For detailed technical information, refer to the specific module documentation and configuration guides.