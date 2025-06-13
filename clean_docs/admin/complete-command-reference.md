# Complete Command Reference

This comprehensive guide covers every command available in BDCraft with detailed explanations, usage examples, and permission requirements.

## Economy Module Commands

### Market Commands

#### `/bdmarket create <name>`
**Description**: Creates a new market at your current location  
**Permission**: `bdcraft.market.create`  
**Usage**: `/bdmarket create MyFarm`  
**Details**: 
- Must be standing on a chest to set as market center
- Market name must be unique on the server
- Creates a 50-block radius trading area initially
- Automatically spawns initial BD villagers (1 Dealer, 1 Collector)
- Costs server currency based on configuration

#### `/bdmarket info [name]`
**Description**: Displays detailed information about a market  
**Permission**: `bdcraft.market.use`  
**Usage**: 
- `/bdmarket info` - Shows info for market you're standing in
- `/bdmarket info MyFarm` - Shows info for specific market
**Details**:
- Shows market level, upgrade costs, and benefits
- Lists all associates and their permissions
- Displays total trades completed and reputation
- Shows villager count and types present

#### `/bdmarket list`
**Description**: Lists all markets on the server with basic information  
**Permission**: `bdcraft.market.use`  
**Usage**: `/bdmarket list`  
**Details**:
- Shows market name, owner, level, and location
- Includes distance from your current position
- Indicates if you have associate permissions
- Markets sorted by distance from player

#### `/bdmarket upgrade`
**Description**: Upgrades your market to the next level  
**Permission**: `bdcraft.market.upgrade`  
**Usage**: `/bdmarket upgrade`  
**Details**:
- Must be market owner and standing in market area
- Requires sufficient server currency and emeralds
- Each level increases radius by 10 blocks (max 100 blocks at level 5)
- Provides 5% crop growth speed bonus per level
- Unlocks additional villager spawning slots

#### `/bdmarket associate add <player>`
**Description**: Adds a player as an associate to your market  
**Permission**: `bdcraft.market.associate`  
**Usage**: `/bdmarket associate add PlayerName`  
**Details**:
- Must be market owner
- Associates can trade with villagers but cannot modify market
- No limit on number of associates
- Associates contribute to market trading volume and reputation

#### `/bdmarket associate remove <player>`
**Description**: Removes an associate from your market  
**Permission**: `bdcraft.market.associate`  
**Usage**: `/bdmarket associate remove PlayerName`  
**Details**:
- Must be market owner
- Immediately revokes all market privileges for that player
- Player keeps any items already obtained from trading

#### `/bdmarket tp <name>`
**Description**: Teleports to a market (donors only)  
**Permission**: `bdcraft.market.donor`  
**Usage**: `/bdmarket tp MyFarm`  
**Details**:
- Donor-only feature for remote market access
- Must have visited the market before or be an associate
- Subject to teleportation cooldowns and delays
- Cannot teleport to private markets without permission

#### `/bdmarket delete`
**Description**: Permanently deletes your market  
**Permission**: `bdcraft.market.delete`  
**Usage**: `/bdmarket delete`  
**Details**:
- Must be market owner and confirm deletion
- Removes all villagers and market data permanently
- Associates lose access immediately
- Cannot be undone - no refunds provided

### Auction Commands

#### `/bdauction`
**Description**: Opens the auction house GUI  
**Permission**: `bdcraft.auction.use`  
**Usage**: `/bdauction`  
**Details**:
- Displays all active listings with prices and time remaining
- Click items to purchase or view details
- Search and filter functionality available
- Shows your own listings separately

#### `/bdauction sell <price>`
**Description**: Lists the item in your hand for sale  
**Permission**: `bdcraft.auction.sell`  
**Usage**: `/bdauction sell 100` (lists for 100 server currency)  
**Details**:
- Must hold a BD item (crops, tools, seeds)
- Regular Minecraft items are rejected
- Price must be between 1 and server maximum
- Listing fees apply based on rank (5% for Newcomer, 3% for Agricultural Expert)
- Listings expire after 3-7 days depending on rank

