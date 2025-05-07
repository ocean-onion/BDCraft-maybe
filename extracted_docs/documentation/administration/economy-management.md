# Economy Management

The BDCraft economy system is a core component of the plugin, providing a unique farming-based economy centered around BD crops, villager trading, and player progression. This guide covers how to manage, balance, and optimize the BD economy on your server.

## Economy Overview

The BD economy consists of several interconnected systems:

1. **Currency System**:
   - Server Currency (for ranks and player-to-player trading)
   - Item Currencies (emeralds, diamonds)

2. **Trading System**:
   - BD Villager trades (seeds, crops)
   - Player-to-player trading via auction house

3. **Progression System**:
   - Ranks (purchased with server currency)
   - Rebirth levels (prestige system)

## Economy Commands

### Basic Economy Commands

#### Viewing Player Balance

```
/bdadmin economy balance <player>
```

**Example**:
```
/bdadmin economy balance Steve
```

Shows Steve's current server currency balance.

#### Giving Currency

```
/bdadmin economy give <player> <amount>
```

**Example**:
```
/bdadmin economy give Steve 5000
```

Adds 5000 server currency to Steve's balance.

#### Taking Currency

```
/bdadmin economy take <player> <amount>
```

**Example**:
```
/bdadmin economy take Steve 1000
```

Removes 1000 server currency from Steve's balance.

#### Setting Currency

```
/bdadmin economy set <player> <amount>
```

**Example**:
```
/bdadmin economy set Steve 10000
```

Sets Steve's server currency balance to 10000.

### Rank Management Commands

#### Setting Player Rank

```
/bdadmin rank set <player> <rank>
```

**Example**:
```
/bdadmin rank set Steve 3
```

Sets Steve's rank to 3 (Expert Farmer).

#### Viewing Player Rank

```
/bdadmin rank view <player>
```

**Example**:
```
/bdadmin rank view Steve
```

Shows Steve's current rank and progress.

### Rebirth Management Commands

#### Setting Rebirth Level

```
/bdadmin rebirth <player> <level>
```

**Example**:
```
/bdadmin rebirth Steve 2
```

Sets Steve's rebirth level to 2.

#### Viewing Rebirth Status

```
/bdadmin rebirth view <player>
```

**Example**:
```
/bdadmin rebirth view Steve
```

Shows Steve's current rebirth level and benefits.

## Economy Configuration

The BD economy is configured in `economy.yml`:

```yaml
# Currency Settings
currency-name: "Coins"
currency-name-plural: "Coins"
currency-symbol: "$"

# Item Prices (Emeralds)
prices:
  regular-seed: 3  # cost for 5 seeds
  green-seed: 9    # cost per seed
  purple-seed: 25  # cost per seed
  bd-harvester: 16  # diamonds
  ultimate-harvester: 32  # diamonds

# Trade Values
trade-values:
  regular-crop:
    emeralds: 2    # per 10 crops
    currency: 50   # per 10 crops
  green-crop:
    emeralds: 10   # per 5 crops
    currency: 150  # per 5 crops
  purple-crop:
    emeralds: 20   # per 3 crops
    currency: 400  # per 3 crops

# Rank Costs
rank-costs:
  farmer: 5000
  expert-farmer: 15000
  master-farmer: 30000
  agricultural-expert: 60000

# Rank Benefits
rank-benefits:
  newcomer:
    crop-value-bonus: 0.0
    reputation-bonus: 0.0
  farmer:
    crop-value-bonus: 0.1
    reputation-bonus: 0.5
  expert-farmer:
    crop-value-bonus: 0.25
    reputation-bonus: 1.0
    double-seed-chance: 0.05
  master-farmer:
    crop-value-bonus: 0.4
    reputation-bonus: 1.5
    double-seed-chance: 0.1
  agricultural-expert:
    crop-value-bonus: 0.6
    reputation-bonus: 2.0
    double-seed-chance: 0.15

# Rebirth Settings
rebirth:
  currency-requirement: 100000
  trades-requirement: 500
  crop-value-multiplier: 0.1  # +10% per rebirth level
```

## Economy Balancing

### Inflation Control

