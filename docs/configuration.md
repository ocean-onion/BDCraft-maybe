# Configuration Guide

BDCraft uses a multi-file configuration system that allows you to customize every aspect of the plugin. All configuration files are located in the `plugins/BDCraft` directory.

## Configuration Files

### Main Configuration: `config.yml`

The main configuration file controls global plugin settings and module activation.

```yaml
# BDCraft Main Configuration

# Plugin Settings
plugin:
  # Prefix for plugin messages
  prefix: "&6[BDCraft] &r"
  # Whether to block competing plugins (recommended: true)
  block-competing-plugins: true
  # Debug mode (enables additional logging)
  debug: false
  
# Module Activation
modules:
  # Economy module settings
  economy:
    # Enable the economy module
    enabled: true
    # Enable specific submodules
    market: true
    auction: true
    villager: true
    
  # Progression module settings
  progression:
    # Enable the progression module
    enabled: true
    # Enable specific submodules
    rank: true
    rebirth: true
    
  # Vital module settings
  vital:
    # Enable the vital module
    enabled: true
    # Enable specific submodules
    home: true
    teleport: true
    chat: true
    tab: true

# Performance settings
performance:
  # How often to save data (in seconds)
  save-interval: 300
  # Maximum markets per player
  max-markets-per-player: 3
  # Maximum homes per player (default, can be overridden by permissions)
  default-max-homes: 3
```

### Economy Configuration: `economy.yml`

Controls all economy-related settings.

```yaml
# BDCraft Economy Configuration

# Currency Settings
currency:
  # Primary currency name
  name: "BDCoin"
  # Plural form of the currency name
  plural: "BDCoins"
  # Currency symbol
  symbol: "&6BD&r"
  # Format for displaying currency (use {amount} and {symbol})
  format: "{symbol}{amount}"
  # Starting balance for new players
  starting-balance: 500
  
# Market Settings
market:
  # Minimum price for items
  minimum-price: 1
  # Maximum price for items
  maximum-price: 1000000
  # Commission rate (percentage)
  commission-rate: 5
  # Time limit for inactive markets (in days, 0 = no limit)
  inactive-timeout: 7
  
# Auction Settings
auction:
  # Minimum auction duration (in minutes)
  min-duration: 60
  # Maximum auction duration (in minutes)
  max-duration: 4320  # 3 days
  # Minimum bid increment (percentage)
  min-bid-increment: 5
  # Featured auction cost
  featured-cost: 1000
  # Maximum active auctions per player
  max-auctions-per-player: 5
  
# Villager Settings
villager:
  # Enable custom villager professions
  custom-professions: true
  # Enable dynamic pricing based on server economy
  dynamic-pricing: true
  # Restock interval (in minutes)
  restock-interval: 60
  # Special event villager frequency (in days, 0 = disabled)
  special-event-frequency: 7
```

### Progression Configuration: `progression.yml`

Controls player progression systems.

```yaml
# BDCraft Progression Configuration

# Rank Settings
rank:
  # Experience required for each rank
  experience-requirements:
    NOVICE: 1000
    APPRENTICE: 5000
    EXPERT: 15000
    MASTER: 30000
    GRANDMASTER: 60000
    LEGEND: 100000
  
  # Perks for each rank (multipliers)
  perks:
    NOVICE:
      economy-multiplier: 1.1
      experience-multiplier: 1.0
    APPRENTICE:
      economy-multiplier: 1.2
      experience-multiplier: 1.1
    EXPERT:
      economy-multiplier: 1.3
      experience-multiplier: 1.2
    MASTER:
      economy-multiplier: 1.4
      experience-multiplier: 1.3
    GRANDMASTER:
      economy-multiplier: 1.5
      experience-multiplier: 1.4
    LEGEND:
      economy-multiplier: 2.0
      experience-multiplier: 1.5
      
  # Rank display format in chat (use {rank}, {name}, {message})
  chat-format: "&7[{rank}] {name}&f: {message}"
  
# Rebirth Settings
rebirth:
  # Whether to enable the rebirth system
  enabled: true
  # Required rank for rebirth
  required-rank: "LEGEND"
  # Maximum rebirth level
  max-level: 10
  # Experience multiplier per rebirth level
  experience-multiplier: 0.1  # 10% boost per level
  # Currency multiplier per rebirth level
  currency-multiplier: 0.05  # 5% boost per level
  # Cost to rebirth (currency)
  cost: 50000
  # Legacy preservation settings
  preserve:
    # Whether to preserve homes on rebirth
    homes: true
    # Whether to preserve a percentage of currency on rebirth
    currency-percentage: 10
```