#### `/bdauction cancel <id>`
**Description**: Cancels one of your auction listings  
**Permission**: `bdcraft.auction.cancel`  
**Usage**: `/bdauction cancel 12345`  
**Details**:
- Can only cancel your own listings
- Item is returned to your inventory immediately
- Listing fees are not refunded
- Cannot cancel if someone is actively purchasing

#### `/bdauction list`
**Description**: Shows all your active auction listings  
**Permission**: `bdcraft.auction.list`  
**Usage**: `/bdauction list`  
**Details**:
- Displays listing ID, item, price, and time remaining
- Shows total listing fees paid
- Indicates which listings have received bids or interest

### Villager Commands

#### `/bdvillager spawn <type>`
**Description**: Spawns a BD villager at your location  
**Permission**: `bdcraft.villager.admin`  
**Usage**: 
- `/bdvillager spawn dealer`
- `/bdvillager spawn collector`
- `/bdvillager spawn seasonal_spring`
**Details**:
- Must be within a market area to spawn
- Villager types: dealer, collector, seasonal_spring, seasonal_summer, seasonal_fall, seasonal_winter
- Spawned villagers are permanent until manually removed
- Each market has a limit on villager count based on level

#### `/bdvillager remove`
**Description**: Removes the villager you're looking at  
**Permission**: `bdcraft.villager.admin`  
**Usage**: `/bdvillager remove` (while looking at a BD villager)  
**Details**:
- Must be looking directly at a BD villager
- Removes villager permanently from the market
- Any active trades are cancelled
- Cannot remove villagers during active trading sessions

#### `/bdvillager list`
**Description**: Lists all BD villagers in the current market  
**Permission**: `bdcraft.villager.use`  
**Usage**: `/bdvillager list`  
**Details**:
- Shows villager type, location, and trade counts
- Indicates which villagers are currently busy
- Displays reputation bonuses for each villager type

#### `/bdvillager reload`
**Description**: Reloads villager configurations and trades  
**Permission**: `bdcraft.villager.admin`  
**Usage**: `/bdvillager reload`  
**Details**:
- Updates all villager trades based on current configuration
- Applies new pricing and item availability
- Does not affect villagers mid-trade

## Progression Module Commands

### Rank Commands

#### `/bdrank`
**Description**: Displays your current rank and progress toward next rank  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdrank`  
**Details**:
- Shows current rank and benefits
- Lists requirements for next rank advancement
- Displays progress toward each requirement (crops harvested, trades completed, etc.)
- Shows currency cost for next rank purchase

#### `/bdrank info <rank>`
**Description**: Shows detailed information about a specific rank  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdrank info Expert Farmer`  
**Details**:
- Lists all benefits and bonuses for the specified rank
- Shows exact requirements to achieve that rank
- Displays currency cost and any special prerequisites
- Compares benefits to your current rank

#### `/bdrank list`
**Description**: Lists all available ranks with basic information  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdrank list`  
**Details**:
- Shows all 5 ranks from Newcomer to Agricultural Expert
- Indicates which ranks you've achieved
- Shows currency costs for each rank
- Highlights your current rank

#### `/bdrank progress`
**Description**: Shows detailed progress toward your next rank  
**Permission**: `bdcraft.rank.progress`  
**Usage**: `/bdrank progress`  
**Details**:
- Detailed breakdown of each requirement
- Shows exact numbers (e.g., "247/500 crops harvested")
- Indicates which requirements are already met
- Estimates time to completion based on recent activity

#### `/bdrank benefits`
**Description**: Lists all benefits of your current rank  
**Permission**: `bdcraft.rank.benefits`  
**Usage**: `/bdrank benefits`  
**Details**:
- Shows harvest yield bonuses
- Lists auction fee reductions
- Displays growth speed improvements
- Shows any special permissions or abilities

#### `/bdrankup`
**Description**: Attempts to purchase the next rank if requirements are met  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdrankup`  
**Details**:
- Checks all requirements automatically
- Deducts currency cost if successful
- Applies new rank benefits immediately
- Updates chat prefix and permissions

### Achievement Commands

