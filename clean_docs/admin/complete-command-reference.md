# Complete Command Reference

This comprehensive guide covers every command available in BDCraft with aliases, permissions, and detailed descriptions.

## Economy Module Commands

### Market Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdmarket create <name>` | `/market create <name>` | `bdcraft.market.create` | Creates a new market at your current location. Must be standing on a chest to set as market center. Market name must be unique on the server. Creates a 50-block radius trading area initially and automatically spawns initial BD villagers (1 Dealer, 1 Collector). Costs server currency based on configuration. |

| `/bdmarket info [name]` | `/market info [name]` | `bdcraft.market.use` | Displays detailed information about a market. Use without name to show info for market you're standing in, or specify name for specific market. Shows market level, upgrade costs, benefits, all associates and their permissions, total trades completed, reputation, and villager count and types present. |
| `/bdmarket list` | `/market list` | `bdcraft.market.use` | Lists all markets on the server with basic information. Shows market name, owner, level, location, and distance from your current position. Indicates if you have associate permissions. Markets sorted by distance from player. |
| `/bdmarket upgrade` | `/market upgrade` | `bdcraft.market.upgrade` | Upgrades your market to the next level. Must be market owner and standing in market area. Requires sufficient server currency and emeralds. Each level increases radius by 10 blocks (max 100 blocks at level 5). Provides 5% crop growth speed bonus per level and unlocks additional villager spawning slots. |
| `/bdmarket associate add <player>` | `/market associate add <player>` | `bdcraft.market.associate` | Adds a player as an associate to your market. Must be market owner. Associates can trade with villagers but cannot modify market. No limit on number of associates. Associates contribute to market trading volume and reputation. |
| `/bdmarket associate remove <player>` | `/market associate remove <player>` | `bdcraft.market.associate` | Removes an associate from your market. Must be market owner. Immediately revokes all market privileges for that player. Player keeps any items already obtained from trading. |
| `/bdmarket tp <name>` | `/market tp <name>` | `bdcraft.market.donor` | Teleports to a market (donors only). Donor-only feature for remote market access. Must have visited the market before or be an associate. Subject to teleportation cooldowns and delays. Cannot teleport to private markets without permission. |
| `/bdmarket delete` | `/market delete` | `bdcraft.market.delete` | Permanently deletes your market. Must be market owner and confirm deletion. Removes all villagers and market data permanently. Associates lose access immediately. Cannot be undone - no refunds provided. |

### Auction Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdauction` | `/auction`, `/ah` | `bdcraft.auction.use` | Opens the auction house GUI. Displays all active listings with prices and time remaining. Click items to purchase or view details. Search and filter functionality available. Shows your own listings separately. |
| `/bdauction sell <price>` | `/auction sell <price>`, `/ah sell <price>` | `bdcraft.auction.sell` | Lists the item in your hand for sale. Must hold a BD item (crops, tools, seeds). Regular Minecraft items are rejected. Price must be between 1 and server maximum. Listing fees apply based on rank (5% for Newcomer, 3% for Agricultural Expert). Listings expire after 3-7 days depending on rank. |
| `/bdauction cancel <id>` | `/auction cancel <id>`, `/ah cancel <id>` | `bdcraft.auction.cancel` | Cancels one of your auction listings. Can only cancel your own listings. Item is returned to your inventory immediately. Listing fees are not refunded. Cannot cancel if someone is actively purchasing. |
| `/bdauction list` | `/auction list`, `/ah list` | `bdcraft.auction.list` | Shows all your active auction listings. Displays listing ID, item, price, and time remaining. Shows total listing fees paid. Indicates which listings have received bids or interest. |

