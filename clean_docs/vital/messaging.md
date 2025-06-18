# Messaging System

BDCraft provides a comprehensive messaging infrastructure that handles both real-time private communication and persistent mail functionality with item storage capabilities.

## Real-Time Communication

### Private Messaging
Direct communication between players with privacy controls and offline notifications:

#### Basic Messaging Commands
- `/msg <player> <message>` - Send a private message to a player
- `/w <player> <message>` - Alternative whisper command
- `/tell <player> <message>` - Tell command for private messaging
- `/r <message>` - Reply to the last player who messaged you
- `/reply <message>` - Alternative reply command

#### Message Formatting
- **To Recipient**: `[From PlayerName]: Message`
- **To Sender**: `[To PlayerName]: Message`
- **Reply Indicator**: Shows who you're replying to
- **Online Status**: Indicates if recipient is online

#### Privacy Controls
- `/ignore <player>` - Block messages from a specific player
- `/unignore <player>` - Remove a player from your ignore list
- `/ignorelist` - View all players you're currently ignoring

### Message Features
- **Cross-World Messaging**: Send messages to players in different worlds
- **Offline Notifications**: Players notified of messages when they log in
- **Message History**: Recent message history maintained during session
- **AFK Detection**: Automatic AFK status affects message delivery

## Persistent Communication

### Mail System
Long-term message storage with item attachment capabilities for offline communication:

#### Mail Commands
- `/bdmail send <player> <message>` - Send mail to online or offline players
- `/mail send <player> <message>` - Alternative mail command
- `/bdmail read` - Read your received mail messages
- `/mail read` - Alternative read command
- `/bdmail clear` - Clear your mail inbox
- `/mail clear` - Alternative clear command

#### Mail Features
- **Offline Storage**: Messages persist until manually cleared
- **Unlimited Recipients**: Send mail to any player who has joined the server
- **Message Limits**: Configurable limits on mail storage per player
- **Timestamp Tracking**: All mail includes send date and time

### Item Storage System
Separate system for storing expired auction items:

#### Storage Commands
- `/bdmail storage` - Access your item storage mailbox
- `/mail storage` - Alternative storage command
- `/bdm s` - Short alias for storage access
- `/mail s` - Alternative short alias

#### Storage Features
- **Automatic Delivery**: Expired auction items automatically sent to storage
- **7-Day Retention**: Unclaimed items expire after one week
- **Inventory Integration**: Items retrieved directly to player inventory
- **Capacity Limits**: Configurable storage limits per player

## Channel Communication

### Global Chat
Server-wide communication accessible to all players:
- **Command**: `/g <message>` or normal typing
- **Range**: Entire server
- **Format**: `[G] [Rank] PlayerName: Message`
- **Default Channel**: Primary communication method

### Local Chat
Proximity-based communication for nearby players:
- **Command**: `/l <message>`
- **Range**: 100 blocks (configurable)
- **Format**: `[L] [Rank] PlayerName: Message`
- **Use Cases**: Coordination, local events, private conversations

### Market Chat
Specialized communication within market areas:
- **Command**: `/m <message>`
- **Range**: Only within market boundaries
- **Format**: `[M] [Rank] PlayerName: Message`
- **Access**: Market owners, associates, and visitors
- **Use Cases**: Trading coordination, market announcements

### Admin Chat
Administrative communication channel:
- **Command**: `/a <message>`
- **Range**: Only visible to administrators
- **Format**: `[Admin] PlayerName: Message`
- **Access**: Requires `bdcraft.chat.admin` permission
- **Use Cases**: Staff coordination, moderation discussions

## Chat Management

### Personal Chat Controls
- `/clearchat` - Clear your personal chat history
- `/cc` - Short alias for clearing chat
- **Scope**: Only affects your own chat display
- **Persistence**: Chat history cleared until next login

