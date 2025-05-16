# Commands Reference

BDCraft provides a comprehensive set of commands for players and administrators. This guide lists all available commands organized by module, including all available aliases.

As a self-contained plugin, BDCraft offers both BD-prefixed commands (e.g., `/bdfly`) and standard versions (e.g., `/fly`) for compatibility. Both versions provide identical functionality.

## Core Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bd` | None | Displays plugin information | None |
| `/bd help` | None | Shows help menu | None |
| `/bd reload` | None | Reloads the plugin configuration | bdcraft.admin |
| `/bd modules` | None | Lists all modules and their status | bdcraft.admin |
| `/bd enable <module>` | None | Enables a module | bdcraft.admin |
| `/bd disable <module>` | None | Disables a module | bdcraft.admin |
| `/bdadmin` | `/admin` | Opens the admin control panel | bdcraft.admin |
| `/bdadmin verify` | `/admin verify` | Verifies the plugin integrity | bdcraft.admin |
| `/bdadmin debug` | `/admin debug` | Toggles debug mode | bdcraft.admin |
| `/bdadmin checkperm <player> <permission>` | `/admin checkperm <player> <permission>` | Checks if a player has a permission | bdcraft.admin |
| `/bdadmin import check` | `/admin import check` | Checks for data that can be imported | bdcraft.admin |
| `/bdadmin import run` | `/admin import run` | Runs the import process | bdcraft.admin |
| `/bdadmin player info <player>` | `/admin player info <player>` | Shows player information | bdcraft.admin |

## Economy Module Commands

### General Economy

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdbalance` | `/balance`, `/bal`, `/bdbal`, `/money` | Show your balance | bdcraft.economy.use |
| `/bdbalance <player>` | `/balance <player>`, `/bal <player>`, `/money <player>` | Show another player's balance | bdcraft.economy.balance.others |
| `/bdpay <player> <amount>` | `/pay <player> <amount>` | Pay another player | bdcraft.economy.pay |
| `/bdeconomy` | `/economy`, `/eco`, `/bdeco` | Opens the economy management panel | bdcraft.economy.admin |
| `/bdeco give <player> <amount>` | `/eco give <player> <amount>` | Give money to a player | bdcraft.economy.admin |
| `/bdeco take <player> <amount>` | `/eco take <player> <amount>` | Take money from a player | bdcraft.economy.admin |
| `/bdeco set <player> <amount>` | `/eco set <player> <amount>` | Set a player's balance | bdcraft.economy.admin |
| `/bdeco reset <player>` | `/eco reset <player>` | Reset a player's balance to default | bdcraft.economy.admin |
| `/bdeco top` | `/eco top`, `/baltop`, `/balancetop` | View top balances on the server | bdcraft.economy.top |
| `/bdeco reload` | `/eco reload` | Reloads the economy configuration | bdcraft.economy.admin |

### Market Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdmarket` | `/market`, `/bdm`, `/m` | Opens the market management menu | bdcraft.market.use |
| `/bdmarket check` | `/market check` | Visualize market boundaries with temporary wool blocks | bdcraft.market.use |
| `/bdmarket info` | `/market info` | Display information about the market you're standing in | bdcraft.market.use |
| `/bdmarket list` | `/market list` | List all markets you have founder or associate status in | bdcraft.market.use |
| `/bdmarket create <name>` | `/market create <name>` | Creates a new market at your location | bdcraft.market.founder |
| `/bdmarket delete <name>` | `/market delete <name>` | Deletes a market you own | bdcraft.market.founder |
| `/bdmarket staff add <player>` | `/market staff add <player>` | Adds a player as staff to your market | bdcraft.market.founder |
| `/bdmarket staff remove <player>` | `/market staff remove <player>` | Removes a player as staff from your market | bdcraft.market.founder |
| `/bdmarket teleport <name>` | `/market tp <name>`, `/market teleport <name>` | Teleport to a market | bdcraft.market.teleport |
| `/bdmarket reload` | `/market reload` | Reload market configuration | bdcraft.market.admin |