### Villager Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdvillager spawn <type>` | `/villager spawn <type>` | `bdcraft.villager.admin` | Spawns a BD villager at your location. Must be within a market area to spawn. Villager types: dealer, collector, seasonal_spring, seasonal_summer, seasonal_fall, seasonal_winter. Spawned villagers are permanent until manually removed. Each market has a limit on villager count based on level. |
| `/bdvillager remove` | `/villager remove` | `bdcraft.villager.admin` | Removes the villager you're looking at. Must be looking directly at a BD villager. Removes villager permanently from the market. Any active trades are cancelled. Cannot remove villagers during active trading sessions. |
| `/bdvillager list` | `/villager list` | `bdcraft.villager.use` | Lists all BD villagers in the current market. Shows villager type, location, and trade counts. Indicates which villagers are currently busy. Displays reputation bonuses for each villager type. |
| `/bdvillager reload` | `/villager reload` | `bdcraft.villager.admin` | Reloads villager configurations and trades. Updates all villager trades based on current configuration. Applies new pricing and item availability. Does not affect villagers mid-trade. |

## Progression Module Commands

### Rank Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdrank` | `/rank`, `/bdrk` | `bdcraft.rank.use` | Displays your current rank and progress toward next rank. Shows current rank and benefits, lists requirements for next rank advancement, displays progress toward each requirement (crops harvested, trades completed, etc.), and shows currency cost for next rank purchase. |
| `/bdrank info <rank>` | `/rank info <rank>` | `bdcraft.rank.use` | Shows detailed information about a specific rank. Lists all benefits and bonuses for the specified rank, shows exact requirements to achieve that rank, displays currency cost and any special prerequisites, and compares benefits to your current rank. |
| `/bdrank list` | `/rank list` | `bdcraft.rank.use` | Lists all available ranks with basic information. Shows all 5 ranks from Newcomer to Agricultural Expert, indicates which ranks you've achieved, shows currency costs for each rank, and highlights your current rank. |
| `/bdrank progress` | `/rank progress` | `bdcraft.rank.progress` | Shows detailed progress toward your next rank. Detailed breakdown of each requirement, shows exact numbers (e.g., "247/500 crops harvested"), indicates which requirements are already met, and estimates time to completion based on recent activity. |
| `/bdrank benefits` | `/rank benefits` | `bdcraft.rank.benefits` | Lists all benefits of your current rank. Shows harvest yield bonuses, lists auction fee reductions, displays growth speed improvements, and shows any special permissions or abilities. |
| `/bdrankup` | `/rankup` | `bdcraft.rank.use` | Attempts to purchase the next rank if requirements are met. Checks all requirements automatically, deducts currency cost if successful, applies new rank benefits immediately, and updates chat prefix and permissions. |

### Achievement Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdachievements` | - | `bdcraft.rank.use` | Opens the achievement GUI showing all categories and progress. Interactive GUI with multiple category tabs, shows progress bars for incomplete achievements, green checkmarks for completed achievements, click achievements to see rewards and descriptions, overall completion percentage displayed. |
| `/bdachievements <category>` | - | `bdcraft.rank.use` | Opens achievement GUI focused on a specific category. Available categories: farming, trading, market, tools, economy, community. Shows only achievements in that category, detailed progress tracking for each achievement, related achievements are grouped together. |
| `/bdstats` | - | `bdcraft.rank.use` | Shows your overall statistics and achievement completion rate. Total achievements completed vs. available, completion percentage for each category, lifetime statistics (total crops harvested, trades completed, etc.), rare achievement highlights. |

