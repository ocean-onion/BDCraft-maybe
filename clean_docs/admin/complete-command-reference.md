# Complete Command Reference

This is the complete command reference for BDCraft plugin with all verified commands found in the actual documentation, their aliases, permissions, and detailed descriptions.

## Core Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bd` | - | None | Displays plugin information and version details. |
| `/bd help` | - | None | Shows help menu with available commands and basic usage information. |
| `/bd reload` | - | `bdcraft.admin` | Reloads the plugin configuration files without restarting the server. All module settings are refreshed. |
| `/bd modules` | - | `bdcraft.admin` | Lists all modules and their current status (enabled/disabled). Shows which features are active. |
| `/bd enable <module>` | - | `bdcraft.admin` | Enables a specific module. Module will start functioning immediately. |
| `/bd disable <module>` | - | `bdcraft.admin` | Disables a specific module. All features of that module will stop working. |
| `/bdadmin` | `/admin` | `bdcraft.admin` | Opens the admin control panel with management options. |
| `/bdadmin verify` | `/admin verify` | `bdcraft.admin` | Verifies the plugin integrity and checks for any configuration issues. |
| `/bdadmin debug` | `/admin debug` | `bdcraft.admin` | Toggles debug mode for detailed logging and troubleshooting. |
| `/bdadmin checkperm <player> <permission>` | `/admin checkperm <player> <permission>` | `bdcraft.admin` | Checks if a specific player has a particular permission. |
| `/bdadmin import check` | `/admin import check` | `bdcraft.admin` | Checks for data that can be imported from other plugins. |
| `/bdadmin import run` | `/admin import run` | `bdcraft.admin` | Runs the import process to migrate data from other plugins. |
| `/bdadmin player info <player>` | `/admin player info <player>` | `bdcraft.admin` | Shows comprehensive information about a player including stats, rank, balance. |
| `/bdadmin register <player>` | `/admin register <player>` | `bdcraft.admin` | Registers a player for the plugin. |
| `/bdadmin unregister <player>` | `/admin unregister <player>` | `bdcraft.admin` | Unregisters a player from the plugin. |

## Permission Management Commands

### GUI Permission Management

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdpermissions` | `/bdperms`, `/perms` | `bdcraft.permissions.admin` | Opens the main permission management GUI with player and group lists. |
| `/bdpermissions player <player>` | `/bdperms player <player>` | `bdcraft.permissions.admin` | Opens permission GUI for a specific player showing all their permissions and groups. |
| `/bdpermissions group <group>` | `/bdperms group <group>` | `bdcraft.permissions.admin` | Opens permission GUI for a specific group showing members and permissions. |

### Player Permission Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm set <player> <permission> <true/false>` | `/perm set <player> <permission> <true/false>` | `bdcraft.permissions.admin` | Sets a specific permission for a player. Overrides group permissions. |
| `/bdperm unset <player> <permission>` | `/perm unset <player> <permission>` | `bdcraft.permissions.admin` | Removes a permission override for a player, reverting to group default. |
| `/bdperm check <player> <permission>` | `/perm check <player> <permission>` | `bdcraft.permissions.admin` | Checks if a player has a specific permission and shows the source. |
| `/bdperm list <player>` | `/perm list <player>` | `bdcraft.permissions.admin` | Lists all permissions for a player including inherited and overridden permissions. |
| `/bdperm copy <source_player> <target_player>` | `/perm copy <source_player> <target_player>` | `bdcraft.permissions.admin` | Copies all permissions from one player to another. |
| `/bdperm reset <player>` | `/perm reset <player>` | `bdcraft.permissions.admin` | Resets a player's permissions to their group defaults, removing all overrides. |