Note: Most market management can also be done through the Market Owner villager GUI, which is accessed by right-clicking the Market Owner NPC.

### Auction Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdauction` | `/auction`, `/ah`, `/bdah` | Opens the auction house GUI | bdcraft.auction.use |
| `/bdah listings` | `/ah listings` | Shows your current auction listings | bdcraft.auction.list |
| `/bdah sell <price> [amount]` | `/ah sell <price> [amount]` | Lists an item for sale | bdcraft.auction.sell |
| `/bdah cancel <listing_id>` | `/ah cancel <listing_id>` | Cancels one of your listings | bdcraft.auction.cancel |
| `/bdah search <query>` | `/ah search <query>` | Searches for items in the auction house | bdcraft.auction.use |
| `/bdah history` | `/ah history` | Shows your auction history | bdcraft.auction.use |
| `/bdah featured` | `/ah featured` | Creates a featured auction (costs extra) | bdcraft.auction.featured |
| `/bdah reload` | `/ah reload` | Reloads the auction configuration | bdcraft.auction.admin |
| `/bdah clear <player>` | `/ah clear <player>` | Clears a player's listings | bdcraft.auction.admin |

### Token System

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdtoken` | `/token`, `/bdtk`, `/tk` | View your token balance | bdcraft.token.use |
| `/bdtoken shop` | `/token shop` | Open the token shop | bdcraft.token.use |
| `/bdtoken send <player> <amount>` | `/token send <player> <amount>` | Send tokens to another player | bdcraft.token.send |
| `/bdtoken history` | `/token history` | View your token transaction history | bdcraft.token.use |
| `/bdtoken top` | `/token top` | View the token leaderboard | bdcraft.token.use |
| `/bdtoken buy <amount>` | `/token buy <amount>` | Purchase tokens with server currency | bdcraft.token.buy |
| `/bdtoken daily` | `/token daily` | Claim your daily token reward | bdcraft.token.daily |
| `/bdtoken rewards` | `/token rewards` | View available token rewards | bdcraft.token.use |
| `/bdtoken admin give <player> <amount>` | `/token admin give <player> <amount>` | Give tokens to a player (admin) | bdcraft.token.admin |
| `/bdtoken admin take <player> <amount>` | `/token admin take <player> <amount>` | Take tokens from a player (admin) | bdcraft.token.admin |
| `/bdtoken admin set <player> <amount>` | `/token admin set <player> <amount>` | Set a player's token balance (admin) | bdcraft.token.admin |

### Villager Trading System

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdvillager list` | `/villager list`, `/bdv list`, `/v list` | Lists all BDCraft villagers in the current world | bdcraft.villager.use |
| `/bdvillager info <id>` | `/villager info <id>` | Shows information about a specific villager | bdcraft.villager.use |
| `/bdvillager create <type>` | `/villager create <type>` | Creates a villager at your location | bdcraft.villager.admin |
| `/bdvillager remove <id>` | `/villager remove <id>` | Removes a BDCraft villager | bdcraft.villager.admin |
| `/bdvillager reload` | `/villager reload` | Reloads the villager configuration | bdcraft.villager.admin |
| `/bdvillager trades` | `/villager trades` | Lists available villager trades | bdcraft.villager.use |
| `/bdvillager restock <id>` | `/villager restock <id>` | Restocks a villager's trades | bdcraft.villager.admin |

## Progression Module Commands

### Rank Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdrank` | `/rank`, `/bdrk` | Shows your current rank and progress | bdcraft.rank.use |
| `/bdrank info <rank>` | `/rank info <rank>` | Shows information about a specific rank | bdcraft.rank.use |
| `/bdrank list` | `/rank list` | Lists all available ranks | bdcraft.rank.use |
| `/bdrank set <player> <rank>` | `/rank set <player> <rank>` | Sets a player's rank | bdcraft.rank.admin |
| `/bdrank reset <player>` | `/rank reset <player>` | Resets a player's rank | bdcraft.rank.admin |
| `/bdrank exp <player> <amount>` | `/rank exp <player> <amount>` | Gives experience to a player | bdcraft.rank.admin |
| `/bdrankup` | `/rankup` | Attempts to rank up to the next level | bdcraft.rank.use |
| `/bdrank progress` | `/rank progress` | Shows detailed progress to next rank | bdcraft.rank.use |
| `/bdrank benefits` | `/rank benefits` | Lists all benefits of your current rank | bdcraft.rank.use |
| `/bdrank reload` | `/rank reload` | Reloads the rank configuration | bdcraft.rank.admin |