#### `/bdachievements`
**Description**: Opens the achievement GUI showing all categories and progress  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdachievements`  
**Details**:
- Interactive GUI with multiple category tabs
- Shows progress bars for incomplete achievements
- Green checkmarks for completed achievements
- Click achievements to see rewards and descriptions
- Overall completion percentage displayed

#### `/bdachievements <category>`
**Description**: Opens achievement GUI focused on a specific category  
**Permission**: `bdcraft.rank.use`  
**Usage**: 
- `/bdachievements farming`
- `/bdachievements trading`
- `/bdachievements community`
**Details**:
- Available categories: farming, trading, market, tools, economy, community
- Shows only achievements in that category
- Detailed progress tracking for each achievement
- Related achievements are grouped together

#### `/bdstats`
**Description**: Shows your overall statistics and achievement completion rate  
**Permission**: `bdcraft.rank.use`  
**Usage**: `/bdstats`  
**Details**:
- Total achievements completed vs. available
- Completion percentage for each category
- Lifetime statistics (total crops harvested, trades completed, etc.)
- Rare achievement highlights

### Rebirth Commands

#### `/bdrebirth`
**Description**: Shows rebirth information and requirements  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth`  
**Details**:
- Shows current rebirth level (if any)
- Lists requirements for next rebirth
- Explains benefits of rebirth system
- Shows what will be reset vs. what persists

#### `/bdrebirth info`
**Description**: Shows detailed rebirth status and benefits  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth info`  
**Details**:
- Current rebirth bonuses active
- Permanent benefits gained from previous rebirths
- Cumulative effect calculations
- Next rebirth requirements and benefits

#### `/bdrebirth requirements`
**Description**: Shows exact requirements for next rebirth  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth requirements`  
**Details**:
- Must be Agricultural Expert rank
- Currency requirements for rebirth
- Special achievement requirements
- Warning about what progress will be reset

#### `/bdrebirth confirm`
**Description**: Confirms and performs rebirth after meeting requirements  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth confirm`  
**Details**:
- Final confirmation prompt with warnings
- Resets rank to Newcomer but keeps rebirth bonuses
- Maintains achievements and permanent progress
- Applies new rebirth level benefits immediately

#### `/bdrebirth top`
**Description**: Shows the rebirth leaderboard  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth top`  
**Details**:
- Lists top players by rebirth level
- Shows their current rank and achievements
- Displays rebirth dates and progression speed

#### `/bdrebirth benefits`
**Description**: Lists all rebirth benefits by level  
**Permission**: `bdcraft.rebirth.use`  
**Usage**: `/bdrebirth benefits`  
**Details**:
- Shows benefits for each rebirth level
- Cumulative bonuses and how they stack
- Exclusive features unlocked at higher rebirth levels

## Vital Module Commands

### Home Commands

#### `/bdhome`
**Description**: Teleports to your default home  
**Permission**: `bdcraft.home.use`  
**Usage**: `/bdhome`  
**Details**:
- Teleports to home named "home" or your first set home
- Subject to teleportation delay based on rank
- Cannot teleport if recently in combat
- Displays teleportation countdown

#### `/bdhome <name>`
**Description**: Teleports to a specific named home  
**Permission**: `bdcraft.home.use`  
**Usage**: `/bdhome farm`  
**Details**:
- Case-sensitive home names
- Must be a home you own
- Same teleportation rules as default home
- Shows location before teleporting

#### `/bdhome set [name]`
**Description**: Sets a home at your current location  
**Permission**: `bdcraft.home.set`  
**Usage**: 
- `/bdhome set` - Sets default "home"
- `/bdhome set farm` - Sets named home "farm"
**Details**:
- Limited by rank-based home limits
- Overwrites existing home with same name
- Cannot set homes in other players' claimed areas
- Shows coordinates where home was set

#### `/bdhome list`
**Description**: Lists all your homes with coordinates  
**Permission**: `bdcraft.home.list`  
**Usage**: `/bdhome list`  
**Details**:
- Shows home name, world, and coordinates
- Indicates which is your default home
- Shows distance from current location
- Displays how many homes you can still set

#### `/bdhome delete <name>`
**Description**: Deletes a specific home  
**Permission**: `bdcraft.home.delete`  
**Usage**: `/bdhome delete farm`  
**Details**:
- Permanently removes the home
- Cannot be undone
- Frees up a home slot for new homes
- Confirmation required for default home deletion