### Group Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdgroup create <group> [parent]` | `/group create <group> [parent]` | `bdcraft.permissions.admin` | Creates a new permission group with optional parent inheritance. |
| `/bdgroup delete <group>` | `/group delete <group>` | `bdcraft.permissions.admin` | Deletes a permission group and removes all members from it. |
| `/bdgroup list` | `/group list` | `bdcraft.permissions.admin` | Lists all permission groups with member counts and basic information. |
| `/bdgroup info <group>` | `/group info <group>` | `bdcraft.permissions.admin` | Shows detailed group information including permissions and members. |
| `/bdgroup set <group> <permission> <true/false>` | `/group set <group> <permission> <true/false>` | `bdcraft.permissions.admin` | Sets a permission for a group, affecting all group members. |
| `/bdgroup unset <group> <permission>` | `/group unset <group> <permission>` | `bdcraft.permissions.admin` | Removes a permission from a group. |
| `/bdgroup add <player> <group>` | `/group add <player> <group>` | `bdcraft.permissions.admin` | Adds a player to a group, granting all group permissions. |
| `/bdgroup remove <player> <group>` | `/group remove <player> <group>` | `bdcraft.permissions.admin` | Removes a player from a group. |
| `/bdgroup setdefault <group>` | `/group setdefault <group>` | `bdcraft.permissions.admin` | Sets the default group for new players joining the server. |

### Advanced Permission Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm import <plugin>` | `/perm import <plugin>` | `bdcraft.permissions.admin` | Imports permissions from another permission plugin (LuckPerms, GroupManager, etc.). |
| `/bdperm export <format>` | `/perm export <format>` | `bdcraft.permissions.admin` | Exports current permissions to file in specified format (YAML, JSON). |
| `/bdperm reload` | `/perm reload` | `bdcraft.permissions.admin` | Reloads permissions from configuration files without server restart. |
| `/bdperm save` | `/perm save` | `bdcraft.permissions.admin` | Saves current permissions to configuration files. |
| `/bdperm backup` | `/perm backup` | `bdcraft.permissions.admin` | Creates a timestamped backup of current permission configuration. |
| `/bdperm restore <backup_name>` | `/perm restore <backup_name>` | `bdcraft.permissions.admin` | Restores permissions from a previously created backup. |
| `/bdperm debug <player>` | `/perm debug <player>` | `bdcraft.permissions.admin` | Shows detailed permission calculation for debugging permission issues. |
| `/bdperm validate` | `/perm validate` | `bdcraft.permissions.admin` | Validates permission configuration for errors and inconsistencies. |

### Bulk Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm bulk set <targets> <permission> <true/false>` | `/perm bulk set <targets> <permission> <value>` | `bdcraft.permissions.admin` | Sets permission for multiple players or groups separated by commas. |
| `/bdperm bulk add <players> <group>` | `/perm bulk add <players> <group>` | `bdcraft.permissions.admin` | Adds multiple players to a group using comma-separated player names. |
| `/bdperm bulk remove <players> <group>` | `/perm bulk remove <players> <group>` | `bdcraft.permissions.admin` | Removes multiple players from a group. |
| `/bdperm template apply <template> <targets>` | `/perm template apply <template> <targets>` | `bdcraft.permissions.admin` | Applies a predefined permission template to specified players or groups. |

## Economy Module Commands

### General Economy

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdbalance` | `/balance`, `/bal`, `/bdbal`, `/money` | `bdcraft.economy.use` | Show your current server currency balance. |
| `/bdbalance <player>` | `/balance <player>`, `/bal <player>`, `/money <player>` | `bdcraft.economy.balance.others` | Show another player's balance. Useful for checking payments. |
| `/bdpay <player> <amount>` | `/pay <player> <amount>` | `bdcraft.economy.pay` | Pay another player from your balance. Amount must be positive and you must have sufficient funds. |
| `/bdeconomy` | `/economy`, `/eco`, `/bdeco` | `bdcraft.economy.admin` | Opens the economy management panel for administrators. |
| `/bdeco give <player> <amount>` | `/eco give <player> <amount>` | `bdcraft.economy.admin` | Give money to a player. No limits for admin commands. |
| `/bdeco take <player> <amount>` | `/eco take <player> <amount>` | `bdcraft.economy.admin` | Take money from a player. Can result in negative balance. |
| `/bdeco set <player> <amount>` | `/eco set <player> <amount>` | `bdcraft.economy.admin` | Set a player's balance to a specific amount. |
| `/bdeco reset <player>` | `/eco reset <player>` | `bdcraft.economy.admin` | Reset a player's balance to the default starting amount. |
| `/bdeco top` | `/eco top`, `/baltop`, `/balancetop` | `bdcraft.economy.top` | View top balances on the server ranked by wealth. |
| `/bdeco reload` | `/eco reload` | `bdcraft.economy.admin` | Reloads the economy configuration without server restart. |

### Market Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdmarket check` | `/market check` | None (rank-based) | Visualize market boundaries with temporary wool blocks that disappear after viewing. |
| `/bdmarket info` | `/market info` | None (rank-based) | Display detailed information about the market you're currently standing in. |
| `/bdmarket list` | `/market list` | None (rank-based) | List all markets you own or have associate status in. Maximum 5 markets per player. |
| `/bdmarket` | `/market` | `bdcraft.market.donor` | Opens the market management GUI remotely. Donor-only feature for convenience. |

