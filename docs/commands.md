# Commands Reference

BDCraft provides a comprehensive set of commands for players and administrators. This guide lists all available commands organized by module.

## Core Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bd` | Displays plugin information | None |
| `/bd help` | Shows help menu | None |
| `/bd reload` | Reloads the plugin configuration | bdcraft.admin |
| `/bd modules` | Lists all modules and their status | bdcraft.admin |
| `/bd enable <module>` | Enables a module | bdcraft.admin |
| `/bd disable <module>` | Disables a module | bdcraft.admin |

## Economy Module Commands

### General Economy

| Command | Description | Permission |
|---------|-------------|------------|
| `/balance` or `/bal` | Show your balance | bdcraft.economy.use |
| `/balance <player>` | Show another player's balance | bdcraft.economy.use |
| `/pay <player> <amount>` | Pay another player | bdcraft.economy.pay |
| `/eco give <player> <amount>` | Give money to a player | bdcraft.economy.admin |
| `/eco take <player> <amount>` | Take money from a player | bdcraft.economy.admin |
| `/eco set <player> <amount>` | Set a player's balance | bdcraft.economy.admin |
| `/eco reset <player>` | Reset a player's balance to default | bdcraft.economy.admin |

### Market Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdmarket check` | Visualize market boundaries with temporary wool blocks | bdcraft.market.use |
| `/bdmarket info` | Display information about the market you're standing in | bdcraft.market.use |
| `/bdmarket list` | List all markets you have founder or associate status in | bdcraft.market.use |

Note: Most market management is handled through the Market Owner villager GUI, which is accessed by right-clicking the Market Owner NPC.

### Auction Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdah` | Opens the auction house GUI | bdcraft.auction.use |
| `/bdah listings` | Shows your current auction listings | bdcraft.auction.list |
| `/bdah sell <price> [amount]` | Lists an item for sale | bdcraft.auction.sell |
| `/bdah cancel <listing_id>` | Cancels one of your listings | bdcraft.auction.cancel |

### Villager Trading System

The plugin enhances Minecraft's villager trading system. Players interact with custom villagers in-game to access specialized trades based on their progression level.

The villager system is primarily managed through configuration and doesn't rely on explicit commands for regular player interaction.

## Progression Module Commands

### Rank Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/rank` | Shows your current rank and progress | bdcraft.rank.use |
| `/rank info <rank>` | Shows information about a specific rank | bdcraft.rank.use |
| `/rank list` | Lists all available ranks | bdcraft.rank.use |
| `/rank set <player> <rank>` | Sets a player's rank | bdcraft.rank.admin |
| `/rank reset <player>` | Resets a player's rank | bdcraft.rank.admin |
| `/rank exp <player> <amount>` | Gives experience to a player | bdcraft.rank.admin |
| `/rankup` | Attempts to rank up to the next level | bdcraft.rank.use |

### Rebirth Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/rebirth` | Performs a rebirth (if eligible) | bdcraft.rebirth.use |
| `/rebirth info` | Shows your rebirth status and benefits | bdcraft.rebirth.use |
| `/rebirth top` | Shows the rebirth leaderboard | bdcraft.rebirth.use |
| `/rebirth reset <player>` | Resets a player's rebirth level | bdcraft.rebirth.admin |
| `/rebirth set <player> <level>` | Sets a player's rebirth level | bdcraft.rebirth.admin |
| `/rebirth benefits` | Lists all rebirth benefits | bdcraft.rebirth.use |

## Vital Module Commands

### Home Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/home` | Teleports to your default home | bdcraft.home.use |
| `/home <name>` | Teleports to a specific home | bdcraft.home.use |
| `/home set` | Sets your default home | bdcraft.home.use |
| `/home set <name>` | Sets a named home | bdcraft.home.use |
| `/home delete <name>` | Deletes a home | bdcraft.home.use |
| `/home list` | Lists all your homes | bdcraft.home.use |
| `/home info <name>` | Shows information about a home | bdcraft.home.use |
| `/home public <name>` | Makes a home public | bdcraft.home.use |
| `/home private <name>` | Makes a home private | bdcraft.home.use |
| `/home <player>:<name>` | Teleports to another player's public home | bdcraft.home.use |
| `/home admin` | Opens the home admin menu | bdcraft.home.admin |

