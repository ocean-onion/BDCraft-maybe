# BD Auction House

The BDCraft Auction House is a specialized marketplace that allows players to buy and sell BD items. This system creates a player-driven economy alongside the villager trading system.

## Accessing the Auction House

### Commands

- **Open the Auction House**: `/bdah`
- **View Your Listings**: `/bdah listings`
- **Sell an Item**: `/bdah sell <price> [amount]`
- **Cancel a Listing**: `/bdah cancel <listing_id>`

### Permissions

- `bdcraft.auction.use` - Access the auction house
- `bdcraft.auction.sell` - Sell items on the auction house
- `bdcraft.auction.cancel` - Cancel your listings
- `bdcraft.auction.list` - View your listings

## Auction House Interface

The auction house uses a chest GUI interface with several pages:

### Main Browsing Interface

- **Top Row**: Navigation controls and category filters
- **Middle Rows**: Item listings with prices
- **Bottom Row**: Personal controls (your listings, sell item, etc.)

### Category Filters

- **All Items**: View all listings
- **Seeds**: Filter for BD seeds only
- **Tools**: Filter for BD tools only
- **Special Items**: Filter for rare/seasonal items

### Listing Details

Each listing shows:
- Item with custom name and lore
- Price (per item)
- Quantity available
- Seller name
- Time remaining before expiration

## Selling Items

### Listing Process

1. Hold the BD item you want to sell in your main hand
2. Use the command: `/bdah sell <price> [amount]`
   - Example: `/bdah sell 15 5` (sells 5 of the held item for 15 currency each)
3. Confirm the listing in the confirmation GUI
4. Pay the listing fee (deducted automatically)

### Listing Rules and Limitations

- **Allowed Items**:
  - BD Seeds (regular, green, purple)
  - BD Tools (BD Stick, BD Harvester, Ultimate BD Harvester)
  - Special/Seasonal items from Seasonal Traders

- **Prohibited Items**:
  - BD Crops (must be sold to Collectors)
  - Non-BD items (regular Minecraft items)
  - Items without proper BD metadata

- **Listing Limitations**:
  - Maximum 10 active listings per player
  - Listings expire after 3 days if not sold
  - Minimum price: 1 currency
  - Maximum price: 10,000 currency
  - Listing fee: 5% of total listing value

## Buying Items

### Purchase Process

1. Open the auction house with `/bdah`
2. Browse listings or use category filters
3. Click on the item you want to purchase
4. Confirm the purchase in the confirmation GUI
5. Item is added to your inventory and currency is deducted

### Purchase Limitations

- Must have sufficient currency to buy
- Must have inventory space for the items
- Cannot purchase your own listings

## Managing Listings

### Viewing Your Listings

- Use the command: `/bdah listings`
- Shows all your active listings with details
- Shows total potential value if all items sell

### Cancelling Listings

- Use the command: `/bdah cancel <listing_id>`
- Listing ID can be found in your listings page
- Cancelled items are returned to your inventory
- Listing fee is not refunded

### Expired Listings

- Listings automatically expire after 3 days
- Expired items are returned to your mailbox
- Access mailbox with: `/bdmail`
- Unclaimed mailbox items expire after 7 days

## Auction House Economy

### Price Recommendations

- **Regular BD Seeds**:
  - Villager price: 3 emeralds for 5 (0.6 emeralds each)
  - Recommended auction price: 2-5 currency each

- **Green BD Seeds**:
  - Villager price: 9 emeralds each
  - Recommended auction price: 20-40 currency each

- **Purple BD Seeds**:
  - Villager price: 25 emeralds each
  - Recommended auction price: 50-100 currency each

- **BD Harvester**:
  - Villager price: 16 diamonds
  - Recommended auction price: 500-1000 currency

- **Ultimate BD Harvester**:
  - Villager price: 32 diamonds
  - Recommended auction price: 2000-5000 currency

### Market Dynamics

- Prices typically fluctuate based on supply and demand
- Higher-ranked players often pay premium for convenience
- New players typically seek affordable starter packs (multiple regular seeds)
- Rare seasonal items can command high prices

## Rank Benefits for Auction House

Different ranks receive special benefits in the auction house:

- **Newcomer (Rank 1)**:
  - Standard 5% listing fee
  - 3-day listing duration
  - Maximum 10 active listings

- **Farmer (Rank 2)**:
  - 4.5% listing fee
  - 3-day listing duration
  - Maximum 15 active listings

- **Expert Farmer (Rank 3)**:
  - 4% listing fee
  - 4-day listing duration
  - Maximum 20 active listings

- **Master Farmer (Rank 4)**:
  - 3.5% listing fee
  - 5-day listing duration
  - Maximum 25 active listings

- **Agricultural Expert (Rank 5)**:
  - 3% listing fee
  - 7-day listing duration
  - Maximum 30 active listings
  - Special Featured Listings (appear at the top)

## Auction House Tips

### For Sellers

1. **Price Competitively**: Check existing listings before setting your price
2. **Bundle for Beginners**: Sell seed bundles to help new players
3. **List During Peak Hours**: More buyers are online during server peak times
4. **Advertise Rare Items**: Use chat to announce valuable or rare listings
5. **Undercut Villagers**: Price slightly below villager prices to attract buyers

### For Buyers

1. **Compare with Villagers**: Check villager prices before buying
2. **Buy in Bulk**: Look for bundle deals which are typically cheaper per item
3. **Check Seller Reputation**: Higher-ranked sellers typically provide better items
4. **Browse Regularly**: Good deals sell quickly
5. **Look for Off-Season Items**: Seasonal items are often cheaper off-season

The BD Auction House creates a complementary economy alongside the villager trading system, allowing players to buy and sell BD items directly with each other for server currency rather than emeralds or diamonds. This system adds depth to the player economy and provides alternative ways to acquire BD items.