**Important Note:** Market creation, associate management, and upgrades can ONLY be done through the Market Owner villager GUI by right-clicking the Market Owner NPC (CARTOGRAPHER with gold nametag).

### Auction Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdauction` | `/auction`, `/ah`, `/bdah` | None (rank-based) | Opens the auction house GUI to browse and purchase items. |
| `/bdauction listings` | `/auction listings`, `/ah listings` | None (rank-based) | Shows your current auction listings with time remaining and prices. |
| `/bdauction sell <price> [amount]` | `/auction sell <price> [amount]`, `/ah sell <price> [amount]` | None (rank-based) | Lists an item for sale. Must hold item in hand. Only BD items accepted. |
| `/bdauction cancel <listing_id>` | `/auction cancel <listing_id>`, `/ah cancel <listing_id>` | None (rank-based) | Cancels one of your listings. Item returned immediately, fees not refunded. |
| `/bdauction search <query>` | `/auction search <query>`, `/ah search <query>` | None (rank-based) | Searches for items in the auction house by name or description. |
| `/bdauction history` | `/auction history`, `/ah history` | None (rank-based) | Shows your auction history including sales and purchases. |
| `/bdauction featured` | `/auction featured`, `/ah featured` | `bdcraft.auction.featured` | Creates a featured auction with priority display. Costs extra fees. |
| `/bdauction reload` | `/auction reload`, `/ah reload` | `bdcraft.auction.admin` | Reloads the auction configuration for administrators. |
| `/bdauction clear <player>` | `/auction clear <player>`, `/ah clear <player>` | `bdcraft.auction.admin` | Clears all listings from a specific player. |

### Token Management (Admin)

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin give <player> markettoken [amount]` | `/admin give <player> markettoken [amount]` | `bdcraft.admin` | Gives market tokens to a player. These are physical items. |
| `/bdadmin give <player> housetoken [amount]` | `/admin give <player> housetoken [amount]` | `bdcraft.admin` | Gives house tokens to a player. These are physical items. |

### Villager Trading System

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdvillager list` | `/villager list`, `/bdv list`, `/v list` | None (all players) | Lists all BDCraft villagers in the current world with their types and locations. |
| `/bdvillager info <id>` | `/villager info <id>` | None (all players) | Shows detailed information about a specific villager including available trades. |
| `/bdvillager create <type>` | `/villager create <type>` | `bdcraft.villager.admin` | Creates a villager at your location. Must specify type (dealer, collector, etc.). |
| `/bdvillager remove <id>` | `/villager remove <id>` | `bdcraft.villager.admin` | Removes a BDCraft villager permanently from the world. |
| `/bdvillager reload` | `/villager reload` | `bdcraft.villager.admin` | Reloads the villager configuration and updates all trades. |
| `/bdvillager trades` | `/villager trades` | None (all players) | Lists all available villager trades and their requirements. |
| `/bdvillager restock <id>` | `/villager restock <id>` | `bdcraft.villager.admin` | Restocks a villager's trades if they've run out of items. |