#### `/bdhome limit`
**Description**: Shows your current home limit and usage  
**Permission**: `bdcraft.home.limit`  
**Usage**: `/bdhome limit`  
**Details**:
- Shows homes used vs. maximum allowed
- Explains how rank affects home limits
- Lists requirements to increase home limit

### Teleportation Commands

#### `/bdtp <player>`
**Description**: Teleports directly to another player  
**Permission**: `bdcraft.teleport.others`  
**Usage**: `/bdtp PlayerName`  
**Details**:
- Admin-only command for instant teleportation
- No delay or restrictions
- Works across worlds
- Target player is notified of teleportation

#### `/bdtp <x> <y> <z>`
**Description**: Teleports to specific coordinates  
**Permission**: `bdcraft.teleport.location`  
**Usage**: `/bdtp 100 64 -200`  
**Details**:
- Admin-only command for coordinate teleportation
- Uses current world unless world specified
- No safety checks - can teleport into walls or lava
- Coordinates must be within world boundaries

#### `/bdtpa <player>`
**Description**: Requests to teleport to another player  
**Permission**: `bdcraft.teleport.request`  
**Usage**: `/bdtpa PlayerName`  
**Details**:
- Sends teleportation request that can be accepted/denied
- Request expires after 60 seconds
- Subject to cooldowns between requests
- Cannot request to players who have blocked requests

#### `/bdtpahere <player>`
**Description**: Requests another player to teleport to you  
**Permission**: `bdcraft.teleport.requesthere`  
**Usage**: `/bdtpahere PlayerName`  
**Details**:
- Requests target player to come to your location
- Same rules as tpa but reversed direction
- Useful for bringing players to your base or market

#### `/bdtpaccept [player]`
**Description**: Accepts a pending teleportation request  
**Permission**: `bdcraft.teleport.request`  
**Usage**: 
- `/bdtpaccept` - Accepts most recent request
- `/bdtpaccept PlayerName` - Accepts request from specific player
**Details**:
- Must have pending request to accept
- Teleportation begins immediately after acceptance
- Both players are notified of acceptance

#### `/bdtpdeny [player]`
**Description**: Denies a pending teleportation request  
**Permission**: `bdcraft.teleport.request`  
**Usage**: 
- `/bdtpdeny` - Denies most recent request
- `/bdtpdeny PlayerName` - Denies request from specific player
**Details**:
- Politely declines teleportation request
- Requesting player is notified of denial
- Does not block future requests from that player

#### `/bdback`
**Description**: Teleports to your previous location  
**Permission**: `bdcraft.teleport.back`  
**Usage**: `/bdback`  
**Details**:
- Returns to location before last teleportation
- Works with homes, warps, and tp commands
- Limited number of previous locations stored
- Subject to same delays as other teleportation

#### `/bdspawn`
**Description**: Teleports to the server spawn point  
**Permission**: `bdcraft.spawn.use`  
**Usage**: `/bdspawn`  
**Details**:
- Teleports to server's designated spawn location
- Free teleportation available to all players
- Subject to teleportation delays
- Safe fallback when lost or in danger

### Chat Commands

#### `/g <message>`
**Description**: Sends a message to global chat (server-wide)  
**Permission**: `bdcraft.chat.global`  
**Usage**: `/g Hello everyone!`  
**Details**:
- Visible to all players on the server
- Default chat channel for most communication
- Shows your rank prefix before your name
- Subject to chat cooldowns and filters

#### `/l <message>`
**Description**: Sends a message to local chat (100 block radius)  
**Permission**: `bdcraft.chat.local`  
**Usage**: `/l Anyone here need help?`  
**Details**:
- Only visible to players within 100 blocks
- Useful for location-specific communication
- Shows distance indicators for recipients
- Good for coordinating local activities

#### `/m <message>`
**Description**: Sends a message to market chat (market area only)  
**Permission**: `bdcraft.chat.market`  
**Usage**: `/m Trading BD crops here!`  
**Details**:
- Only visible to players in the same market area
- Ideal for coordinating trades and market activities
- Includes market name in message format
- Must be standing in a market to use

#### `/a <message>`
**Description**: Sends a message to admin chat (staff only)  
**Permission**: `bdcraft.chat.admin`  
**Usage**: `/a Player reports possible bug with villagers`  
**Details**:
- Only visible to players with admin chat permission
- Used for staff coordination and moderation
- Bypasses all chat cooldowns and filters
- Shows across all worlds and distances