### Rebirth Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdrebirth` | `/rebirth`, `/bdrb` | `bdcraft.rebirth.use` | Shows rebirth information and requirements. Shows current rebirth level (if any), lists requirements for next rebirth, explains benefits of rebirth system, shows what will be reset vs. what persists. |
| `/bdrebirth info` | `/rebirth info` | `bdcraft.rebirth.use` | Shows detailed rebirth status and benefits. Current rebirth bonuses active, permanent benefits gained from previous rebirths, cumulative effect calculations, next rebirth requirements and benefits. |
| `/bdrebirth requirements` | `/rebirth requirements` | `bdcraft.rebirth.use` | Shows exact requirements for next rebirth. Must be Agricultural Expert rank, currency requirements for rebirth, special achievement requirements, warning about what progress will be reset. |
| `/bdrebirth confirm` | `/rebirth confirm` | `bdcraft.rebirth.use` | Confirms and performs rebirth after meeting requirements. Final confirmation prompt with warnings, resets rank to Newcomer but keeps rebirth bonuses, maintains achievements and permanent progress, applies new rebirth level benefits immediately. |
| `/bdrebirth top` | `/rebirth top` | `bdcraft.rebirth.use` | Shows the rebirth leaderboard. Lists top players by rebirth level, shows their current rank and achievements, displays rebirth dates and progression speed. |
| `/bdrebirth benefits` | `/rebirth benefits` | `bdcraft.rebirth.use` | Lists all rebirth benefits by level. Shows benefits for each rebirth level, cumulative bonuses and how they stack, exclusive features unlocked at higher rebirth levels. |

## Vital Module Commands

### Home Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdhome` | `/home`, `/h` | `bdcraft.home.use` | Teleports to your default home. Teleports to home named "home" or your first set home. Subject to teleportation delay based on rank. Cannot teleport if recently in combat. Displays teleportation countdown. |
| `/bdhome <name>` | `/home <name>` | `bdcraft.home.use` | Teleports to a specific named home. Case-sensitive home names. Must be a home you own. Same teleportation rules as default home. Shows location before teleporting. |
| `/bdhome set [name]` | `/home set [name]`, `/sethome [name]` | `bdcraft.home.set` | Sets a home at your current location. Limited by rank-based home limits. Overwrites existing home with same name. Cannot set homes in other players' claimed areas. Shows coordinates where home was set. |
| `/bdhome list` | `/home list`, `/homes` | `bdcraft.home.list` | Lists all your homes with coordinates. Shows home name, world, and coordinates. Indicates which is your default home. Shows distance from current location. Displays how many homes you can still set. |
| `/bdhome delete <name>` | `/home delete <name>`, `/delhome <name>` | `bdcraft.home.delete` | Deletes a specific home. Permanently removes the home. Cannot be undone. Frees up a home slot for new homes. Confirmation required for default home deletion. |
| `/bdhome limit` | `/home limit` | `bdcraft.home.limit` | Shows your current home limit and usage. Shows homes used vs. maximum allowed. Explains how rank affects home limits. Lists requirements to increase home limit. |

### Teleportation Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdtp <player>` | `/tp <player>` | `bdcraft.teleport.others` | Teleports directly to another player. Admin-only command for instant teleportation. No delay or restrictions. Works across worlds. Target player is notified of teleportation. |
| `/bdtp <x> <y> <z>` | `/tp <x> <y> <z>` | `bdcraft.teleport.location` | Teleports to specific coordinates. Admin-only command for coordinate teleportation. Uses current world unless world specified. No safety checks - can teleport into walls or lava. Coordinates must be within world boundaries. |
| `/bdtpa <player>` | `/tpa <player>` | `bdcraft.teleport.request` | Requests to teleport to another player. Sends teleportation request that can be accepted/denied. Request expires after 60 seconds. Subject to cooldowns between requests. Cannot request to players who have blocked requests. |
| `/bdtpahere <player>` | `/tpahere <player>` | `bdcraft.teleport.requesthere` | Requests another player to teleport to you. Requests target player to come to your location. Same rules as tpa but reversed direction. Useful for bringing players to your base or market. |
| `/bdtpaccept [player]` | `/tpaccept [player]`, `/tpyes [player]` | `bdcraft.teleport.request` | Accepts a pending teleportation request. Must have pending request to accept. Teleportation begins immediately after acceptance. Both players are notified of acceptance. Use without player name to accept most recent request. |
| `/bdtpdeny [player]` | `/tpdeny [player]`, `/tpno [player]` | `bdcraft.teleport.request` | Denies a pending teleportation request. Politely declines teleportation request. Requesting player is notified of denial. Does not block future requests from that player. Use without player name to deny most recent request. |
| `/bdback` | `/back` | `bdcraft.teleport.back` | Teleports to your previous location. Returns to location before last teleportation. Works with homes, warps, and tp commands. Limited number of previous locations stored. Subject to same delays as other teleportation. |
| `/bdspawn` | `/spawn` | `bdcraft.spawn.use` | Teleports to the server spawn point. Teleports to server's designated spawn location. Free teleportation available to all players. Subject to teleportation delays. Safe fallback when lost or in danger. |