### Admin Villager Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin spawn dealer` | `/admin spawn dealer` | `bdcraft.admin` | Spawns a BD Dealer villager. |
| `/bdadmin spawn collector` | `/admin spawn collector` | `bdcraft.admin` | Spawns a collector villager. |
| `/bdadmin villager reload` | `/admin villager reload` | `bdcraft.admin` | Reloads villager configurations. |
| `/bdadmin villager remove <id>` | `/admin villager remove <id>` | `bdcraft.admin` | Removes a specific BD Villager. |

## Progression Module Commands

### Rank Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdrank` | `/rank`, `/bdr` | None (all players) | Shows your current rank and progress toward the next rank. |
| `/bdrank info <rank>` | `/rank info <rank>` | None (all players) | Shows detailed information about a specific rank including benefits and requirements. |
| `/bdrank list` | `/rank list` | None (all players) | Lists all available ranks from Newcomer to Agricultural Expert. |
| `/bdrank progress` | `/rank progress` | None (all players) | Shows detailed progress to next rank with exact numbers and requirements. |
| `/bdrank benefits` | `/rank benefits` | None (all players) | Lists all benefits of your current rank including bonuses and permissions. |
| `/bdrankup` | `/rankup` | None (all players) | Attempts to rank up to the next level if all requirements are met. |
| `/bdrank set <player> <rank>` | `/rank set <player> <rank>` | `bdcraft.rank.admin` | Sets a player's rank directly, bypassing requirements. |
| `/bdrank reset <player>` | `/rank reset <player>` | `bdcraft.rank.admin` | Resets a player's rank to Newcomer. |
| `/bdrank reload` | `/rank reload` | `bdcraft.rank.admin` | Reloads the rank configuration. |

### Admin Rank Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin rank set <player> <rank>` | `/admin rank set <player> <rank>` | `bdcraft.admin` | Set a player's rank. |
| `/bdadmin rank reset <player>` | `/admin rank reset <player>` | `bdcraft.admin` | Reset a player's rank to Newcomer. |
| `/bdadmin rank reload` | `/admin rank reload` | `bdcraft.admin` | Reload rank configuration. |

### Achievement Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdachievements` | - | None (all players) | Opens achievement GUI with all categories and progress tracking. |
| `/bdachievements <category>` | - | None (all players) | Opens specific achievement category (farming, trading, market, tools, economy, community). |
| `/bdstats` | - | None (all players) | Shows overall statistics and achievement completion rate with lifetime stats. |

### Admin Achievement Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin achievement list <player>` | `/admin achievement list <player>` | `bdcraft.admin` | View player's achievement status. |
| `/bdadmin achievement grant <player> <achievement>` | `/admin achievement grant <player> <achievement>` | `bdcraft.admin` | Manually grant achievement. |
| `/bdadmin achievement reset <player> <achievement>` | `/admin achievement reset <player> <achievement>` | `bdcraft.admin` | Reset specific achievement. |
| `/bdadmin achievement reload` | `/admin achievement reload` | `bdcraft.admin` | Reload achievement configuration. |

### Rebirth Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdrebirth` | `/rebirth`, `/bdrb` | None (Agricultural Expert+) | Check your rebirth status and available options. |
| `/bdrebirth info` | `/rebirth info` | None (all players) | View information about the rebirth system. |
| `/bdrebirth requirements` | `/rebirth requirements` | None (all players) | Check what you need for your next rebirth. |
| `/bdrebirth confirm` | `/rebirth confirm` | None (Agricultural Expert+) | Initiate the rebirth process when eligible. |
| `/bdrebirth top` | `/rebirth top` | None (all players) | Shows the rebirth leaderboard with top players by rebirth level. |
| `/bdrebirth benefits` | `/rebirth benefits` | None (all players) | Lists all rebirth benefits and how they stack with multiple rebirths. |
| `/bdrebirth reset <player>` | `/rebirth reset <player>` | `bdcraft.rebirth.admin` | Resets a player's rebirth level to 0. |
| `/bdrebirth set <player> <level>` | `/rebirth set <player> <level>` | `bdcraft.rebirth.admin` | Sets a player's rebirth level directly. |
| `/bdrebirth reload` | `/rebirth reload` | `bdcraft.rebirth.admin` | Reloads the rebirth configuration. |

