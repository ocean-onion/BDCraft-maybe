# Economy Module

The Economy Module provides a comprehensive economic system that forms the backbone of BDCraft's gameplay experience. This module creates a complete economic loop through player-built markets, specialized villager trading, auction systems, and dual currency mechanics.

## Overview

The Economy Module consists of four interconnected systems that work together to create a dynamic player-driven economy:

### Core Components

- **[Currency System](currency.md)** - Dual currency mechanics with village reputation
- **[BD Crops](crops.md)** - Complete crop growing system with three tiers and yield bonuses
- **[Trading System](trading.md)** - Comprehensive villager trading with bonuses and reputation mechanics
- **[Market System](market.md)** - Player-built trading centers with physical structures
- **[Villager System](villager.md)** - Specialized villagers including seasonal traders
- **[Auction System](auction.md)** - Server-wide marketplace for BD items

## Economic Flow

The economy operates on a complete loop designed to encourage continuous player engagement:

1. **Seed Purchase** - Players buy BD seeds from Dealers using emeralds
2. **Crop Production** - Players grow BD crops in markets (faster growth rates)
3. **Crop Trading** - Players sell crops to Collectors for emeralds AND server currency
4. **Progression Investment** - Players use server currency for rank upgrades
5. **Enhanced Returns** - Higher ranks provide better crop yields and trading bonuses

## Currency Systems

### Emerald Economy
- **Primary Trading Currency** - Used for purchasing seeds, tools, and market items
- **Earned Through Trading** - Collectors pay emeralds for BD crops
- **Market Circulation** - Stays in circulation as players reinvest in seeds

### Server Currency
- **Progression Currency** - Used for rank purchases and major upgrades
- **Tracked Digitally** - No physical item, managed by plugin database
- **Enhanced by Ranks** - Higher ranks earn more server currency per trade

### Village Reputation System
- **Market-Specific Reputation** - Each market tracks individual player reputation
- **Trading Benefits** - Higher reputation provides better prices and bonuses
- **Reputation Sources**:
  - BD Dealers: +2 reputation per trade
  - Collectors: +3 reputation per trade
  - Seasonal Traders: +4 reputation per trade

## Market Infrastructure

### Physical Market Creation
- **Building Requirements** - 3x3 minimum roof, walls with door, bed, item frame
- **Token Activation** - BD Market Token placed in item frame creates market
- **Area Definition** - 98x98 block radius (128x128 for donors)
- **Villager Spawning** - Automatic BD Dealer and Market Owner creation

### Market Expansion
- **Collector Houses** - Additional structures within market boundaries
- **House Tokens** - BD House Tokens spawn Collector villagers
- **Upgrade Levels** - 5 market levels affecting collector limits and bonuses
- **Associate System** - Market owners can add up to 5 building associates

### Market Management
- **Owner Control** - Market creators have full management access
- **Donor Privileges** - Remote GUI access via `/bdmarket` command
- **Security Settings** - Control building permissions and trading access
- **Visualization Tools** - Commands to display market boundaries

## Villager Specializations

### BD Dealer
- **Seed Trading** - Sells all three types of BD seeds for emeralds
- **Tool Sales** - Provides BD Harvester and Ultimate BD Harvester for diamonds
- **Natural Spawning** - 45% chance in Minecraft villages
- **Market Spawning** - Automatically created in player markets

### Market Owner
- **Management Interface** - Provides GUI for market administration
- **Upgrade Sales** - Handles market level upgrades and improvements
- **Associate Management** - Add/remove market building associates
- **Market Confined** - Cannot leave the market stall building

### Collectors
- **Crop Purchasing** - Buy BD crops for emeralds and server currency
- **Market Movement** - Can move within entire market boundary
- **Level Scaling** - Trading bonuses based on market upgrade level
- **Limited Numbers** - 3-15 collectors based on market level

### Seasonal Traders
- **Periodic Appearance** - Visit level 3+ markets every 2 weeks for 3 days
- **Seasonal Variants** - Spring, Summer, Fall, Winter themed traders
- **Special Items** - Exclusive seasonal decorations and themed items
- **Event Trading** - Special traders during server events
- **Enhanced Reputation** - Highest reputation gains (+4 per trade)

## Auction House System

### Item Trading
- **BD Items Only** - Seeds, tools, and special items allowed
- **Rank Benefits** - Better fees and longer listing durations for higher ranks
- **Donor Advantages** - Fixed 2% fee regardless of rank
- **Listing Limits** - 10-30 active listings based on rank

### Fee Structure
- **Standard Fees** - 5% for newcomers, decreasing with rank progression
- **Donor Benefits** - Fixed 2% fee with inflation protection
- **High-Value Protection** - Special fee rates for expensive seed listings
- **Priority Listings** - Donor auctions appear first on main page

## Token Economy

### BD Market Tokens
- **Market Creation** - Required to establish player markets
- **Crafting Recipe** - BD Stick + 8 diamonds + 2 emeralds
- **Single Use** - Consumed permanently when placed
- **Location Requirements** - Must be 50+ blocks from existing markets

### BD House Tokens
- **Collector Houses** - Create additional trading points within markets
- **Crafting Recipe** - 8 BD crops surrounding a bed
- **Market Purchase** - Available from Market Owner at level 2+
- **Expansion Limits** - Based on market upgrade level

### BD Stick Crafting
- **Precursor Item** - Required ingredient for Market Token crafting
- **Durability System** - 5 uses before breaking
- **Special Effects** - Applies random potion effects when used
- **Crafting Recipe** - Paper + BD Crop + Flint in horizontal row

## Advanced Features

### Dynamic Pricing
- **Market Level Bonuses** - Better prices in upgraded markets
- **Reputation Scaling** - Higher reputation improves trading rates
- **Rank Multipliers** - Higher ranks earn more from all trades
- **Donor Bonuses** - 10% boost to all trading benefits

### Market Visualization
- **Boundary Display** - `/bdmarket check` shows temporary wool borders
- **Information System** - `/bdmarket info` displays market details
- **Admin Tools** - Server administrators can visualize any market

### Data Management
- **Transaction Logging** - Complete history of all economic activities
- **Balance Tracking** - Secure storage of all player currency data
- **Market Analytics** - Performance metrics for market optimization

## Commands

For a complete list of Economy Module commands, see the [Admin Commands](../admin/commands.md) reference.

## Configuration

For detailed configuration options, see the [Configuration Guide](../configuration/configuration.md).

## Permissions

For permission settings, see the [Permissions Reference](../configuration/permissions.md).

## Developer API

For technical integration details, see the [API Reference](../development/api-reference.md).

---

The Economy Module transforms Minecraft into a rich economic experience where players can build, trade, and prosper through strategic market development and villager interactions.