### Vital Configuration: `vital.yml`

Controls essential utility features.

```yaml
# BDCraft Vital Configuration

# Home Settings
home:
  # Cooldown between teleports (in seconds)
  teleport-cooldown: 30
  # Warmup time before teleport (in seconds)
  teleport-warmup: 3
  # Whether to cancel teleport on movement
  cancel-on-movement: true
  # Maximum homes per rank (overrides default)
  max-homes:
    NOVICE: 3
    APPRENTICE: 5
    EXPERT: 7
    MASTER: 10
    GRANDMASTER: 15
    LEGEND: 25
    
# Teleport Settings
teleport:
  # Cooldown between teleport requests (in seconds)
  request-cooldown: 60
  # How long a teleport request remains valid (in seconds)
  request-timeout: 30
  # Whether to allow cross-world teleportation
  cross-world: true
  # Whether to enable back command
  enable-back: true
  # Whether to enable random teleport
  enable-random-teleport: true
  # Random teleport distance (blocks)
  random-teleport-distance: 10000
  
# Chat Settings
chat:
  # Chat cooldown (in seconds, 0 = none)
  cooldown: 2
  # Maximum message length
  max-message-length: 100
  # Enable chat channels
  enable-channels: true
  # Default channel
  default-channel: "global"
  # Enable private messaging
  enable-private-messaging: true
  # Enable chat color (requires permission)
  allow-color: true
  # Enable profanity filter
  enable-profanity-filter: true
  
# Tab Settings
tab:
  # Enable custom tab list
  enabled: true
  # Custom header (uses built-in placeholders)
  header: "&6Welcome to the server!\n&eOnline: {online}/{max}"
  # Custom footer
  footer: "&7Server running BDCraft"
  # Sort players by rank
  sort-by-rank: true
```

### Messages Configuration: `messages.yml`

Controls all player-facing messages.

```yaml
# BDCraft Messages Configuration

# General messages
general:
  prefix: "&6[BDCraft] &r"
  no-permission: "&cYou don't have permission to do that."
  player-only: "&cThis command can only be used by players."
  invalid-player: "&cCould not find player: {player}"
  
# Economy messages
economy:
  balance: "&aYour balance: {symbol}{amount}"
  balance-other: "&a{player}'s balance: {symbol}{amount}"
  given: "&aYou've been given {symbol}{amount}"
  taken: "&c{symbol}{amount} has been taken from you"
  paid: "&aYou paid {player} {symbol}{amount}"
  received: "&aYou received {symbol}{amount} from {player}"
  not-enough-money: "&cYou don't have enough money. Required: {symbol}{amount}"
  
# Market messages
market:
  created: "&aMarket created: {name}"
  opened: "&aMarket opened: {name}"
  item-added: "&aItem added to your market for {symbol}{price}"
  item-sold: "&aYou sold {item} for {symbol}{amount}"
  item-purchased: "&aYou purchased {item} for {symbol}{amount}"
  market-limit-reached: "&cYou've reached your market limit ({limit})"
  
# Auction messages
auction:
  item-listed: "&aItem listed for auction with starting bid of {symbol}{amount}"
  bid-placed: "&aYou placed a bid of {symbol}{amount} on {item}"
  outbid: "&cYou were outbid on {item}. New highest bid: {symbol}{amount}"
  auction-won: "&aYou won the auction for {item} with a bid of {symbol}{amount}"
  auction-ended: "&aYour auction for {item} ended. Winning bid: {symbol}{amount}"
  
# Progression messages
progression:
  rank-up: "&aCongratulations! You've ranked up to {rank}!"
  exp-gained: "&aYou gained {amount} experience points"
  rebirth: "&a⭐ You've been reborn! Your rebirth level is now {level} ⭐"
  rebirth-failed: "&cYou need to be rank {rank} to rebirth"
  
# Home messages
home:
  set: "&aHome '{name}' set at your current location"
  deleted: "&cHome '{name}' deleted"
  teleported: "&aTeleported to home '{name}'"
  max-homes: "&cYou've reached your home limit ({limit})"
  no-home: "&cYou don't have a home named '{name}'"
  
# Teleport messages
teleport:
  request-sent: "&aTeleport request sent to {player}"
  request-received: "&a{player} wants to teleport to you. Type /tpaccept to accept."
  request-accepted: "&a{player} accepted your teleport request"
  request-denied: "&c{player} denied your teleport request"
  teleporting: "&aTeleporting in {time} seconds. Don't move!"
  teleport-cancelled: "&cTeleport cancelled due to movement"
  
# Chat messages
chat:
  channel-switched: "&aSwitched to channel: {channel}"
  message-sent: "&aMessage sent to {player}"
  reply-no-target: "&cYou have nobody to reply to"
  cooldown: "&cPlease wait {time} seconds before chatting again"
```

