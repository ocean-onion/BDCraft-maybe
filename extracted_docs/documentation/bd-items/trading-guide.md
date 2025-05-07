# BD Trading Guide

This guide covers all aspects of trading within the BDCraft economy system, including both market-based and natural village trading, as well as player-to-player trading mechanics.

## Villager Trading

### Trading with BD Dealers

BD Dealers are your primary source for seeds and harvesting tools:

1. **Finding Dealers**:
   - Natural dealers spawn in vanilla Minecraft villages (45% chance)
   - Every player market has its own dealer
   - Dealers have a green name tag and FARMER profession
   - Use `/bdvil locate` to find natural dealers in villages
   - Or create your own market to have guaranteed dealer access

2. **Available Trades**:
   - Regular BD Seeds: 3 emeralds for 5 seeds
   - Green BD Seeds: 9 emeralds each (requires Farmer rank)
   - Purple BD Seeds: 25 emeralds each (requires Master Farmer rank)
   - BD Harvester: 16 diamonds (requires Expert Farmer rank)
   - Ultimate BD Harvester: 32 diamonds (available only from Seasonal Traders, requires Agricultural Expert rank)

3. **Trading Process**:
   - Right-click the dealer to open the trading menu
   - Place the required emeralds/diamonds in the left slot
   - Take the purchased items from the right slot
   - Each successful trade gives +2 reputation with that specific dealer

### Trading with BD Collectors

BD Collectors purchase your harvested crops:

1. **Finding Collectors**:
   - Collectors only spawn in player markets when House Tokens are placed
   - Collectors have a blue name tag and LIBRARIAN profession
   - Each market can have multiple collectors (based on market level)
   - Collectors never spawn naturally in villages - they must be created in markets

2. **Available Trades**:
   - Regular BD Crops: 10 crops for 2 emeralds + 50 server currency
   - Green BD Crops: 5 crops for 10 emeralds + 150 server currency
   - Purple BD Crops: 3 crops for 20 emeralds + 400 server currency
   - Bulk Trade: 50 regular BD crops for 1 diamond

3. **Trading Process**:
   - Right-click the collector to open the trading menu
   - Place your crops in the left slot
   - Take the emeralds/diamonds from the right slot
   - Server currency is automatically added to your balance
   - Each successful trade gives +3 reputation with that specific collector

### Trading with Seasonal Traders

Seasonal Traders offer unique, limited-time items:

1. **Finding Seasonal Traders**:
   - Seasonal Traders only appear in level 3+ player markets
   - They have a yellow name tag and NITWIT profession
   - Their trades change with the Minecraft season/month

2. **Available Trades**:
   - Varies by season
   - Always includes some purple BD seeds
   - May include Ultimate BD Harvester (32 diamonds)
   - Usually offers exclusive seasonal items

3. **Trading Process**:
   - Right-click the seasonal trader to open the trading menu
   - Available trades change every Minecraft month (20 real-world days)
   - Each successful trade gives +4 reputation with that specific trader

## Collector-Based Reputation System

Reputation is tracked per individual trader (not per village):

1. **Reputation Tiers**:
   - Hostile (-100 to -50): +50% prices when buying, -50% value when selling
   - Unfriendly (-49 to -10): +25% prices when buying, -25% value when selling
   - Neutral (-9 to 9): Standard prices
   - Friendly (10 to 49): -10% prices when buying, +10% value when selling
   - Hero (50 to 100): -20% prices when buying, +20% value when selling

2. **Gaining Reputation**:
   - Successfully trading with Dealers: +2 reputation with that specific dealer
   - Successfully trading with Collectors: +3 reputation with that specific collector
   - Successfully trading with Seasonal Traders: +4 reputation with that specific trader
   - Reputation gain is increased by rank bonuses

3. **Losing Reputation**:
   - Failed trades (insufficient items): -1 reputation with that specific trader
   - Attacking villagers: -10 reputation with that specific trader
   - Killing villagers: -50 reputation with that specific trader and -20 with others in the same market