#### `/bdmsg <player> <message>`
**Description**: Sends a private message to another player  
**Permission**: `bdcraft.chat.msg`  
**Usage**: `/bdmsg PlayerName Want to trade some crops?`  
**Details**:
- Private one-on-one communication
- Both players see the message
- Recipient can reply using /bdreply
- Ignores normal chat channels and distance

#### `/bdreply <message>`
**Description**: Replies to the last private message received  
**Permission**: `bdcraft.chat.reply`  
**Usage**: `/bdreply Sure, I'll come to your market`  
**Details**:
- Quick way to respond to private messages
- Sends message to whoever last messaged you
- Maintains conversation flow
- Shows who you're replying to

#### `/bdmail send <player> <message>`
**Description**: Sends mail to a player (offline messaging)  
**Permission**: `bdcraft.chat.mail`  
**Usage**: `/bdmail send PlayerName Thanks for the seeds!`  
**Details**:
- Messages delivered when recipient logs in
- Useful for communicating with offline players
- Mail persists until read or deleted
- Limited number of messages per player

#### `/bdmail read`
**Description**: Reads your unread mail messages  
**Permission**: `bdcraft.chat.mail`  
**Usage**: `/bdmail read`  
**Details**:
- Shows all unread messages with sender and timestamp
- Messages marked as read after viewing
- Can view multiple messages at once
- Shows mail from newest to oldest

#### `/bdmail clear`
**Description**: Deletes all your mail messages  
**Permission**: `bdcraft.chat.mail`  
**Usage**: `/bdmail clear`  
**Details**:
- Permanently removes all mail (read and unread)
- Cannot be undone
- Frees up mail storage space
- Confirmation required

#### `/bdmail storage`
**Description**: Access item storage mailbox for expired auction items  
**Permission**: `bdcraft.chat.mail`  
**Usage**: `/bdmail storage`  
**Details**:
- Opens inventory GUI with expired auction items
- Items from cancelled or expired listings
- Limited storage time before permanent loss
- Separate from text mail system

#### `/bdignore <player>`
**Description**: Blocks all messages from a specific player  
**Permission**: `bdcraft.chat.ignore`  
**Usage**: `/bdignore AnnoyingPlayer`  
**Details**:
- Blocks private messages, not public chat
- Prevents teleportation requests from that player
- Player is not notified they are being ignored
- Can ignore multiple players

#### `/bdunignore <player>`
**Description**: Stops ignoring a previously ignored player  
**Permission**: `bdcraft.chat.unignore`  
**Usage**: `/bdunignore FormerlyAnnoyingPlayer`  
**Details**:
- Removes player from ignore list
- They can message and send requests again
- Does not notify player they were unignored
- Takes effect immediately

### Player Status Commands

#### `/bdfly`
**Description**: Toggles flight mode for yourself  
**Permission**: `bdcraft.vital.fly`  
**Usage**: `/bdfly`  
**Details**:
- Enables creative-style flying in survival mode
- Toggles on/off with each use
- Flight persists until manually disabled or logout
- Speed can be adjusted with additional commands

#### `/bdfly <player>`
**Description**: Toggles flight mode for another player  
**Permission**: `bdcraft.vital.fly.others`  
**Usage**: `/bdfly PlayerName`  
**Details**:
- Admin command to control other players' flight
- Target player is notified of flight status change
- Useful for giving temporary flight for building
- Can be used to disable flight as punishment

#### `/bdfly speed <1-10>`
**Description**: Sets your flight speed  
**Permission**: `bdcraft.vital.fly.speed`  
**Usage**: `/bdfly speed 5`  
**Details**:
- Speed range from 1 (very slow) to 10 (very fast)
- Default speed is typically 5
- Higher speeds use more server resources
- Speed persists until changed or logout

#### `/bdheal`
**Description**: Instantly heals yourself to full health  
**Permission**: `bdcraft.vital.heal`  
**Usage**: `/bdheal`  
**Details**:
- Restores health to maximum (20 hearts)
- Removes all negative potion effects
- Does not affect hunger or experience
- Subject to cooldowns to prevent abuse