### Rebirth Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdrebirth` | `/rebirth`, `/bdrb` | Performs a rebirth (if eligible) | bdcraft.rebirth.use |
| `/bdrebirth info` | `/rebirth info` | Shows your rebirth status and benefits | bdcraft.rebirth.use |
| `/bdrebirth top` | `/rebirth top` | Shows the rebirth leaderboard | bdcraft.rebirth.use |
| `/bdrebirth reset <player>` | `/rebirth reset <player>` | Resets a player's rebirth level | bdcraft.rebirth.admin |
| `/bdrebirth set <player> <level>` | `/rebirth set <player> <level>` | Sets a player's rebirth level | bdcraft.rebirth.admin |
| `/bdrebirth benefits` | `/rebirth benefits` | Lists all rebirth benefits | bdcraft.rebirth.use |
| `/bdrebirth requirements` | `/rebirth requirements` | Shows requirements for next rebirth | bdcraft.rebirth.use |
| `/bdrebirth confirm` | `/rebirth confirm` | Confirms your rebirth decision | bdcraft.rebirth.use |
| `/bdrebirth reload` | `/rebirth reload` | Reloads the rebirth configuration | bdcraft.rebirth.admin |

## Vital Module Commands

### Player Status Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdfly` | `/fly` | Toggles flight mode | bdcraft.vital.fly |
| `/bdfly <player>` | `/fly <player>` | Toggles flight mode for another player | bdcraft.vital.fly.others |
| `/bdfly speed <1-10>` | `/fly speed <1-10>` | Sets your flight speed | bdcraft.vital.fly.speed |
| `/bdheal` | `/heal` | Heals yourself to full health | bdcraft.vital.heal |
| `/bdheal <player>` | `/heal <player>` | Heals another player | bdcraft.vital.heal.others |
| `/bdfeed` | `/feed` | Restores your hunger | bdcraft.vital.feed |
| `/bdfeed <player>` | `/feed <player>` | Restores another player's hunger | bdcraft.vital.feed.others |
| `/bdgod` | `/god`, `/invincible` | Toggles god mode (invulnerability) | bdcraft.vital.god |
| `/bdspeed <1-10>` | `/speed <1-10>` | Sets your walking speed | bdcraft.vital.speed |
| `/bdvanish` | `/vanish`, `/v` | Makes you invisible to other players | bdcraft.vital.vanish |
| `/bdping` | `/ping` | Shows your ping | bdcraft.vital.ping |
| `/bdping <player>` | `/ping <player>` | Shows another player's ping | bdcraft.vital.ping.others |
| `/bdnear` | `/near` | Shows players near you | bdcraft.vital.near |
| `/bdseen <player>` | `/seen <player>` | Shows when a player was last online | bdcraft.vital.seen |
| `/bdsuicide` | `/suicide` | Kills yourself | bdcraft.vital.suicide |

### Item Management Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdrepair` | `/repair`, `/fix` | Repairs the item in your hand | bdcraft.vital.repair |
| `/bdrepair all` | `/repair all`, `/fix all` | Repairs all your items | bdcraft.vital.repair.all |
| `/bdhat` | `/hat` | Places the item in your hand as a hat | bdcraft.vital.hat |
| `/bditemname <name>` | `/itemname <name>`, `/rename <name>` | Rename the item in your hand | bdcraft.vital.itemname |
| `/bditemlore <text>` | `/itemlore <text>` | Set lore text for the item in your hand | bdcraft.vital.itemlore |
| `/bditemdb` | `/itemdb`, `/idb` | Get information about the item in your hand | bdcraft.vital.itemdb |
| `/bdenchant <enchant> <level>` | `/enchant <enchant> <level>` | Enchant the item in your hand | bdcraft.vital.enchant |
| `/bdmore` | `/more`, `/stack` | Fills your hand item to maximum stack size | bdcraft.vital.more |
| `/bdkits` | `/kits` | Lists available kits | bdcraft.vital.kits |
| `/bdkit <name>` | `/kit <name>` | Gives you a kit | bdcraft.vital.kit |

