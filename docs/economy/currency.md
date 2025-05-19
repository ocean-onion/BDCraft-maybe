# BDCraft Currency System

BDCraft implements a comprehensive server currency system that forms the foundation of the server's economy. This document explains how the currency works, its denominations, and how it integrates with other aspects of the server.

## Currency Overview

BDCraft uses a primary currency called "BDCoins" that powers all economic transactions on the server. The currency is:

- **Self-contained**: Managed entirely within BDCraft without external economy plugins
- **Secure**: All transactions are logged and protected against exploits
- **Scalable**: Designed to handle economies of any size
- **Integrated**: Deeply connected with all BDCraft modules

## Currency Denominations

BDCoins come in several denominations to facilitate different transaction sizes:

| Denomination | Value | Physical Item | Usage |
|--------------|-------|--------------|-------|
| Copper BDCoin | 1 | Copper Ingot (with custom texture) | Small purchases |
| Silver BDCoin | 10 | Iron Ingot (with custom texture) | Medium purchases |
| Gold BDCoin | 100 | Gold Ingot (with custom texture) | Large purchases |
| Diamond BDCoin | 1,000 | Diamond (with custom texture) | Major purchases |
| Netherite BDCoin | 10,000 | Netherite Ingot (with custom texture) | Premium purchases |

## Physical Currency

BDCoins exist as physical items in the game with custom textures. Players can:

- Hold physical coins in their inventory
- Store coins in containers
- Transfer coins to other players directly
- Exchange lower denominations for higher ones (and vice versa)

### Currency Conversion

Players can combine and break down currency at any Bank Villager:

- 10 Copper → 1 Silver
- 10 Silver → 1 Gold
- 10 Gold → 1 Diamond
- 10 Diamond → 1 Netherite

## Digital Currency

In addition to physical coins, players have digital BDCoin balances tracked by the server. Digital currency:

- Is accessed through commands like `/bdbalance`
- Can be used for market transactions
- Allows for precise payment amounts
- Is secured against loss (unlike physical coins)

### Converting Between Physical and Digital

Players can convert between physical and digital currency at Bank Villagers:
- Deposit: Convert physical coins to digital balance
- Withdraw: Convert digital balance to physical coins

## Currency Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdbalance` | `/balance`, `/bal`, `/money` | Check your digital balance | bdcraft.economy.use |
| `/bdbalance <player>` | `/balance <player>` | Check another player's balance | bdcraft.economy.balance.others |
| `/bdpay <player> <amount>` | `/pay <player> <amount>` | Pay another player from your digital balance | bdcraft.economy.pay |
| `/bddeposit <amount>` | `/deposit <amount>` | Convert physical coins to digital balance | bdcraft.economy.use |
| `/bdwithdraw <amount>` | `/withdraw <amount>` | Convert digital balance to physical coins | bdcraft.economy.use |
| `/bdconvert` | `/convert` | Opens the currency conversion GUI | bdcraft.economy.use |
| `/bdcurrency value <item>` | `/currency value <item>` | Check the value of a currency item | bdcraft.economy.use |

## Earning Currency

Players can earn BDCoins through various activities:

- **Farming**: Selling BD crops to Collectors
- **Markets**: Selling items at player-owned markets
- **Quests**: Completing server quests and challenges
- **Selling**: Trading with other players
- **Mining**: Finding rare minerals and resources
- **Events**: Participating in server events
- **Achievements**: Completing certain achievements

## Banking System

The BDCraft banking system provides secure storage and management of currency:

### Bank Villagers

Located in spawn and player markets, Bank Villagers offer:
- Currency conversion (physical ↔ digital)
- Denomination exchange
- Basic banking services
- Interest on deposits (configurable)

### Bank Accounts

Players can open bank accounts for additional features:
- Interest accrual on savings
- Joint accounts with other players
- Transaction history
- Scheduled payments
- Loan services (for qualifying accounts)

## Market Integration

BDCoins are the primary currency used in BDCraft's market system:

- **Market Stalls**: Players can buy and sell items using BDCoins
- **Auctions**: Items are bid on and purchased with BDCoins
- **Market Fees**: Market usage fees are collected in BDCoins
- **Market Ownership**: Purchasing and upgrading markets requires BDCoins

## Economy Management

Server administrators can manage the economy using:

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdeco give <player> <amount>` | Give BDCoins to a player | bdcraft.economy.admin |
| `/bdeco take <player> <amount>` | Take BDCoins from a player | bdcraft.economy.admin |
| `/bdeco set <player> <amount>` | Set a player's balance | bdcraft.economy.admin |
| `/bdeco reset <player>` | Reset a player's balance to default | bdcraft.economy.admin |
| `/bdeco top` | View top balances on the server | bdcraft.economy.admin |
| `/bdeco history <player>` | View transaction history | bdcraft.economy.admin |
| `/bdeco reload` | Reload economy configuration | bdcraft.economy.admin |

### Economy Configuration

The economy can be configured in `economy.yml`:

```yaml
# BDCraft Economy Configuration

currency:
  # Currency name
  name: "BDCoin"
  # Plural form
  plural: "BDCoins"
  # Currency symbol
  symbol: "฿"
  # Starting balance for new players
  starting-balance: 100
  # Maximum amount that can be held in a single stack of each denomination
  max-stack:
    copper: 64
    silver: 64
    gold: 32
    diamond: 16
    netherite: 8
  # Physical currency enabled
  physical-enabled: true
  # Custom model data IDs for currency items
  custom-model-data:
    copper: 1001
    silver: 1002
    gold: 1003
    diamond: 1004
    netherite: 1005

banking:
  # Whether to enable the banking system
  enabled: true
  # Interest rate (daily)
  interest-rate: 0.001  # 0.1% daily
  # Maximum bank account balance
  max-balance: 1000000
  # Minimum balance for interest
  min-balance-for-interest: 1000
  # Loan settings
  loans:
    enabled: true
    interest-rate: 0.01  # 1% daily
    max-loan: 10000
    payment-period: 7  # days

economy-balance:
  # Money sinks to keep the economy balanced
  sinks:
    market-fees: true
    teleport-costs: true
    repair-costs: true
  
  # Money fountains to inject currency
  fountains:
    login-rewards: true
    achievement-rewards: true
    voting-rewards: true
```

## Economy Protection Features

BDCraft includes several features to protect the server's economy:

- **Inflation Control**: Automatic adjustment of prices and fees
- **Money Sinks**: Strategic removal of currency from circulation
- **Transaction Limits**: Caps on certain transaction types
- **Audit System**: Comprehensive logging of all transactions
- **Anti-Duplication**: Protection against currency duplication exploits

## Currency vs. Token System

BDCoins differ from BD Collection Tokens in several important ways:

| Feature | BDCoins | Collection Tokens |
|---------|---------|------------------|
| Primary Purpose | General economy currency | Collectible rewards |
| Physical Form | Yes (denominations) | Yes (unique tokens) |
| Digital Form | Yes (account balances) | No (always physical) |
| Earning Methods | All activities | Specific achievements |
| Usage | All transactions | Special trades only |
| Storage | Bank accounts and inventory | Inventory only |

## Best Practices

For server administrators:

1. **Balance Generation/Removal**: Monitor currency creation and removal rates
2. **Regular Audits**: Check transaction logs for unusual activity
3. **Economy Health Checks**: Use `/bdeco stats` to view economic health
4. **Reset Prevention**: Configure automatic backups of economy data
5. **Pricing Guidelines**: Provide suggested price ranges to players