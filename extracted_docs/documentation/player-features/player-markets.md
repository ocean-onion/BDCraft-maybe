# Player Markets

The BDCraft plugin allows players to create and manage their own BD markets through a building process rather than relying on admin-created villages. This guide covers all aspects of the player market system.

## Market Creation Process

### Building Requirements

To create a BD market, players must build a market stall with the following requirements:

1. **Structure Requirements**:
   - **Roof**: Minimum 3x3 solid blocks at the same height
   - **Walls**: At least 3x3 wall formation with one doorway
   - **Interior**: Must contain one bed
   - **Item Frame**: Must be placed above the door on the outside

2. **BD Market Token**:
   - Craft a BD Market Token using:
     - BD Stick surrounded by diamonds
     - Two emeralds in the middle left and right positions
   - Place the token in the item frame above the door

3. **Placement Restrictions**:
   - Must be at least 30 blocks away from any existing market
   - Creates a 49x49 block radius market area
   - Only one market allowed per player (unless they have special permissions)

### Market Initialization

Once a valid market structure is built and the token is placed:

1. **Villager Spawning**:
   - A BD Dealer (FARMER) villager spawns inside the market
   - A Market Owner (CARTOGRAPHER) villager also spawns inside
   - The dealer sells seeds, the market owner provides upgrades

2. **Player Permissions**:
   - The player who placed the token becomes the "Market Founder"
   - Founders can add other players as "Market Associates"
   - Only founders and associates can build collector houses

## Market Expansion

### Collector Houses

Players can expand their markets by adding collector houses:

1. **Building Requirements**:
   - Same structure requirements as the main market (3x3 roof, walls, bed, item frame)
   - Must be within the 49x49 block market radius
   - Limited by the market's collector limit (initially 3)

2. **House Token**:
   - Craft by surrounding a bed with **BD crops only** (not regular ferns)
   - Only harvested BD crops can be used in the crafting recipe
   - Can also be purchased from Market Owner at market level 2+
   - Place in an item frame above the collector house door

3. **Collector Spawning**:
   - A BD Collector (LIBRARIAN) villager spawns inside
   - Collector buys crops from players for emeralds and server currency

### Market Levels and Upgrades

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

Players have access to several commands for managing their markets:

- `/bdmarket check` - Visualize market boundaries with temporary wool blocks
- `/bdmarket info` - Display information about the market you're standing in
- `/bdmarket list` - List all markets you have founder or associate status in
- `/bdmarket associate add <player>` - Add a player as an associate to your market
- `/bdmarket associate remove <player>` - Remove a player's associate status
- `/bdmarket upgrade` - Opens market upgrade GUI when used near Market Owner

### Market Boundaries

Players can visualize market boundaries using the `/bdmarket check` command:

- Top layer blocks within boundary become red wool for 10 seconds
- Corner blocks become black wool for 10 seconds
- All changed blocks automatically revert to original state
- Temporary blocks are unbreakable during visualization

### Market Management GUI

Players can access a comprehensive Market Management GUI by right-clicking the Market Owner villager:

1. **GUI Access**:
   - Right-click the Market Owner villager in your market
   - Select the "Market Management" option from the trading menu
   - Only the market founder and associates can access management features

2. **Management Features**:
   - **Associates Tab**: Add or remove market associates (founder only)
   - **Permissions Tab**: Control who can trade with collectors (public/associates/founder only)
   - **Security Tab**: Manage building permissions within market area
   - **Upgrades Tab**: Purchase market upgrades using currency and materials
   - **Settings Tab**: Configure market appearance and effects

### Market Associates

Market Founders can manage who has special permissions in their market:

1. **Adding Associates**:
   - Use the Market GUI by right-clicking the Market Owner villager
   - Select "Associates" tab and click on empty slots to add players
   - Alternatively use `/bdmarket associate add <player>`
   - Max 5 associates per market
   - Associates must be online when added

2. **Associate Permissions**:
   - Can build collector houses using House Tokens
   - Can upgrade the market (if near the Market Owner)
   - Cannot add or remove other associates

3. **Removing Associates**:
   - Use the Market GUI's Associates tab and click on player icons to remove them
   - Alternatively use `/bdmarket associate remove <player>`
   - Only the founder can remove associates

4. **Trading Permissions**:
   - Use the Permissions tab in the Market GUI
   - Control who can trade with collectors in your market (public/associates/founder)
   - Adjust individual collector settings by clicking their icons

## Benefits of Player Markets

1. **Control and Ownership**:
   - Players create and manage their own economic zones
   - No dependency on admin-created villages
   - Personalized market area with custom structures

2. **Economic Advantages**:
   - Market upgrades provide better trading prices
   - Higher level markets attract Seasonal Traders with rare items
   - Market buffs provide minor advantages to members

3. **Community Building**:
   - Markets encourage player cooperation through associate system
   - Creates natural player hubs and gathering locations
   - Promotes construction of themed market districts

## Technical Limitations

1. **Performance Considerations**:
   - Server limits total number of markets per world for performance reasons
   - Each market can have a maximum of 10 collector houses (at level 4)
   - Only one Seasonal Trader can visit a market at a time

2. **Building Restrictions**:
   - Markets cannot be too close to each other (30 block minimum)
   - Market buildings must meet specific dimensional requirements
   - Markets must remain active (inactive for 30+ days may be removed)

3. **Anti-Exploitation Measures**:
   - Players cannot use creative mode to place market tokens
   - Market tokens cannot be moved once placed
   - Market benefits only apply to legitimate associates

The player market system provides an engaging and player-driven approach to the BD economy, encouraging player creativity, community building, and long-term engagement with the plugin's mechanics.