### Admin Rebirth Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin rebirth set <player> <level>` | `/admin rebirth set <player> <level>` | `bdcraft.admin` | Set a player's rebirth level. |
| `/bdadmin rebirth reset <player>` | `/admin rebirth reset <player>` | `bdcraft.admin` | Reset a player's rebirth progress. |
| `/bdadmin rebirth reload` | `/admin rebirth reload` | `bdcraft.admin` | Reload rebirth configuration. |

## Vital Module Commands

### Player Status Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdfly` | `/fly` | `bdcraft.vital.fly` | Toggles flight mode for yourself. Creative-style flying in survival. |
| `/bdfly <player>` | `/fly <player>` | `bdcraft.vital.fly.others` | Toggles flight mode for another player. Admin command. |
| `/bdfly speed <1-10>` | `/fly speed <1-10>` | `bdcraft.vital.fly.speed` | Sets your flight speed from 1 (slow) to 10 (fast). |
| `/bdheal` | `/heal` | `bdcraft.vital.heal` | Heals yourself to full health and removes negative effects. |
| `/bdheal <player>` | `/heal <player>` | `bdcraft.vital.heal.others` | Heals another player to full health. |
| `/bdfeed` | `/feed` | `bdcraft.vital.feed` | Restores your hunger and saturation to maximum. |
| `/bdfeed <player>` | `/feed <player>` | `bdcraft.vital.feed.others` | Restores another player's hunger. |
| `/bdgod` | `/god`, `/invincible` | `bdcraft.vital.god` | Toggles god mode making you immune to all damage. |
| `/bdspeed <1-10>` | `/speed <1-10>` | `bdcraft.vital.speed` | Sets your walking/running speed. Default is 2. |
| `/bdvanish` | `/vanish`, `/v` | `bdcraft.vital.vanish` | Makes you invisible to other players and removes you from tab list. |
| `/bdping` | `/ping` | `bdcraft.vital.ping` | Shows your connection ping to the server in milliseconds. |
| `/bdping <player>` | `/ping <player>` | `bdcraft.vital.ping.others` | Shows another player's ping. Admin command. |
| `/bdnear` | `/near` | `bdcraft.vital.near` | Shows all players within a certain radius with distances. |
| `/bdseen <player>` | `/seen <player>` | `bdcraft.vital.seen` | Shows when a player was last online with exact time. |
| `/bdsuicide` | `/suicide` | `bdcraft.vital.suicide` | Kills yourself instantly. |

### Item Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdrepair` | `/repair`, `/fix` | `bdcraft.vital.repair` | Repairs the item in your hand to full durability. |
| `/bdrepair all` | `/repair all`, `/fix all` | `bdcraft.vital.repair.all` | Repairs all items in your inventory to full durability. |
| `/bdhat` | `/hat` | `bdcraft.vital.hat` | Places the item in your hand as a hat on your head. |
| `/bditemname <name>` | `/itemname <name>`, `/rename <name>` | `bdcraft.vital.itemname` | Renames the item in your hand to the specified name. |
| `/bditemlore <text>` | `/itemlore <text>` | `bdcraft.vital.itemlore` | Sets lore text for the item in your hand. |
| `/bditemdb` | `/itemdb`, `/idb` | `bdcraft.vital.itemdb` | Get detailed information about the item in your hand. |
| `/bdenchant <enchant> <level>` | `/enchant <enchant> <level>` | `bdcraft.vital.enchant` | Enchants the item in your hand with specified enchantment. |
| `/bdmore` | `/more`, `/stack` | `bdcraft.vital.more` | Fills your hand item to maximum stack size. |
| `/bdkits` | `/kits` | `bdcraft.vital.kits` | Lists all available kits you can claim. |
| `/bdkit <name>` | `/kit <name>` | `bdcraft.vital.kit` | Gives you the specified kit if eligible. |

