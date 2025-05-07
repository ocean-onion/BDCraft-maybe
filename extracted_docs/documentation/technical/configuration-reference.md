# Configuration Reference

This document provides a comprehensive reference for all configuration options in the BDCraft plugin. Each configuration file is detailed with explanations for every setting and example values.

## Configuration Files Overview

BDCraft uses several YAML configuration files, each with a specific purpose:

| File | Purpose |
|------|--------|
| `config.yml` | Core plugin settings |
| `economy.yml` | Economy settings |
| `messages.yml` | Customizable messages |
| `permissions.yml` | Permission configuration |
| `players.yml` | Player data storage |
| `metrics.yml` | Plugin metrics settings |
| `help.yml` | In-game help content |
| `tutorials.yml` | In-game tutorial content |

## Core Configuration (`config.yml`)

### Plugin Settings

```yaml
# BDCraft Core Configuration

# Plugin Settings
debug-mode: false                # Enables detailed logging for troubleshooting
metrics-enabled: true            # Send anonymous usage data
check-updates: true              # Check for plugin updates on startup
update-notify: true              # Notify admins about available updates
language: "en"                  # Plugin language (en, fr, de, es)
```

### Module Settings

```yaml
# Module Enablement
modules:
  economy: true                  # BD economy system
  perms: true                    # Permissions system
  auction: true                  # Auction house functionality
  rebirth: true                  # Rebirth system
  tutorial: true                 # In-game tutorials
  logging: true                  # Advanced activity logging
  storage-mysql: false           # Use MySQL instead of file storage
```

### Village Settings

```yaml
# Village Configuration
village:
  radius: 50                     # Effective radius of villages in blocks
  max-villagers-per-village: 8   # Maximum BD villagers per village
  dealer-village-ratio: 3        # One dealer per X villages (every third village)
  village-discovery-range: 100   # How close a player must be to discover a village
  protected-zone: true           # Create protected areas around villages
  natural-village-conversion:
    enabled: true                # Allow conversion of natural villages
    max-per-world: 5             # Maximum natural villages to convert
  display-particles: true        # Show particles at village centers
  reputation-decay:
    enabled: false               # Enable reputation decay over time
    rate: 1                      # Points lost per decay period
    period: 7                    # Days between decay events
```

### BD Seeds and Crops

```yaml
# BD Seeds and Crops
growing:
  regular-growth-ticks: 4800     # Base growth time for regular seeds (in ticks)
  green-growth-modifier: 0.7     # Growth time multiplier for green seeds (30% faster)
  purple-growth-rate: 1.0        # Growth time multiplier for purple seeds
  particles-enabled: true        # Show particles during growth
  growth-stage-notification: true # Notify players when crops advance growth stage
  bonemeal-allowed: false        # Allow bonemeal to speed growth
  special-biome-bonuses: true    # Enable biome-specific growth bonuses
  biome-modifiers:               # Growth speed modifiers by biome
    PLAINS: 1.0
    FOREST: 1.2
    JUNGLE: 0.8
    DESERT: 1.3
```

### Player Market Settings

```yaml
# Player Markets
player-markets:
  enabled: true                  # Enable player markets
  min-distance: 30               # Minimum blocks between markets
  market-radius: 49              # Market radius in blocks
  max-markets-per-player: 1      # Maximum markets per player
  max-collectors-initial: 3      # Initial collector limit
  max-collectors-upgraded: 10    # Maximum collectors after upgrades
  market-visualize-time: 10      # Seconds for boundary visualization
  market-upgrade-costs:          # Costs for market upgrades
    level2:
      diamonds: 16
      currency: 5000
    level3:
      diamonds: 32
      currency: 10000
    level4:
      diamonds: 64
      currency: 25000
  market-benefits:               # Benefits for each market level
    level2:
      price-bonus: 0.05          # 5% better prices
      collector-limit: 5         # Collector house limit
    level3:
      price-bonus: 0.10          # 10% better prices
      collector-limit: 7         # Collector house limit
      seasonal-trader: true      # Allows seasonal trader visits
    level4:
      price-bonus: 0.15          # 15% better prices
      collector-limit: 10        # Collector house limit
      seasonal-trader: true      # Allows seasonal trader visits
      player-buffs: true         # Provides buffs to players in market
```