#### `/bdheal <player>`
**Description**: Heals another player to full health  
**Permission**: `bdcraft.vital.heal.others`  
**Usage**: `/bdheal PlayerName`  
**Details**:
- Admin command to heal other players
- Target player is notified of healing
- Useful for helping players in emergencies
- No cooldown for admin usage

#### `/bdfeed`
**Description**: Restores your hunger to full  
**Permission**: `bdcraft.vital.feed`  
**Usage**: `/bdfeed`  
**Details**:
- Sets hunger to maximum (20 drumsticks)
- Sets saturation to maximum for slower hunger loss
- Does not affect health or experience
- Subject to cooldowns

#### `/bdfeed <player>`
**Description**: Feeds another player  
**Permission**: `bdcraft.vital.feed.others`  
**Usage**: `/bdfeed PlayerName`  
**Details**:
- Admin command to feed other players
- Target player is notified
- Useful for preventing starvation
- No cooldown for admin usage

#### `/bdgod`
**Description**: Toggles invulnerability (god mode)  
**Permission**: `bdcraft.vital.god`  
**Usage**: `/bdgod`  
**Details**:
- Makes you immune to all damage sources
- Toggles on/off with each use
- Includes fall damage, drowning, lava, etc.
- Visual indicator shows god mode status

#### `/bdspeed <1-10>`
**Description**: Sets your walking/running speed  
**Permission**: `bdcraft.vital.speed`  
**Usage**: `/bdspeed 3`  
**Details**:
- Speed range from 1 (slow) to 10 (very fast)
- Default Minecraft speed is 2
- Affects both walking and sprinting
- Speed persists until changed or logout

#### `/bdvanish`
**Description**: Makes you invisible to other players  
**Permission**: `bdcraft.vital.vanish`  
**Usage**: `/bdvanish`  
**Details**:
- Completely hides you from other players
- Removes you from tab list and player counts
- Prevents players from seeing your chat messages
- Useful for moderation and admin tasks

#### `/bdping`
**Description**: Shows your connection ping to the server  
**Permission**: `bdcraft.vital.ping`  
**Usage**: `/bdping`  
**Details**:
- Displays latency in milliseconds
- Lower numbers indicate better connection
- Useful for troubleshooting connection issues
- Updates in real-time

#### `/bdping <player>`
**Description**: Shows another player's ping  
**Permission**: `bdcraft.vital.ping.others`  
**Usage**: `/bdping PlayerName`  
**Details**:
- Admin command to check other players' connections
- Useful for identifying lag sources
- Can help determine if issues are server or client-side

