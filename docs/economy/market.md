# Player Markets

The BDCraft Market System allows players to establish their own trading centers with defined boundaries and special features.

## Overview

Markets in BDCraft provide:
- Physical trading zones with defined boundaries
- Custom collector villagers for buying/selling items
- Market management through a special Market Owner villager
- Associate system for collaborative market management
- Progression through market upgrades

## Creating a Market

To create a market:

1. Obtain a Market Token (from server events or admins)
2. Find a suitable location that's at least 30 blocks away from other markets
3. Use the Market Token (right-click) at your desired location
4. A Market Owner villager will spawn at that location

## Market Features

### Market Owner

The Market Owner villager is the central management NPC for your market. Through this villager, you can:
- Access the Market Management GUI
- Purchase market upgrades
- Manage market associates
- Control trading permissions

### Collector Houses

Markets can have multiple collector houses where trading takes place:
- Each house requires a House Token to establish
- Initial markets can have up to 3 collectors
- Upgraded markets can have up to 10 collectors
- Each collector specializes in different trade types

### Market Boundaries

Markets have defined boundaries:
- Standard markets have a 49-block radius
- Use `/bdmarket check` to visualize boundaries temporarily
- Only the founder and associates can build within these boundaries

## Market Levels and Upgrades

Markets can be upgraded through the Market Owner:

1. **Level 1 (Initial)**:
   - Up to 3 collector houses
   - Basic trading prices

2. **Level 2**:
   - Cost: 16 diamonds + 5000 currency
   - Up to 5 collector houses
   - 5% better trading prices
   - House Tokens available from Market Owner

3. **Level 3**:
   - Cost: 32 diamonds + 10000 currency
   - Up to 7 collector houses
   - 10% better trading prices
   - Seasonal Trader visits

4. **Level 4 (Maximum)**:
   - Cost: 64 diamonds + 25000 currency
   - Up to 10 collector houses
   - 15% better trading prices
   - Player buffs within market radius

## Market Management

Most market management is handled through the Market Owner villager GUI, which is accessed by right-clicking the Market Owner NPC in your market.

### Market Commands

Only a few commands are available for basic market information:

- `/bdmarket check` - Visualize market boundaries with temporary blocks
- `/bdmarket info` - Display information about the current market
- `/bdmarket list` - List all markets you're associated with

All other market functions including associate management, upgrading, and permissions are handled through the Market Owner GUI interface.

### Market Associates

Market Founders can manage who has special permissions in their market through the Market Owner GUI:

1. **Adding Associates**:
   - Access the Market Management GUI by right-clicking the Market Owner villager
   - Select the "Associates" tab and click on empty slots to add players
   - Max 5 associates per market
   - Associates must be online when added

2. **Associate Permissions**:
   - Can build collector houses using House Tokens
   - Can upgrade the market (if near the Market Owner)
   - Cannot add or remove other associates

3. **Removing Associates**:
   - Use the Market GUI's Associates tab and click on player icons to remove them
   - Only the founder can remove associates

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