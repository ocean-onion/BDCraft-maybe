# BD Villager System

The BD Villager System provides specialized villagers that form the backbone of the BDCraft economy and trading systems. Each villager type has unique roles, appearances, and trading capabilities.

## BD Villager Types

### BD Dealer

**Visual Appearance:**
- Base: Villager
- Profession: FARMER
- Villager Type: PLAINS
- Level: 2 (Experienced)
- Name: "BD Dealer" (green text)

**Distribution & Spawning:**
- Spawns naturally in Minecraft villages (45% chance)
- Spawns automatically when a player creates a BD Market

**Trades:**
- Regular BD Seeds: 5 seeds for 1 emerald (available to all players)
- Green BD Seeds: Premium seeds for 15 emeralds (unlocked at Farmer rank)
- Purple BD Seeds: Rare seeds for 30 emeralds (unlocked at Master Farmer rank)
- BD Harvester: Special tool for 16 diamonds (unlocked at Expert Farmer rank)
- Ultimate BD Harvester: Top-tier tool for 32 diamonds (unlocked at Agricultural Expert rank)

*Note: These crops primarily differ in their value - the higher-tier crops take longer to unlock as players progress through ranks but provide significantly better returns when sold to collectors.*

#### BD Crop Growth Information
- **Growth Times**: 
  - Regular BD Crops: 12 minutes to fully mature
  - Green BD Crops: 20 minutes to fully mature
  - Purple BD Crops: 32 minutes to fully mature
  - Market Growth Reduction: 5% reduction per market level (maximum 25% at level 5)
- **Growth Stages**: 4 visible stages identical to melon stems
- **Seed Appearance in Inventory**: 
  - Regular BD Seeds: Represented as wheat seeds
  - Green BD Seeds: Represented as melon seeds
  - Purple BD Seeds: Represented as pumpkin seeds
- **Crop Appearance When Growing**:
  - All BD crop types grow as melon stems during growth stages (stages 1-3)
  - Seeds appear differently in inventory but all grow identically as melon stems
- **Crop Appearance When Fully Grown**:
  - Regular BD Crops: Transform into ferns when fully mature (stage 4)
  - Green BD Crops: Transform into double ferns when fully mature (stage 4)
  - Purple BD Crops: Transform into enchanted double ferns when fully mature (stage 4)
- **Physical Properties**:
  - No growth bonuses from different biomes or seasons
  - Growth time reduction only applies when planted within market boundaries
- **Growth Transformation**:
  - BD crops grow as melon stems through stages 1-3
  - At stage 4 (fully mature), they transform into their respective fern types
  - BD crops do NOT drop as special items when harvested
  - When harvested, crops are represented in inventory as their corresponding items:
    - Regular BD Crops: Appear as ferns in inventory
    - Green BD Crops: Appear as double ferns in inventory
    - Purple BD Crops: Appear as enchanted double ferns in inventory
- **Optimal Conditions**: 
  - Require at least light level 9
  - Must be planted on farmland with water within 4 blocks
  - Growth rate is not affected by biome
  - Bone meal does not work on BD crops
  - Growth time is reduced when planted within market boundaries (based on market level)
- **Harvesting**: 
  - Regular harvesting gives 1 crop per plant
  - Using BD Harvester gives 2 crops per plant
  - Using Ultimate BD Harvester gives 3 crops per plant
  - Crops must be fully mature to harvest

### Market Owner

**Visual Appearance:**
- Base: Villager
- Profession: CARTOGRAPHER
- Villager Type: TAIGA
- Level: 3 (Expert)
- Name: "[Player]'s Market Owner [market_level]" (gold text)

**Spawning:**
- Created when a player builds a valid market structure and uses a Market Token
- One per player market

**Functions:**
- Provides Market Management GUI
- Offers market upgrades
- Manages market associates and permissions
- Sells House Tokens at higher market levels

### Collector

**Visual Appearance:**
- Base: Villager
- Profession: Leatherworker or Weaponsmith
- Villager Type: Based on biome
- Level: 1-5 (based on market level)
- Name: "[Owner's Market] Collector" (cyan text)

**Spawning:**
- Created when a player builds a collector house and uses a House Token
- Limited by market level (3-10 based on level)

**Functions:**
- Buys BD crops ONLY - will not accept any other items
- Trading prices affected by market level
- Primary source for converting BD crops into server currency

### Seasonal Trader

**Visual Appearance:**
- Base: Villager
- Profession: NITWIT
- Villager Type: SWAMP
- Level: 4 (Master)
- Name: "Seasonal Trader" (purple text)

