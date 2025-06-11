# BD Auction House

The BDCraft Auction House is a specialized marketplace that allows players to buy and sell BD items through a GUI interface.

## Accessing the Auction House

Players can access the auction house using these commands:
- `/bdauction` or `/auction` - Opens the auction house GUI
- `/bdauction listings` or `/auction listings` - Shows your current auction listings
- `/bdauction sell <price> [amount]` or `/auction sell <price> [amount]` - Lists an item for sale
- `/bdauction cancel <listing_id>` or `/auction cancel <listing_id>` - Cancels one of your listings

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
2. Use the command: `/bdauction sell <price> [amount]`
   - Example: `/bdauction sell 15 5` (sells 5 of the held item for 15 currency each)
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
  - Listing fee: 5% of total listing value for regular players
  - Donor listing fee: Fixed 2% regardless of rank
  - High-value seed listings: 30% fee if priced above 500 currency (20% for donors)
  - Fee increases by 3% for every 50 currency above threshold (1% for donors)

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

- Listings automatically expire after the duration for your rank
  - Newcomer: 3 days
  - Farmer: 3 days
  - Expert Farmer: 4 days
  - Master Farmer: 5 days
  - Agricultural Expert: 7 days
- Expired items are returned to your mailbox
- Access mailbox with: `/bdmail`
- Unclaimed mailbox items expire after 7 days

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