# Custom Villager System

The BDCraft Villager System provides specialized villager NPCs that form the backbone of the server's economic and trading infrastructure.

## Overview

The BDCraft Villager System includes:
- Specialized villager types with unique appearances and trades
- Integration with markets and economic systems
- Custom trades that enhance the vanilla Minecraft experience
- Reputation and progression through trading

## Villager Types

### BD Dealer

**Appearance:**
- Profession: FARMER
- Villager Type: PLAINS
- Level: 2 (Experienced)
- Name: "BD Dealer" (green text)

**Spawning:**
- Natural spawning in vanilla villages (45% chance)
- Automatically spawns with new player markets
- Can be spawned by admins with commands

**Trades:**
- Regular BD Seeds (5 seeds for 1 emerald)
- Green BD Seeds (premium seeds, higher cost)
- Purple BD Seeds (rare seeds, 25 emeralds)
- BD Harvester (special tool, 16 diamonds)
- Ultimate BD Harvester (top-tier tool, 32 diamonds)

### Market Owner

**Appearance:**
- Profession: LIBRARIAN
- Villager Type: TAIGA
- Level: 3 (Expert)
- Name: "[Player]'s Market" (gold text)

**Spawning:**
- Created when a player uses a Market Token
- One per player market

**Features:**
- Market management interface
- Upgrade options for markets
- Associate management
- House Token sales (at higher levels)

### Collector

**Appearance:**
- Various professions based on specialization
- Villager Type: Based on biome
- Level: 1-5 (based on market level)
- Name: "[Type] Collector" (cyan text)

**Spawning:**
- Created when a player uses a House Token within market boundaries
- Limited by market level (3-10 based on level)

**Trades:**
- Specialized trades based on collector type
- Pricing affected by market level
- Trade offerings refreshed periodically

### Seasonal Trader

**Appearance:**
- Profession: NITWIT
- Villager Type: SWAMP
- Level: 4 (Master)
- Name: "Seasonal Trader" (purple text)

**Spawning:**
- Appears periodically in level 3+ markets
- Available for limited time periods

**Trades:**
- Rare seasonal items
- Special event-related trades
- Limited quantity offerings

## Villager Interaction

Players can interact with BD Villagers through:
- Right-clicking to open trade interface
- Special GUI menus for market management
- Command-based management for market owners

## Administrative Commands

Administrators can manage BD Villagers with:
- `/bdadmin spawn dealer` - Spawns a BD Dealer at current location
- `/bdadmin spawn collector <type>` - Spawns a specific collector type
- `/bdadmin villager reload` - Reloads villager configurations
- `/bdadmin villager remove <id>` - Removes a specific BD Villager

## Technical Implementation

BD Villagers are enhanced versions of vanilla Minecraft villagers with:
- Custom trading mechanics
- Special behaviors and AI
- Persistent data storage
- Integration with other BDCraft systems

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