### Virtual Interfaces

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdanvil` | `/anvil` | Opens a portable anvil | bdcraft.vital.anvil |
| `/bdworkbench` | `/workbench`, `/wb`, `/craft` | Opens a portable crafting table | bdcraft.vital.workbench |
| `/bdenderchest` | `/enderchest`, `/ec` | Opens your ender chest | bdcraft.vital.enderchest |
| `/bdpotionbrew` | `/potionbrew`, `/brew` | Opens a brewing stand interface | bdcraft.vital.potionbrew |
| `/bdinvsee <player>` | `/invsee <player>`, `/inv <player>` | View and edit a player's inventory | bdcraft.vital.invsee |
| `/bdendersee <player>` | `/endersee <player>`, `/ecsee <player>` | View a player's enderchest | bdcraft.vital.endersee |

### Admin Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdgamemode <mode>` | `/gamemode <mode>`, `/gm <mode>` | Changes your game mode | bdcraft.vital.gamemode |
| `/bdgamemode <mode> <player>` | `/gamemode <mode> <player>` | Changes another player's game mode | bdcraft.vital.gamemode.others |
| `/bdgamemode creative` | `/gamemode creative`, `/gmc` | Sets to creative mode | bdcraft.vital.gamemode |
| `/bdgamemode survival` | `/gamemode survival`, `/gms` | Sets to survival mode | bdcraft.vital.gamemode |
| `/bdgamemode adventure` | `/gamemode adventure`, `/gma` | Sets to adventure mode | bdcraft.vital.gamemode |
| `/bdgamemode spectator` | `/gamemode spectator`, `/gmsp` | Sets to spectator mode | bdcraft.vital.gamemode |
| `/bdtime <time>` | `/time <time>` | Sets the time | bdcraft.vital.time |
| `/bdweather <weather>` | `/weather <weather>` | Sets the weather | bdcraft.vital.weather |
| `/bdkill <player>` | `/kill <player>` | Kills a player | bdcraft.vital.kill |
| `/bdclearinv <player>` | `/clearinv <player>`, `/ci <player>` | Clear a player's inventory | bdcraft.vital.clearinventory |
| `/bdmoreexp <amount>` | `/moreexp <amount>`, `/exp <amount>` | Give yourself experience | bdcraft.vital.moreexp |
| `/bdmoreexp <player> <amount>` | `/moreexp <player> <amount>` | Give a player experience | bdcraft.vital.moreexp.others |
| `/bdspawner <mob>` | `/spawner <mob>` | Change a spawner's type | bdcraft.vital.spawner |
| `/bdlightning` | `/lightning`, `/strike` | Strikes lightning at your location | bdcraft.vital.lightning |

### Moderation Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdkick <player> [reason]` | `/kick <player> [reason]` | Kicks a player from the server | bdcraft.vital.kick |
| `/bdban <player> [reason]` | `/ban <player> [reason]` | Bans a player from the server | bdcraft.vital.ban |
| `/bdunban <player>` | `/unban <player>`, `/pardon <player>` | Unbans a player | bdcraft.vital.unban |
| `/bdtempban <player> <time> [reason]` | `/tempban <player> <time> [reason]` | Temporarily bans a player | bdcraft.vital.tempban |
| `/bdbroadcast <message>` | `/broadcast <message>`, `/bc <message>` | Broadcasts a message to the entire server | bdcraft.vital.broadcast |
| `/bdmotd` | `/motd` | Shows the server message of the day | bdcraft.vital.motd |

