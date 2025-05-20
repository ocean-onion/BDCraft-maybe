# BDCraft Tokens System

BDCraft utilizes special tokens that allow players to create and expand their markets within the game world. These tokens represent a key part of the economy system, enabling players to establish trading infrastructure.

## Types of Tokens

BDCraft features two primary token types that serve specific functions:

### BD Market Token

Market tokens are special items used to establish player-owned markets where BD economic activity takes place.

- **Appearance**: Enchanted stick with custom name "BD Market Token" in gold text
- **Function**: Creates a player-owned market when placed in an item frame above a market stall door
- **Acquisition**: Crafting only

#### Crafting Recipe
```
D D D
E S E
D D D
```
Where:
- D = Diamond
- E = Emerald
- S = BD Stick

#### Usage
- Build a market stall structure (minimum 3x3 roof, walls with door, bed inside)
- Place the BD Market Token in an item frame above the door
- This spawns a BD Dealer and Market Owner in your new market
- Creates a 49x49 block market area around the token
- Makes you the "Market Founder" with full management permissions

#### Special Properties
- Can only be placed if location is at least 30 blocks from another market
- Single-use item (consumed when placed)
- Cannot be used in creative mode (prevents market spam)
- Required for accessing the BD economy system

### BD House Token

House tokens allow players to expand their markets by creating collector houses where crop sales take place.

- **Appearance**: Bed with custom name "BD House Token" in aqua text
- **Function**: Creates collector houses with BD Collectors in existing markets
- **Acquisition**: Crafting or purchase from Market Owner villager

#### Crafting Recipe
```
C C C
C B C
C C C
```
Where:
- C = BD Crop (Fern)
- B = Bed (any color)

#### Usage
- Build a collector house structure (3x3 roof, walls with door, bed inside)
- Place the House Token in an item frame above the door
- This spawns a BD Collector villager in the house
- Must be placed within an existing market's radius to work

#### Special Properties
- Single-use item (consumed when placed)
- Cannot place if market has reached collector limit
- Higher level markets allow more collector houses
- Initial markets support up to 3 collectors
- Upgraded markets can support up to 10 collectors

## Token Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdmarket check` | Visualize market boundaries | bdcraft.market.use |
| `/bdmarket info` | Display information about the market you're standing in | bdcraft.market.use |
| `/bdadmin give <player> markettoken [amount]` | Give market tokens to a player (admin) | bdcraft.admin |
| `/bdadmin give <player> housetoken [amount]` | Give house tokens to a player (admin) | bdcraft.admin |

## Market Creation Process

1. **Build a Market Structure**:
   - Create a building with at least 3x3 roof
   - Place walls with a door
   - Put a bed inside
   - Place an item frame above the door

2. **Place the BD Market Token**:
   - Craft a BD Market Token
   - Place it in the item frame above the door
   - This establishes your market and defines its boundary

3. **Expand with Collector Houses**:
   - Build additional small houses within your market boundary
   - Place House Tokens in item frames above their doors
   - This adds Collectors who buy crops from players

## Market Limitations

- Markets must be at least 30 blocks apart
- Initial market size is 49x49 blocks (can be expanded with upgrades)
- Starting markets are limited to 3 collector houses
- Only Market Founders can add associates who can manage the market
- Players are initially limited to owning one market (higher ranks allow more)

## Token Administration

Server administrators can manage the token system using these commands:

- `/bdadmin give <player> markettoken [amount]` - Give market tokens to a player
- `/bdadmin give <player> housetoken [amount]` - Give house tokens to a player
- `/bdadmin market delete <id>` - Remove a market and its tokens
- `/bdadmin market setlevel <id> <level>` - Set market upgrade level (1-4)

## Token Configuration

Token behavior can be configured in `market.yml`:

```yaml
# BDCraft Market Token Configuration

tokens:
  market-token:
    enabled: true
    display-name: "BD Market Token"
    lore:
      - "Used to create a BD Market"
      - "Place in an item frame above a market building door"
    
  house-token:
    enabled: true
    display-name: "BD House Token"
    lore:
      - "Used to create a BD Collector house"
      - "Place in an item frame above a house door"
    
market:
  min-distance: 30              # Minimum blocks between markets
  market-radius: 49             # Radius of player markets in blocks
  max-collectors-initial: 3     # Starting collector limit
  max-collectors-upgraded: 10   # Maximum collectors after upgrades
```

## BD Stick

The BD Stick is a precursor item required for crafting BD Market Tokens:

- **Appearance**: Enchanted Breeze Rod with custom name "BD Stick"
- **Durability**: 5 uses before breaking
- **Function**: Applies random potion effects when used and required for market creation
- **Crafting Recipe**: Paper + BD Crop (Fern) + Flint in any horizontal row

The BD Stick serves as the entry point to the market system, as it's a key ingredient for BD Market Tokens that establish player markets.