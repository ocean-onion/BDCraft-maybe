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
- Admin villages: Every third village can have a dealer

**Trades:**
- Regular BD Seeds: 5 seeds for 1 emerald
- Green BD Seeds: Premium seeds for more emeralds
- Purple BD Seeds: Rare seeds for 25 emeralds
- BD Harvester: Special tool for 16 diamonds
- Ultimate BD Harvester: Top-tier tool for 32 diamonds

### Market Owner

**Visual Appearance:**
- Base: Villager
- Profession: CARTOGRAPHER
- Villager Type: TAIGA
- Level: 3 (Expert)
- Name: "[Player]'s Market" (gold text)

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
- Profession: Various based on specialization
- Villager Type: Based on biome
- Level: 1-5 (based on market level)
- Name: "[Type] Collector" (cyan text)

**Spawning:**
- Created when a player builds a collector house and uses a House Token
- Limited by market level (3-10 based on level)

**Functions:**
- Buys BD crops from players for emeralds and server currency
- Trading prices affected by market level
- Specializes in different crop types

### Seasonal Trader

**Visual Appearance:**
- Base: Villager
- Profession: NITWIT
- Villager Type: SWAMP
- Level: 4 (Master)
- Name: "Seasonal Trader" (purple text)

**Spawning:**
- Appears periodically in level 3+ markets
- Available for limited time periods

**Functions:**
- Sells rare seasonal items
- Offers special event-related trades
- Provides limited quantity offerings

## Villager Interaction

Players interact with BD Villagers by right-clicking on them, which opens their trading interface. Special villagers like the Market Owner will also provide access to GUI menus for additional management functions.

### Trading With Villagers

1. Right-click on the villager to open the trading interface
2. Browse available trades by scrolling through the interface
3. Select a trade and fulfill the requirements (provide items/emeralds)
4. Receive the traded item in return

### Market Owner Interaction

1. Right-click on the Market Owner villager
2. Select "Market Management" from the trading menu
3. Access various tabs for different management functions:
   - Associates Tab
   - Permissions Tab
   - Security Tab
   - Upgrades Tab
   - Settings Tab

## Market Collectors

Market collectors are specialized villagers that buy crops from players. Each collector has different specializations:

1. **Wheat Collector:**
   - Buys BD wheat and related products
   - Gives emeralds and server currency

2. **Vegetable Collector:**
   - Buys BD carrots, potatoes, beetroots
   - Gives emeralds and server currency

3. **Fruit Collector:**
   - Buys BD melons, pumpkins, berries
   - Gives emeralds and server currency

4. **Special Collector:**
   - Buys rare BD crops
   - Gives premium prices in emeralds and server currency

## Villager Administration

Server administrators can manage BD Villagers using these commands:

- `/bdadmin spawn dealer` - Spawns a BD Dealer
- `/bdadmin spawn collector <type>` - Spawns a specific collector type
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