### Virtual Interfaces

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdanvil` | `/anvil` | `bdcraft.vital.anvil` | Opens a portable anvil interface for repairing and renaming items. |
| `/bdworkbench` | `/workbench`, `/wb`, `/craft` | `bdcraft.vital.workbench` | Opens a portable 3x3 crafting table interface. |
| `/bdenderchest` | `/enderchest`, `/ec` | `bdcraft.vital.enderchest` | Opens your personal ender chest inventory. |
| `/bdpotionbrew` | `/potionbrew`, `/brew` | `bdcraft.vital.potionbrew` | Opens a brewing stand interface for making potions. |
| `/bdinvsee <player>` | `/invsee <player>`, `/inv <player>` | `bdcraft.vital.invsee` | View and edit another player's inventory. |
| `/bdendersee <player>` | `/endersee <player>`, `/ecsee <player>` | `bdcraft.vital.endersee` | View another player's ender chest inventory. |

### Admin Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdgamemode <mode>` | `/gamemode <mode>`, `/gm <mode>` | `bdcraft.vital.gamemode` | Changes your game mode to survival, creative, adventure, or spectator. |
| `/bdgamemode <mode> <player>` | `/gamemode <mode> <player>` | `bdcraft.vital.gamemode.others` | Changes another player's game mode. |
| `/bdgamemode creative` | `/gamemode creative`, `/gmc` | `bdcraft.vital.gamemode` | Quick command to set creative mode. |
| `/bdgamemode survival` | `/gamemode survival`, `/gms` | `bdcraft.vital.gamemode` | Quick command to set survival mode. |
| `/bdgamemode adventure` | `/gamemode adventure`, `/gma` | `bdcraft.vital.gamemode` | Quick command to set adventure mode. |
| `/bdgamemode spectator` | `/gamemode spectator`, `/gmsp` | `bdcraft.vital.gamemode` | Quick command to set spectator mode. |
| `/bdtime <time>` | `/time <time>` | `bdcraft.vital.time` | Sets the world time. Use values like 'day', 'night', or specific numbers. |
| `/bdweather <weather>` | `/weather <weather>` | `bdcraft.vital.weather` | Sets the weather to clear, rain, or storm. |
| `/bdkill <player>` | `/kill <player>` | `bdcraft.vital.kill` | Kills the specified player instantly. |
| `/bdclearinv <player>` | `/clearinv <player>`, `/ci <player>` | `bdcraft.vital.clearinventory` | Clears a player's entire inventory. |
| `/bdmoreexp <amount>` | `/moreexp <amount>`, `/exp <amount>` | `bdcraft.vital.moreexp` | Gives yourself the specified amount of experience points. |
| `/bdmoreexp <player> <amount>` | `/moreexp <player> <amount>` | `bdcraft.vital.moreexp.others` | Gives another player experience points. |
| `/bdspawner <mob>` | `/spawner <mob>` | `bdcraft.vital.spawner` | Changes the type of spawner you're looking at. |
| `/bdlightning` | `/lightning`, `/strike` | `bdcraft.vital.lightning` | Strikes lightning at your current location. |

### Moderation Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdkick <player> [reason]` | `/kick <player> [reason]` | `bdcraft.vital.kick` | Kicks a player from the server with optional reason. |
| `/bdban <player> [reason]` | `/ban <player> [reason]` | `bdcraft.vital.ban` | Permanently bans a player from the server. |
| `/bdunban <player>` | `/unban <player>`, `/pardon <player>` | `bdcraft.vital.unban` | Removes a ban from a player, allowing them to reconnect. |
| `/bdtempban <player> <time> [reason]` | `/tempban <player> <time> [reason]` | `bdcraft.vital.tempban` | Temporarily bans a player for specified duration (1d, 1h, etc.). |
| `/bdbroadcast <message>` | `/broadcast <message>`, `/bc <message>` | `bdcraft.vital.broadcast` | Broadcasts a message to all players on the server. |
| `/bdmotd` | `/motd` | `bdcraft.vital.motd` | Shows the server message of the day. |

### Tab List Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdtab reload` | `/tab reload` | `bdcraft.tab.admin` | Reloads the tab list configuration and updates display. |
| `/bdtab header <text>` | `/tab header <text>` | `bdcraft.tab.admin` | Sets a custom header text temporarily in the tab list. |
| `/bdtab footer <text>` | `/tab footer <text>` | `bdcraft.tab.admin` | Sets a custom footer text temporarily in the tab list. |
| `/bdtab reset` | `/tab reset` | `bdcraft.tab.admin` | Resets tab list to configuration defaults. |