### Integration Settings

```yaml
# Plugin Integrations
integrations:
  vault: true                    # Vault integration
  placeholderapi: true           # PlaceholderAPI integration
  worldguard: true               # WorldGuard integration
  worldedit: true                # WorldEdit integration
  coreprotect: true              # CoreProtect integration
  dynmap: true                   # Dynmap integration
  luckperms: true                # LuckPerms integration

# WorldGuard-specific settings
worldguard-integration:
  auto-protect-villages: true    # Create protected regions around villages
  protection-radius: 50          # Radius of protected regions
  default-flags:                 # Default flags for village regions
    bd-farming: true
    bd-villager-trading: true
    pvp: false

# Dynmap-specific settings
dynmap-integration:
  show-villages: true            # Show villages on Dynmap
  show-villagers: false          # Show villagers on Dynmap
  icon: "village.png"            # Icon for village markers
  label-format: "BD Village: %id%" # Format for village labels
```

### Database Settings

```yaml
# Database Configuration (if storage-mysql is true)
database:
  host: "localhost"              # Database host
  port: 3306                     # Database port
  database: "bdcraft"            # Database name
  username: "bdcraft"            # Database username
  password: "password"           # Database password
  prefix: "bd_"                  # Table prefix
  connection-pool: 3             # Connection pool size
  ssl: false                     # Use SSL for database connection
```

## Economy Configuration (`economy.yml`)

### Currency Settings

```yaml
# Currency Settings
currency-name: "Coins"           # Name of server currency
currency-name-plural: "Coins"    # Plural name of server currency
currency-symbol: "$"             # Currency symbol
decimal-format: "#,##0.00"       # Number format for currency display
starting-balance: 0              # Starting balance for new players
max-balance: 1000000000          # Maximum currency a player can have
```

### Item Prices

```yaml
# Item Prices (Emeralds)
prices:
  regular-seed:
    cost: 3                      # Emerald cost for regular seeds
    amount: 5                    # Seeds given per purchase
  green-seed:
    cost: 9                      # Emerald cost per green seed
    amount: 1                    # Seeds given per purchase
  purple-seed:
    cost: 25                     # Emerald cost per purple seed
    amount: 1                    # Seeds given per purchase
  bd-harvester: 16               # Diamond cost for BD Harvester
  ultimate-harvester: 32         # Diamond cost for Ultimate BD Harvester
```

### Trade Values

```yaml
# Trade Values
trade-values:
  regular-crop:
    emeralds: 2                  # Emeralds for selling 10 regular crops
    currency: 50                 # Currency for selling 10 regular crops
    amount: 10                   # Crops needed for trade
  green-crop:
    emeralds: 10                 # Emeralds for selling 5 green crops
    currency: 150                # Currency for selling 5 green crops
    amount: 5                    # Crops needed for trade
  purple-crop:
    emeralds: 20                 # Emeralds for selling 3 purple crops
    currency: 400                # Currency for selling 3 purple crops
    amount: 3                    # Crops needed for trade
  bulk-trades:
    enabled: true                # Enable bulk trades
    regular-diamond:
      crops: 50                  # Regular crops needed for diamond trade
      diamonds: 1                # Diamonds given for trade
```

### Rank System

```yaml
# Rank Settings
rank-enabled: true               # Enable rank system
rank-names:                      # Names for each rank
  1: "Newcomer"
  2: "Farmer"
  3: "Expert Farmer"
  4: "Master Farmer"
  5: "Agricultural Expert"

# Rank Costs
rank-costs:
  farmer: 5000                   # Cost to upgrade to Farmer
  expert-farmer: 15000           # Cost to upgrade to Expert Farmer
  master-farmer: 30000           # Cost to upgrade to Master Farmer
  agricultural-expert: 60000     # Cost to upgrade to Agricultural Expert

# Rank Benefits
rank-benefits:
  newcomer:
    crop-value-bonus: 0.0        # 0% extra crop value
    reputation-bonus: 0.0        # 0 extra reputation gain
  farmer:
    crop-value-bonus: 0.1        # 10% extra crop value
    reputation-bonus: 0.5        # +0.5 reputation gain
  expert-farmer:
    crop-value-bonus: 0.25       # 25% extra crop value
    reputation-bonus: 1.0        # +1.0 reputation gain
    double-seed-chance: 0.05     # 5% chance for double seeds
  master-farmer:
    crop-value-bonus: 0.4        # 40% extra crop value
    reputation-bonus: 1.5        # +1.5 reputation gain
    double-seed-chance: 0.1      # 10% chance for double seeds
  agricultural-expert:
    crop-value-bonus: 0.6        # 60% extra crop value
    reputation-bonus: 2.0        # +2.0 reputation gain
    double-seed-chance: 0.15     # 15% chance for double seeds
```

