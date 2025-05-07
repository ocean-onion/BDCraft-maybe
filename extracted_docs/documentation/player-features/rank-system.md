# BD Rank System

The BDCraft plugin features a comprehensive rank system that provides progression paths for players within the BD economy. Each rank unlocks new abilities, items, and benefits.

## Rank Overview

There are five main ranks in the BD system, each with increasing benefits and requirements:

### Rank 1: Newcomer

**Starting Rank - Default for all players**

- **Access to**:
  - Regular BD Seeds (purchase from Dealers)
  - BD Stick crafting
  - Basic trading with BD Collectors

- **Benefits**:
  - Base crop value (no modifiers)
  - Standard reputation gain with villagers

- **Requirements**:
  - None (default rank)

### Rank 2: Farmer

**First progression rank**

- **Access to**:
  - Green BD Seeds (purchase from Dealers)
  - All Newcomer features

- **Benefits**:
  - 10% increased BD crop value when trading
  - Slightly faster reputation gain (+0.5 per trade)

- **Requirements**:
  - 5,000 server currency to purchase
  - Command: `/bdrank up` (purchases automatically)

### Rank 3: Expert Farmer

**Specialized tool access rank**

- **Access to**:
  - BD Harvester purchase (from Dealers)
  - All Farmer features

- **Benefits**:
  - 25% increased BD crop value when trading
  - Faster reputation gain (+1 per trade)
  - 5% chance to get double seeds when purchasing

- **Requirements**:
  - 15,000 server currency to purchase
  - Must have Farmer rank first
  - Command: `/bdrank up` (purchases automatically)

### Rank 4: Master Farmer

**Advanced crop access rank**

- **Access to**:
  - Purple BD Seeds (purchase from Dealers)
  - All Expert Farmer features
  - Special auction privileges (lower fees)

- **Benefits**:
  - 40% increased BD crop value when trading
  - Much faster reputation gain (+1.5 per trade)
  - 10% chance to get double seeds when purchasing

- **Requirements**:
  - 30,000 server currency to purchase
  - Must have Expert Farmer rank first
  - Command: `/bdrank up` (purchases automatically)

### Rank 5: Agricultural Expert

**Master rank**

- **Access to**:
  - Ultimate BD Harvester purchase (from Seasonal Traders)
  - All Master Farmer features
  - Ability to sponsor other players (share bonus)

- **Benefits**:
  - 60% increased BD crop value when trading
  - Maximum reputation gain (+2 per trade)
  - 15% chance to get double seeds when purchasing
  - Ability to sell seeds for 10% more to other players

- **Requirements**:
  - 60,000 server currency to purchase
  - Must have Master Farmer rank first
  - Command: `/bdrank up` (purchases automatically)

## How to Earn Ranks

1. **Earn Server Currency**:
   - Sell BD crops to Collectors
   - Complete server tasks/missions (if enabled)
   - Trade with other players

2. **Check Current Status**:
   - Use `/bdrank` command to see current rank and progress
   - Use `/bdbal` to check current server currency balance

3. **Purchase Rank Upgrade**:
   - Use `/bdrank up` with sufficient server currency
   - System automatically takes currency and grants rank

## Rank Benefits Breakdown

| Rank | Name | Crop Value Bonus | Rep Gain | Double Seeds Chance | Cost |
|------|------|-----------------|----------|---------------------|------|
| 1 | Newcomer | 0% | Standard | 0% | Free |
| 2 | Farmer | +10% | +0.5 | 0% | 5,000 |
| 3 | Expert Farmer | +25% | +1.0 | 5% | 15,000 |
| 4 | Master Farmer | +40% | +1.5 | 10% | 30,000 |
| 5 | Agricultural Expert | +60% | +2.0 | 15% | 60,000 |

## Special Rank Features

### Sponsor System (Rank 5 Only)

Agricultural Experts can sponsor other players:

- Command: `/bdsponsor <player>`
- Effect: Sponsored player gets +15% crop value for 2 hours
- Cooldown: 6 hours between sponsorships
- Maximum: Can sponsor up to 3 players simultaneously

### Rank Prestige and Rebirth

After reaching the Agricultural Expert rank, players can choose to "rebirth":

- Command: `/bdrebirth`
- Effect:
  - Resets rank to Newcomer
  - Removes all server currency
  - Adds permanent +10% crop value multiplier (stacks with each rebirth)
  - Grants special prefix and particle effects

After 10 rebirths, players reach "Agricultural Deity" status with special abilities.

## Commands

- `/bdrank` - Shows current rank and progress
- `/bdrank up` - Purchases next rank if requirements are met
- `/bdrank info <rank>` - Shows detailed information about a specific rank
- `/bdrank list` - Lists all ranks and their benefits
- `/bdbal` - Shows current server currency balance

The rank system provides a clear progression path for players in the BDCraft economy, with each rank offering meaningful benefits that enhance the BD farming experience.