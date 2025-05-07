# BD Villager System

The villager system is a core component of the BDCraft plugin, providing the trading infrastructure for the BD economy. There are four types of specialized BD villagers, each with unique roles and appearance. These villagers form the backbone of both admin-created villages and player-driven markets.

## BD Villager Types

### 1. BD Dealer

**Visual Appearance:**
- Base Entity: Villager (EntityType.VILLAGER)
- Profession: FARMER (Villager.Profession.FARMER)
- Villager Type: PLAINS (Villager.Type.PLAINS)
- Villager Level: 2 (Experienced)
- Custom Name: "BD Dealer" (with green text color)
- Name Visibility: Always visible

**Distribution & Spawning:**
- Naturally spawns in vanilla Minecraft villages (45% chance per village)
- Spawns automatically when a player creates a BD Market
- Admin villages: Every third village can have a dealer
- Admin spawn command: `/bdadmin spawn dealer <village_id>`

**Trades:**
- Regular BD Seeds: 5 seeds for 1 emerald (Wheat Seeds item)
- Green BD Seeds: Premium seeds for more emeralds (Beetroot Seeds item)
- Purple BD Seeds: Rare seeds for 25 emeralds (Pumpkin Seeds item)
- BD Harvester: Special tool for 16 diamonds, breaks after 20 uses
- Ultimate BD Harvester: Top-tier tool for 32 diamonds, breaks after 60 uses

**Reputation Impact:**
- Successful Trade: +2 reputation in the village

### 2. BD Collector

**Visual Appearance:**
- Base Entity: Villager (EntityType.VILLAGER)
- Profession: LIBRARIAN (Villager.Profession.LIBRARIAN)
- Villager Type: PLAINS (Villager.Type.PLAINS)
- Villager Level: 3 (Expert)
- Custom Name: "BD Collector" (with aqua/blue text color)
- Name Visibility: Always visible

**Distribution & Spawning:**
- Spawns when a player places a House Token in a properly built collector house
- Admin villages: Each village can have up to 4 BD collectors
- Admin spawn command: `/bdadmin spawn collector <village_id>`

**Trades:**
- Regular BD Crops: Buys 10 regular BD crops (ferns) for 2 emeralds + 50 server currency
- Green BD Crops: Buys 5 green BD crops (large ferns) for 10 emeralds + 150 server currency
- Purple BD Crops: Buys 3 purple BD crops for 20 emeralds + 400 server currency
- Large Quantities: Special trades for bulk amounts (50 regular BD for 1 diamond)

**Reputation Impact:**
- Successful Trade: +3 reputation in the village

### 3. Market Owner

**Visual Appearance:**
- Base Entity: Villager (EntityType.VILLAGER)
- Profession: CARTOGRAPHER (Villager.Profession.CARTOGRAPHER)
- Villager Type: PLAINS (Villager.Type.PLAINS)
- Villager Level: 4 (Expert)
- Custom Name: "Market Owner" (with gold text color)
- Name Visibility: Always visible

**Distribution & Spawning:**
- Spawns automatically when a player creates a BD Market
- Only spawns in player-created markets, not in admin villages

**Trades:**
- Market Upgrades: Trade diamonds and server currency to level up market
- House Tokens: At higher levels, sell pre-made house tokens
- Market Information: Trade emerald for current market status report
- Special Decorations: Unique items for market decoration

**Market Upgrade Benefits:**
- Level 1 (Initial): Basic market, up to 3 collectors
- Level 2: Increased collector limit (5), 5% better prices, house tokens available
- Level 3: Increased collector limit (7), 10% better prices, seasonal trader visits
- Level 4: Increased collector limit (10), 15% better prices, player buffs in market radius

**Reputation Impact:**
- Successful Trade: +2 reputation in the village

### 4. Seasonal BD Trader

**Visual Appearance:**
- Base Entity: Villager (EntityType.VILLAGER)
- Profession: NITWIT (Villager.Profession.NITWIT)
- Villager Type: PLAINS (Villager.Type.PLAINS)
- Villager Level: 1 (Novice)
- Custom Name: "Seasonal BD Trader" (with yellow text color)
- Name Visibility: Always visible

