# Rank System

The BDCraft rank system provides progression through farming achievements, offering players increased benefits and status as they advance.

## Rank Overview

The plugin features 5 progressive ranks that players can achieve:

1. **Newcomer (Rank 1)** - Starting rank for all players
2. **Farmer (Rank 2)** - Basic farming accomplishments
3. **Expert Farmer (Rank 3)** - Advanced farming skills
4. **Master Farmer (Rank 4)** - Exceptional farming mastery
5. **Agricultural Expert (Rank 5)** - Elite farming status

## Rank Requirements

Each rank requires specific achievements to unlock:

### Newcomer (Rank 1)
- Default starting rank
- No requirements

### Farmer (Rank 2)
- Harvest 100 BD crops
- Own at least 1 BD tool
- Trade with BD Dealers 10 times

### Expert Farmer (Rank 3)
- Harvest 500 BD crops
- Own at least 3 different BD tools
- Reach level 2 in a player market
- Trade with collectors 25 times

### Master Farmer (Rank 4)
- Harvest 2,000 BD crops
- Own all BD tools including Ultimate BD Harvester
- Reach level 3 in a player market

### Agricultural Expert (Rank 5)
- Harvest 10,000 BD crops
- Own a level 4 player market
- Complete all farming achievements
- Help 10 other players with BD farming (Give players BD seeds at least 10 players minimum 20 seeds each)

## Rank Benefits

Each rank provides increasing benefits:

### Newcomer (Rank 1)
- Basic access to BD content
- Standard harvest rates from BD crops
- Regular auction house fees (5%)

### Farmer (Rank 2)
- 5% increased harvest yield from BD crops
- 4.5% auction house fees
- Access to Farmer-only trades with BD Dealers
- Custom chat prefix: [Farmer]

### Expert Farmer (Rank 3)
- 10% increased harvest yield from BD crops
- 4% auction house fees
- Extended auction listing duration (4 days)
- Ability to grow green BD seeds faster
- Custom chat prefix: [Expert Farmer]

### Master Farmer (Rank 4)
- 20% increased harvest yield from BD crops
- 3.5% auction house fees
- Extended auction listing duration (5 days)
- Higher chance of rare drops from BD crops
- Custom chat prefix: [Master Farmer]

### Agricultural Expert (Rank 5)
- 30% increased harvest yield from BD crops
- 3% auction house fees
- Extended auction listing duration (7 days)
- Featured auction listings
- Ability to set custom market effects
- Custom chat prefix: [Agricultural Expert]

## Rank Commands

Players can use these commands to interact with the rank system:

- `/bdrank` - View your current rank and progress
- `/bdrank info <rank>` - View information about a specific rank
- `/bdrank benefits` - List the benefits of your current rank
- `/bdrank progress` - Check your progress toward the next rank

Administrators can use additional commands:

- `/bdadmin rank set <player> <rank>` - Set a player's rank
- `/bdadmin rank reset <player>` - Reset a player's rank to Newcomer
- `/bdadmin rank reload` - Reload rank configuration

## Rank Configuration

Ranks are configurable in the plugin's configuration:

```yaml
ranks:
  farmer:
    required-harvests: 100
    benefits:
      yield-bonus: 5
      auction-fee: 4.5
  
  expert-farmer:
    required-harvests: 500
    benefits:
      yield-bonus: 10
      auction-fee: 4.0
  
  master-farmer:
    required-harvests: 2000
    benefits:
      yield-bonus: 20
      auction-fee: 3.5
  
  agricultural-expert:
    required-harvests: 10000
    benefits:
      yield-bonus: 30
      auction-fee: 3.0
```