# Vital Module

The Vital Module provides essential utilities that enhance the Minecraft experience and support the economic systems of BDCraft. This module handles core server functionality including communication, navigation, and player convenience features.

## Overview

The Vital Module consists of four integrated systems that provide the foundation for player interaction and server navigation:

### Core Components

- **[Chat System](chat.md)** - Multi-channel communication with messaging and moderation
- **[Home System](home.md)** - Personal teleportation points with rank-based limits
- **[Teleport System](teleport.md)** - Comprehensive teleportation and location management
- **[Tab List System](tab.md)** - Customizable player listings with rank display

## Communication Infrastructure

### Multi-Channel Chat System
- **Global Chat** - Server-wide communication using `/g` or normal typing
- **Local Chat** - 100-block radius communication using `/l`
- **Market Chat** - Market-area-only communication using `/m`
- **Admin Chat** - Administrator-only communication using `/a`

### Private Messaging
- **Direct Messages** - Send private messages to online/offline players
- **Reply System** - Quick responses to recent messages
- **Ignore Functionality** - Block unwanted communications
- **Message History** - Persistent message storage and retrieval

### Mail System Integration
- **Messaging Functions** - Send/receive mail using `/bdmail` commands
- **Item Storage** - Retrieve expired auction items via `/bdmail storage`
- **Storage Management** - Automatic item delivery from auction expiration
- **Expiration Handling** - 7-day storage limit for unclaimed items

### Chat Management Tools
- **Personal Chat Clearing** - `/clearchat` for individual history cleanup
- **Global Chat Control** - Admin tools for server-wide chat management
- **Player Moderation** - Mute/unmute functionality with duration controls
- **Chat Formatting** - Rank-based prefixes and color permissions

## Navigation Systems

### Home Management
- **Multiple Homes** - 1-3 homes based on rank and donor status
- **Named Locations** - Custom names for easy identification
- **Teleport Delays** - 3-second warmup with movement cancellation
- **Cooldown Protection** - 30-second intervals between home teleports
- **Distance Requirements** - 100-block minimum spacing between homes

### Comprehensive Teleportation
- **Player Teleportation** - Direct teleport to other players with `/tp`
- **Teleport Requests** - Request-based teleportation with `/tpa` and `/tpahere`
- **Request Management** - Accept, deny, and cancel teleport requests
- **Location Recall** - Return to previous locations with `/back`
- **Random Teleportation** - Explore new areas with `/tpr` (5-minute cooldown)

### Warp System
- **Server Warps** - Administrator-defined teleportation points
- **Warp Categories** - Organized warp listings and descriptions
- **Public Access** - Player-accessible warp destinations
- **Admin Management** - Create, modify, and delete warp points

### Spawn Management
- **Server Spawn** - Centralized server starting point
- **Spawn Teleportation** - Quick access via `/spawn` command
- **Admin Controls** - Set and modify spawn locations

## Player Information Systems

### Tab List Customization
- **Dynamic Headers** - Server information display at top of player list
- **Custom Footers** - Real-time server statistics at bottom
- **Rank-Based Sorting** - Organize players by progression level
- **Player Prefixes** - Display rank and status information
- **Update Intervals** - Real-time information refresh (2-second default)

### Built-in Variables
- **Server Statistics** - Online count, maximum capacity, TPS monitoring
- **Resource Monitoring** - Memory usage and allocation display
- **Player Information** - Individual rank, rebirth level, and status
- **Custom Placeholders** - Configurable information display

### Administrative Controls
- **Header Management** - Temporary header modifications via commands
- **Footer Control** - Dynamic footer updates for announcements
- **Configuration Reload** - Live updates without server restart
- **Player Visibility** - Hide vanished players from regular users

## Advanced Features

### Combat Integration
- **Combat Tagging** - Prevent teleportation during combat
- **Safe Teleportation** - Location validation for player safety
- **PvP Considerations** - Configurable restrictions during conflicts

### World Management
- **Cross-World Teleportation** - Support for multi-world servers
- **World Restrictions** - Configurable limitations per world
- **Disabled Worlds** - Prevent home setting in specific worlds

### Permission Integration
- **Rank-Based Limits** - Home and teleport limits tied to progression
- **Donor Benefits** - Enhanced limits and bypass permissions
- **Administrative Override** - Admin bypass for all restrictions

## Security and Moderation

### Anti-Abuse Measures
- **Cooldown Systems** - Prevent rapid teleportation abuse
- **Warmup Delays** - Allow interruption of teleportation attempts
- **Movement Cancellation** - Cancel teleports if player moves during warmup
- **Request Timeouts** - Automatic expiration of teleport requests

### Moderation Tools
- **Player Muting** - Temporary or permanent chat restrictions
- **Chat Monitoring** - Administrative oversight of communications
- **Ignore Systems** - Player-controlled communication filtering
- **Spam Protection** - Automatic prevention of chat flooding

### Data Protection
- **Secure Storage** - Protected home and teleport data
- **Permission Validation** - Constant permission checking
- **Cross-Reference Protection** - Prevent unauthorized access

## Integration with Other Modules

### Economy Module Connections
- **Market Chat** - Specialized communication within market areas
- **Auction Notifications** - Chat integration for auction events
- **Trading Convenience** - Quick access to trading locations

### Progression Module Benefits
- **Rank Display** - Chat prefixes and tab list recognition
- **Benefit Scaling** - Enhanced limits based on advancement
- **Achievement Integration** - Progress tracking for social achievements

## Configuration Options

### Chat Customization
- **Channel Settings** - Enable/disable specific chat channels
- **Format Configuration** - Customize chat appearance per rank
- **Range Settings** - Adjust local chat distance and other limits
- **Moderation Settings** - Configure mute durations and restrictions

### Teleportation Tuning
- **Delay Configuration** - Adjust warmup and cooldown timers
- **Cost Settings** - Optional economy integration for teleportation
- **Safety Checks** - Configure location validation and restrictions
- **World Settings** - Per-world teleportation rules

### Tab List Personalization
- **Display Options** - Choose information to show in tab list
- **Update Frequency** - Configure refresh rates for performance
- **Sorting Methods** - Select player organization preferences
- **Visual Styling** - Customize colors and formatting

## Commands

For a complete list of Vital Module commands, see the [Admin Commands](../admin/commands.md) reference.

## Configuration

For detailed configuration options, see the [Configuration Guide](../configuration/configuration.md).

## Permissions

For permission settings, see the [Permissions Reference](../configuration/permissions.md).

## Developer API

For technical integration details, see the [API Reference](../development/api-reference.md).

---

The Vital Module ensures that players have all the essential tools they need for communication, navigation, and convenience while participating in BDCraft's economic systems.