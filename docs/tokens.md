# BDCraft Collector Token System

BDCraft includes a specialized token collection system that enhances gameplay through collectible tokens that can be redeemed at markets and special villagers. This system encourages exploration, trading, and participation in server activities.

## Types of Collectible Tokens

BDCraft features several distinct types of collectible tokens:

### Market Tokens

Market tokens are special collectible items that can be redeemed at player-owned markets for exclusive trades and deals.

- **Basic Market Token**: Common token redeemable at any market
- **Premium Market Token**: Rare token offering better trades and deals
- **Founder's Market Token**: Special token only usable at a market founder's stalls
- **Seasonal Market Token**: Limited-time token that changes each season

### Collector Tokens

Collector tokens are rare items sought by specific collector villagers who offer unique items in exchange.

- **Ancient Token**: Extremely rare token found in dungeons
- **Craftsman Token**: Token earned through high-level crafting
- **Hunter Token**: Token earned by defeating specific mobs
- **Explorer Token**: Token found by discovering specific locations
- **Festival Token**: Special token earned during server events

## Obtaining Tokens

Players can acquire tokens through various gameplay activities:

| Token Type | Acquisition Methods |
|------------|---------------------|
| Basic Market Token | Mining, fishing, mob drops (common) |
| Premium Market Token | Dungeon chests, raid completion (uncommon) |
| Founder's Market Token | Market participation, special trades (rare) |
| Seasonal Market Token | Seasonal events and activities |
| Ancient Token | Only in ancient ruins and dungeons |
| Craftsman Token | Crafting special items |
| Hunter Token | Defeating specific high-level mobs |
| Explorer Token | Discovering landmark locations |
| Festival Token | Participating in server festivals |

## Using Tokens

### Market Token Usage

Market tokens can be used at player markets for special benefits:

- **Access to exclusive items**: Some market stalls only accept tokens for certain items
- **Discounted trades**: Use tokens for better prices on regular goods
- **Special ordering**: Reserve rare items with tokens before they're available to others
- **Market status**: Accumulate tokens to rise in market ranks

### Collector Usage

Collector villagers will exchange valuable and unique items for specific collector tokens:

- **Rare item trades**: Exchange tokens for items not available elsewhere
- **Custom enchantments**: Some collector villagers offer special enchantments
- **Decorative items**: Unique decorative blocks and items
- **Knowledge books**: Special recipes and crafting knowledge

## Token Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdcollection` | `/collection`, `/bdcol` | View your token collection | bdcraft.collection.use |
| `/bdcollection info <token>` | `/collection info <token>` | View information about a specific token | bdcraft.collection.use |
| `/bdcollection exchange <token>` | `/collection exchange <token>` | Exchange a token | bdcraft.collection.use |
| `/bdcollection track <token>` | `/collection track <token>` | Track a specific token type | bdcraft.collection.track |
| `/bdcollection untrack` | `/collection untrack` | Stop tracking tokens | bdcraft.collection.track |
| `/bdcollection top` | `/collection top` | View top token collectors | bdcraft.collection.use |
| `/bdcollection admin add <player> <token> <amount>` | `/collection admin add <player> <token> <amount>` | Add tokens to a player | bdcraft.collection.admin |
| `/bdcollection admin remove <player> <token> <amount>` | `/collection admin remove <player> <token> <amount>` | Remove tokens from a player | bdcraft.collection.admin |
| `/bdcollection admin reload` | `/collection admin reload` | Reload token configuration | bdcraft.collection.admin |

## Collection Book

The Collection Book is a special in-game GUI that tracks token collection progress:

- Access with `/bdcollection` command
- Shows all token types discovered and their quantities
- Displays information about where to find missing tokens
- Tracks collection milestones and rewards
- Provides a leaderboard for token collectors

## Token Configuration

Token behavior and drop rates are configured in the `collection.yml` file:

```yaml
# BDCraft Collection Token Configuration

# Market Tokens
market:
  basic:
    name: "Basic Market Token"
    description: "A common token accepted at most market stalls"
    rarity: "COMMON"
    drop-rates:
      mining: 0.05  # 5% chance when mining
      fishing: 0.03  # 3% chance when fishing
      mob-kill: 0.02  # 2% chance from mob kills
  
  premium:
    name: "Premium Market Token"
    description: "An uncommon token offering better deals at markets"
    rarity: "UNCOMMON"
    drop-rates:
      dungeon-chest: 0.15  # 15% chance in dungeon chests
      raid-completion: 0.25  # 25% chance after raid
      boss-kill: 0.20  # 20% chance from boss kills
  
  founders:
    name: "Founder's Market Token"
    description: "A rare token usable at market founders' stalls"
    rarity: "RARE"
    drop-rates:
      special-trades: 0.10  # 10% chance from special trades
      market-participation: 0.01  # 1% daily chance when using markets
  
  seasonal:
    name: "Seasonal Market Token"
    description: "A limited-time token that changes seasonally"
    rarity: "SEASONAL"
    enabled: true  # Can be disabled during off-seasons
    current-season: "Summer"  # Current season

# Collector Tokens
collector:
  ancient:
    name: "Ancient Token"
    description: "An extremely rare token found in ancient ruins"
    rarity: "VERY_RARE"
    drop-rates:
      dungeon-chest: 0.05  # 5% chance in dungeon chests
      ancient-ruins: 0.10  # 10% chance in ancient ruins
  
  craftsman:
    name: "Craftsman Token"
    description: "A token earned through high-level crafting"
    rarity: "RARE"
    crafting-recipes:
      - "NETHERITE_TOOL"
      - "BEACON"
      - "ELYTRA_REPAIR"
  
  hunter:
    name: "Hunter Token"
    description: "A token earned by defeating specific mobs"
    rarity: "UNCOMMON"
    mob-drops:
      ENDER_DRAGON: 1.0  # 100% chance
      WITHER: 1.0  # 100% chance
      ELDER_GUARDIAN: 0.5  # 50% chance
  
  explorer:
    name: "Explorer Token"
    description: "A token found by discovering specific locations"
    rarity: "RARE"
    locations:
      - "DESERT_PYRAMID"
      - "WOODLAND_MANSION"
      - "OCEAN_MONUMENT"
      - "END_CITY"
  
  festival:
    name: "Festival Token"
    description: "A special token earned during server events"
    rarity: "EVENT"
    enabled: false  # Only enabled during events
    event: "Summer Festival"  # Current event

# Collection Milestones
milestones:
  market-tokens:
    10:
      reward: "MARKET_DISCOUNT_CARD"
      description: "10% discount at all markets"
    50:
      reward: "PREMIUM_MARKET_ACCESS"
      description: "Access to premium market areas"
    100:
      reward: "MARKET_FOUNDER_RIGHTS"
      description: "Ability to create special market stalls"
  
  collector-tokens:
    5:
      reward: "COLLECTOR_BAG"
      description: "Special storage for collector items"
    25:
      reward: "RARE_RECIPE_BOOK"
      description: "Book of rare crafting recipes"
    50:
      reward: "COLLECTOR_TITLE"
      description: "Special 'Master Collector' title"
```

## Collector Villagers

Special collector villagers can be found throughout the world, each specializing in different token types:

- **Archeologist**: Trades for Ancient Tokens
- **Master Craftsman**: Trades for Craftsman Tokens
- **Trophy Hunter**: Trades for Hunter Tokens
- **Cartographer**: Trades for Explorer Tokens
- **Festival Merchant**: Trades for Festival Tokens (only during events)

These villagers offer unique items, enchantments, and decorative blocks not available through any other means.

## Market Token Integration

Market tokens integrate directly with the BDCraft market system:

- Market owners can configure special token-only stalls
- Markets can offer token-based discounts and special deals
- Token collection status can influence market reputation
- Seasonal market events feature special token-based activities

## Admin Tools

Server administrators have several tools to manage the token system:

- Token drop rate configuration in collection.yml
- Admin commands for token management
- Event scheduling tools for seasonal tokens
- Token-based achievement configuration
- Market integration settings

## Best Practices

For server administrators:

1. **Balance Token Rarity**: Ensure tokens maintain their collectible value
2. **Rotate Seasonal Content**: Keep the token system fresh with seasonal changes
3. **Create Token Sinks**: Ensure there are valuable ways for players to use tokens
4. **Run Special Events**: Use festival tokens to drive participation in server events
5. **Maintain Collector Value**: Keep collector villagers' offerings unique and desirable