**Spawning:**
- Appears periodically in level 3+ markets
- Available for limited time periods (3 days every 2 weeks)
- Appears more frequently in level 5 markets
- Announces arrival in server-wide message

**Functions:**
- Sells rare seasonal items
- Offers special event-related trades
- Provides limited quantity offerings

**Seasonal Offerings:**
- **Spring Trader** (March-May): Sells special flower-themed decorations, unique spring crop variants
- **Summer Trader** (June-August): Offers beach-themed items, summer cosmetics, cooling potions
- **Fall Trader** (September-November): Provides harvest-themed decorations, special pumpkin variants
- **Winter Trader** (December-February): Sells snow-themed items, holiday decorations, warming potions
- **Special Event Traders**: Appear during server events with exclusive themed items

## Villager Interaction

Players interact with BD Villagers by right-clicking on them, which opens their trading interface. Special villagers like the Market Owner will also provide access to GUI menus for additional management functions.

## Villager Behavior and Movement

BD Villagers follow specific movement and behavior rules:

- **Natural BD Dealer**: 
  - Behaves like regular villagers
  - Can walk freely around their village
  - Can be killed, though this is discouraged as they provide valuable trades

- **Market Owner**: 
  - Limited to walking within the market stall (the building where it spawned)
  - Cannot leave the physical structure of the market building
  - Cannot be moved with boats, minecarts, or any other transportation method
  - Cannot be killed by any means
  - Removed only if the market token is removed

- **Market Dealer**: 
  - Limited to walking within the market stall (the building where it spawned)
  - Cannot leave the physical structure of the market building
  - Cannot be moved with boats, minecarts, or any other transportation method
  - Cannot be killed by any means
  - Removed only if the market token is removed

- **Collector**: 
  - Limited to walking within the market boundary (not just the collector house)
  - Can walk freely within the entire market area but cannot leave the market boundary
  - Cannot be moved with boats, minecarts, or any other transportation method
  - Can be killed by players or mobs
  - If killed, the House Token in the item frame becomes unenchanted
  - Unenchanted tokens must be picked up and placed again to respawn the collector

## Villager Trading Rates

Collector villagers operate with dynamic trade rates:

- Base rates are determined by market level
- Rates may increase based on market activity and demand
- All rates reset to default values automatically every 24 real-world days
- Market founder rebirth level provides additional bonuses to trade rates
- Donor players receive 10% better prices than regular players with the same rank

### Trading With Villagers

1. Right-click on the villager to open the trading interface
2. Browse available trades by scrolling through the interface
3. Select a trade and fulfill the requirements (provide items/emeralds)
4. Receive the traded item in return

### Market Owner Interaction

1. Right-click on the Market Owner villager
2. Select "Market Management" from the trading menu
3. Access various tabs for different management functions:
   - Associates Tab: Add or remove market associates (founder only)
   - Permissions Tab: Control who can trade with collectors
   - Security Tab: Manage building permissions within market area
   - Upgrades Tab: Purchase market upgrades using currency and materials
   - Settings Tab: Configure market appearance and effects

**Important:** For most players, this is the ONLY way to manage your market. However, donors with the bdcraft.market.donor permission can use the `/bdmarket` command to access the Market Owner GUI remotely.

## Collectors

Collectors are specialized villagers that ONLY buy BD crops from players. They are crucial to the server economy as they are the primary way players convert their BD crops into emeralds.

Collectors will:
- Only accept BD crops (no vanilla crops)
- Pay with emeralds AND server currency for BD crops
- Reject any items that are not BD crops
- Have limited daily purchasing capacity
- Reset their trades on a regular schedule

*Note: Crop values are determined by market level, player rank, and rebirth level. Base values vary based on these factors and are configured by server administrators.*

Unlike regular villagers, Collectors do not offer any items for sale - they only purchase BD crops from players.

## Villager Administration

Server administrators can manage BD Villagers using these commands:

- `/bdadmin spawn dealer` - Spawns a BD Dealer
- `/bdadmin spawn collector` - Spawns a collector
- `/bdadmin villager reload` - Reloads villager configurations
- `/bdadmin villager remove <id>` - Removes a specific BD Villager

## Configuration

Villager behavior can be configured in the plugin's configuration:

```yaml
villagers:
  dealer:
    spawn-chance: 45  # Percentage chance in villages
    trade-refresh: 24 # Hours between trade refreshes
  
  collectors:
    max-level-1: 3    # Maximum collectors at market level 1
    max-level-4: 10   # Maximum collectors at market level 4
  
  seasonal:
    visit-frequency: 7 # Days between visits
    stay-duration: 3   # Days the trader stays
```