#### `/bdnear`
**Description**: Shows all players within a certain radius  
**Permission**: `bdcraft.vital.near`  
**Usage**: `/bdnear`  
**Details**:
- Lists players within default radius (usually 100 blocks)
- Shows distance and direction to each player
- Excludes vanished players (unless you're admin)
- Useful for finding nearby players

#### `/bdseen <player>`
**Description**: Shows when a player was last online  
**Permission**: `bdcraft.vital.seen`  
**Usage**: `/bdseen PlayerName`  
**Details**:
- Shows exact date and time of last logout
- Indicates if player is currently online
- Shows total playtime if configured
- Works with offline players

### World Management Commands

#### `/bdtime <time>`
**Description**: Sets the world time  
**Permission**: `bdcraft.vital.time`  
**Usage**: 
- `/bdtime day` - Sets to daytime
- `/bdtime night` - Sets to nighttime
- `/bdtime 6000` - Sets to specific time value
**Details**:
- Affects all players in the current world
- Time values: 0=dawn, 6000=noon, 12000=dusk, 18000=midnight
- Changes take effect immediately
- Does not affect sleep schedules

#### `/bdweather <weather>`
**Description**: Sets the weather in the current world  
**Permission**: `bdcraft.vital.weather`  
**Usage**: 
- `/bdweather clear` - Clear skies
- `/bdweather rain` - Starts raining
- `/bdweather storm` - Thunderstorm
**Details**:
- Affects entire world immediately
- Weather persists for natural duration unless changed again
- Storm weather can cause lightning strikes
- Useful for events or ambiance

#### `/bdgamemode <mode> [player]`
**Description**: Changes gamemode for yourself or another player  
**Permission**: `bdcraft.vital.gamemode`  
**Usage**: 
- `/bdgamemode creative` - Switch to creative mode
- `/bdgamemode survival PlayerName` - Set player to survival
**Details**:
- Modes: survival, creative, adventure, spectator
- Flying ability automatically adjusted based on mode
- Inventory preserved when switching modes
- Target player notified of gamemode change

### Moderation Commands

#### `/bdkick <player> [reason]`
**Description**: Kicks a player from the server  
**Permission**: `bdcraft.vital.kick`  
**Usage**: `/bdkick Griefer Destroying other players' builds`  
**Details**:
- Immediately disconnects player from server
- Player can reconnect unless banned
- Reason shown to kicked player and logged
- Other players notified of kick

#### `/bdban <player> [reason]`
**Description**: Permanently bans a player from the server  
**Permission**: `bdcraft.vital.ban`  
**Usage**: `/bdban Cheater Using unauthorized mods`  
**Details**:
- Prevents player from connecting to server
- Ban persists until manually removed
- Reason logged and shown to player if they try to connect
- IP-based banning available with additional configuration

#### `/bdunban <player>`
**Description**: Removes a ban from a player  
**Permission**: `bdcraft.vital.unban`  
**Usage**: `/bdunban FormerCheater`  
**Details**:
- Allows previously banned player to reconnect
- Takes effect immediately
- Ban removal is logged for admin records
- Player can connect as soon as command is executed

#### `/bdtempban <player> <duration> [reason]`
**Description**: Temporarily bans a player for a specified time  
**Permission**: `bdcraft.vital.tempban`  
**Usage**: `/bdtempban Spammer 1d Excessive chat spam`  
**Details**:
- Duration formats: 1m (minute), 1h (hour), 1d (day), 1w (week)
- Player automatically unbanned when time expires
- Reason shown to player during ban period
- Remaining time displayed if player tries to connect

#### `/mute <player> [duration] [reason]`
**Description**: Prevents a player from chatting  
**Permission**: `bdcraft.chat.mute.player`  
**Usage**: `/mute Spammer 30m Excessive caps and spam`  
**Details**:
- Player can still see chat but cannot send messages
- Duration optional - permanent if not specified
- Prevents all chat types (global, local, private)
- Muted player notified of mute and reason

#### `/unmute <player>`
**Description**: Removes a chat mute from a player  
**Permission**: `bdcraft.chat.unmute`  
**Usage**: `/unmute FormerSpammer`  
**Details**:
- Restores full chat privileges immediately
- Player notified they can chat again
- Mute removal logged for admin records

#### `/bdbroadcast <message>`
**Description**: Sends a server-wide announcement  
**Permission**: `bdcraft.vital.broadcast`  
**Usage**: `/bdbroadcast Server will restart in 5 minutes!`  
**Details**:
- Message sent to all online players
- Appears in special broadcast format with colors
- Bypasses all chat channels and ignore lists
- Used for important server announcements

### Administrative Commands

#### `/bdadmin reload`
**Description**: Reloads all plugin configuration files  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin reload`  
**Details**:
- Reloads all module configurations without restart
- Players remain connected during reload
- New settings take effect immediately
- Some changes may require restart for full effect

#### `/bdadmin save`
**Description**: Forces save of all player data to disk  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin save`  
**Details**:
- Immediately writes all data to database/files
- Useful before server maintenance or backup
- Includes player balances, ranks, achievements, homes
- Progress shown during save operation

#### `/bdadmin backup`
**Description**: Creates a backup of all plugin data  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin backup`  
**Details**:
- Creates timestamped backup of all plugin data
- Includes configuration files and player data
- Backups stored in designated backup directory
- Can be restored manually if needed

#### `/bdadmin debug`
**Description**: Toggles debug mode for detailed logging  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin debug`  
**Details**:
- Enables verbose logging for troubleshooting
- Shows detailed information about plugin operations
- Increases log file size significantly
- Should be disabled after debugging to save space

#### `/bdadmin player info <player>`
**Description**: Shows comprehensive information about a player  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin player info PlayerName`  
**Details**:
- Shows rank, achievements, balances, and statistics
- Displays homes, market ownership, and trading history
- Includes login/logout times and playtime
- Useful for player support and administration

#### `/bdadmin economy give <player> <amount>`
**Description**: Gives server currency to a player  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin economy give PlayerName 1000`  
**Details**:
- Adds specified amount to player's balance
- No upper limit for admin commands
- Transaction logged for audit purposes
- Player notified of balance increase

#### `/bdadmin economy take <player> <amount>`
**Description**: Removes server currency from a player  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin economy take PlayerName 500`  
**Details**:
- Subtracts amount from player's balance
- Can result in negative balance if configured
- Transaction logged for audit purposes
- Player notified of balance decrease

#### `/bdadmin economy set <player> <amount>`
**Description**: Sets a player's currency balance to specific amount  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin economy set PlayerName 2500`  
**Details**:
- Replaces current balance with specified amount
- Previous balance is lost
- Transaction logged with old and new balances
- Player notified of balance change

#### `/bdadmin rank set <player> <rank>`
**Description**: Directly sets a player's rank  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin rank set PlayerName "Expert Farmer"`  
**Details**:
- Bypasses normal rank requirements and costs
- Immediately applies all rank benefits
- Does not refund costs if player is being demoted
- Rank change logged and player notified

#### `/bdadmin rank reset <player>`
**Description**: Resets a player's rank to Newcomer  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin rank reset PlayerName`  
**Details**:
- Sets rank to Newcomer regardless of current rank
- Removes all rank-based benefits
- Does not affect achievements or other progress
- Useful for correcting rank issues

#### `/bdadmin achievement grant <player> <achievement>`
**Description**: Manually grants an achievement to a player  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin achievement grant PlayerName "Master Harvester"`  
**Details**:
- Awards achievement regardless of actual progress
- Achievement shows as completed in player's GUI
- Does not affect related statistics or requirements
- Useful for event rewards or correcting issues

#### `/bdadmin achievement reset <player>`
**Description**: Resets all achievements for a player  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin achievement reset PlayerName`  
**Details**:
- Removes all achievement progress and completions
- Achievement GUI shows everything as incomplete
- Does not affect rank progress or other statistics
- Cannot be undone - use with caution

#### `/bdadmin market force-delete <name>`
**Description**: Forcibly deletes any market regardless of owner  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin market force-delete ProblemMarket`  
**Details**:
- Bypasses owner permission checks
- Removes all villagers and market data
- All associates lose access immediately
- Used for removing abandoned or problematic markets

#### `/bdadmin villager spawn-force <type> <x> <y> <z>`
**Description**: Spawns a villager at specific coordinates  
**Permission**: `bdcraft.admin`  
**Usage**: `/bdadmin villager spawn-force dealer 100 64 200`  
**Details**:
- Bypasses market area requirements
- Can spawn villagers anywhere in any world
- Useful for creating special trading areas
- Spawned villagers may not function normally outside markets

#### `/donorverified <player>`
**Description**: Marks a player as a verified donor  
**Permission**: `bdcraft.admin`  
**Usage**: `/donorverified PlayerName`  
**Details**:
- Grants donor-specific permissions and benefits
- Allows access to donor-only features like remote market access
- Donor status persists across server restarts
- Used to manually grant donor benefits

## Quick Reference

### Most Common Player Commands
- `/bdachievements` - View achievement progress
- `/bdrank` - Check rank progress
- `/bdhome` - Teleport home
- `/bdmarket info` - Check market information
- `/bdauction` - Open auction house
- `/g <message>` - Global chat

### Essential Admin Commands
- `/bdadmin reload` - Reload configuration
- `/bdvillager spawn <type>` - Spawn villagers
- `/bdmarket info <name>` - Inspect any market
- `/bdadmin player info <player>` - Check player details
- `/donorverified <player>` - Grant donor status

### Emergency Commands
- `/bdspawn` - Return to spawn
- `/bdheal` - Restore health
- `/bdgod` - Toggle invulnerability
- `/bdfly` - Enable flight
- `/bdback` - Return to previous location

---

This reference covers every command available in BDCraft. Commands are organized by module and functionality. All commands include proper permission requirements and detailed usage examples.