### Chat Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/g <message>` | - | `bdcraft.chat.global` | Sends a message to global chat (server-wide). Visible to all players on the server. Default chat channel for most communication. Shows your rank prefix before your name. Subject to chat cooldowns and filters. |
| `/l <message>` | - | `bdcraft.chat.local` | Sends a message to local chat (100 block radius). Only visible to players within 100 blocks. Useful for location-specific communication. Shows distance indicators for recipients. Good for coordinating local activities. |
| `/m <message>` | - | `bdcraft.chat.market` | Sends a message to market chat (market area only). Only visible to players in the same market area. Ideal for coordinating trades and market activities. Includes market name in message format. Must be standing in a market to use. |
| `/a <message>` | - | `bdcraft.chat.admin` | Sends a message to admin chat (staff only). Only visible to players with admin chat permission. Used for staff coordination and moderation. Bypasses all chat cooldowns and filters. Shows across all worlds and distances. |
| `/bdmsg <player> <message>` | `/msg <player> <message>`, `/w <player> <message>`, `/tell <player> <message>` | `bdcraft.chat.msg` | Sends a private message to another player. Private one-on-one communication. Both players see the message. Recipient can reply using /bdreply. Ignores normal chat channels and distance. |
| `/bdreply <message>` | `/reply <message>`, `/r <message>` | `bdcraft.chat.reply` | Replies to the last private message received. Quick way to respond to private messages. Sends message to whoever last messaged you. Maintains conversation flow. Shows who you're replying to. |
| `/bdmail send <player> <message>` | `/mail send <player> <message>` | `bdcraft.chat.mail` | Sends mail to a player (offline messaging). Messages delivered when recipient logs in. Useful for communicating with offline players. Mail persists until read or deleted. Limited number of messages per player. |
| `/bdmail read` | `/mail read` | `bdcraft.chat.mail` | Reads your unread mail messages. Shows all unread messages with sender and timestamp. Messages marked as read after viewing. Can view multiple messages at once. Shows mail from newest to oldest. |
| `/bdmail clear` | `/mail clear` | `bdcraft.chat.mail` | Deletes all your mail messages. Permanently removes all mail (read and unread). Cannot be undone. Frees up mail storage space. Confirmation required. |
| `/bdmail storage` | `/mail storage`, `/bdm s`, `/mail s` | `bdcraft.chat.mail` | Access item storage mailbox for expired auction items. Opens inventory GUI with expired auction items. Items from cancelled or expired listings. Limited storage time before permanent loss. Separate from text mail system. |
| `/bdignore <player>` | `/ignore <player>` | `bdcraft.chat.ignore` | Blocks all messages from a specific player. Blocks private messages, not public chat. Prevents teleportation requests from that player. Player is not notified they are being ignored. Can ignore multiple players. |
| `/bdunignore <player>` | `/unignore <player>` | `bdcraft.chat.unignore` | Stops ignoring a previously ignored player. Removes player from ignore list. They can message and send requests again. Does not notify player they were unignored. Takes effect immediately. |

