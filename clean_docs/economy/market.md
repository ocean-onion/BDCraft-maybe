# Player Markets

The BDCraft Market System allows all players to establish their own trading centers with physically built market structures. Any player can create a market by building a structure and placing a Market Token.

## Market Creation

To create a BD market, players must:

1. **Build a Market Structure**:
   - **Roof**: Minimum 3x3 solid blocks at the same height (can be larger)
   - **Walls**: At least 3x3 wall formation with one doorway (can be larger, like 3x6 or 4x8)
   - **Interior**: Must contain one bed
   - **Item Frame**: Must be placed directly above the door on the outside

2. **Use a BD Market Token**:
   - Craft using BD Stick surrounded by diamonds in a square pattern with two emeralds on the sides
   - Recipe: 8 diamonds around the edges, BD Stick in center, emeralds on left and right middle
   - The token appears as an enchanted emerald with gold text and blue enchantment glow in inventory
   - Place the token in the item frame directly above the door
   - Must be at least 50 blocks away from any existing market
   - Creates a 98x98 block area (49 block radius in all directions)
   - Donor players receive an additional 30 blocks of market area (128x128 total)

3. **Market Initialization**:
   - A BD Dealer (FARMER) villager spawns inside the market
   - A Market Owner (CARTOGRAPHER) villager also spawns inside
   - The player who placed the token becomes the market owner
   - The Market Owner villager shows the player's name: "[username]'s Market Owner [market_level]"

## Collector Houses

Players can expand their markets by adding collector houses:

1. **Building Requirements**:
   - Same structure requirements as the main market
   - Must be within the 49x49 block market radius
   - Limited by the market's collector limit (initially 3)

2. **House Token**:
   - Craft by surrounding a bed with harvested BD crops
   - Can also be purchased from Market Owner at market level 2+
   - Appears as an enchanted bed in inventory
   - Place in an item frame above the collector house door
   - If the collector is killed, the token loses its enchantment
   - Unenchanted tokens must be picked up and placed again to respawn the collector

3. **Collector Spawning**:
   - A BD Collector villager spawns inside
   - Collector buys crops from players for emeralds and server currency

## Market Levels and Upgrades

Markets can be upgraded through the Market Owner villager:

1. **Level 1 (Initial)**:
   - Up to 3 collector houses
   - Basic trading prices
   - No special benefits

2. **Level 2**:
   - Cost: 32 emeralds + 500 server currency
   - Up to 5 collector houses
   - 5% better trading prices
   - Market Owner sells House Tokens

3. **Level 3**:
   - Cost: 64 emeralds + 1,500 server currency
   - Up to 7 collector houses
   - 10% better trading prices
   - Seasonal Trader visits periodically (3 days every 2 weeks)

4. **Level 4**:
   - Cost: 128 emeralds + 3,000 server currency
   - Up to 10 collector houses
   - 15% better trading prices
   - Players receive minor buffs within market radius

5. **Level 5 (Maximum)**:
   - Cost: 256 emeralds + 6,000 server currency
   - Up to 15 collector houses
   - 20% better trading prices
   - Enhanced player buffs within market radius
   - Special seasonal events occur more frequently

## Market Management

### Market Commands

The following commands are available for basic market information:

- `/bdmarket check` - Visualize market boundaries with temporary wool blocks
- `/bdmarket info` - Display information about the market you're standing in
- `/bdmarket list` - List all markets you own or have associate status in (maximum 5 markets per player)

### Market Management GUI

Most players can ONLY access the Market Management GUI through the Market Owner villager:

1. **Accessing the GUI**:
   - Right-click the Market Owner villager in your market (CARTOGRAPHER with gold name tag)
   - Select the "Market Management" option from the trading menu
   - Only the market owner and associates can access management features
   - Physical interaction with the Market Owner is required for all regular players

2. **Donor Access**:
   - Donors have access to a special command that lets them access the market GUI remotely
   - `/bdmarket` - Opens the market management GUI for donors (requires bdcraft.market.donor permission)
   - This privilege is exclusive to server donors

3. **Management Features**:
   - **Associates Tab**: Add or remove market associates (market owner only)
   - **Permissions Tab**: Control who can trade with collectors
   - **Security Tab**: Manage building permissions within market area
   - **Upgrades Tab**: Purchase market upgrades using currency and materials (only through this interface)
   - **Settings Tab**: Configure market appearance and effects

### Market Associates

The player who creates a market (the market owner) can manage who has special permissions in their market:

1. **Adding Associates**:
   - Use the Market GUI by right-clicking the Market Owner villager
   - Select "Associates" tab and click on empty slots to add players
   - Maximum 5 associates per market
   - Associates must be online when added
   - Associates can ONLY be added through the Market Owner GUI, not through commands

2. **Associate Permissions**:
   - Can build collector houses using House Tokens
   - Can upgrade the market (if near the Market Owner)
   - Cannot add or remove other associates

### Trading Permissions

Markets can control who can trade with collectors through the Permissions tab:
- Public: Anyone can trade with the market's collectors
- Associates: Only the market founder and associates can trade
- Founder Only: Only the market founder can trade

## Administration

Server administrators can manage markets using:
- `/bdadmin market visualize <id>` - Show boundaries of a market
- `/bdadmin spawn dealer` - Spawn a BD Dealer at your location
- `/bdadmin give <player> markettoken [amount]` - Give market tokens
- `/bdadmin give <player> housetoken [amount]` - Give house tokens