### Home Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdhome` | `/home`, `/h` | `bdcraft.home.use` | Teleports to your default home location. |
| `/bdhome <name>` | `/home <name>` | `bdcraft.home.use` | Teleports to a specific named home. |
| `/bdhome set` | `/home set`, `/sethome` | `bdcraft.home.set` | Sets your default home at current location. |
| `/bdhome set <name>` | `/home set <name>`, `/sethome <name>` | `bdcraft.home.set` | Sets a named home at current location. |
| `/bdhome list` | `/home list`, `/homes` | `bdcraft.home.list` | Lists all your homes with coordinates and distances. |
| `/bdhome delete <name>` | `/home delete <name>`, `/delhome <name>` | `bdcraft.home.delete` | Deletes a specific home permanently. |
| `/bdhome limit` | `/home limit` | `bdcraft.home.limit` | Shows your current home limit and usage. |

### Teleportation Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdteleport <player>` | `/teleport <player>`, `/tp <player>` | `bdcraft.teleport.player` | Teleports to a player. Admin command for instant teleportation. |
| `/bdteleport <x> <y> <z>` | `/teleport <x> <y> <z>`, `/tp <x> <y> <z>` | `bdcraft.teleport.location` | Teleports to specific coordinates. Admin command for coordinate teleportation. |
| `/bdteleport <player> <player>` | `/teleport <player> <player>`, `/tp <player> <player>` | `bdcraft.teleport.others` | Teleports a player to another player. Admin command. |
| `/bdtpa <player>` | `/tpa <player>` | `bdcraft.teleport.request` | Requests to teleport to another player. They must accept the request. |
| `/bdtpahere <player>` | `/tpahere <player>` | `bdcraft.teleport.requesthere` | Requests another player to teleport to you. |
| `/bdtpaccept` | `/tpaccept` | `bdcraft.teleport.accept` | Accepts a pending teleportation request. |
| `/bdtpdeny` | `/tpdeny` | `bdcraft.teleport.deny` | Denies a pending teleportation request. |
| `/bdtpcancel` | `/tpcancel` | `bdcraft.teleport.cancel` | Cancels your pending teleport request. |
| `/bdtoggletp` | `/toggletp`, `/tptoggle` | `bdcraft.teleport.toggle` | Toggles whether you receive teleportation requests. |
| `/bdback` | `/back` | `bdcraft.teleport.back` | Teleports to your previous location before last teleport. |
| `/bdrtp` | `/rtp`, `/wild` | `bdcraft.teleport.random` | Teleports to a random location in the wilderness. |

### Warp Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdwarp <name>` | `/warp <name>` | `bdcraft.warp.use` | Teleports to a public warp location created by admins. |
| `/bdwarp list` | `/warp list`, `/warps` | `bdcraft.warp.list` | Lists all available public warps you can access. |
| `/bdwarp set <name>` | `/warp set <name>`, `/setwarp <name>` | `bdcraft.warp.set` | Creates a new warp at your current location. |
| `/bdwarp delete <name>` | `/warp delete <name>`, `/delwarp <name>` | `bdcraft.warp.delete` | Deletes an existing warp permanently. |
| `/bdwarp info <name>` | `/warp info <name>` | `bdcraft.warp.info` | Shows detailed information about a specific warp. |

### Spawn Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdspawn` | `/spawn` | `bdcraft.spawn.use` | Teleports to the server spawn location. Free for all players. |
| `/bdsetspawn` | `/setspawn` | `bdcraft.spawn.set` | Sets the server spawn location to your current position. |