### Rebirth System

```yaml
# Rebirth Settings
rebirth:
  enabled: true                  # Enable rebirth system
  currency-requirement: 100000   # Currency needed for rebirth
  trades-requirement: 500        # Trades needed for rebirth
  crop-value-multiplier: 0.1     # 10% crop value increase per rebirth level
  max-rebirth-level: 10          # Maximum rebirth level
  deity-abilities:               # Special abilities for max rebirth level
    golden-touch: true           # Enable Golden Touch ability
    harvester-blessing: true     # Enable Harvester's Blessing ability
    divine-favor: true           # Enable Divine Favor ability
    seasonal-insight: true       # Enable Seasonal Insight ability
    abundance-aura: true         # Enable Abundance Aura ability
```

### Auction House

```yaml
# Auction House Settings
auction-house:
  enabled: true                  # Enable auction house
  listing-fee-percent: 5         # Listing fee percentage
  listing-duration-days: 3       # Days before listings expire
  max-listings-per-player: 10    # Maximum active listings per player
  min-price: 1                   # Minimum listing price
  max-price: 10000               # Maximum listing price
  allowed-items:                 # Types of items allowed in auction house
    seeds: true                  # BD seeds
    tools: true                  # BD tools
    special: true                # Special/seasonal items
  rank-benefits:                 # Rank-specific benefits
    enabled: true                # Enable rank benefits for auction house
    farmer:
      fee-reduction: 0.5         # 0.5% fee reduction
      max-listings: 15           # Max listings for Farmer rank
    expert-farmer:
      fee-reduction: 1.0         # 1.0% fee reduction
      max-listings: 20           # Max listings for Expert Farmer rank
    master-farmer:
      fee-reduction: 1.5         # 1.5% fee reduction
      max-listings: 25           # Max listings for Master Farmer rank
    agricultural-expert:
      fee-reduction: 2.0         # 2.0% fee reduction
      max-listings: 30           # Max listings for Agricultural Expert rank
      featured-listings: true    # Enable featured listings (appear at top)
```

### Seasonal Events

```yaml
# Seasonal Events
seasonal-events:
  enabled: true                  # Enable seasonal events
  seasons:                       # Season definitions
    spring:                      # Spring season
      days: 0-5000               # Minecraft days for spring
      special-items:             # Special items available
        - "fast_growing_seeds"
        - "spring_booster"
      trade-modifiers:           # Trade value modifiers
        green-seed-discount: 0.1 # 10% discount on green seeds
    summer:                      # Summer season
      days: 5001-10000           # Minecraft days for summer
      special-items:             # Special items available
        - "heat_resistant_seeds"
        - "special_harvester"
      trade-modifiers:           # Trade value modifiers
        regular-crop-bonus: 0.15 # 15% bonus for regular crops
    fall:                        # Fall season
      days: 10001-15000          # Minecraft days for fall
      special-items:             # Special items available
        - "purple_seed_special"
        - "fall_fertilizer"
      trade-modifiers:           # Trade value modifiers
        purple-seed-discount: 0.2 # 20% discount on purple seeds
    winter:                      # Winter season
      days: 15001-20000          # Minecraft days for winter
      special-items:             # Special items available
        - "frost_seeds"
        - "winter_bundle"
      trade-modifiers:           # Trade value modifiers
        all-seed-discount: 0.15  # 15% discount on all seeds
```

