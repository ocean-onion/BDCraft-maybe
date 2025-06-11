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
| `/bdmarket check` | `/market check` | Visualize market boundaries with temporary wool blocks | bdcraft.market.use |
| `/bdmarket info` | `/market info` | Display information about the market you're standing in | bdcraft.market.use |
| `/bdmarket list` | `/market list` | List all markets you own or have associate status in (maximum 5 markets per player) | bdcraft.market.use |

**Important Note:** Market management (creating markets, adding associates, upgrading markets, etc.) can ONLY be done through the Market Owner villager GUI, which is accessed by right-clicking the Market Owner NPC (CARTOGRAPHER with gold nametag). 

Only donors have access to a special command that lets them access the market GUI remotely:
| `/bdmarket` | `/market` | Opens the market management GUI (donor-only) | bdcraft.market.donor |

### Auction Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdauction` | `/auction`, `/ah`, `/bdah` | Opens the auction house GUI | bdcraft.auction.use |
| `/bdauction listings` | `/auction listings`, `/ah listings` | Shows your current auction listings | bdcraft.auction.list |
| `/bdauction sell <price> [amount]` | `/auction sell <price> [amount]`, `/ah sell <price> [amount]` | Lists an item for sale | bdcraft.auction.sell |
| `/bdauction cancel <listing_id>` | `/auction cancel <listing_id>`, `/ah cancel <listing_id>` | Cancels one of your listings | bdcraft.auction.cancel |
| `/bdauction search <query>` | `/auction search <query>`, `/ah search <query>` | Searches for items in the auction house | bdcraft.auction.use |
| `/bdauction history` | `/auction history`, `/ah history` | Shows your auction history | bdcraft.auction.use |
| `/bdauction featured` | `/auction featured`, `/ah featured` | Creates a featured auction (costs extra) | bdcraft.auction.featured |
| `/bdauction reload` | `/auction reload`, `/ah reload` | Reloads the auction configuration | bdcraft.auction.admin |
| `/bdauction clear <player>` | `/auction clear <player>`, `/ah clear <player>` | Clears a player's listings | bdcraft.auction.admin |

### Market and House Tokens

**Note:** BD Market Tokens and House Tokens are physical items that players craft and place, not a virtual currency. There are no commands to manage these tokens directly as they are regular in-game items.

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdadmin give <player> markettoken [amount]` | `/admin give <player> markettoken [amount]` | Gives market tokens to a player (admin) | bdcraft.admin |
| `/bdadmin give <player> housetoken [amount]` | `/admin give <player> housetoken [amount]` | Gives house tokens to a player (admin) | bdcraft.admin |

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
| `/bditemname <n>` | `/itemname <n>`, `/rename <n>` | Rename the item in your hand | bdcraft.vital.itemname |
| `/bditemlore <text>` | `/itemlore <text>` | Set lore text for the item in your hand | bdcraft.vital.itemlore |
| `/bditemdb` | `/itemdb`, `/idb` | Get information about the item in your hand | bdcraft.vital.itemdb |
| `/bdenchant <enchant> <level>` | `/enchant <enchant> <level>` | Enchant the item in your hand | bdcraft.vital.enchant |
| `/bdmore` | `/more`, `/stack` | Fills your hand item to maximum stack size | bdcraft.vital.more |
| `/bdkits` | `/kits` | Lists available kits | bdcraft.vital.kits |
| `/bdkit <n>` | `/kit <n>` | Gives you a kit | bdcraft.vital.kit |

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
| `/bdhome <n>` | `/home <n>` | Teleports to a specific home | bdcraft.home.use |
| `/bdhome set` | `/home set`, `/sethome` | Sets your default home | bdcraft.home.set |
| `/bdhome set <n>` | `/home set <n>`, `/sethome <name>` | Sets a named home | bdcraft.home.set |
| `/bdhome list` | `/home list`, `/homes` | Lists all your homes | bdcraft.home.list |
| `/bdhome delete <n>` | `/home delete <n>`, `/delhome <name>` | Deletes a home | bdcraft.home.delete |
| `/bdhome limit` | `/home limit` | Shows your home limit | bdcraft.home.limit |

### Teleport Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdteleport <player>` | `/teleport <player>`, `/tp <player>` | Teleports to a player | bdcraft.teleport.player |
| `/bdteleport <x> <y> <z>` | `/teleport <x> <y> <z>`, `/tp <x> <y> <z>` | Teleports to coordinates | bdcraft.teleport.location |
| `/bdteleport <player> <player>` | `/teleport <player> <player>`, `/tp <player> <player>` | Teleports a player to another player | bdcraft.teleport.others |
| `/bdtpa <player>` | `/tpa <player>` | Requests to teleport to a player | bdcraft.teleport.request |
| `/bdtpahere <player>` | `/tpahere <player>` | Requests a player to teleport to you | bdcraft.teleport.requesthere |
| `/bdtpaccept` | `/tpaccept` | Accepts a teleport request | bdcraft.teleport.accept |
| `/bdtpdeny` | `/tpdeny` | Denies a teleport request | bdcraft.teleport.deny |
| `/bdtpcancel` | `/tpcancel` | Cancels your pending teleport request | bdcraft.teleport.cancel |
| `/bdtoggletp` | `/toggletp`, `/tptoggle` | Toggles whether you receive teleport requests | bdcraft.teleport.toggle |
| `/bdback` | `/back` | Teleports to your previous location | bdcraft.teleport.back |
| `/bdrtp` | `/rtp`, `/wild` | Teleports to a random location | bdcraft.teleport.random |

