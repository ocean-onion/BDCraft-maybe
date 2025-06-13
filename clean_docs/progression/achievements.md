# Achievement System

BDCraft features a comprehensive achievement system with permanent game-wide accomplishments that never reset. Players can view their progress through an interactive GUI and earn titles that showcase their expertise across all areas of the plugin.

## Achievement GUI

Access the achievement system through `/bdachievements` to open an interactive GUI showing:
- **Progress Bars**: Visual progress for all achievements
- **Categories**: Organized tabs for different achievement types
- **Completion Status**: Green checkmarks for completed achievements
- **Rewards**: Click achievements to see titles and benefits earned
- **Statistics**: Overall completion percentage and totals

## Achievement Categories

### Farming Achievements
Permanent accomplishments tracking your agricultural mastery:

#### Crop Harvesting
- **Novice Harvester**: Harvest 100 BD crops total
- **Experienced Harvester**: Harvest 500 BD crops total
- **Master Harvester**: Harvest 2,000 BD crops total
- **Elite Harvester**: Harvest 10,000 BD crops total
- **Legendary Farmer**: Harvest 25,000 BD crops total

#### Seed Planting
- **Green Thumb**: Plant 100 BD seeds total
- **Apprentice Planter**: Plant 300 BD seeds total
- **Skilled Planter**: Plant 800 BD seeds total
- **Expert Planter**: Plant 3,500 BD seeds total
- **Master Gardener**: Plant 10,000 BD seeds total

#### Crop Diversity
- **Multi-Crop Farmer**: Successfully grow all three crop types
- **Quality Producer**: Generate rare crops through quality system
- **Seasonal Grower**: Participate in seasonal crop events

### Trading Achievements
Economic interaction accomplishments demonstrating market participation:

#### Dealer Interactions
- **First Customer**: Complete 10 trades with BD Dealers total
- **Regular Shopper**: Complete 25 trades with BD Dealers total
- **Regular Customer**: Complete 50 trades with BD Dealers total
- **Valued Customer**: Complete 80 trades with BD Dealers total
- **VIP Customer**: Complete 200 trades with BD Dealers total

#### Collector Interactions
- **Market Participant**: Complete 25 trades with Collectors total
- **Active Trader**: Complete 80 trades with Collectors total
- **Master Trader**: Complete 150 trades with Collectors total
- **Elite Trader**: Complete 300 trades with Collectors total
- **Trading Mogul**: Complete 500 trades with Collectors total

#### Seasonal Trading
- **Seasonal Shopper**: Complete trades with all four seasonal trader types
- **Event Participant**: Participate in special trading events
- **Collector of Rarities**: Acquire exclusive seasonal items

### Market Development Achievements
Infrastructure and community accomplishments:

#### Market Ownership
- **Market Founder**: Create your first market
- **Market Developer**: Upgrade a market to level 3
- **Market Master**: Upgrade a market to level 5
- **Multi-Market Owner**: Own 2 markets simultaneously
- **Market Empire**: Own 5 markets simultaneously

#### Community Building
- **Helper**: Give BD seeds to 5 different players (10+ seeds each)
- **Mentor**: Give BD seeds to 10 different players (20+ seeds each)
- **Community Leader**: Have 5+ associates across all your markets
- **Trade Hub**: Own a market with 50+ total trades by associates
- **Server Pillar**: Help 25 different players with BD farming

### Tool Mastery Achievements
Equipment and efficiency accomplishments:

#### Tool Acquisition
- **First Tool**: Acquire your first BD tool (BD Stick)
- **Tool Collector**: Own BD Stick and BD Harvester
- **Ultimate Collector**: Own all BD tools (including Ultimate BD Harvester)
- **Tool Master**: Use each BD tool for 100+ harvests

#### Tool Efficiency
- **Efficient Harvester**: Use BD Harvester for 1,000 crop harvests
- **Master Harvester**: Use Ultimate BD Harvester for 500 crop harvests
- **Tool Maintainer**: Keep tools in optimal condition

### Economic Achievements
Currency and financial accomplishments:

#### Currency Accumulation
- **First Earnings**: Earn 100 server currency total
- **Saver**: Accumulate 1,000 server currency at once
- **Rich**: Accumulate 5,000 server currency at once
- **Wealthy**: Accumulate 10,000 server currency at once
- **Millionaire**: Earn 100,000 server currency total (lifetime)

#### Trading Volume
- **Active Participant**: Complete 100 total trades across all villager types
- **Heavy Trader**: Complete 500 total trades
- **Master Trader**: Complete 1,000 total trades
- **Trading Legend**: Complete 2,500 total trades

## Achievement Tracking

### Progress Monitoring
Detailed tracking of all achievement-related activities:
- **Real-Time Updates**: Achievement progress updated immediately
- **Milestone Notifications**: Alerts when significant progress is made
- **Completion Rewards**: Recognition and benefits upon achievement completion
- **Progress Display**: `/bdrank progress` shows current achievement status

