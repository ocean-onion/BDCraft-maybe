# Currency System

The BDCraft economy operates on a sophisticated dual-currency framework that creates sustainable economic loops through physical item trading and digital progression currency.

## Currency Infrastructure

### Dual Currency Framework
BDCraft utilizes complementary currency types for different economic functions:

#### Physical Item Currency
- **Emeralds**: Primary trading medium for seed purchases and villager transactions
- **Diamonds**: Premium currency for specialized harvesting tools and high-tier equipment

#### Digital Progression Currency

- **Server Currency**: Earned alongside emeralds when trading with collectors
- Used for purchasing ranks and other progression advancements
- Tracked internally by the plugin (no physical item)
- Automatically added to your balance when trading with collectors
- Displayed through `/bdbalance` command
- Stored securely in plugin's database with transaction history

## Currency Acquisition

### Primary Earning Methods
Players accumulate currency through structured economic activities:

- **Trading with Collectors**: Selling BD crops to collectors provides both emeralds AND server currency
- **Special Events**: Participating in server events
- **Rank Bonuses**: Some ranks provide currency bonuses

## Currency Utilization

### Physical Currency Applications

#### Emerald Economy

Emeralds are used primarily for:
- Purchasing seeds from Dealers
- Trading with Market Villager

#### Diamond Economy

Diamonds are used for premium purchases:
- BD Harvester (16 diamonds)
- Ultimate BD Harvester (32 diamonds)

### Digital Currency Applications

#### Progression Economy

Server currency is used for progression:
- Purchasing ranks (5,000 - 60,000 per rank)
- Market upgrades and expansions
- Special permissions and abilities

## Currency Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdbalance` or `/bdbal` | Check your server currency balance | bdcraft.economy.use |
| `/bdbalance <player>` | Check another player's balance | bdcraft.economy.balance.others |
| `/bdpay <player> <amount>` | Pay another player from your balance | bdcraft.economy.pay |
| `/bdeco give <player> <amount>` | Give server currency to a player (admin) | bdcraft.economy.admin |
| `/bdeco take <player> <amount>` | Take server currency from a player (admin) | bdcraft.economy.admin |
| `/bdeco set <player> <amount>` | Set a player's server currency balance (admin) | bdcraft.economy.admin |
| `/bdeco top` | View top balances on the server | bdcraft.economy.top |

## Economic Flow

The BDCraft currency system is designed to create a complete economic loop:

1. Players purchase seeds from Dealers using emeralds
2. Players grow BD crops from these seeds
3. Players sell BD crops to Collectors for emeralds AND server currency
4. Players use emeralds to purchase more seeds
5. Players use server currency to purchase ranks
6. Higher ranks provide better crop values, creating more currency

## Currency Administration

Server administrators can manage the economy using the following features:

### Admin Commands

All economy administration is handled through the `/bdeco` command:

- `/bdeco give <player> <amount>` - Give server currency to a player
- `/bdeco take <player> <amount>` - Take server currency from a player
- `/bdeco set <player> <amount>` - Set a player's server currency balance
- `/bdeco reset <player>` - Reset a player's server currency to default
- `/bdeco reload` - Reload economy configuration

### Currency Configuration

The economy can be configured in `economy.yml`:

```yaml
# BDCraft Economy Configuration

currency:
  # Currency name
  name: "BD Coin"
  # Plural form
  plural: "BD Coins"
  # Currency symbol
  symbol: "฿"
  # Starting balance for new players
  starting-balance: 0
  
ranks:
  # Rank costs in server currency
  costs:
    FARMER: 5000
    EXPERT_FARMER: 15000
    MASTER_FARMER: 30000
    AGRICULTURAL_EXPERT: 60000
  
  # Crop value multipliers per rank
  multipliers:
    NEWCOMER: 1.0
    FARMER: 1.1
    EXPERT_FARMER: 1.25
    MASTER_FARMER: 1.4
    AGRICULTURAL_EXPERT: 1.6
    
# Collector trade settings
collectors:
  # Base emerald values for crops
  emerald-values:
    regular: 1
    green: 2
    purple: 5
    
  # Base server currency values
  currency-values:
    regular: 10
    green: 25
    purple: 100
    
  # Market level bonuses (percentage)
  market-level-bonus:
    1: 0
    2: 5
    3: 10
    4: 15
    5: 20
```

## Rebirth and Currency

The rebirth system interacts directly with the server currency:

- Rebirth requires reaching a specific currency threshold
- Upon rebirth, all server currency is reset to zero
- Each rebirth level provides a permanent multiplier to all BD crop values
- After 10 rebirths, players reach maximum currency generation efficiency

## Village Reputation System

The village reputation system affects currency generation:

- Higher reputation results in better prices from villagers
- Successful trades increase reputation
- Different villager types give different reputation gains:
  - Dealers: +2 reputation
  - Collectors: +3 reputation
  - Seasonal: +4 reputation
- Reputation is specific to each market and/or village

## Best Practices

For server administrators:

1. **Balance Currency Sources**: Monitor emerald and server currency generation rates
2. **Configure Rank Costs**: Adjust rank costs based on server economy size
3. **Monitor Prices**: Use the admin panel to monitor crop prices and adjust as needed
4. **Regular Economy Reports**: Run periodic reports to check economic health