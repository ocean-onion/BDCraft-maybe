# BDCraft Token System

The BDCraft token system provides a specialized second currency that integrates directly with the plugin's progression and economy systems. Unlike generic token systems, BDCraft tokens are deeply woven into the server's ecosystem, providing unique gameplay opportunities and rewards.

## Token Core Mechanics

BDCraft tokens function as a premium currency with several key characteristics:

1. **Server-Bound**: Tokens are tied to the specific BDCraft instance and cannot be transferred between servers
2. **Account-Linked**: Tokens are directly linked to player accounts through BDCraft's internal database
3. **Permission-Controlled**: All token functions are managed through BDCraft's permission system
4. **Progression-Integrated**: Tokens directly interact with the rank and rebirth systems

## Token Economy

### Token Sources

Players can obtain tokens through:

- **Milestone Rewards**: Automatically awarded when reaching specific rank thresholds
- **Achievement Completion**: Granted for completing server achievements
- **Market Leadership**: Earned by owning the most profitable market stands
- **Special Events**: Awarded during server events and competitions
- **Rebirth Bonus**: Significant token reward upon rebirth
- **Admin Grants**: Given by administrators for contributions to the server

### Token Uses

Tokens can be spent on various exclusive benefits:

- **Rank Fast-Tracking**: Skip certain rank requirements using tokens
- **Cooldown Bypasses**: Use tokens to bypass teleport and command cooldowns
- **Exclusive Items**: Purchase items only available through the token shop
- **Market Upgrades**: Expand market capabilities beyond normal limits
- **Visual Effects**: Unlock particle effects, chat colors, and other cosmetics
- **Special Abilities**: Purchase time-limited special abilities

## Token Commands

BDCraft provides a complete set of token-related commands:

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdtoken` | `/token` | View your token balance | bdcraft.token.use |
| `/bdtoken shop` | `/token shop` | Open the token shop GUI | bdcraft.token.use |
| `/bdtoken transfer <player> <amount>` | `/token transfer <player> <amount>` | Transfer tokens to another player | bdcraft.token.transfer |
| `/bdtoken leaderboard` | `/token leaderboard`, `/token top` | View top token holders | bdcraft.token.use |
| `/bdtoken rewards` | `/token rewards` | View available token rewards | bdcraft.token.use |
| `/bdtoken history` | `/token history` | View your token transaction history | bdcraft.token.use |
| `/bdtoken admin give <player> <amount>` | `/token admin give <player> <amount>` | Give tokens to a player | bdcraft.token.admin |
| `/bdtoken admin take <player> <amount>` | `/token admin take <player> <amount>` | Remove tokens from a player | bdcraft.token.admin |
| `/bdtoken admin set <player> <amount>` | `/token admin set <player> <amount>` | Set a player's token balance | bdcraft.token.admin |
| `/bdtoken admin reload` | `/token admin reload` | Reload token configuration | bdcraft.token.admin |

## Token Integration

### Economy Module Integration

- **Market Bond**: Tokens can be used to create premium market stalls with extra features
- **Auction Tags**: Use tokens to make auctions more visible and featured
- **Villager Expansion**: Unlock special villager trades with tokens

### Progression Module Integration

- **Rank Skipping**: Use tokens to skip certain rank requirements
- **Rebirth Benefits**: Rebirth levels provide token multipliers
- **Experience Boosting**: Purchase temporary XP multipliers with tokens

### Vital Module Integration

- **Command Cooldowns**: Bypass teleport and other command cooldowns
- **Home Expansion**: Purchase additional home slots beyond permission limits
- **Premium Effects**: Unlock special visual and status effects

## Token Configuration

Token behavior is configured in the `tokens.yml` file:

```yaml
# BDCraft Token System Configuration

# General Settings
tokens:
  # Token name (displayed in messages)
  name: "BD Token"
  # Plural form of token name
  plural: "BD Tokens"
  # Token symbol
  symbol: "✦"
  # Maximum tokens a player can have
  max-balance: 1000
  # Can tokens be transferred between players?
  transferable: true
  # Require confirmation for token transfers?
  confirm-transfers: true

# Token Rewards
rewards:
  # Rank milestone rewards
  ranks:
    APPRENTICE: 5
    EXPERT: 10
    MASTER: 25
    GRANDMASTER: 50
    LEGEND: 100
  
  # Rebirth rewards
  rebirth:
    base-amount: 25
    multiplier: 0.5  # Additional 50% per rebirth level
  
  # Achievement rewards
  achievements:
    market_master: 15
    potion_brewer: 10
    enchantment_specialist: 20
    dungeon_crawler: 30
    
# Token Shop Items
shop:
  categories:
    effects:
      flame_trail:
        name: "Flame Trail"
        description: "Leave a trail of fire particles behind you"
        cost: 25
        duration: 604800  # 1 week in seconds
      
    boosters:
      xp_boost:
        name: "XP Booster"
        description: "1.5x XP gain for 24 hours"
        cost: 20
        multiplier: 1.5
        duration: 86400  # 24 hours in seconds
        
    upgrades:
      extra_home:
        name: "Extra Home Slot"
        description: "Adds one additional home slot"
        cost: 15
        permanent: true
    
    market:
      featured_stall:
        name: "Featured Market Stall"
        description: "Highlight your market stall for 3 days"
        cost: 10
        duration: 259200  # 3 days in seconds
```

## Token Shop

The token shop provides a GUI interface for purchasing token-exclusive items:

1. **Cosmetic Effects**: Visual effects and cosmetic enhancements
2. **Gameplay Boosters**: Experience and economy multipliers
3. **Command Unlocks**: Special command abilities or cooldown bypasses
4. **Permanent Upgrades**: Account-level upgrades that persist indefinitely
5. **Temporary Benefits**: Time-limited special abilities or permissions

## Token Administration

Administrators can manage tokens using:

- The token admin commands for monitoring and adjustment
- Direct configuration in the tokens.yml file
- In-game GUIs for token shop management

## Tokens vs. Economy

BDCraft maintains a clear separation between the main economy (BDCoins) and tokens:

| Feature | Economy (BDCoins) | Tokens |
|---------|-------------------|--------|
| Primary Purpose | General transactions | Special rewards |
| Acquisition | Regular gameplay | Achievements and milestones |
| Usage | Standard purchases | Premium features |
| Transferability | Freely transferable | Limited transfer |
| Storage | Economic database | Player data records |

## Best Practices

For server administrators:

1. **Balance Token Rewards**: Carefully control the rate at which tokens enter the economy
2. **Create Token Sinks**: Ensure there are valuable ways for players to spend tokens
3. **Exclusive Benefits**: Keep token purchases genuinely exclusive without creating pay-to-win
4. **Regular Updates**: Refresh token shop offerings to maintain player interest
5. **Event Integration**: Use tokens as rewards for server events and competitions