### Player Status Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdfly` | `/fly` | `bdcraft.vital.fly` | Toggles flight mode for yourself. Enables creative-style flying in survival mode. Toggles on/off with each use. Flight persists until manually disabled or logout. Speed can be adjusted with additional commands. |
| `/bdfly <player>` | `/fly <player>` | `bdcraft.vital.fly.others` | Toggles flight mode for another player. Admin command to control other players' flight. Target player is notified of flight status change. Useful for giving temporary flight for building. Can be used to disable flight as punishment. |
| `/bdfly speed <1-10>` | `/fly speed <1-10>` | `bdcraft.vital.fly.speed` | Sets your flight speed. Speed range from 1 (very slow) to 10 (very fast). Default speed is typically 5. Higher speeds use more server resources. Speed persists until changed or logout. |
| `/bdheal` | `/heal` | `bdcraft.vital.heal` | Instantly heals yourself to full health. Restores health to maximum (20 hearts). Removes all negative potion effects. Does not affect hunger or experience. Subject to cooldowns to prevent abuse. |
| `/bdheal <player>` | `/heal <player>` | `bdcraft.vital.heal.others` | Heals another player to full health. Admin command to heal other players. Target player is notified of healing. Useful for helping players in emergencies. No cooldown for admin usage. |
| `/bdfeed` | `/feed` | `bdcraft.vital.feed` | Restores your hunger to full. Sets hunger to maximum (20 drumsticks). Sets saturation to maximum for slower hunger loss. Does not affect health or experience. Subject to cooldowns. |
| `/bdfeed <player>` | `/feed <player>` | `bdcraft.vital.feed.others` | Feeds another player. Admin command to feed other players. Target player is notified. Useful for preventing starvation. No cooldown for admin usage. |
| `/bdgod` | `/god`, `/invincible` | `bdcraft.vital.god` | Toggles invulnerability (god mode). Makes you immune to all damage sources. Toggles on/off with each use. Includes fall damage, drowning, lava, etc. Visual indicator shows god mode status. |
| `/bdspeed <1-10>` | `/speed <1-10>` | `bdcraft.vital.speed` | Sets your walking/running speed. Speed range from 1 (slow) to 10 (very fast). Default Minecraft speed is 2. Affects both walking and sprinting. Speed persists until changed or logout. |
| `/bdvanish` | `/vanish`, `/v` | `bdcraft.vital.vanish` | Makes you invisible to other players. Completely hides you from other players. Removes you from tab list and player counts. Prevents players from seeing your chat messages. Useful for moderation and admin tasks. |
| `/bdping` | `/ping` | `bdcraft.vital.ping` | Shows your connection ping to the server. Displays latency in milliseconds. Lower numbers indicate better connection. Useful for troubleshooting connection issues. Updates in real-time. |
| `/bdping <player>` | `/ping <player>` | `bdcraft.vital.ping.others` | Shows another player's ping. Admin command to check other players' connections. Useful for identifying lag sources. Can help determine if issues are server or client-side. |
| `/bdnear` | `/near` | `bdcraft.vital.near` | Shows all players within a certain radius. Lists players within default radius (usually 100 blocks). Shows distance and direction to each player. Excludes vanished players (unless you're admin). Useful for finding nearby players. |
| `/bdseen <player>` | `/seen <player>` | `bdcraft.vital.seen` | Shows when a player was last online. Shows exact date and time of last logout. Indicates if player is currently online. Shows total playtime if configured. Works with offline players. |

### World Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdtime <time>` | `/time <time>` | `bdcraft.vital.time` | Sets the world time. Affects all players in the current world. Time values: 0=dawn, 6000=noon, 12000=dusk, 18000=midnight. Changes take effect immediately. Does not affect sleep schedules. Use 'day', 'night', or specific time values. |
| `/bdweather <weather>` | `/weather <weather>` | `bdcraft.vital.weather` | Sets the weather in the current world. Affects entire world immediately. Weather persists for natural duration unless changed again. Storm weather can cause lightning strikes. Useful for events or ambiance. Options: clear, rain, storm. |
| `/bdgamemode <mode> [player]` | `/gamemode <mode> [player]`, `/gm <mode> [player]` | `bdcraft.vital.gamemode` | Changes gamemode for yourself or another player. Modes: survival, creative, adventure, spectator. Flying ability automatically adjusted based on mode. Inventory preserved when switching modes. Target player notified of gamemode change. |

### Moderation Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdkick <player> [reason]` | `/kick <player> [reason]` | `bdcraft.vital.kick` | Kicks a player from the server. Immediately disconnects player from server. Player can reconnect unless banned. Reason shown to kicked player and logged. Other players notified of kick. |
| `/bdban <player> [reason]` | `/ban <player> [reason]` | `bdcraft.vital.ban` | Permanently bans a player from the server. Prevents player from connecting to server. Ban persists until manually removed. Reason logged and shown to player if they try to connect. IP-based banning available with additional configuration. |
| `/bdunban <player>` | `/unban <player>`, `/pardon <player>` | `bdcraft.vital.unban` | Removes a ban from a player. Allows previously banned player to reconnect. Takes effect immediately. Ban removal is logged for admin records. Player can connect as soon as command is executed. |
| `/bdtempban <player> <duration> [reason]` | `/tempban <player> <duration> [reason]` | `bdcraft.vital.tempban` | Temporarily bans a player for a specified time. Duration formats: 1m (minute), 1h (hour), 1d (day), 1w (week). Player automatically unbanned when time expires. Reason shown to player during ban period. Remaining time displayed if player tries to connect. |
| `/mute <player> [duration] [reason]` | - | `bdcraft.chat.mute.player` | Prevents a player from chatting. Player can still see chat but cannot send messages. Duration optional - permanent if not specified. Prevents all chat types (global, local, private). Muted player notified of mute and reason. |
| `/unmute <player>` | - | `bdcraft.chat.unmute` | Removes a chat mute from a player. Restores full chat privileges immediately. Player notified they can chat again. Mute removal logged for admin records. |
| `/bdbroadcast <message>` | `/broadcast <message>`, `/bc <message>` | `bdcraft.vital.broadcast` | Sends a server-wide announcement. Message sent to all online players. Appears in special broadcast format with colors. Bypasses all chat channels and ignore lists. Used for important server announcements. |

### Administrative Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdadmin reload` | `/admin reload` | `bdcraft.admin` | Reloads all plugin configuration files. Reloads all module configurations without restart. Players remain connected during reload. New settings take effect immediately. Some changes may require restart for full effect. |
| `/bdadmin save` | `/admin save` | `bdcraft.admin` | Forces save of all player data to disk. Immediately writes all data to database/files. Useful before server maintenance or backup. Includes player balances, ranks, achievements, homes. Progress shown during save operation. |
| `/bdadmin backup` | `/admin backup` | `bdcraft.admin` | Creates a backup of all plugin data. Creates timestamped backup of all plugin data. Includes configuration files and player data. Backups stored in designated backup directory. Can be restored manually if needed. |
| `/bdadmin debug` | `/admin debug` | `bdcraft.admin` | Toggles debug mode for detailed logging. Enables verbose logging for troubleshooting. Shows detailed information about plugin operations. Increases log file size significantly. Should be disabled after debugging to save space. |
| `/bdadmin player info <player>` | `/admin player info <player>` | `bdcraft.admin` | Shows comprehensive information about a player. Shows rank, achievements, balances, and statistics. Displays homes, market ownership, and trading history. Includes login/logout times and playtime. Useful for player support and administration. |
| `/bdadmin economy give <player> <amount>` | `/admin eco give <player> <amount>` | `bdcraft.admin` | Gives server currency to a player. Adds specified amount to player's balance. No upper limit for admin commands. Transaction logged for audit purposes. Player notified of balance increase. |
| `/bdadmin economy take <player> <amount>` | `/admin eco take <player> <amount>` | `bdcraft.admin` | Removes server currency from a player. Subtracts amount from player's balance. Can result in negative balance if configured. Transaction logged for audit purposes. Player notified of balance decrease. |
| `/bdadmin economy set <player> <amount>` | `/admin eco set <player> <amount>` | `bdcraft.admin` | Sets a player's currency balance to specific amount. Replaces current balance with specified amount. Previous balance is lost. Transaction logged with old and new balances. Player notified of balance change. |
| `/bdadmin rank set <player> <rank>` | `/admin rank set <player> <rank>` | `bdcraft.admin` | Directly sets a player's rank. Bypasses normal rank requirements and costs. Immediately applies all rank benefits. Does not refund costs if player is being demoted. Rank change logged and player notified. |
| `/bdadmin rank reset <player>` | `/admin rank reset <player>` | `bdcraft.admin` | Resets a player's rank to Newcomer. Sets rank to Newcomer regardless of current rank. Removes all rank-based benefits. Does not affect achievements or other progress. Useful for correcting rank issues. |
| `/bdadmin achievement grant <player> <achievement>` | `/admin ach grant <player> <achievement>` | `bdcraft.admin` | Manually grants an achievement to a player. Awards achievement regardless of actual progress. Achievement shows as completed in player's GUI. Does not affect related statistics or requirements. Useful for event rewards or correcting issues. |
| `/bdadmin achievement reset <player>` | `/admin ach reset <player>` | `bdcraft.admin` | Resets all achievements for a player. Removes all achievement progress and completions. Achievement GUI shows everything as incomplete. Does not affect rank progress or other statistics. Cannot be undone - use with caution. |
| `/bdadmin market force-delete <name>` | `/admin market del <name>` | `bdcraft.admin` | Forcibly deletes any market regardless of owner. Bypasses owner permission checks. Removes all villagers and market data. All associates lose access immediately. Used for removing abandoned or problematic markets. |
| `/bdadmin villager spawn-force <type> <x> <y> <z>` | `/admin vil spawn <type> <x> <y> <z>` | `bdcraft.admin` | Spawns a villager at specific coordinates. Bypasses market area requirements. Can spawn villagers anywhere in any world. Useful for creating special trading areas. Spawned villagers may not function normally outside markets. |
| `/donorverified <player>` | `/donor <player>` | `bdcraft.admin` | Marks a player as a verified donor. Grants donor-specific permissions and benefits. Allows access to donor-only features like remote market access. Donor status persists across server restarts. Used to manually grant donor benefits. |

## Quick Reference Tables

### Most Common Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdachievements` | View achievement progress GUI | `bdcraft.rank.use` |
| `/bdrank` | Check rank progress | `bdcraft.rank.use` |
| `/bdhome` | Teleport home | `bdcraft.home.use` |
| `/bdmarket info` | Check market information | `bdcraft.market.use` |
| `/bdauction` | Open auction house | `bdcraft.auction.use` |
| `/g <message>` | Global chat | `bdcraft.chat.global` |

### Essential Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdadmin reload` | Reload configuration | `bdcraft.admin` |
| `/bdvillager spawn <type>` | Spawn villagers | `bdcraft.villager.admin` |
| `/bdmarket info <name>` | Inspect any market | `bdcraft.market.use` |
| `/bdadmin player info <player>` | Check player details | `bdcraft.admin` |
| `/donorverified <player>` | Grant donor status | `bdcraft.admin` |

### Emergency Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdspawn` | Return to spawn | `bdcraft.spawn.use` |
| `/bdheal` | Restore health | `bdcraft.vital.heal` |
| `/bdgod` | Toggle invulnerability | `bdcraft.vital.god` |
| `/bdfly` | Enable flight | `bdcraft.vital.fly` |
| `/bdback` | Return to previous location | `bdcraft.teleport.back` |

---

**This reference covers every command available in BDCraft with aliases, permissions, and detailed descriptions. Commands are organized by module and functionality for easy navigation.**