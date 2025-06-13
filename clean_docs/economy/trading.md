# Trading System

The BDCraft trading system facilitates all economic interactions between players and BD villagers, forming the core of the server's economic activity.

## Trading Mechanics

### Dual Currency Trading
All BD villager trades provide both physical and digital currency:
- **Emeralds**: Physical items added directly to player inventory
- **Server Currency**: Digital currency automatically added to player balance
- **Simultaneous Payment**: Both currencies awarded in single transaction
- **Conversion Rates**: Fixed ratios based on crop type and market conditions

### Trading Locations
Trading can only occur in designated areas:
- **Player Markets**: 98x98 block areas (128x128 for donors) around Market Tokens
- **Natural Villages**: Original Minecraft villages with BD Dealer spawns
- **Market Buildings**: Physical structures within market boundaries

## Villager Trading Types

### BD Dealer Trading
**Purchase Transactions** (Player buys from Dealer):
- **Regular BD Seeds**: 5 seeds for 1 emerald (all ranks)
- **Green BD Seeds**: Premium seeds for 15 emeralds (Farmer+ only)
- **Purple BD Seeds**: Rare seeds for 30 emeralds (Master Farmer+ only)
- **BD Harvester**: Special tool for 16 diamonds (Expert Farmer+ only)
- **Ultimate BD Harvester**: Top-tier tool for 32 diamonds (Agricultural Expert only)

**Reputation Gain**: +2 reputation per completed trade

### Collector Trading
**Sale Transactions** (Player sells to Collector):
- **Regular BD Crops**: 1 emerald + 10 server currency base value (configurable)
- **Green BD Crops**: 2 emeralds + 25 server currency base value (configurable)
- **Purple BD Crops**: 5 emeralds + 100 server currency base value (configurable)
- **Bulk Trading**: Can sell entire inventory stacks at once
- **Note**: Actual values affected by market level, rank, rebirth level, and reputation

**Reputation Gain**: +3 reputation per completed trade

### Market Owner Trading
**Market Management** (Player interacts with Market Owner):
- **Market Upgrades**: Purchase level improvements using emeralds + server currency
- **House Tokens**: Buy BD House Tokens at market level 2+
- **Associate Management**: Add/remove market building associates
- **Market Information**: View market statistics and performance data

**Reputation Gain**: +1 reputation per management interaction

### Seasonal Trader Trading
**Limited-Time Transactions** (Player trades with Seasonal Trader):
- **Seasonal Items**: Exclusive themed decorations and special items
- **Event Goods**: Limited-quantity offerings during special events
- **Premium Exchanges**: High-value trades for rare materials
- **Seasonal Variants**: Special crop types available only during trader visits

**Reputation Gain**: +4 reputation per completed trade (highest rate)

## Trading Bonuses and Modifiers

### Rank-Based Multipliers
Higher ranks provide better trading returns:
- **Newcomer**: 1.0x base trading rates
- **Farmer**: 1.05x multiplier (+5% better trades)
- **Expert Farmer**: 1.10x multiplier (+10% better trades)
- **Master Farmer**: 1.20x multiplier (+20% better trades)
- **Agricultural Expert**: 1.30x multiplier (+30% better trades)

### Market Level Bonuses
Market upgrades enhance all trading within the market:
- **Level 1**: No bonus (base trading rates)
- **Level 2**: +5% better trading prices
- **Level 3**: +10% better trading prices
- **Level 4**: +15% better trading prices
- **Level 5**: +20% better trading prices

### Rebirth Bonuses
Permanent bonuses that stack with other modifiers:
- **Rebirth 1**: +3% better trading prices
- **Rebirth 2**: +6% better trading prices
- **Rebirth 3**: +9% better trading prices
- **Rebirth 4**: +12% better trading prices
- **Rebirth 5**: +15% better trading prices

### Donor Benefits
Special advantages for server supporters:
- **Enhanced Multiplier**: +10% boost to all rank bonuses
- **Market Access**: Remote trading via `/bdmarket` command
- **Priority Status**: Better trading queue position during high traffic
- **Exclusive Access**: Trade with all BD tool dealers regardless of rank

## Village Reputation System

### Reputation Mechanics
Each market tracks individual player reputation affecting trade quality:
- **Market-Specific**: Reputation tied to individual markets, not global
- **Gradual Improvement**: Better prices unlock as reputation increases
- **Persistent Tracking**: Reputation maintained across server sessions
- **No Decay**: Reputation never decreases over time