## Permissions Configuration (`permissions.yml`)

### Permission Groups

```yaml
# BDCraft Permissions Configuration

# Mode: 'simple' uses built-in permissions, 'external' relies on other permission plugins
mode: "simple"

# Groups
groups:
  player:
    default: true                # Default group for new players
    permissions:
      - bdcraft.use
      - bdcraft.village.locate
      - bdcraft.rank.view
      - bdcraft.rank.up
      - bdcraft.balance
      - bdcraft.auction.use
  vip:
    permissions:
      - bdcraft.use
      - bdcraft.village.locate
      - bdcraft.rank.view
      - bdcraft.rank.up
      - bdcraft.balance
      - bdcraft.auction.use
      - bdcraft.auction.sell
      - bdcraft.vip.discounts
      - bdcraft.vip.extra.harvester
  admin:
    permissions:
      - bdcraft.admin
      - bdcraft.admin.village
      - bdcraft.admin.villager
      - bdcraft.admin.economy
      - bdcraft.admin.items
      - bdcraft.admin.rank
      - bdcraft.admin.rebirth
      - bdcraft.admin.reload
      - bdcraft.admin.debug
      - bdcraft.admin.market
```

### External Integration

```yaml
# Integration with external permission systems
vault-integration:
  enabled: true                  # Enable Vault integration
  register-as-provider: false    # Register as a Vault permission provider

luckperms-integration:
  enabled: true                  # Enable LuckPerms integration
  sync-ranks: true               # Sync BD ranks with LuckPerms groups
  rank-groups:                   # Mapping of BD ranks to LuckPerms groups
    1: "bd_newcomer"
    2: "bd_farmer"
    3: "bd_expert_farmer"
    4: "bd_master_farmer"
    5: "bd_agricultural_expert"
```

### Market Permissions

```yaml
# Market system permissions
market-permissions:
  base:                          # Base market permissions
    - "bdcraft.market.create"    # Ability to create markets
    - "bdcraft.market.check"     # Check market boundaries
    - "bdcraft.market.info"      # View market info
  founder:                       # Market founder permissions
    - "bdcraft.market.manage"    # Manage own market
    - "bdcraft.market.associate" # Add/remove associates
    - "bdcraft.market.upgrade"   # Upgrade market
  associate:                     # Market associate permissions
    - "bdcraft.market.build"     # Build in market area
    - "bdcraft.market.harvest"   # Harvest BD crops in market
    - "bdcraft.market.collector" # Add collector houses
  admin:                         # Admin market permissions
    - "bdcraft.admin.market"     # Full control over all markets
    - "bdcraft.admin.override"   # Override market restrictions
```

## Messages Configuration (`messages.yml`)

The `messages.yml` file contains all plugin messages. Here's a sample section:

```yaml
# BDCraft Messages Configuration

plugin:
  prefix: "&6[&2BD&aCraft&6] &r"
  reload: "&aPlugin reloaded successfully."
  no-permission: "&cYou don't have permission to use that command."
  player-only: "&cThis command can only be used by players."

village:
  created: "&aCreated a new BD village with ID: &e%id%"
  deleted: "&aDeleted BD village with ID: &e%id%"
  not-found: "&cVillage with ID &e%id% &cnot found."
  center-set: "&aSet the center of village &e%id% &ato your current location."
  list-header: "&6===== &2BD &aVillages &6====="
  list-entry: "&e%id% &7- &aCenter: %world% %x%,%y%,%z% &7- &aVillagers: %count%"
  list-empty: "&cNo BD villages found."

villager:
  spawned: "&aSpawned a &e%type% &ain village &e%village%&a."
  removed: "&aRemoved villager with UUID &e%uuid%&a."
  cannot-spawn-dealer: "&cCannot spawn a Dealer in this village. Only every third village can have a Dealer."
  list-header: "&6===== &2%village% &aVillagers &6====="
  list-entry: "&e%type% &7- &aUUID: %uuid% &7- &aLocation: %world% %x%,%y%,%z%"
  list-empty: "&cNo villagers found in village &e%village%&c."

# Additional message sections for economy, ranks, etc.
```

## Additional Configuration Files

### Help Configuration (`help.yml`)

