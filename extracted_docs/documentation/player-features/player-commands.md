# BDCraft Player Commands

This guide covers all player-accessible commands in the BDCraft plugin. These commands allow players to interact with the BD economy, check their ranks, manage their items, and more.

## Command Access

All player commands require the `bdcraft.use` permission, which is typically granted to all players by default. Some commands require additional permissions as noted below.

## General Commands

### `/bdcraft`

Base command for all player functions. Shows general plugin information and version.

**Usage**: `/bdcraft`
**Permission**: `bdcraft.use`

### `/bdhelp`

Displays a list of available commands and basic help information.

**Usage**: `/bdhelp [topic]`
**Examples**:
- `/bdhelp` - General help
- `/bdhelp crops` - Information about BD crops
- `/bdhelp villagers` - Information about BD villagers
**Permission**: `bdcraft.use`

## Economy Commands

### `/bdbal`

Checks your current server currency balance.

**Usage**: `/bdbal`
**Permission**: `bdcraft.balance`

### `/bdpay <player> <amount>`

Sends server currency to another player.

**Usage**: `/bdpay <player> <amount>`
**Example**: `/bdpay Steve 500`
**Permission**: `bdcraft.pay`

### `/bdtop`

Displays the top players by server currency balance.

**Usage**: `/bdtop [economy|rank|rebirth]`
**Examples**:
- `/bdtop` - Default leaderboard (economy)
- `/bdtop economy` - Currency leaderboard
- `/bdtop rank` - Rank leaderboard
- `/bdtop rebirth` - Rebirth leaderboard
**Permission**: `bdcraft.top`

## Rank Commands

### `/bdrank`

Displays your current rank, benefits, and progress toward the next rank.

**Usage**: `/bdrank`
**Permission**: `bdcraft.rank.view`

### `/bdrank up`

Purchases the next rank if you meet the requirements.

**Usage**: `/bdrank up`
**Permission**: `bdcraft.rank.up`

### `/bdrank info <rank>`

Displays detailed information about a specific rank.

**Usage**: `/bdrank info <rank>`
**Example**: `/bdrank info farmer`
**Permission**: `bdcraft.rank.view`

### `/bdrank list`

Lists all ranks and their benefits.

**Usage**: `/bdrank list`
**Permission**: `bdcraft.rank.view`

## Rebirth Commands

### `/bdrebirth`

Displays general rebirth information.

**Usage**: `/bdrebirth`
**Permission**: `bdcraft.rebirth.view`

### `/bdrebirth check`

Checks if you're eligible for rebirth.

**Usage**: `/bdrebirth check`
**Permission**: `bdcraft.rebirth.check`

### `/bdrebirth confirm`

Confirms and initiates the rebirth process if eligible.

**Usage**: `/bdrebirth confirm`
**Permission**: `bdcraft.rebirth.use`

### `/bdrebirth stats [player]`

Displays rebirth statistics for yourself or another player.

**Usage**: `/bdrebirth stats [player]`
**Example**: `/bdrebirth stats Steve`
**Permission**: `bdcraft.rebirth.view`

## Village and Market Commands

### `/bdvil locate`

Locates the nearest BD village and shows the distance and direction.

**Usage**: `/bdvil locate`
**Permission**: `bdcraft.village.locate`

### `/bdvil list`

Lists all BD villages you have discovered.

**Usage**: `/bdvil list`
**Permission**: `bdcraft.village.list`

### `/bdvil info <id>`

Displays detailed information about a specific village.

**Usage**: `/bdvil info <id>`
**Example**: `/bdvil info farming_village1`
**Permission**: `bdcraft.village.info`

### `/bdmarket check`

Visualizes the boundaries of your market or the market you're currently standing in with temporary wool blocks.

**Usage**: `/bdmarket check`
**Permission**: `bdcraft.market.check`

### `/bdmarket info [id]`

Displays information about your market or a specific market.

**Usage**: `/bdmarket info [id]`
**Example**: `/bdmarket info market_1`
**Permission**: `bdcraft.market.info`

### `/bdmarket list`

Lists all markets you own or are an associate of.

**Usage**: `/bdmarket list`
**Permission**: `bdcraft.market.create`

### `/bdmarket associate add <player>`

Adds a player as an associate to your market, allowing them to build and use collectors.

**Usage**: `/bdmarket associate add <player>`
**Example**: `/bdmarket associate add Steve`
**Permission**: `bdcraft.market.associate`

### `/bdmarket associate remove <player>`

Removes a player as an associate from your market.

**Usage**: `/bdmarket associate remove <player>`
**Example**: `/bdmarket associate remove Steve`
**Permission**: `bdcraft.market.associate`

### `/bdmarket upgrade`

Upgrades your market to the next level if you meet the requirements.

**Usage**: `/bdmarket upgrade`
**Permission**: `bdcraft.market.upgrade`

## Reputation Commands

### `/bdrep`

Checks your reputation in the nearest village or all villages.

**Usage**: `/bdrep [all]`
**Examples**:
- `/bdrep` - Reputation in the nearest village
- `/bdrep all` - Reputation in all villages
**Permission**: `bdcraft.reputation`

## Special Rank Feature Commands

### `/bdsponsor <player>`

Sponsors another player, giving them a temporary trading boost. Only available to Agricultural Experts (Rank 5).

**Usage**: `/bdsponsor <player>`
**Example**: `/bdsponsor Steve`
**Permission**: `bdcraft.sponsor`
**Requirements**: Rank 5 (Agricultural Expert)

### Agricultural Deity Commands (Rebirth 10)

These commands are only available to players who have achieved 10 rebirths (Agricultural Deity status):

#### `/bdbless <player>`

Gives the target player a temporary trading boost.

**Usage**: `/bdbless <player>`
**Example**: `/bdbless Steve`
**Permission**: `bdcraft.deity.bless`
**Requirements**: Rebirth level 10

#### `/bdpredict`

Shows upcoming seasonal trader stock.

**Usage**: `/bdpredict`
**Permission**: `bdcraft.deity.predict`
**Requirements**: Rebirth level 10

#### `/bdaura`

Toggles the visibility of your abundance aura.

**Usage**: `/bdaura`
**Permission**: `bdcraft.deity.aura`
**Requirements**: Rebirth level 10

## Auction House Commands

### `/bdah`

Opens the BD auction house GUI.

**Usage**: `/bdah`
**Permission**: `bdcraft.auction.use`

### `/bdah sell <item> <price> [amount]`

Sells an item on the auction house. Item must be held in hand.

**Usage**: `/bdah sell <item> <price> [amount]`
**Example**: `/bdah sell bdseed 10 5`
**Permission**: `bdcraft.auction.sell`

### `/bdah cancel <listing_id>`

Cancels one of your auction listings.

**Usage**: `/bdah cancel <listing_id>`
**Example**: `/bdah cancel 123`
**Permission**: `bdcraft.auction.cancel`

### `/bdah listings`

Shows your current auction listings.

**Usage**: `/bdah listings`
**Permission**: `bdcraft.auction.list`

## Command Syntax Legend

- `<required>` - A required argument
- `[optional]` - An optional argument
- `<player>` - A player name
- `<amount>` - A numeric amount
- `<item>` - An item identifier

These commands allow players to fully interact with all aspects of the BDCraft plugin's features.