### Chat Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/g <message>` | - | `bdcraft.chat.global` | Sends a message to global chat visible to all players server-wide. |
| `/l <message>` | - | `bdcraft.chat.local` | Sends a message to local chat within 100 block radius. |
| `/m <message>` | - | `bdcraft.chat.market` | Sends a message to market chat (market area only). |
| `/a <message>` | - | `bdcraft.chat.admin` | Sends a message to admin chat (admins only). |
| `/bdmsg <player> <message>` | `/msg <player> <message>`, `/tell <player> <message>`, `/w <player> <message>` | `bdcraft.chat.msg` | Sends a private message to another player. |
| `/bdreply <message>` | `/reply <message>`, `/r <message>` | `bdcraft.chat.reply` | Replies to the last private message you received. |
| `/bdignore <player>` | `/ignore <player>` | `bdcraft.chat.ignore` | Blocks private messages from a specific player. |
| `/bdunignore <player>` | `/unignore <player>` | `bdcraft.chat.unignore` | Unblocks a previously ignored player. |
| `/bdmail send <player> <message>` | `/mail send <player> <message>` | `bdcraft.chat.mail` | Sends offline mail to a player that they'll see when they log in. |
| `/bdmail read` | `/mail read` | `bdcraft.chat.mail` | Reads your unread mail messages. |
| `/bdmail clear` | `/mail clear` | `bdcraft.chat.mail` | Clears all your mail messages permanently. |
| `/bdmail storage` | `/mail storage`, `/bdm s`, `/mail s` | `bdcraft.chat.mail` | Access your item storage mailbox for expired auction items. |

### Chat Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/clearchat` | `/cc` | `bdcraft.chat.clear` | Clears your personal chat history. |
| `/clearchat all` | `/cc all` | `bdcraft.chat.clear.all` | Clears everyone's chat (admin only). |
| `/mutechat` | - | `bdcraft.chat.mute` | Temporarily disable global chat (admin only). |
| `/mute <player> [duration] [reason]` | - | `bdcraft.chat.mute.player` | Prevent a player from chatting. |
| `/unmute <player>` | - | `bdcraft.chat.unmute` | Remove a chat mute from a player. |
| `/bdnick <nick>` | `/nick <nick>` | `bdcraft.chat.nick` | Sets your nickname. |
| `/bdnick reset` | `/nick reset` | `bdcraft.chat.nick` | Removes your nickname. |
| `/bdnick <player> <nick>` | `/nick <player> <nick>` | `bdcraft.chat.nick.others` | Sets another player's nickname. |
| `/bdchatcolor <color>` | `/chatcolor <color>` | `bdcraft.chat.color` | Sets your chat color. |

### Additional Chat Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/ignorelist` | - | `bdcraft.chat.ignore` | View all players you're currently ignoring. |

## Quick Reference Tables

### Most Common Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdachievements` | View achievement progress GUI | `bdcraft.rank.use` |
| `/bdrank` | Check rank progress | `bdcraft.rank.use` |
| `/bdhome` | Teleport home | `bdcraft.home.use` |
| `/bdauction` | Open auction house | `bdcraft.auction.use` |
| `/bdbalance` | Check your money | `bdcraft.economy.use` |
| `/g <message>` | Global chat | `bdcraft.chat.global` |

### Essential Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bd reload` | Reload configuration | `bdcraft.admin` |
| `/bdperms` | Open permission GUI | `bdcraft.permissions.admin` |
| `/bdperm set <player> <permission> <true/false>` | Set player permission | `bdcraft.permissions.admin` |
| `/bdgroup add <player> <group>` | Add player to group | `bdcraft.permissions.admin` |
| `/bdvillager create <type>` | Spawn villagers | `bdcraft.villager.admin` |
| `/bdadmin player info <player>` | Check player details | `bdcraft.admin` |
| `/bdeco give <player> <amount>` | Give money to player | `bdcraft.economy.admin` |
| `/bdrank set <player> <rank>` | Set player rank | `bdcraft.rank.admin` |

### Emergency Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdspawn` | Return to spawn | `bdcraft.spawn.use` |
| `/bdheal` | Restore health | `bdcraft.vital.heal` |
| `/bdgod` | Toggle invulnerability | `bdcraft.vital.god` |
| `/bdfly` | Enable flight | `bdcraft.vital.fly` |
| `/bdback` | Return to previous location | `bdcraft.teleport.back` |

---

**This reference contains all verified commands found in BDCraft documentation with accurate aliases, permissions, and descriptions based on the actual plugin implementation.**