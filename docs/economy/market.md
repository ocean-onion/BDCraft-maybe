# Player Markets

The BDCraft Market System allows players to establish their own trading centers with physically built market structures.

## Market Creation

To create a BD market, players must:

1. **Build a Market Structure**:
   - **Roof**: Minimum 3x3 solid blocks at the same height
   - **Walls**: At least 3x3 wall formation with one doorway
   - **Interior**: Must contain one bed
   - **Item Frame**: Must be placed above the door on the outside

2. **Use a BD Market Token**:
   - Craft using BD Stick surrounded by diamonds with two emeralds
   - Place the token in the item frame above the door
   - Must be at least 30 blocks away from any existing market
   - Creates a 49x49 block radius market area

3. **Market Initialization**:
   - A BD Dealer (FARMER) villager spawns inside the market
   - A Market Owner (CARTOGRAPHER) villager also spawns inside
   - The player who placed the token becomes the "Market Founder"

## Collector Houses

Players can expand their markets by adding collector houses:

1. **Building Requirements**:
   - Same structure requirements as the main market
   - Must be within the 49x49 block market radius
   - Limited by the market's collector limit (initially 3)

2. **House Token**:
   - Craft by surrounding a bed with harvested BD crops
   - Can also be purchased from Market Owner at market level 2+
   - Place in an item frame above the collector house door

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
   - Cost: 16 diamonds + 5000 server currency
   - Up to 5 collector houses
   - 5% better trading prices
   - Market Owner sells House Tokens

3. **Level 3**:
   - Cost: 32 diamonds + 10000 server currency
   - Up to 7 collector houses
   - 10% better trading prices
   - Seasonal Trader visits periodically

4. **Level 4 (Maximum)**:
   - Cost: 64 diamonds + 25000 server currency
   - Up to 10 collector houses
   - 15% better trading prices
   - Players receive minor buffs within market radius

## Market Management

### Market Commands

The following commands are available for market management:

- `/bdmarket check` - Visualize market boundaries with temporary wool blocks
- `/bdmarket info` - Display information about the market you're standing in
- `/bdmarket list` - List all markets you have founder or associate status in

### Market Management GUI

Players access most market features through the Market Owner villager:

1. **Accessing the GUI**:
   - Right-click the Market Owner villager in your market
   - Select the "Market Management" option from the trading menu
   - Only the market founder and associates can access management features

2. **Management Features**:
   - **Associates Tab**: Add or remove market associates (founder only)
   - **Permissions Tab**: Control who can trade with collectors
   - **Security Tab**: Manage building permissions within market area
   - **Upgrades Tab**: Purchase market upgrades using currency and materials
   - **Settings Tab**: Configure market appearance and effects

### Market Associates

Market Founders can manage who has special permissions in their market:

1. **Adding Associates**:
   - Use the Market GUI by right-clicking the Market Owner villager
   - Select "Associates" tab and click on empty slots to add players
   - Maximum 5 associates per market
   - Associates must be online when added

2. **Associate Permissions**:
   - Can build collector houses using House Tokens
   - Can upgrade the market (if near the Market Owner)
   - Cannot add or remove other associates

### Trading Permissions

Markets can control who can trade with collectors:
- Public: Anyone can trade
- Associates: Only founder and associates can trade
- Founder: Only the founder can trade

## Administration

Server administrators can manage markets using:
- `/bdadmin market visualize <id>` - Show boundaries of a market
- `/bdadmin spawn dealer` - Spawn a BD Dealer at your location
- `/bdadmin give <player> markettoken [amount]` - Give market tokens
- `/bdadmin give <player> housetoken [amount]` - Give house tokens