### Permissions Configuration: `permissions.yml`

Defines all permission nodes.

```yaml
# BDCraft Permissions Configuration

# Core permissions
core:
  admin: "bdcraft.admin"  # Full admin access
  
# Economy permissions
economy:
  use: "bdcraft.economy.use"  # Basic economy commands
  admin: "bdcraft.economy.admin"  # Economy admin commands
  pay: "bdcraft.economy.pay"  # Allow paying other players
  
  # Market permissions
  market:
    use: "bdcraft.market.use"  # Basic market usage
    create: "bdcraft.market.create"  # Create markets
    multiple: "bdcraft.market.multiple"  # Create multiple markets
    
  # Auction permissions
  auction:
    use: "bdcraft.auction.use"  # Basic auction usage
    create: "bdcraft.auction.create"  # Create auctions
    featured: "bdcraft.auction.featured"  # Create featured auctions
    
  # Villager permissions
  villager:
    use: "bdcraft.villager.use"  # Interact with villagers
    admin: "bdcraft.villager.admin"  # Manage villagers
    
# Progression permissions
progression:
  rank:
    use: "bdcraft.rank.use"  # View ranks
    admin: "bdcraft.rank.admin"  # Manage ranks
    
  rebirth:
    use: "bdcraft.rebirth.use"  # Use rebirth system
    admin: "bdcraft.rebirth.admin"  # Manage rebirth system
    
# Vital permissions
vital:
  home:
    use: "bdcraft.home.use"  # Basic home usage
    multiple: "bdcraft.home.multiple.{number}"  # Set multiple homes
    others: "bdcraft.home.others"  # Manage others' homes
    
  teleport:
    use: "bdcraft.teleport.use"  # Basic teleport usage
    bypass-cooldown: "bdcraft.teleport.bypass-cooldown"  # Bypass teleport cooldowns
    others: "bdcraft.teleport.others"  # Teleport others
    
  chat:
    color: "bdcraft.chat.color"  # Use chat colors
    format: "bdcraft.chat.format"  # Use chat formatting
    bypass-cooldown: "bdcraft.chat.bypass-cooldown"  # Bypass chat cooldowns
```

## Configuration Tips

1. **Start Small**: Begin by enabling only the modules you need, then add more as you become familiar with the plugin.

2. **Test Changes**: After making configuration changes, test them on your server to ensure they work as expected.

3. **Use Comments**: Add your own comments to configuration files to remember why you made specific changes.

4. **Back Up First**: Always create a backup of your configuration files before making significant changes.

5. **Permissions Setup**: Ensure you've set up permissions correctly for your staff and players.

## Advanced Configuration

### Custom Module Chains

You can create custom module chains by configuring how different modules interact within BDCraft. For example, you might want market sales to award both currency and experience points:

```yaml
# In economy.yml
market:
  # ... other settings ...
  award-experience: true
  experience-per-sale: 10
```

### Database Storage

By default, BDCraft stores data in its own files, but you can configure it to use a database:

```yaml
# In config.yml
storage:
  # Storage type: file, mysql, sqlite
  type: "mysql"
  
  # MySQL settings (if using mysql)
  mysql:
    host: "localhost"
    port: 3306
    database: "bdcraft"
    username: "username"
    password: "password"
    prefix: "bd_"
```

## Next Steps

After configuring BDCraft, check out the [Commands](commands.md) guide to learn how to use the plugin.