### Home Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdhome` | `/home`, `/h` | Teleports to your default home | bdcraft.home.use |
| `/bdhome <name>` | `/home <name>` | Teleports to a specific home | bdcraft.home.use |
| `/bdhome set` | `/home set`, `/sethome` | Sets your default home | bdcraft.home.set |
| `/bdhome set <name>` | `/home set <name>`, `/sethome <name>` | Sets a named home | bdcraft.home.set |
| `/bdhome delete <name>` | `/home delete <name>`, `/delhome <name>` | Deletes a home | bdcraft.home.delete |
| `/bdhome list` | `/home list`, `/homes` | Lists all your homes | bdcraft.home.use |
| `/bdhome info <name>` | `/home info <name>` | Shows information about a home | bdcraft.home.use |
| `/bdhome public <name>` | `/home public <name>` | Makes a home public | bdcraft.home.public |
| `/bdhome private <name>` | `/home private <name>` | Makes a home private | bdcraft.home.use |
| `/bdhome visit <player>:<name>` | `/home visit <player>:<name>` | Teleports to another player's public home | bdcraft.home.visit |
| `/bdhome admin` | `/home admin` | Opens the home admin menu | bdcraft.home.admin |
| `/bdhome limit` | `/home limit` | Shows your home limit | bdcraft.home.use |
| `/bdhome clearbeds` | `/home clearbeds` | Clears all bed spawn points | bdcraft.home.admin |
| `/bdhome reload` | `/home reload` | Reloads the home configuration | bdcraft.home.admin |

### Teleport Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdtp <player>` | `/tp <player>`, `/teleport <player>` | Teleports to a player | bdcraft.teleport.tp |
| `/bdtp <player1> <player2>` | `/tp <player1> <player2>` | Teleports player1 to player2 | bdcraft.teleport.others |
| `/bdtpa <player>` | `/tpa <player>` | Sends a teleport request to a player | bdcraft.teleport.tpa |
| `/bdtpaccept` | `/tpaccept`, `/tpyes` | Accepts a teleport request | bdcraft.teleport.use |
| `/bdtpdeny` | `/tpdeny`, `/tpno` | Denies a teleport request | bdcraft.teleport.use |
| `/bdtpahere <player>` | `/tpahere <player>` | Requests a player to teleport to you | bdcraft.teleport.tpahere |
| `/bdtpall` | `/tpall` | Teleports all players to you | bdcraft.teleport.others |
| `/bdtppos <x> <y> <z>` | `/tppos <x> <y> <z>` | Teleports to coordinates | bdcraft.teleport.coord |
| `/bdback` | `/back` | Teleports to your previous location | bdcraft.teleport.back |
| `/bdrtp` | `/rtp`, `/randomtp` | Teleports to a random location | bdcraft.teleport.random |
| `/bdspawn` | `/spawn` | Teleports to the spawn point | bdcraft.teleport.spawn |
| `/bdsetspawn` | `/setspawn` | Sets the spawn point | bdcraft.teleport.admin |
| `/bdwarp <name>` | `/warp <name>` | Teleports to a warp point | bdcraft.teleport.warp |
| `/bdwarp set <name>` | `/warp set <name>`, `/setwarp <name>` | Creates a warp point | bdcraft.teleport.admin |
| `/bdwarp delete <name>` | `/warp delete <name>`, `/delwarp <name>` | Deletes a warp point | bdcraft.teleport.admin |
| `/bdwarp list` | `/warp list`, `/warps` | Lists all available warps | bdcraft.teleport.warp |
| `/bdtptoggle` | `/tptoggle` | Toggles teleport requests on/off | bdcraft.teleport.toggle |
| `/bdtop` | `/top` | Teleports to the highest block at your location | bdcraft.teleport.top |
| `/bdjump` | `/jump`, `/j` | Teleports you to the block you're looking at | bdcraft.teleport.jump |
| `/bdwild` | `/wild` | Teleports you to a random wilderness location | bdcraft.teleport.wild |
| `/bdtploc <world> <x> <y> <z>` | `/tploc <world> <x> <y> <z>` | Teleports to specific coordinates in a world | bdcraft.teleport.loc |