**Distribution & Spawning:**
- Spawns in player markets at level 3 or higher
- Rare visitor in both player and admin villages
- Admin spawn command: `/bdadmin spawn seasonal <village_id>`

**Trades:**
- Seasonal Specialties: Unique, limited-time trades that change with the current season
- Special Items: Rare and unique BD-related items
- Purple BD Seeds: Highest tier of BD seeds (extremely rare)

**Reputation Impact:**
- Successful Trade: +4 reputation in the village (highest gain due to rarity)

## Village Creation & Management

### Natural Village Dealers
- BD Dealers have a 45% chance to naturally spawn in vanilla Minecraft villages
- These wandering dealers allow players to start participating in the BD economy
- Players can find these dealers in villages and purchase their first BD seeds
- Dealers in natural villages cannot be removed and will respawn if killed
- Dealers ONLY spawn in natural villages or when a player creates a market - they don't spawn anywhere else
- Collectors NEVER spawn naturally - they only appear when players build collector houses with tokens

### Player Progression Path
1. Find wandering BD Dealer in a natural village (45% spawn chance)
2. Purchase BD seeds (wheat, beetroot, or pumpkin seeds)
3. Grow BD crops and harvest them as ferns
4. Find or create a BD Collector to sell crops for emeralds (only found in player markets)
5. Craft BD sticks using Paper + BD crop + Flint (5 uses per stick)
6. Create a BD Market Token (BD Stick surrounded by diamonds with 2 emeralds)
7. Build your own market to have reliable access to both dealers and collectors

### Player-Driven Market Creation
- Players can create their own BD markets by building specific structures
- Build a market stall with a 3x3 roof, bed, walls with door, and BD Market Token
- Place BD Market Token (crafted item) in an item frame above the market stall door
- This spawns a Dealer and Market Owner at the location

### Collector Houses
- Players can expand their markets by building collector houses
- Same requirements as market stall (3x3 roof, bed, walls with door)
- Place a House Token (bed surrounded by BD crops) in an item frame above door
- This spawns a BD Collector in the house

### Market Boundaries
- Each market has a 49x49 block radius from the Market Token
- Markets must be at least 30 blocks away from other existing markets
- Use `/bdmarket check` to visualize boundaries with temporary wool blocks
- Red wool shows the boundary, black wool marks corners

### Admin Village Management
- Admins can still create villages using: `/bdadmin village create <n>`
- Use `/bdadmin spawn collector <village_id>` to spawn a collector
- Use `/bdadmin spawn dealer <village_id>` to spawn a dealer
- Use `/bdadmin spawn seasonal <village_id>` to spawn a seasonal trader

### Village Finder
- Players can use `/bdvil locate` to find the nearest BD village
- Players can use `/bdvil list` to see all villages they have discovered
- Players can use `/bdmarket list` to see all markets they own or are associates of

## Reputation System

The BDCraft plugin features a trader-specific reputation system:

1. **Per-Trader Tracking**:
   - Reputation is tracked separately for each individual BD trader
   - Players can have different reputation levels with different traders
   - Each trader (dealer/collector/seasonal) has its own reputation counter

2. **Reputation Benefits**:
   - Higher reputation = Better prices from that specific trader
   - Lower buy prices from dealers with higher reputation
   - Higher sell prices when trading with collectors with higher reputation
   - Market reputation is the average of all traders within a market

3. **Reputation Gain/Loss**:
   - Trading with Dealers: +2 reputation with that specific dealer
   - Trading with Collectors: +3 reputation with that specific collector
   - Trading with Seasonal Traders: +4 reputation with that specific trader
   - Failed trades (not enough items): -1 reputation with that specific trader
   - Attacking traders: -10 reputation with that specific trader
   - Killing traders: -50 reputation with that specific trader and -20 with others in the same market

4. **Reputation Display**:
   - Players receive reputation change notifications when trading
   - Use `/bdrep` to check reputation with the nearest trader
   - Use `/bdrep all` to see reputation with all traders you've interacted with
   - Use `/bdrep market` to check reputation with all traders in your current market

These specialized villagers form the backbone of the BD economy system, creating a complete trading loop with different tiers of items and rewards based on player progression. The individual-trader reputation system encourages players to build relationships with specific traders rather than whole villages.