### Achievement Validation
Robust verification system prevents achievement exploitation:
- **Activity Verification**: All actions validated against game state
- **Time-Based Checks**: Prevents rapid-fire fake achievements
- **Cross-Reference Validation**: Multiple data sources confirm achievement completion
- **Anti-Farming Protection**: Measures to prevent achievement farming

### Data Persistence
Achievement progress maintained across all sessions:
- **Secure Storage**: Achievement data stored in protected plugin database
- **Backup Protection**: Achievement progress included in all data backups
- **Migration Support**: Achievements preserved during plugin updates
- **Recovery Options**: Admin tools available for achievement restoration

## Achievement Integration

### Rank Requirements
Achievements serve as gates for rank advancement:
- **Prerequisite System**: Specific achievements must be completed before rank purchase
- **Cumulative Requirements**: Higher ranks require completion of all lower achievements
- **Verification Process**: Automatic checking when attempting rank purchase
- **Progress Guidance**: Clear indication of remaining achievement requirements

## Achievement Display System

### Chat Display
- **Rank in Chat**: Only your current rank is displayed in chat (e.g., [Farmer] PlayerName)
- **Achievement Privacy**: Achievements are not shown in chat to keep it clean
- **GUI Screenshots**: Players can share screenshots of their achievement GUI to showcase accomplishments
- **Personal Progress**: Achievements are for personal satisfaction and long-term goals

### Achievement Sharing
Players can show off their achievements by:
- Taking screenshots of the `/bdachievements` GUI
- Sharing specific achievement category screenshots
- Posting completion percentages from `/bdstats`
- Discussing rare achievements they've unlocked

### Economic Integration
Achievements directly connect to economic benefits:
- **Trading Bonuses**: Achievement completion may unlock trading advantages
- **Market Benefits**: Infrastructure achievements improve market functionality
- **Tool Access**: Achievement gates for purchasing advanced tools
- **Permission Unlocks**: Certain achievements grant additional permissions

## Special Achievement Categories

### Rebirth Achievements
Unique accomplishments available only to rebirth players:
- **Rebirth Pioneer**: Complete your first rebirth
- **Persistence**: Reach Agricultural Expert rank after rebirth
- **Multi-Rebirth Master**: Complete multiple rebirth cycles
- **Ultimate Dedication**: Reach maximum rebirth level (Rebirth 5)

### Seasonal Achievements
Time-limited accomplishments during special events:
- **Seasonal Collector**: Acquire items from all seasonal traders
- **Event Champion**: Excel during special server events
- **Holiday Spirit**: Participate in holiday-themed activities
- **Limited Edition**: Obtain exclusive seasonal items

### Community Achievements
Server-wide accomplishments that require collective effort:
- **Market Network**: Participate in server-wide market development
- **Economic Growth**: Contribute to overall server economic health
- **Knowledge Sharing**: Help new players learn the systems
- **Collaboration**: Work with other players on major projects

## Achievement Commands

### Player Commands
- `/bdachievements` - Open achievement GUI with all categories and progress
- `/bdachievements <category>` - View specific achievement category (farming, trading, etc.)
- `/bdstats` - View your overall statistics and achievement completion rate
- `/bdrank` - View current rank and requirements for next rank
- `/bdrank achievements` - List all completed and available achievements
- `/bdrank info <rank>` - View specific rank requirements and achievements

### Administrative Commands
- `/bdadmin achievement list <player>` - View player's achievement status
- `/bdadmin achievement grant <player> <achievement>` - Manually grant achievement
- `/bdadmin achievement reset <player> <achievement>` - Reset specific achievement
- `/bdadmin achievement reload` - Reload achievement configuration

## Achievement Configuration

### Customizable Requirements
Server administrators can adjust achievement requirements:
- **Quantity Adjustments**: Modify harvest, planting, and trading requirements
- **Time Limits**: Set deadlines for time-sensitive achievements
- **Difficulty Scaling**: Adjust requirements based on server population
- **Custom Achievements**: Create server-specific achievement categories

### Reward Configuration
Achievement completion rewards can be customized:
- **Currency Rewards**: Server currency bonuses for achievement completion
- **Item Rewards**: Special items granted upon achievement completion
- **Permission Grants**: Temporary or permanent permission bonuses
- **Cosmetic Rewards**: Visual effects, titles, or other cosmetic benefits

### Integration Settings
Configure how achievements integrate with other systems:
- **Rank Gates**: Enable/disable achievement requirements for ranks
- **Community Features**: Configure group achievement mechanics
- **Notification Settings**: Control achievement announcement frequency
- **Leaderboard Options**: Customize public achievement displays

---

The achievement system ensures that progression through BDCraft is meaningful, rewarding players for genuine engagement with the economic systems while providing clear goals and community recognition for their accomplishments.