### Teleport Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/tp <player>` | Teleports to a player | bdcraft.teleport.use |
| `/tp <player1> <player2>` | Teleports player1 to player2 | bdcraft.teleport.others |
| `/tpa <player>` | Sends a teleport request to a player | bdcraft.teleport.use |
| `/tpaccept` | Accepts a teleport request | bdcraft.teleport.use |
| `/tpdeny` | Denies a teleport request | bdcraft.teleport.use |
| `/tpahere <player>` | Requests a player to teleport to you | bdcraft.teleport.use |
| `/tpall` | Teleports all players to you | bdcraft.teleport.others |
| `/tppos <x> <y> <z>` | Teleports to coordinates | bdcraft.teleport.coord |
| `/back` | Teleports to your previous location | bdcraft.teleport.back |
| `/rtp` | Teleports to a random location | bdcraft.teleport.random |
| `/spawn` | Teleports to the spawn point | bdcraft.teleport.use |
| `/setspawn` | Sets the spawn point | bdcraft.teleport.admin |
| `/warp <name>` | Teleports to a warp point | bdcraft.teleport.warp |
| `/warp set <name>` | Creates a warp point | bdcraft.teleport.admin |
| `/warp delete <name>` | Deletes a warp point | bdcraft.teleport.admin |
| `/warp list` | Lists all available warps | bdcraft.teleport.use |

### Chat Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/msg <player> <message>` | Sends a private message | bdcraft.chat.use |
| `/reply <message>` | Replies to the last private message | bdcraft.chat.use |
| `/ignore <player>` | Ignores a player's messages | bdcraft.chat.use |
| `/unignore <player>` | Stops ignoring a player's messages | bdcraft.chat.use |
| `/channel <name>` | Switches to a chat channel | bdcraft.chat.channel |
| `/channel create <name>` | Creates a new chat channel | bdcraft.chat.admin |
| `/channel delete <name>` | Deletes a chat channel | bdcraft.chat.admin |
| `/channel invite <player> <channel>` | Invites a player to a channel | bdcraft.chat.channel |
| `/announce <message>` | Makes a server announcement | bdcraft.chat.announce |
| `/me <action>` | Performs an action in chat | bdcraft.chat.me |
| `/broadcast <message>` | Broadcasts a message to the entire server | bdcraft.chat.broadcast |
| `/nick <nickname>` | Sets your nickname | bdcraft.chat.nick |
| `/color <code>` | Sets your chat color | bdcraft.chat.color |

### Tab List Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/tablist reload` | Reloads the tab list configuration | bdcraft.tab.admin |
| `/tablist update` | Manually updates all players' tab lists | bdcraft.tab.admin |
| `/afk` | Toggles your AFK status | bdcraft.tab.afk |

## Command Aliases

Many commands have shorter aliases for convenience:

- `/balance` → `/bal`
- `/economy` → `/eco`
- `/auction` → `/ah`
- `/tpaccept` → `/tpa`
- `/message` → `/msg`, `/tell`, `/w`
- `/reply` → `/r`

## Command Arguments

Arguments in the command list are noted as follows:

- `<required>` - A required argument
- `[optional]` - An optional argument

## Command Cooldowns

Many commands have cooldowns to prevent abuse. These can be configured in the respective module configuration files.

Administrators with the appropriate bypass permissions can ignore these cooldowns.

## Next Steps

After familiarizing yourself with the commands, check out the individual module documentation for more detailed information on each feature:

- [Economy Module](economy/README.md)
- [Progression Module](progression/README.md)
- [Vital Module](vital/README.md)