The BD economy includes several inflation control mechanisms:

1. **Currency Sinks**:
   - Rank purchases
   - Auction house fees (5% of listing price)
   - Rebirth (resets currency to 0)

2. **Item Value Control**:
   - Fixed villager prices
   - Balanced crop yields
   - Limited special tool durability

### Economy Metrics

Monitor these key metrics to assess economic health:

1. **Currency Distribution**:
   - Average player balance
   - Top player balances
   - Currency distribution across ranks

2. **Trading Volume**:
   - Number of villager trades per day
   - Auction house listings and sales
   - Most traded items

3. **Progression Rate**:
   - Average time to reach each rank
   - Number of players at each rank
   - Rebirth distribution

## Economy Optimization

### For New Servers

If you're just starting a server:

1. **Lower Initial Costs**:
   ```yaml
   rank-costs:
     farmer: 3000
     expert-farmer: 10000
     master-farmer: 20000
     agricultural-expert: 40000
   ```

2. **Boost Early Rewards**:
   ```yaml
   trade-values:
     regular-crop:
       emeralds: 2
       currency: 75  # Increased from 50
   ```

3. **Increase Seed Availability**:
   - Spawn more dealer villages initially
   - Consider giving new players starter packs (5-10 regular seeds)

### For Established Servers

For mature economies that need rebalancing:

1. **Adjust for Inflation**:
   - Increase rank costs
   - Add additional currency sinks
   - Adjust rebirth requirements

2. **Add Progression Paths**:
   - Introduce more valuable seasonal items
   - Create special limited-time events
   - Add exclusive rebirth rewards

## Economic Events

Consider implementing these special events to stimulate the economy:

### Harvest Festival

- Double crop yields for a limited time
- Special seasonal trader with unique items
- Temporary auction house fee reduction

### Market Crash

- Sudden change in crop values (can go up or down)
- Limited-time emergency trades
- Recovery incentives

### BD Development Grant

- One-time currency grant to all active players
- Special BD item distribution
- Temporary rank discount



## Economy Data Reset

In extreme cases, you may need to reset the economy:

### Partial Reset

```
/bdadmin economy reset currency
```

Resets all player currency to 0 without affecting ranks or rebirth levels.

### Complete Reset

```
/bdadmin economy reset all
```

Resets all currency, ranks, and rebirth levels. Use with caution!

## Technical Details

### Economy Data Storage

Economy data is stored in:

```
plugins/BDCraft/data/economy.json
```

This file contains all player balances, ranks, and rebirth levels.

### Transaction Logging

All economy transactions are logged in:

```
plugins/BDCraft/logs/economy_YYYY-MM-DD.log
```

This log includes currency changes, rank purchases, and rebirth events.

## Troubleshooting

### Common Issues

1. **Rapid Inflation**:
   - Check for economy exploits
   - Increase rank costs and add currency sinks
   - Consider a partial economy reset

2. **Slow Progression**:
   - Reduce rank costs
   - Increase crop trade values
   - Add progression bonuses or events

3. **Economy Imbalance**:
   - Review player distribution across ranks
   - Adjust trade values for different crop types
   - Implement targeted events for underutilized features

### Player Complaints

Addressing common player complaints:

1. **"It's too grindy"**:
   - Reduce rank costs
   - Increase crop values
   - Add periodic bonus events

2. **"High-rank players have too much advantage"**:
   - Balance rebirth benefits
   - Add catch-up mechanics for new players
   - Implement progressive taxation (higher auction fees for higher ranks)

3. **"There's nothing to spend currency on"**:
   - Add cosmetic rewards
   - Implement special abilities that cost currency
   - Create limited-time purchases

## Best Practices

1. **Regular Monitoring**: Check economy metrics weekly
2. **Gradual Changes**: Make small adjustments over time rather than dramatic overhauls
3. **Player Feedback**: Regularly collect and incorporate player feedback
4. **Documentation**: Keep records of all economy changes and their effects
5. **Seasonal Planning**: Plan economic events to coincide with real-world seasons or holidays

A well-managed BD economy creates a balanced and engaging experience for players at all levels, from newcomers to agricultural deities.