### Administrative Chat Controls
- `/clearchat all` - Clear everyone's chat (admin only)
- `/cc all` - Admin chat clearing
- **Scope**: Affects all players on server
- **Use Cases**: Clean up after spam, fresh start after events

### Chat Moderation
- `/mutechat` - Temporarily disable global chat
- `/mute <player> [duration] [reason]` - Prevent a player from chatting
- `/unmute <player>` - Remove chat restrictions from a player
- **Duration**: Configurable mute periods (minutes, hours, permanent)
- **Bypass**: Admins can always communicate regardless of mute status

## Advanced Features

### Nickname System
- `/nick <nickname>` - Set a custom display name
- `/nick reset` - Remove your nickname
- `/nick <player> <nickname>` - Set another player's nickname (admin)
- **Restrictions**: Configurable length and character limits
- **Permissions**: Rank-based nickname permissions

### Chat Colors and Formatting
- **Color Codes**: Use `&` codes for colored text (permission required)
- **Format Codes**: Bold, italic, underline formatting
- **Rank Integration**: Automatic color application based on rank
- **Permission Gates**: Color usage restricted by permissions

### Spam Protection
- **Rate Limiting**: Prevent rapid message sending
- **Duplicate Detection**: Block repeated identical messages
- **Length Limits**: Maximum message length restrictions
- **Flood Protection**: Automatic temporary muting for spam

## Integration Features

### Economy Integration
- **Trade Coordination**: Market chat for trading discussions
- **Auction Notifications**: Automatic messages for auction events
- **Payment Confirmations**: Messages confirming economic transactions

### Progression Integration
- **Rank Announcements**: Server-wide notifications for rank advances
- **Achievement Messages**: Chat integration for achievement completion
- **Rebirth Celebrations**: Special messages for rebirth completions

### Donor Benefits
- **Enhanced Colors**: Additional color options for donors
- **Priority Messaging**: Reduced cooldowns and enhanced limits
- **Special Formatting**: Exclusive formatting options

## Security and Privacy

### Message Encryption
- **Local Storage**: Messages encrypted in local storage
- **Transmission Security**: Secure message transmission between players
- **Admin Privacy**: Administrative messages separated from player logs

### Data Retention
- **Message Limits**: Configurable retention periods for different message types
- **Privacy Controls**: Players can clear their own message history
- **GDPR Compliance**: Data deletion options for privacy compliance

### Anti-Harassment Features
- **Ignore System**: Comprehensive blocking of unwanted communication
- **Report System**: Built-in reporting for inappropriate messages
- **Automatic Filtering**: Configurable word filtering and content moderation

## Configuration Options

### Channel Settings
```yaml
chat:
  channels:
    global:
      enabled: true
      format: "&7[G] {rank_prefix}{player}: {message}"
      range: -1  # -1 for unlimited
    local:
      enabled: true
      format: "&7[L] {rank_prefix}{player}: {message}"
      range: 100
    market:
      enabled: true
      format: "&7[M] {rank_prefix}{player}: {message}"
      permission: "bdcraft.chat.market"
    admin:
      enabled: true
      format: "&c[Admin] {player}: {message}"
      permission: "bdcraft.chat.admin"
```

### Message Limits
```yaml
messaging:
  cooldowns:
    private_message: 1    # seconds between private messages
    mail_send: 5         # seconds between mail sends
    channel_switch: 2    # seconds between channel changes
  
  limits:
    message_length: 256  # maximum characters per message
    mail_storage: 50     # maximum stored mail messages
    ignore_list: 20      # maximum ignored players
```

### Moderation Settings
```yaml
moderation:
  spam_protection:
    enabled: true
    max_messages_per_second: 3
    duplicate_threshold: 3
    auto_mute_duration: 300  # 5 minutes
  
  word_filter:
    enabled: true
    action: "warn"  # warn, mute, kick, ban
    replacement: "***"
```

---

The messaging system provides comprehensive communication tools that support both casual player interaction and serious economic coordination, with robust moderation and privacy features.