4. **Checking Reputation**:
   - Use `/bdrep` to check reputation with the nearest BD trader
   - Use `/bdrep all` to check reputation with all traders you've interacted with
   - Use `/bdrep market` to check reputation with all traders in your current market

## Player-to-Player Trading

### Direct Currency Transfer

Players can send server currency directly to each other:

1. **Command**: `/bdpay <player> <amount>`
2. **Example**: `/bdpay Steve 500`
3. **Requirements**:
   - Must have sufficient balance
   - Both players must be online
   - Permission: `bdcraft.pay`

### Auction House System

The BDCraft auction house allows players to buy and sell BD items:

1. **Accessing the Auction House**:
   - Command: `/bdah`
   - Permission: `bdcraft.auction.use`

2. **Selling Items**:
   - Hold the BD item in your hand
   - Command: `/bdah sell <price> [amount]`
   - Example: `/bdah sell 15 5` (sells 5 of the held item for 15 currency each)
   - Listing fee: 5% of listing price

3. **Buying Items**:
   - Browse listings in the auction house GUI
   - Click on an item to purchase
   - The item will be added to your inventory and currency deducted

4. **Auction House Restrictions**:
   - Only BD items can be sold (seeds, tools)
   - BD crops (harvested crops) cannot be traded
   - Listings expire after 3 days if not sold
   - Maximum 10 active listings per player

5. **Managing Listings**:
   - View your listings: `/bdah listings`
   - Cancel a listing: `/bdah cancel <listing_id>`

## Trading Strategies

### For New Players

1. **Starting Out**:
   - Find natural dealers in villages or create your own market
   - Buy regular BD seeds from dealers
   - Plant and harvest for regular BD crops
   - Build your own market with collectors to sell crops
   - Sell to collectors for emeralds and currency
   - Save currency for rank upgrades

2. **Efficiency Tips**:
   - Focus on building reputation with one specific collector
   - Create your own market as soon as possible for reliable trading
   - Craft a BD Stick for temporary farming buffs
   - Plant crops in organized rows for easy harvesting
   - Use the Market GUI to manage your market effectively

### For Advanced Players

1. **Maximizing Profits**:
   - Invest in a BD Harvester as soon as possible
   - Focus on green and purple crops once available
   - Build reputation to Hero status with specific collectors
   - Upgrade your market to level 3 to attract Seasonal Traders
   - Build multiple collector houses for diversified trading

2. **Rank Progression Strategy**:
   - Advance to Farmer rank quickly for green seeds
   - Save for Expert Farmer to access the BD Harvester
   - Master Farmer unlocks purple seeds (highest value)
   - Agricultural Expert grants significant trading advantages
   - Use Market GUI to control who can trade with your collectors

### Auction House Strategy

1. **Selling Strategy**:
   - Sell regular seeds in bulk for new players
   - Sell green/purple seeds individually for maximum profit
   - Price slightly below dealer prices to ensure sales
   - List during peak server hours for better visibility

2. **Buying Strategy**:
   - Look for seeds being sold below dealer prices
   - Buy in bulk when possible for better value
   - Check regularly for rare items from seasonal traders

## Trading Calendar

Seasonal Traders offer special items based on the Minecraft calendar:

- **Spring** (Days 0-5000):
  - Faster-growing seed variants
  - More favorable green seed trades

- **Summer** (Days 5001-10000):
  - Heat-resistant seeds that don't require water
  - BD Harvester with bonus durability

- **Fall** (Days 10001-15000):
  - Purple seed discount
  - Special autumn-themed items

- **Winter** (Days 15001-20000):
  - Cold-resistant seeds
  - Special winter-themed items
  - End-of-year special offers

This trading guide covers all aspects of the BDCraft economy system, from basic villager trading to advanced auction house strategies. Understanding these mechanics will help you maximize your profits and progress efficiently through the BD ranks.