# Donor Benefits

Donors receive special perks and privileges across all aspects of BDCraft as a thank you for supporting the server.

## How to Become a Donor

Players can become donors through:
- Server website purchase (processed by an administrator)
- In-game verification using `/donorverified [username]` (admin only command)

## General Donor Benefits

Donors receive these core benefits:

- **Remote Market Access**: Use `/bdmarket` to access your Market Owner GUI from anywhere
- **Multiple Homes**: 3 homes instead of the standard 1 home
- **Reduced Auction House Fee**: Fixed 2% fee instead of the standard 5%
- **Rank Discount**: 15% discount on all rank upgrade costs
- **Currency Retention**: Keep 20% of currency after rebirth (regular players lose all currency)
- **Auction Listing Priority**: Donor auctions appear first on the first page

## Enhanced Benefits by Category

### Market System

- **Multiple Markets**: Permission to own multiple markets (bdcraft.market.multiple)
- **Market Benefits**: 10% better trading prices compared to regular players of the same rank

### Auction House

- **Fixed Fee**: 2% auction house fee (vs. 5% for regular players)
- **Inflation Protection**: When seed prices are above 500 server currency:
  - Donor fee: 20% (vs. 30% for regular players)
  - Fee increases by only 1% per 50 server currency above threshold (vs. 3% for regular players)

### Rebirth System

- **Currency Retention**: Keep 20% of server currency when performing rebirth
- **Rebirth Bonuses**: 10% increase to all rebirth-related bonuses

### Rank System

- **Enhanced Perks**: All rank perks are increased by 10%
- **Discount**: 15% discount on all rank upgrade costs

## Configuration

Server administrators can configure donor benefits in the plugin configuration:

```yaml
donor:
  enabled: true
  benefits:
    multiple-markets: true
    remote-market-access: true
    homes: 3
    auction-fee: 2.0
    rank-discount: 15
    rebirth-retention: 20
    auction-priority: true
    inflation-protection: true
```