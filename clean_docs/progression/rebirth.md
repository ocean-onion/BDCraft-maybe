# Rebirth System

The BDCraft Rebirth System provides prestigious progression mechanics enabling elite players to exchange accumulated progress for permanent enhancement bonuses, creating extended gameplay engagement beyond traditional rank advancement.

## Prestige Progression Infrastructure

### Elite Advancement Mechanism
The Rebirth system enables Agricultural Expert players to undergo complete progression reset in exchange for permanent enhancement benefits:

- Reset to Newcomer rank (Rank 1)
- Lose all BD items and market buildings
- Gain permanent bonuses to crop yields and trading
- Receive special cosmetic effects and titles
- Unlock exclusive Rebirth-only features

## Prestige Enhancement Tiers

### Multi-Level Rebirth System
Players can undergo multiple rebirth cycles, with each iteration providing escalating permanent benefits:

#### Prestige Level 1
- +5% permanent bonus to all crop yields
- +3% permanent better trading prices
- Title: [Reborn Farmer]
- Access to special Rebirth 1 cosmetics

#### Prestige Level 2
- +10% permanent bonus to all crop yields
- +6% permanent better trading prices
- Title: [Twice Reborn]
- Exclusive BD seed color variations
- Special market visual effects

#### Prestige Level 3
- +15% permanent bonus to all crop yields
- +9% permanent better trading prices
- Title: [Thrice Reborn]
- Exclusive collector villager appearances
- Ability to create markets with expanded radius

#### Prestige Level 4
- +20% permanent bonus to all crop yields
- +12% permanent better trading prices
- Title: [Ascended Farmer]
- Unlock legendary BD crops
- Create markets with no building requirements

#### Prestige Level 5 (Maximum)
- +25% permanent bonus to all crop yields
- +15% permanent better trading prices
- Title: [BD Master]
- Halo effect around player
- Custom particle effects while farming
- Ability to create special Mastery Markets

## Rebirth Process

To undergo the rebirth process:

1. Reach Agricultural Expert rank (Rank 5)
2. Complete all special achievements for your rebirth level
3. Gather required resources for the rebirth ritual:
   - Rebirth 1: 64 emeralds + 16 diamonds
   - Rebirth 2: 128 emeralds + 32 diamonds
   - Rebirth 3: 256 emeralds + 64 diamonds
   - Rebirth 4: 512 emeralds + 128 diamonds
   - Rebirth 5: 1024 emeralds + 256 diamonds
4. Use `/bdrebirth confirm` to initiate the rebirth process
5. Confirm the process in the confirmation GUI

### What Happens During Rebirth

When you complete a rebirth, the following changes occur:

1. **Rank Reset**: 
   - Your rank is reset to Newcomer (Rank 1)
   - You must progress through ranks again to reach Agricultural Expert

2. **Currency Reset**:
   - Your server currency is reset to 0 
   - Regular players lose all server currency
   - Donors retain 20% of their server currency (bdcraft.rebirth.retention perk)

3. **Market Impact**:
   - Your existing markets remain intact
   - Market levels and structures are preserved
   - All associates and permissions remain unchanged

4. **Inventory Impact**:
   - BD Seeds in your inventory are preserved
   - BD Tools (Harvester, Ultimate Harvester) remain in your inventory
   - BD Tokens (Market/House) in your inventory are preserved
   - All other BD items are preserved

5. **Permanent Benefits**:
   - You gain the permanent yield and trading bonuses for your new rebirth level
   - You unlock the cosmetic and functional benefits of your rebirth level
   - All bonuses from previous rebirth levels are retained and stack

6. **Formula for Benefits**:
   - Crop Yield Bonus: Base Yield × (1 + (Rebirth_Level × 0.05))
   - Trading Price Bonus: Base Price × (1 + (Rebirth_Level × 0.03))
   - These bonuses apply to all transactions with BD villagers

## Rebirth Benefits

The rebirth system provides several types of permanent benefits:

### Yield Bonuses
- Each rebirth level adds a permanent % increase to all BD crop yields
- These bonuses stack with regular rank bonuses
- Example: Rank 3 Expert Farmer with Rebirth 2 gets 10% (rank) + 10% (rebirth) = 20% total yield bonus

### Trading Bonuses
- Better prices when trading with all BD villagers
- Stacks with market level bonuses
- Example: Level 3 market with Rebirth 2 gets 10% (market) + 6% (rebirth) = 16% better prices

### Exclusive Features
- Each rebirth level unlocks special features not available to non-rebirth players
- Includes special crops, building abilities, visual effects, and more
- Higher rebirth levels unlock increasingly rare and powerful features

## Rebirth Commands

Players can interact with the rebirth system using these commands:

- `/bdrebirth` - Check your rebirth status and available options
- `/bdrebirth info` - View information about the rebirth system
- `/bdrebirth requirements` - Check what you need for your next rebirth
- `/bdrebirth confirm` - Initiate the rebirth process (when eligible)

Administrators can use:

- `/bdadmin rebirth set <player> <level>` - Set a player's rebirth level
- `/bdadmin rebirth reset <player>` - Reset a player's rebirth progress
- `/bdadmin rebirth reload` - Reload rebirth configuration

## Configuration

The rebirth system is configurable in the plugin's configuration:

```yaml
rebirth:
  enabled: true
  max-level: 5
  
  requirements:
    rebirth-1:
      emeralds: 64
      diamonds: 16
    rebirth-2:
      emeralds: 128
      diamonds: 32
    # Additional levels configured similarly
  
  benefits:
    rebirth-1:
      yield-bonus: 5
      trading-bonus: 3
    rebirth-2:
      yield-bonus: 10
      trading-bonus: 6
    # Additional levels configured similarly
```