### Chat Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdmsg <player> <message>` | `/msg <player> <message>`, `/w <player> <message>`, `/tell <player> <message>` | Sends a private message | bdcraft.chat.msg |
| `/bdreply <message>` | `/reply <message>`, `/r <message>` | Replies to the last private message | bdcraft.chat.reply |
| `/bdignore <player>` | `/ignore <player>` | Ignores a player's messages | bdcraft.chat.ignore |
| `/bdunignore <player>` | `/unignore <player>` | Stops ignoring a player's messages | bdcraft.chat.ignore |
| `/bdchannel <name>` | `/channel <name>`, `/ch <name>` | Switches to a chat channel | bdcraft.chat.channel |
| `/bdchannel create <name>` | `/channel create <name>` | Creates a new chat channel | bdcraft.chat.admin |
| `/bdchannel delete <name>` | `/channel delete <name>` | Deletes a chat channel | bdcraft.chat.admin |
| `/bdchannel invite <player> <channel>` | `/channel invite <player> <channel>` | Invites a player to a channel | bdcraft.chat.channel |
| `/bdannounce <message>` | `/announce <message>` | Makes a server announcement | bdcraft.chat.announce |
| `/bdme <action>` | `/me <action>` | Performs an action in chat | bdcraft.chat.me |
| `/bdbroadcast <message>` | `/broadcast <message>`, `/bcast <message>` | Broadcasts a message to the entire server | bdcraft.chat.broadcast |
| `/bdnick <nickname>` | `/nick <nickname>` | Sets your nickname | bdcraft.chat.nick |
| `/bdcolor <code>` | `/color <code>` | Sets your chat color | bdcraft.chat.color |
| `/bdchat clear` | `/chat clear`, `/chatclear` | Clears your chat | bdcraft.chat.clear |
| `/bdchat mute <player> [duration]` | `/chat mute <player> [duration]`, `/mute <player> [duration]` | Mutes a player | bdcraft.chat.mute |
| `/bdchat unmute <player>` | `/chat unmute <player>`, `/unmute <player>` | Unmutes a player | bdcraft.chat.unmute |
| `/bdchat reload` | `/chat reload` | Reloads the chat configuration | bdcraft.chat.admin |
| `/bdchat list` | `/chat list` | Lists all chat channels | bdcraft.chat.use |
| `/bdchat format <format>` | `/chat format <format>` | Sets your chat format | bdcraft.chat.format |
| `/bdchatevent <message>` | `/chatevent <message>` | Creates a chat event | bdcraft.chat.event |
| `/bdchat spy` | `/chat spy`, `/socialspy` | Toggles social spy | bdcraft.chat.spy |
| `/bdchat toggle` | `/chat toggle` | Toggles chat on/off | bdcraft.chat.toggle |

### Tab List Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdtab reload` | `/tab reload` | Reloads the tab list configuration | bdcraft.tab.admin |
| `/bdtab update` | `/tab update` | Manually updates all players' tab lists | bdcraft.tab.admin |
| `/bdafk` | `/afk` | Toggles your AFK status | bdcraft.tab.afk |
| `/bdtab hide` | `/tab hide` | Hides you from the tab list | bdcraft.tab.hide |
| `/bdtab show` | `/tab show` | Shows you on the tab list | bdcraft.tab.hide |
| `/bdtab sort <type>` | `/tab sort <type>` | Sets the tab list sorting type | bdcraft.tab.admin |
| `/bdtab color <code>` | `/tab color <code>` | Sets your name color in the tab list | bdcraft.tab.color |
| `/bdtab prefix <text>` | `/tab prefix <text>` | Sets your prefix in the tab list | bdcraft.tab.prefix |
| `/bdtab suffix <text>` | `/tab suffix <text>` | Sets your suffix in the tab list | bdcraft.tab.suffix |
| `/bdtab header <text>` | `/tab header <text>` | Sets the tab list header | bdcraft.tab.admin |
| `/bdtab footer <text>` | `/tab footer <text>` | Sets the tab list footer | bdcraft.tab.admin |

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
- [Token System](tokens.md)