### Reputation Sources
Different villager types provide varying reputation gains:
- **BD Dealers**: +2 reputation per trade
- **Collectors**: +3 reputation per trade
- **Market Owners**: +1 reputation per interaction
- **Seasonal Traders**: +4 reputation per trade

### Reputation Benefits
Higher reputation unlocks progressively better trading terms:
- **Tier 1** (0-99 reputation): Standard trading rates
- **Tier 2** (100-299 reputation): +2% better prices
- **Tier 3** (300-599 reputation): +5% better prices
- **Tier 4** (600-999 reputation): +8% better prices
- **Tier 5** (1000+ reputation): +12% better prices

## Trading Calculations

### Final Price Formula
All trading prices calculated using comprehensive modifier system:
```
Final Price = Base Price × (1 + Rank_Bonus + Market_Level_Bonus + Rebirth_Bonus + Reputation_Bonus + Donor_Bonus)
```

### Example Calculation
Master Farmer (Rank 4) with Rebirth 2, trading in Level 3 market with 500 reputation:
- Base Green BD Crop Value: 2 emeralds + 25 server currency
- Rank Bonus: +20%
- Market Level Bonus: +10%
- Rebirth Bonus: +6%
- Reputation Bonus: +5%
- **Total Multiplier**: 1.41x
- **Final Value**: 2.82 emeralds + 35.25 server currency

## Trading Restrictions

### Item Restrictions
Only specific items can be traded with BD villagers:
- **Accepted by Dealers**: Currency (emeralds, diamonds) for seeds/tools
- **Accepted by Collectors**: BD crops only (ferns, double ferns, enchanted ferns with BD metadata)
- **Rejected Items**: Regular Minecraft items, non-BD crops, items without BD metadata

### Location Restrictions
Trading limited to authorized areas:
- **Market Boundaries**: Must be within market radius for collector trading
- **Village Areas**: BD Dealers in natural villages accessible anywhere in village
- **Building Requirements**: Some villagers confined to specific structures

### Permission Requirements
Trading requires appropriate permissions:
- **Basic Trading**: `bdcraft.villager.use` and `bdcraft.economy.use`
- **Dealer Access**: `bdcraft.villager.dealer` permission
- **Collector Access**: `bdcraft.villager.collector` permission
- **Seasonal Trading**: `bdcraft.villager.seasonal` permission

## Advanced Trading Features

### Bulk Trading
Efficient trading for large quantities:
- **Stack Trading**: Sell entire inventory stacks at once
- **Multi-Slot**: Trade multiple item types in single transaction
- **Quick Exchange**: Streamlined interface for rapid trading
- **Quantity Validation**: Automatic calculation of total values

### Trading History
Comprehensive tracking of all trading activity:
- **Transaction Logs**: Complete record of all trades
- **Reputation Tracking**: Detailed reputation gain history
- **Performance Analytics**: Trading efficiency and profit analysis
- **Export Options**: Data export for external analysis

### Trading Notifications
Real-time information about trading opportunities:
- **Price Alerts**: Notifications when favorable trading conditions occur
- **Seasonal Announcements**: Alerts when seasonal traders arrive
- **Market Updates**: Information about market level changes affecting prices
- **Reputation Milestones**: Notifications when reputation tiers are reached

## Trading Strategies

### Beginner Strategies
- **Start with Regular Crops**: Build initial capital with consistent trades
- **Focus on Reputation**: Prioritize reputation building in chosen market
- **Reinvest Profits**: Use emerald profits to purchase more seeds
- **Save Server Currency**: Accumulate for rank purchases

### Intermediate Strategies
- **Diversify Crop Types**: Balance regular and green crops for optimal returns
- **Market Shopping**: Compare prices across different markets
- **Tool Investment**: Purchase BD Harvester for doubled yields
- **Timing Management**: Coordinate harvesting with seasonal trader visits

### Advanced Strategies
- **Multi-Market Operations**: Develop reputation across multiple markets
- **Seasonal Optimization**: Time major trades with seasonal trader bonuses
- **Rebirth Planning**: Strategic rebirth timing for maximum benefit retention
- **Community Coordination**: Organize group trading for market development

---

The trading system creates a dynamic economy where player choices, progression, and community involvement directly impact economic success and server-wide prosperity.