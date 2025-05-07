# BDCraft Configuration Basics

This guide covers the essential configuration options for the BDCraft plugin. Understanding these settings will help you customize the plugin to suit your server's needs.

## Configuration Files Overview

BDCraft uses several YAML configuration files, each focusing on a specific aspect of the plugin:

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

## Essential Configuration

### Core Settings (`config.yml`)

```yaml
# BDCraft Core Configuration

# Plugin Settings
debug-mode: false
metrics-enabled: true

# Village Settings
village-radius: 50
max-villagers-per-village: 8
dealer-village-ratio: 3

# BD Crops
bd-crop-growth-multiplier: 1.0
green-crop-growth-multiplier: 0.7  # 30% faster growth
purple-crop-retention-days: 7

# Player Markets
player-markets:
  enabled: true
  min-distance: 30      # Minimum blocks between markets
  market-radius: 49      # Radius of player markets
  max-markets-per-player: 1
  max-collectors-initial: 3
  max-collectors-upgraded: 10

# Module Settings
modules:
  economy: true
  perms: true
  auction: true
  rebirth: true
  tutorial: true
```

### Economy Settings (`economy.yml`)

```yaml
# BDCraft Economy Configuration

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

## Configuration Tips

### Balancing the Economy

The economy settings are crucial for server balance:

1. **Crop Values**: Adjust the `trade-values` section to control how much players earn
2. **Rank Costs**: Modify the `rank-costs` section to make progression easier or harder
3. **Item Prices**: Change the `prices` section to adjust how much players spend

### Customizing Messages

All player-facing messages can be customized in `messages.yml`:

```yaml
# BDCraft Messages Configuration

plugin:
  prefix: "&6[&2BD&aCraft&6] &r"
  reload: "&aPlugin reloaded successfully."

village:
  created: "&aCreated a new BD village with ID: &e%id%"
  deleted: "&aDeleted BD village with ID: &e%id%"
  not-found: "&cVillage with ID &e%id% &cnot found."

villager:
  spawned: "&aSpawned a &e%type% &ain village &e%village%&a."
  removed: "&aRemoved villager with UUID &e%uuid%&a."

# Additional message sections...
```

### Permissions Configuration

The `permissions.yml` file defines permission groups and their access levels:

```yaml
# BDCraft Permissions Configuration

groups:
  player:
    default: true
    permissions:
      - bdcraft.use
      - bdcraft.village.locate
      - bdcraft.rank.view
      - bdcraft.rank.up
      - bdcraft.balance
      - bdcraft.market.create
      - bdcraft.market.check
      - bdcraft.market.info
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

## Player Market Configuration

Player markets are configured through the `player-markets` section:

```yaml
player-markets:
  enabled: true                  # Enable player market system
  min-distance: 30              # Minimum blocks between markets
  market-radius: 49             # Market radius in blocks
  max-markets-per-player: 1     # Maximum markets per player
  max-collectors-initial: 3     # Starting collector limit
  max-collectors-upgraded: 10   # Maximum collectors after all upgrades
  market-visualize-time: 10     # Seconds for boundary visualization
  market-upgrade-costs:         # Cost for each market level
    level2: {diamonds: 16, currency: 5000}
    level3: {diamonds: 32, currency: 10000}
    level4: {diamonds: 64, currency: 25000}
```

These settings control how player markets work on your server.

## Advanced Configuration

### Enabling/Disabling Modules

BDCraft uses a modular system. You can enable or disable entire features:

```yaml
modules:
  economy: true  # Core economy system
  perms: true    # Permissions system
  auction: true  # Auction house functionality
  rebirth: true  # Rebirth system
  tutorial: true # In-game tutorials
```

Disabling a module will completely remove its functionality from the server.

### Custom Drop Rates

You can configure the chance of special drops:

```yaml
drop-rates:
  green-seed-from-regular: 0.05  # 5% chance
  purple-seed-from-green: 0.01   # 1% chance
  diamond-from-regular: 0.02     # 2% chance for 50 regular crops
```

### Growth and Spawning Settings

Configure aspects of the BD farming system:

```yaml
bd-crop-growth-multiplier: 1.0     # Normal growth speed
green-crop-growth-multiplier: 0.7   # 30% faster growth
plant-particles-enabled: true       # Visual indicators
village-spawn-rates:
  dealer: 0.6      # 60% chance
  collector: 0.3   # 30% chance
  seasonal: 0.1    # 10% chance
```

## Common Configuration Questions

### How do I make progression easier?

To make progression easier for players:

1. Decrease rank costs in `economy.yml`
2. Increase trade values for crops
3. Decrease seed and tool prices

### How do I change what items represent BD crops?

Currently, BD crops use fixed item representations (ferns and large ferns). This cannot be changed in configuration files. Future versions may allow customization.

### How do I adjust the economy for a small server?

For smaller servers with fewer players:

1. Reduce the village-radius to create more compact BD economies
2. Lower rank costs to account for fewer trading opportunities
3. Increase crop values slightly to compensate for lower trading volume

### How do I configure player markets?

To optimize player markets for your server:

1. Adjust `market-radius` to control how much land each market covers
2. Modify `min-distance` to control market density
3. Increase `max-markets-per-player` for trusted players
4. Balance upgrade costs to match your server economy

## Applying Configuration Changes

After modifying configuration files:

1. Save the changes
2. Use `/bdadmin reload` to apply them without restarting
3. For major changes, a server restart is recommended

## Configuration Best Practices

1. **Make incremental changes**: Adjust settings gradually to avoid economic disruption
2. **Backup configurations**: Keep backups before making significant changes
3. **Test on a separate server**: Test major changes on a test server first
4. **Document your changes**: Keep notes on what you've modified
5. **Monitor effects**: Watch how changes affect player behavior and economy

For detailed configuration information, including all available options, see the [Configuration Reference](../technical/configuration-reference.md).