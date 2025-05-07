# Admin Commands

This guide covers all administrative commands available in the BDCraft plugin. These commands allow server operators to manage the BD economy, villages, villagers, and more.

## Command Access

All admin commands require the `bdcraft.admin` permission or operator status. Some commands have additional permission requirements as noted below.

## Core Admin Commands

### `/bdadmin`

Base command for all administrative functions. Shows help menu when used alone.

**Usage**: `/bdadmin [subcommand] [arguments]`
**Permission**: `bdcraft.admin`

## BD Villager Management

### `/bdadmin spawn dealer <location>`

Spawns a natural BD Dealer at your current location. These dealers function like the ones that naturally spawn in vanilla villages.

**Usage**: `/bdadmin spawn dealer`
**Permission**: `bdcraft.admin.villager`

### `/bdadmin spawn collector <location>`

Spawns a BD Collector at your current location. This is primarily for testing purposes, as collectors should normally only exist in player markets.

**Usage**: `/bdadmin spawn collector`
**Permission**: `bdcraft.admin.villager`

### `/bdadmin spawn seasonal <location>`

Spawns a Seasonal BD Trader at your current location. This is primarily for testing purposes, as seasonal traders should normally only appear in level 3+ player markets.

**Usage**: `/bdadmin spawn seasonal`
**Permission**: `bdcraft.admin.villager`

### `/bdadmin villager list`

Lists all BD villagers currently active on the server.

**Usage**: `/bdadmin villager list`
**Permission**: `bdcraft.admin.villager`

### `/bdadmin villager remove <uuid>`

Removes a BD villager by UUID.

**Usage**: `/bdadmin villager remove <uuid>`
**Example**: `/bdadmin villager remove 550e8400-e29b-41d4-a716-446655440000`
**Permission**: `bdcraft.admin.villager`

## Economy Management

### `/bdadmin economy reload`

Reloads the economy configuration.

**Usage**: `/bdadmin economy reload`
**Permission**: `bdcraft.admin.economy`

### `/bdadmin economy give <player> <amount>`

Gives server currency to a player.

**Usage**: `/bdadmin economy give <player> <amount>`
**Example**: `/bdadmin economy give Steve 5000`
**Permission**: `bdcraft.admin.economy`

### `/bdadmin economy take <player> <amount>`

Takes server currency from a player.

**Usage**: `/bdadmin economy take <player> <amount>`
**Example**: `/bdadmin economy take Steve 1000`
**Permission**: `bdcraft.admin.economy`

### `/bdadmin economy set <player> <amount>`

Sets a player's server currency to the specified amount.

**Usage**: `/bdadmin economy set <player> <amount>`
**Example**: `/bdadmin economy set Steve 10000`
**Permission**: `bdcraft.admin.economy`

## Item Management

### `/bdadmin give <player> <item> [amount]`

Gives BD items to a player.

**Usage**: `/bdadmin give <player> <item> [amount]`
**Example**: `/bdadmin give Steve bdcrop 10`
**Permission**: `bdcraft.admin.items`

**Available Items**:
- `bdseed` - Regular BD seed
- `greenseed` - Green BD seed
- `purpleseed` - Purple BD seed
- `bdcrop` - Regular BD crop
- `greencrop` - Green BD crop
- `purplecrop` - Purple BD crop
- `bdstick` - BD Stick
- `harvester` - BD Harvester
- `ultimateharvester` - Ultimate BD Harvester
- `markettoken` - BD Market Token
- `housetoken` - BD House Token

## Player Management

### `/bdadmin rank set <player> <rank>`

Sets a player's BD rank.

**Usage**: `/bdadmin rank set <player> <rank>`
**Example**: `/bdadmin rank set Steve 3`
**Permission**: `bdcraft.admin.rank`

### `/bdadmin rebirth <player> [level]`

Sets a player's rebirth level.

**Usage**: `/bdadmin rebirth <player> [level]`
**Example**: `/bdadmin rebirth Steve 2`
**Permission**: `bdcraft.admin.rebirth`

## System Management

### `/bdadmin reload`

Reloads the BDCraft plugin configuration.

**Usage**: `/bdadmin reload`
**Permission**: `bdcraft.admin.reload`

### `/bdadmin debug <on|off>`

Toggles debug mode for the plugin.

**Usage**: `/bdadmin debug <on|off>`
**Example**: `/bdadmin debug on`
**Permission**: `bdcraft.admin.debug`

### `/bdadmin version`

Displays the current version of the BDCraft plugin.

**Usage**: `/bdadmin version`
**Permission**: `bdcraft.admin`

## Market Management Commands

### `/bdadmin market list`

Lists all player markets on the server.

**Usage**: `/bdadmin market list`
**Permission**: `bdcraft.admin.market`

### `/bdadmin market info <id>`

Displays detailed information about a specific market.

**Usage**: `/bdadmin market info <id>`
**Example**: `/bdadmin market info market_1`
**Permission**: `bdcraft.admin.market`

### `/bdadmin market delete <id>`

Deletes a player market and removes all associated structures and villagers.

**Usage**: `/bdadmin market delete <id>`
**Example**: `/bdadmin market delete market_1`
**Permission**: `bdcraft.admin.market`

### `/bdadmin market setlevel <id> <level>`

Sets the upgrade level of a market (1-4).

**Usage**: `/bdadmin market setlevel <id> <level>`
**Example**: `/bdadmin market setlevel market_1 3`
**Permission**: `bdcraft.admin.market`

### `/bdadmin market visualize <id>`

Visualizes the boundaries of a market with temporary blocks.

**Usage**: `/bdadmin market visualize <id>`
**Example**: `/bdadmin market visualize market_1`
**Permission**: `bdcraft.admin.market`

These administrative commands provide comprehensive control over all aspects of the BDCraft plugin, allowing server operators to manage the BD economy, villages, villagers, and player progression.