# Rank System

The BDCraft rank system provides progression through statistical requirements, offering players increased benefits and status as they advance.

## Rank Overview

The plugin features 5 progressive ranks that players can achieve:

1. **Newcomer (Rank 1)** - Starting rank for all players
2. **Farmer (Rank 2)** - Basic farming accomplishments
3. **Expert Farmer (Rank 3)** - Advanced farming skills
4. **Master Farmer (Rank 4)** - Exceptional farming mastery
5. **Agricultural Expert (Rank 5)** - Elite farming status

## Rank Requirements

Ranks are purchased using server currency, with specific achievement requirements that must be completed before purchase:

### Newcomer (Rank 1)
- Default starting rank
- No requirements
- No cost

### Farmer (Rank 2)
- Requirements:
  - Harvest 100 BD crops
  - Trade with BD Dealers 10 times
- Cost: 500 server currency

### Expert Farmer (Rank 3)
- Requirements:
  - Harvest 500 BD crops
  - Plant 300 BD seeds
  - Trade with collectors 25 times
- Cost: 2,000 server currency

### Master Farmer (Rank 4)
- Requirements:
  - Harvest 2,000 BD crops
  - Plant 800 BD seeds
  - Trade with collectors 80 times
  - Trade with dealers 50 times
  - Own or be an associate in a market
- Cost: 5,000 server currency

### Agricultural Expert (Rank 5)
- Requirements:
  - Harvest 10,000 BD crops
  - Plant 3,500 BD seeds
  - Trade with collectors 150 times
  - Trade with dealers 80 times
  - Own a level 5 market
  - Own all BD tools
  - Help 10 other players with BD farming (give at least 20 seeds to each of 10 different players)
- Cost: 10,000 server currency

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
- 4% auction house fees instead of 5%
- Extended auction listing duration (4 days)
- Seeds planted grow 5% faster
- Access to buy BD tools from natural spawning dealers
- Custom chat prefix: [Expert Farmer]

### Master Farmer (Rank 4)
- 20% increased harvest yield from BD crops
- 3.5% auction house fees instead of 5%
- Extended auction listing duration (5 days)
- Seeds planted grow 15% faster
- 15% higher chance of getting a rare crop than the one planted
- Custom chat prefix: [Master Farmer]

### Agricultural Expert (Rank 5)
- 30% increased harvest yield from BD crops
- 3% auction house fees instead of 5%
- Extended auction listing duration (7 days)
- Seeds planted grow 25% faster
- 25% higher chance of getting a rare crop than the one planted
- Choice of permanent effect (invisibility, strength, night vision, fire resistance, jump boost, speed, slow falling, or water breathing)
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