# BDCraft Token System

BDCraft includes a powerful token system that allows players to earn and spend special tokens throughout the server. This document explains how the token system works and how it integrates with other aspects of the plugin.

## What Are Tokens?

Tokens are a secondary currency in BDCraft that function differently from the main economy. While the main economy (BDCoins) is used for everyday transactions, tokens are special rewards earned through specific activities and used to purchase unique items and perks.

## Earning Tokens

Players can earn tokens through various activities:

| Activity | Token Reward | Cooldown |
|----------|--------------|----------|
| Daily login | 1-3 tokens | 24 hours |
| Boss defeats | 5-20 tokens | Per boss |
| Special events | 10-50 tokens | Per event |
| Rank advancement | 5 tokens per rank | One-time |
| Rebirth | 25 tokens | Per rebirth |
| Voting for the server | 1 token | 24 hours per site |
| Market achievements | 1-10 tokens | Various |
| Weekly challenges | 5-25 tokens | Weekly |

## Spending Tokens

Tokens can be spent in the following ways:

### Token Shop

The token shop offers special items that can only be purchased with tokens:

| Item Category | Description | Token Cost |
|---------------|-------------|------------|
| Cosmetic Items | Special visual effects, particle trails, etc. | 5-25 tokens |
| Unique Equipment | Special equipment with custom enchants | 20-100 tokens |
| Boosters | Experience and currency boosters | 10-50 tokens |
| Exclusive Recipes | Unlock special crafting recipes | 15-40 tokens |
| Titles | Special titles for chat | 5-15 tokens |
| Pets | Companion mobs with special abilities | 30-100 tokens |

### Special Features

Tokens can also be used to:

| Feature | Description | Token Cost |
|---------|-------------|------------|
| Reset cooldowns | Reset teleportation or ability cooldowns | 1-5 tokens |
| Extra homes | Purchase additional home slots | 10 tokens per slot |
| Auction house features | Create featured listings | 5 tokens |
| Market upgrades | Upgrade market size or features | 15-50 tokens |
| Villager restocks | Force a villager to restock trades | 2 tokens |
| Skill respec | Reset and reallocate skill points | 20 tokens |

## Token Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdtoken` or `/bdtk` | View your token balance | bdcraft.token.use |
| `/bdtoken shop` | Open the token shop | bdcraft.token.use |
| `/bdtoken send <player> <amount>` | Send tokens to another player | bdcraft.token.send |
| `/bdtoken history` | View your token transaction history | bdcraft.token.use |
| `/bdtoken top` | View the token leaderboard | bdcraft.token.use |
| `/bdtoken buy <amount>` | Purchase tokens with server currency | bdcraft.token.buy |
| `/bdtoken daily` | Claim your daily token reward | bdcraft.token.daily |
| `/bdtoken rewards` | View available token rewards | bdcraft.token.use |
| `/bdtoken admin give <player> <amount>` | Give tokens to a player (admin) | bdcraft.token.admin |
| `/bdtoken admin take <player> <amount>` | Take tokens from a player (admin) | bdcraft.token.admin |
| `/bdtoken admin set <player> <amount>` | Set a player's token balance (admin) | bdcraft.token.admin |

## Token Configuration

Administrators can configure the token system in the `tokens.yml` configuration file:

```yaml
# Token System Configuration

# Token Rewards
rewards:
  daily:
    min: 1
    max: 3
    cooldown: 86400  # 24 hours in seconds
  voting:
    amount: 1
    sites:
      - "example1.com"
      - "example2.com"
  boss-defeats:
    ender_dragon: 15
    wither: 10
    elder_guardian: 5
  
# Token Shop
shop:
  categories:
    cosmetics:
      enabled: true
      items:
        particle_trail_fire:
          name: "Fire Trail"
          cost: 15
          description: "Leave a trail of fire particles as you walk"
        
    equipment:
      enabled: true
      items:
        lucky_sword:
          name: "Sword of Fortune"
          cost: 50
          description: "A sword with Fortune III and Looting III"
          
    boosters:
      enabled: true
      items:
        xp_booster:
          name: "XP Booster"
          cost: 20
          duration: 3600  # 1 hour in seconds
          multiplier: 2.0
          description: "Double XP for one hour"

# Token Economy Settings
economy:
  # Whether tokens can be traded between players
  tradeable: true
  # Whether tokens can be purchased with regular currency
  purchasable: true
  # The cost in regular currency per token
  purchase-cost: 5000
  # Maximum tokens a player can have
  max-balance: 1000
```

## Integration with Other Modules

The token system integrates with other BDCraft modules:

### Economy Module

- Special market stalls can accept tokens instead of regular currency
- Auctions can be listed with token prices
- Villagers can offer token-based trades

### Progression Module

- Rank advancements grant token rewards
- Rebirth provides significant token bonuses
- Tokens can be used to purchase rank or rebirth discounts

### Vital Module

- Teleport cooldowns can be bypassed using tokens
- Additional home slots can be purchased with tokens
- Special chat formats and colors can be unlocked with tokens

## Token Events

BDCraft regularly runs special events where players can earn bonus tokens:

- **Token Rush**: Periodic events where token rewards are doubled
- **Token Quests**: Special quests that offer token rewards
- **Boss Events**: Special boss fights with increased token drops
- **Token Lottery**: Spend tokens for a chance to win more tokens

## Best Practices

For server administrators:

1. **Balance the Economy**: Carefully balance token rewards and costs to maintain token value
2. **Regular Events**: Host regular token events to keep players engaged
3. **Seasonal Rewards**: Offer special seasonal items in the token shop
4. **Monitor Usage**: Track token usage patterns and adjust rewards/costs accordingly
5. **Token Sinks**: Ensure there are enough ways to spend tokens to keep them valuable