```yaml
# BDCraft Help Configuration

# General help entries
general:
  - topic: "general"
    title: "BDCraft General Help"
    content:
      - "&2BDCraft &ais a farming and economy plugin"
      - "&aUse &e/bdhelp <topic> &ato get help on a specific topic"
      - "&aAvailable topics: crops, villagers, ranks, rebirth"

# Crops help
crops:
  - topic: "crops"
    title: "BD Crops Guide"
    content:
      - "&2BD Seeds &agrow into special crops that can be sold to Collectors"
      - "&aThere are three types of BD seeds:"
      - "&e- Regular BD Seeds &7(Wheat Seeds)"
      - "&e- Green BD Seeds &7(Beetroot Seeds)"
      - "&e- Purple BD Seeds &7(Pumpkin Seeds)"
      - "&aPlant them like normal seeds and harvest when fully grown"

# More help entries for other topics
```

### Tutorials Configuration (`tutorials.yml`)

```yaml
# BDCraft Tutorials Configuration

# Enable the tutorial system
enabled: true

# Tutorials
tutorials:
  - id: "getting_started"
    title: "Getting Started with BD Farming"
    steps:
      - title: "Welcome to BDCraft"
        message: "This tutorial will teach you the basics of BD farming."
        action: "NONE"
      - title: "Buy Some Seeds"
        message: "Find a BD Dealer and right-click them to buy Regular BD Seeds."
        action: "FIND_DEALER"
      - title: "Plant Your Seeds"
        message: "Plant your BD Seeds in farmland just like regular seeds."
        action: "PLANT_SEEDS"
      - title: "Wait for Growth"
        message: "Wait for your crops to fully grow. This takes about 4 minutes."
        action: "WAIT_GROWTH"
      - title: "Harvest Your Crops"
        message: "Harvest your fully grown BD crops by breaking them."
        action: "HARVEST_CROPS"
      - title: "Sell to a Collector"
        message: "Find a BD Collector and sell your crops for emeralds and currency."
        action: "FIND_COLLECTOR"
      - title: "Completion"
        message: "Congratulations! You've completed the basic BD farming cycle."
        action: "NONE"
        rewards:
          - command: "bdadmin give %player% bdseed 10"
            message: "Here are 10 more BD Seeds to get you started!"

  # Additional tutorials
```

## Using Configuration Variables

Many configuration options support variables:

### Common Variables

- `%player%` - Player name
- `%balance%` - Player's currency balance
- `%rank%` - Player's rank name
- `%rank_id%` - Player's numeric rank
- `%rebirth%` - Player's rebirth level
- `%village%` - Village ID
- `%world%` - World name
- `%x%`, `%y%`, `%z%` - Coordinates

### Variable Usage Example

```yaml
economy:
  rank-up: "&aCongratulations %player%! You've reached rank %rank%."
  balance: "&aYour current balance is %balance% %currency%."
  transaction: "&a%amount% %currency% has been %action% your account."
```

## Color and Formatting Codes

BDCraft supports both legacy color codes and MiniMessage formatting:

### Legacy Color Codes

- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White

### Legacy Formatting Codes

- `&l` - Bold
- `&m` - Strikethrough
- `&n` - Underline
- `&o` - Italic
- `&r` - Reset

### MiniMessage Format

If MiniMessage is enabled, you can use advanced formatting:

```yaml
message-format:
  use-minimessage: true

messages:
  rank-up: "<gradient:gold:green>Congratulations! You reached BD rank: %rank%</gradient>"
  rebirth: "<rainbow>You have achieved rebirth level %rebirth%!</rainbow>"
```

## Configuration Best Practices

1. **Make Backups**: Always back up configuration files before making changes
2. **Incremental Changes**: Make small changes and test before making more
3. **Use Reload Command**: Apply changes with `/bdadmin reload` when possible
4. **Document Changes**: Keep notes of changes you make to configuration
5. **Check Syntax**: Ensure YAML syntax is correct (proper indentation, no tabs)

This reference provides a comprehensive guide to all configuration options in the BDCraft plugin. Refer to specific sections when configuring particular aspects of the plugin to customize it for your server's needs.