### Warp Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdwarp <name>` | `/warp <name>` | Teleports to a warp location | bdcraft.warp.use |
| `/bdwarp list` | `/warp list`, `/warps` | Lists all available warps | bdcraft.warp.list |
| `/bdwarp set <name>` | `/warp set <name>`, `/setwarp <name>` | Creates a warp at your location | bdcraft.warp.set |
| `/bdwarp delete <name>` | `/warp delete <name>`, `/delwarp <name>` | Deletes a warp | bdcraft.warp.delete |
| `/bdwarp info <name>` | `/warp info <name>` | Shows information about a warp | bdcraft.warp.info |

### Spawn Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdspawn` | `/spawn` | Teleports to the server spawn | bdcraft.spawn.use |
| `/bdsetspawn` | `/setspawn` | Sets the server spawn location | bdcraft.spawn.set |

### Chat Commands

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdmsg <player> <message>` | `/msg <player> <message>`, `/w <player> <message>`, `/tell <player> <message>` | Sends a private message to a player | bdcraft.chat.msg |
| `/bdreply <message>` | `/reply <message>`, `/r <message>` | Replies to the last private message | bdcraft.chat.reply |
| `/bdignore <player>` | `/ignore <player>` | Ignores a player's messages | bdcraft.chat.ignore |
| `/bdunignore <player>` | `/unignore <player>` | Stops ignoring a player's messages | bdcraft.chat.unignore |
| `/bdmail send <player> <message>` | `/mail send <player> <message>` | Sends mail to a player | bdcraft.chat.mail |
| `/bdmail read` | `/mail read` | Reads your mail | bdcraft.chat.mail |
| `/bdmail clear` | `/mail clear` | Clears your mail | bdcraft.chat.mail |
| `/bdnick <nick>` | `/nick <nick>` | Sets your nickname | bdcraft.chat.nick |
| `/bdnick reset` | `/nick reset` | Removes your nickname | bdcraft.chat.nick |
| `/bdnick <player> <nick>` | `/nick <player> <nick>` | Sets another player's nickname | bdcraft.chat.nick.others |
| `/bdchatcolor <color>` | `/chatcolor <color>` | Sets your chat color | bdcraft.chat.color |

## Admin-Only Commands

These commands are only available to server administrators:

| Command | Aliases | Description | Permission |
|---------|---------|-------------|------------|
| `/bdadmin register <player>` | `/admin register <player>` | Registers a player for the plugin | bdcraft.admin |
| `/bdadmin unregister <player>` | `/admin unregister <player>` | Unregisters a player from the plugin | bdcraft.admin |
| `/bdadmin reset <player>` | `/admin reset <player>` | Resets a player's plugin data | bdcraft.admin |
| `/bdadmin reload all` | `/admin reload all` | Reloads all configuration files | bdcraft.admin |
| `/bdadmin migrate <source> <target>` | `/admin migrate <source> <target>` | Migrates data from another plugin | bdcraft.admin |
| `/bdadmin update` | `/admin update` | Checks for plugin updates | bdcraft.admin |
| `/bdadmin cleanup` | `/admin cleanup` | Cleans up old data | bdcraft.admin |
| `/bdadmin backup` | `/admin backup` | Creates a backup of plugin data | bdcraft.admin |
| `/bdadmin restore <backup>` | `/admin restore <backup>` | Restores from a backup | bdcraft.admin |