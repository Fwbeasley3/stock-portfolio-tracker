### FR-001: Portfolio Management

**Status:** Approved  
**Priority:** High

**Description:**

The system shall allow authenticated users to create, retrieve, update, and delete investment portfolios.

Each user may own multiple portfolios. Each portfolio belongs to exactly one user.

**Acceptance Criteria:**

1. An authenticated user can create a portfolio with a name and optional description.
2. An authenticated user can retrieve a list of all portfolios they own.
3. An authenticated user can retrieve an individual portfolio they own.
4. An authenticated user can update the name or description of a portfolio they own.
5. An authenticated user can delete a portfolio they own only when the portfolio contains no holdings.
6. The system shall prevent users from accessing, modifying, or deleting portfolios belonging to another user.
7. The system shall require portfolio names to contain between 1 and 100 Unicode code points after trimming leading and trailing whitespace.
8. The system shall return an appropriate error when a requested portfolio does not exist or is not accessible to the user.
9. The system shall reject attempts to delete a portfolio containing one or more holdings and return an appropriate conflict error.
10. The system shall require portfolio names to be unique within the set of portfolios owned by the same authenticated user.
11. The system shall allow different authenticated users to create portfolios with identical names.
12. The system shall reject attempts to create a portfolio with a name that already exists for the authenticated user.
13. The system shall reject attempts to rename a portfolio to a name already used by another portfolio owned by the same authenticated user.
14. The system shall remove leading and trailing whitespace from portfolio names before storing them.
15. The system shall compare portfolio names case-insensitively when enforcing uniqueness within the same user's portfolios.
16. The system shall preserve the user's capitalization in the stored and displayed portfolio name after trimming leading and trailing whitespace.
17. The system shall apply the same portfolio name normalization and uniqueness rules during both portfolio creation and renaming.
18. The system shall allow portfolio descriptions to be omitted.
19. The system shall reject portfolio descriptions exceeding 500 Unicode code points after trimming leading and trailing whitespace.
20. The system shall reject portfolio creation and renaming requests when the normalized portfolio name is blank or exceeds 100 Unicode code points.
21. The system shall apply the same name and description length validation rules during portfolio creation and updates.
22. The system shall enforce a configurable maximum number of portfolios per authenticated user, initially set to 100.
23. The system shall allow an authenticated user to create a portfolio when their current portfolio count is below the configured maximum.
24. The system shall reject portfolio creation when the authenticated user has reached the configured maximum, returning an appropriate error.
25. The system shall enforce the portfolio limit consistently when concurrent portfolio creation requests are received for the same user.

**Out of Scope (Version 1):**

- Sharing portfolios between users.
- Collaborative portfolio management.

**Open Business Rules:**

**Resolved Business Rule: Portfolio Name Uniqueness**

- Portfolio names shall be unique per authenticated user.
- Different users may have portfolios with identical names.
- Portfolio name uniqueness shall be enforced during creation and renaming.
- Duplicate portfolio names shall be rejected with an appropriate conflict error.
- Portfolio uniqueness shall be enforced consistently at the application and persistence layers.
- Portfolio names shall be trimmed of leading and trailing whitespace before storage.
- Portfolio name uniqueness comparisons shall be case-insensitive.
- The original capitalization shall be preserved for display.
- The same normalization and uniqueness rules shall apply during creation and renaming.

**Resolved Business Rule: Portfolio Metadata Limits**

- Portfolio names are required.
- Portfolio names shall contain between 1 and 100 Unicode code points after trimming leading and trailing whitespace.
- Portfolio descriptions are optional.
- Portfolio descriptions shall contain no more than 500 Unicode code points after trimming leading and trailing whitespace.
- Leading and trailing whitespace shall be removed from portfolio names and descriptions before storage.
- Empty or whitespace-only portfolio names shall be rejected.
- Missing or blank descriptions shall be treated as no description.
- Metadata validation shall apply consistently during portfolio creation and updates.

**Resolved Business Rule: Maximum Portfolios Per User**

- Each authenticated user may own up to 100 portfolios by default.
- The maximum portfolio count shall be configurable.
- The limit shall apply independently to each authenticated user.
- Portfolio creation shall be rejected when the configured maximum has been reached.
- Deleting an eligible portfolio shall free capacity for creating another portfolio.
- Concurrent creation requests shall not allow a user to exceed the configured maximum.

### FR-002: Holding Management

**Status:** Approved — Initial Scope  
**Priority:** High

**Description:**

The system shall allow authenticated users to manage eligible stock and ETF holdings within portfolios they own.

Each holding shall contain a supported security symbol, number of shares owned, and average purchase cost per share.

**Acceptance Criteria:**

1. An authenticated user can add a stock holding to a portfolio they own.
2. An authenticated user can retrieve all holdings associated with a portfolio they own.
3. The system shall allow authenticated users to correct the average purchase cost of holdings in portfolios they own, while requiring quantity changes to use dedicated share addition or reduction operations and prohibiting direct changes to holding security identity.
4. An authenticated user can remove a holding from a portfolio they own.
5. Each holding shall contain a security symbol that passes format validation and is verified by the configured market data provider as an eligible USD-denominated individual stock or ETF listed on a supported U.S. exchange before the holding is created.
6. The system shall support positive fractional share quantities with up to six decimal places.
7. The system shall allow a holding's average purchase cost per share to be zero or greater, but shall reject negative purchase costs.
8. The system shall calculate the total cost basis of a holding as:

   `Total Cost Basis = Shares × Average Cost Per Share`

9. The system shall calculate unrealized gain or loss when a usable market price is available:

   `Unrealized Gain/Loss = Market Value − Total Cost Basis`

10. The system shall prevent users from accessing or modifying holdings belonging to another user's portfolio.
11. The system shall reject invalid holding information with an appropriate validation error.
12. When a user adds shares of a stock already held in the same portfolio, the system shall merge the additional shares into the existing holding rather than create a duplicate holding record.
13. The system shall reject share quantities containing more than six decimal places.
14. The system shall preserve the supported share quantity precision when storing, retrieving, and calculating holding values.
15. The system shall reject attempts to add holdings with invalid or unsupported stock ticker symbols.
16. When ticker validation cannot be completed because the market data provider is unavailable, the system shall reject the creation request with a temporary validation failure rather than treating the symbol as invalid.
17. When merging holdings, the system shall calculate the new share quantity as the sum of the existing quantity and the additional quantity.
18. When merging holdings, the system shall recalculate the average cost per share using the weighted average cost of the existing and additional shares.
19. The system shall maintain one aggregate holding per stock ticker symbol within each portfolio.
20. The system shall allow an authenticated user to reduce the share quantity of a holding belonging to a portfolio they own.
21. The system shall reject attempts to reduce a holding by a quantity greater than the number of shares currently owned.
22. When reducing shares, the system shall maintain the existing average cost per share and recalculate the remaining total cost basis.
23. When the reduction quantity equals the total shares owned, the system shall remove the holding from the portfolio.
24. The system shall preserve sufficient decimal precision when storing share quantities, average purchase costs, and holding cost basis.
25. The system shall perform holding-related financial calculations using decimal arithmetic rather than binary floating-point arithmetic.
26. The system shall avoid rounding intermediate financial calculations when merging holdings or calculating cost basis, except where an explicitly defined precision limit requires rounding.
27. The system shall round displayed USD holding monetary totals to two decimal places using the HALF_EVEN rounding mode.
28. The system shall allow holdings with a zero average purchase cost per share.
29. The system shall calculate a holding's total cost basis as zero when its average purchase cost per share is zero.
30. The system shall allow users to correct a holding's average purchase cost per share to a value greater than or equal to zero.
31. When a holding's average purchase cost per share is corrected, the system shall recalculate its total cost basis using the unchanged share quantity.
32. The system shall not allow direct modification of a holding's share quantity through the cost-basis correction operation.
33. Changes to a holding's share quantity shall be performed through the defined add-shares or reduce-shares operations.
34. The system shall enforce a configurable maximum number of unique security holdings per portfolio, initially set to 500.
35. The system shall allow users to add a new stock ticker symbol to a portfolio when the number of unique holdings is below the configured maximum.
36. The system shall reject attempts to add a new stock ticker symbol when the portfolio has reached the configured maximum number of unique holdings.
37. The system shall allow users to add shares to an existing holding even when the portfolio has reached the configured maximum number of unique holdings.
38. The system shall enforce the unique holdings limit consistently when concurrent holding creation requests are received for the same portfolio.
39. The system shall support eligible USD-denominated individual stocks and exchange-traded funds (ETFs) listed on supported U.S. exchanges for Version 1.
40. The system shall reject attempts to add holdings whose securities are listed exclusively on unsupported exchanges, are denominated in currencies other than USD, or are outside the supported security types.
41. The system shall validate market and currency eligibility using trusted security metadata from the configured market data provider.
42. The system shall verify the security type of a requested holding using trusted metadata from the configured market data provider.
43. The system shall reject unsupported security types, including mutual funds, options, bonds, and cryptocurrencies, in Version 1.
44. The system shall apply the same share quantity, cost basis, holding management, and valuation rules to supported individual stocks and ETFs.
45. The system shall treat the security identity associated with a holding as immutable after the holding is created.
46. The system shall reject attempts to change the ticker symbol or security identity of an existing holding through a holding update operation.
47. The system shall allow an authenticated user to remove an existing holding from a portfolio they own.
48. To correct an incorrectly entered security, the user shall remove the incorrect holding and create a new holding for the intended security.
49. A replacement holding shall be subject to the same security validation, uniqueness, portfolio capacity, quantity, and cost basis rules as any newly created holding.

**Out of Scope (Version 1):**

- Individual buy and sell transaction history.
- Realized capital gains and losses.
- Tax-lot accounting.
- Dividend tracking.
- Automated brokerage synchronization.
- Corporate-action notifications and stock split adjustments.
- Automatic handling of official ticker-symbol changes.

**Resolved Business Rule: Portfolio Deletion and Holdings**

- A portfolio may be deleted only when it contains no holdings, as defined in FR-001.
- Users must remove all holdings before deleting their portfolio.

**Resolved Business Rule: Stock Ticker Validation**

- Stock ticker symbols must pass format validation.
- Stock ticker symbols must be verified against the configured market data provider before a new holding is created.
- Invalid or unsupported ticker symbols shall be rejected.
- Provider outages shall be distinguished from invalid symbols.
- Stock ticker symbols shall be normalized to a consistent format before storage.

**Resolved Business Rule: Duplicate Holding Management**

- A portfolio shall contain only one aggregate holding per stock ticker symbol.
- Adding additional shares of an existing stock shall merge them into the existing holding.
- The resulting quantity shall equal the sum of existing and additional shares.
- The average cost per share shall be recalculated using a weighted average.
- Merging holdings shall preserve the correct total cost basis.
- Individual purchase transactions shall not be stored in Version 1.

**Resolved Business Rule: Share Reduction**

- Users may reduce the quantity of shares held within a portfolio.
- Share reductions must be greater than zero and cannot exceed the current share quantity.
- Share reductions shall support up to six decimal places.
- The average cost per share shall remain unchanged when reducing shares.
- The total cost basis shall be recalculated using the remaining share quantity.
- Reducing a holding to zero shares shall remove the holding from the portfolio.
- Share reductions shall not generate realized gain/loss calculations in Version 1.

**Resolved Business Rule: Holding Monetary Precision**

- Share quantities shall support up to six decimal places.
- Average purchase costs and calculated cost basis shall retain higher internal decimal precision.
- Financial calculations shall use decimal arithmetic.
- Intermediate calculations shall preserve precision within the application's defined numeric limits.
- Displayed USD monetary totals shall use HALF_EVEN rounding to two decimal places.
- The exact internal numeric precision and scale shall be defined before implementation.

**Resolved Business Rule: Zero Purchase Cost**

- A holding's average purchase cost per share may be zero.
- Negative purchase costs shall be rejected.
- Holdings with a zero average purchase cost shall have a zero cost basis.
- A zero purchase cost shall be treated as an explicitly supplied value, not as a missing or unknown purchase cost.
- Unrealized dollar gain/loss calculations shall remain available when a usable market price exists.

**Resolved Business Rule: Holding Corrections**

- Users may correct the average purchase cost per share of an existing holding.
- Corrected average purchase costs must be greater than or equal to zero.
- Cost-basis corrections shall not change the holding's share quantity.
- The holding's total cost basis shall be recalculated after a correction.
- Share quantity changes must use the add-shares or reduce-shares operations.
- Cost-basis corrections shall not create individual purchase or sale transactions.

**Resolved Business Rule: Maximum Holdings Per Portfolio**

- Each portfolio may contain up to 500 unique eligible stock or ETF holdings by default.
- The maximum unique holdings count shall be configurable.
- The limit shall apply independently to each portfolio.
- Each distinct normalized ticker symbol shall count as one holding.
- Adding shares to an existing holding shall not increase the unique holdings count.
- Adding a new ticker symbol shall be rejected when the configured maximum has been reached.
- Removing a holding completely shall free capacity for another unique holding.
- Concurrent holding creation requests shall not allow a portfolio to exceed the configured maximum.

**Resolved Business Rule: Supported Holdings and Markets**

- Version 1 shall support eligible USD-denominated individual stocks and ETFs listed on supported U.S. exchanges.
- All holding purchase costs shall be expressed in USD.
- Security symbols must be validated against the configured market data provider.
- Security type, exchange eligibility, and trading currency must be verified before a new holding is created.
- Securities listed exclusively on unsupported exchanges shall be rejected.
- Non-USD-denominated securities shall be rejected.
- Mutual funds, options, bonds, cryptocurrencies, and other unsupported security types shall be excluded from Version 1.
- The set of supported U.S. exchanges shall be explicitly defined before implementation.
- Foreign exchange conversion shall not be supported in Version 1.

**Resolved Business Rule: Holding Security Identity**

- A holding's security identity shall be immutable after creation.
- Users shall not be permitted to directly edit the ticker symbol or security identity of an existing holding.
- Correcting an incorrectly entered security shall require removing the existing holding and creating a new holding.
- Removing a holding for correction shall not be recorded as a sale transaction.
- Newly created replacement holdings shall follow all existing validation and business rules.
- Quantity adjustments and average purchase cost corrections shall remain permitted through their previously defined operations.

### FR-003: Stock Pricing and Valuation

**Status:** Approved — Initial Scope  
**Priority:** High

**Description:**

The system shall provide the latest available market prices for supported stocks and ETFs and use those prices to calculate the current market value of individual holdings and investment portfolios.

The system shall communicate pricing freshness and availability to users.

**Acceptance Criteria:**

1. The system shall retrieve the latest available stock price from a configured market data provider.
2. The system shall allow users to view the latest available price for each stock holding.
3. Each stock price shall include the ticker symbol, price, currency, and timestamp indicating when the price was observed.
4. The system shall indicate whether a stock price is fresh, delayed, or stale according to defined freshness rules.
5. The system shall calculate the market value of a holding as:

   `Market Value = Shares Owned × Latest Available Price`

6. The system shall calculate unrealized gain or loss as:

   `Unrealized Gain/Loss = Market Value − Total Cost Basis`

7. The system shall calculate a portfolio's total market value by summing the market values of its holdings when usable prices are available for all holdings.
8. When one or more holdings lack usable market prices, the system shall return the sum of available holding market values as a partial valuation, clearly identify the valuation as incomplete, and identify the holdings with unavailable prices.
9. When the market data provider is unavailable during market hours, the system shall use a previously retrieved stock price if its age does not exceed five minutes. The system shall clearly identify the price as stale. The maximum stale-price age shall be configurable.
10. The system shall not present delayed or stale pricing information as real-time market data.
11. The system shall not use a previously retrieved stock price for valuation when its age exceeds the configured maximum stale-price age.
12. When no usable stock price is available, the system shall indicate that the affected holding's market value cannot be calculated.
13. The system shall support verifying whether a stock ticker symbol is recognized and supported by the configured market data provider.
14. The system shall distinguish between unsupported ticker symbols and temporary failures when validating ticker symbols.
15. When the regular stock market is closed, the system shall use the latest available official closing price for portfolio valuation.
16. The system shall clearly identify prices originating from an official market close and provide the corresponding market session date or observation timestamp.
17. The system shall distinguish official closing prices from stale intraday prices when determining whether a price is eligible for portfolio valuation.
18. The system shall not apply the five-minute intraday stale-price fallback limit to valid official closing prices while the market is closed.
19. The system shall retrieve USD-denominated market prices for supported individual stocks and ETFs.
20. The system shall verify that retrieved pricing data corresponds to the requested supported security and its eligible market listing.
21. The system shall reject or mark as unavailable pricing data that does not satisfy the supported market and currency requirements.
22. The system shall not perform foreign exchange conversion when calculating holding or portfolio market values in Version 1.
23. The system shall support retrieving and valuing eligible individual stocks and ETFs using the same core market-value calculation.
24. The system shall identify the security type associated with pricing metadata when that information is available from the configured market data provider.
25. The system shall not use pricing data for unsupported security types when calculating portfolio valuations.

**Out of Scope (Version 1):**

- Real-time streaming stock prices.
- Historical stock-price charting.
- Predictive price analytics.
- Automated trading.
- Multi-currency conversion.

**Open Business Rules:**

- Define the supported market data provider and its quote limitations.

**Resolved Business Rule: Stale-Price Fallback**

- During market hours, the maximum permitted age for stale-price fallback shall initially be five minutes.
- The maximum stale-price age shall be configurable.
- Quote age shall be determined using the market price observation timestamp.
- Quotes exceeding the maximum permitted age shall not be used in portfolio valuation.
- Separate pricing freshness rules for after-hours trading, weekends, and market holidays remain to be defined.

**Resolved Business Rule: Partial Portfolio Valuation**

- The system shall support partial portfolio valuations.
- Holdings with usable market prices shall contribute to the partial market value.
- Holdings without usable market prices shall be excluded from the partial market value.
- Missing market prices shall not be treated as zero.
- The system shall identify holdings with unavailable pricing.
- The system shall clearly indicate that the valuation is incomplete.
- A partial market value shall not be presented as the complete portfolio market value.

**Resolved Business Rule: Market-Closed Pricing**

- When the regular market is closed, the system shall use the latest available official closing price for valuation.
- Official closing prices shall be clearly identified as closing prices.
- The system shall provide the relevant market session date or observation timestamp.
- The five-minute intraday stale-price fallback limit shall not apply to valid official closing prices while the market is closed.
- The system shall not treat an arbitrary cached intraday price as an official closing price.
- If the latest required official closing price cannot be obtained or verified, the system shall indicate that pricing is unavailable.
- Extended-hours trading prices are out of scope for Version 1.

**Resolved Business Rule: Pricing Currency and Market Scope**

- Version 1 pricing shall support eligible U.S.-listed, USD-denominated individual stocks and ETFs.
- Retrieved market prices used for valuation must be denominated in USD.
- Pricing data must correspond to the intended supported security and listing.
- Security type and exchange eligibility shall be verified using trusted provider metadata.
- Unsupported securities and currencies shall not be used for portfolio valuation.
- Stocks and ETFs shall use the same core market-value calculation.
- Foreign exchange conversion shall be out of scope for Version 1.
- The configured provider must supply sufficient metadata to verify market, security type, and currency eligibility.

### FR-004: User Authentication and Authorization

**Status:** Approved — Initial Scope  
**Priority:** High

**Description:**

The system shall require users to authenticate through an external identity provider using OpenID Connect and OAuth 2.0 standards.

The system shall authorize access to portfolio resources based on the authenticated user's identity and resource ownership.

**Acceptance Criteria:**

1. Users shall authenticate through a supported external identity provider.
2. The system shall require a valid access token to access protected portfolio operations.
3. The system shall reject requests with missing, expired, invalid, or otherwise unacceptable access tokens.
4. The system shall identify the authenticated user using a trusted identity claim from the validated token.
5. Each portfolio shall be associated with exactly one authenticated user.
6. Authenticated users shall only be permitted to access portfolios they own.
7. Authenticated users shall only be permitted to access or modify holdings associated with portfolios they own.
8. The system shall prevent a user from assigning or changing portfolio ownership by manipulating request data.
9. The system shall return appropriate authorization errors when access is denied.
10. Users shall not be required to create or manage passwords directly within the Stock Portfolio Tracker.

**Out of Scope (Version 1):**

- Custom password storage and management.
- Multi-factor authentication implementation within the application.
- Administrative access to other users' portfolios.
- Portfolio sharing and delegated permissions.
- Custom identity provider development.

**Open Business Rules:**

- Select an external identity provider.
- Define whether users must explicitly register or are automatically provisioned after first authentication.
- Define account deletion and data retention behavior.

### FR-005: Portfolio Summary and Performance

**Status:** Approved — Initial Scope  
**Priority:** High

**Description:**

The system shall provide authenticated users with a financial summary of each portfolio they own, including total market value, total cost basis, unrealized gain or loss, and unrealized return percentage.

The system shall also provide corresponding financial calculations for individual holdings.

**Acceptance Criteria:**

1. An authenticated user can retrieve the financial summary of a portfolio they own.
2. The system shall calculate the total cost basis by summing the cost basis of all holdings in the portfolio.
3. The system shall calculate the total market value by summing the market values of all holdings, provided usable market prices are available.
4. The system shall calculate unrealized gain or loss as total market value minus total cost basis.
5. The system shall calculate unrealized return percentage as unrealized gain or loss divided by total cost basis, multiplied by 100, when total cost basis is greater than zero.
6. The system shall provide market value, cost basis, unrealized gain or loss, and unrealized return percentage for each holding.
7. When one or more holdings lack usable market prices, the system shall provide a partial market value for holdings with usable prices and clearly identify the portfolio valuation as incomplete.
8. The system shall communicate when a valuation includes delayed or stale pricing information.
9. Users shall not be permitted to retrieve financial summaries for portfolios they do not own.
10. The system shall calculate financial values using appropriate decimal precision and consistent rounding rules.
11. An empty portfolio shall have a total cost basis and market value of zero, with unrealized return percentage reported as unavailable.
12. When a portfolio valuation is incomplete, the system shall not report the partial market value as the complete portfolio market value or calculate a complete portfolio unrealized gain/loss or return percentage from that partial value.
13. The system shall continue to provide the total cost basis of all holdings when market pricing information is partially or completely unavailable.
14. The system shall calculate portfolio cost basis, market value, and unrealized gain/loss using decimal arithmetic.
15. The system shall preserve sufficient internal precision when aggregating monetary values across multiple holdings.
16. The system shall round displayed USD portfolio monetary totals to two decimal places using the HALF_EVEN rounding mode.
17. The system shall avoid cumulative rounding errors caused by rounding individual holding calculations before portfolio aggregation.
18. When a holding has a zero cost basis and a usable market price, the system shall calculate its unrealized dollar gain/loss as market value minus zero.
19. When a holding has a zero cost basis, the system shall report its unrealized gain/loss percentage as unavailable.
20. When a portfolio's total cost basis is zero, the system shall report its portfolio-level unrealized gain/loss percentage as unavailable.
21. The system shall not represent an undefined gain/loss percentage as zero, 100%, infinity, or another numeric value.
22. The system shall calculate and report holding and portfolio monetary values in USD for Version 1.
23. The system shall aggregate portfolio market value, cost basis, and unrealized gain/loss using USD-denominated values without foreign exchange conversion.
24. The system shall identify USD as the currency associated with reported monetary amounts.

**Out of Scope (Version 1):**

- Historical portfolio performance charts.
- Daily portfolio performance comparisons.
- Time-weighted and money-weighted returns.
- Portfolio benchmarking against market indexes.
- Investment allocation analytics.

**Open Business Rules:**

- Define whether delayed and stale quotes are permitted in portfolio totals.

**Resolved Business Rule: Portfolio Monetary Precision**

- Portfolio financial calculations shall use decimal arithmetic.
- Holding-level values shall be aggregated using their internal precision.
- Intermediate calculations shall not be rounded prematurely.
- Displayed USD monetary totals shall be rounded to two decimal places using HALF_EVEN.
- The exact internal numeric precision and scale shall be defined before implementation.

**Resolved Business Rule: Zero Cost Basis**

- Zero cost basis is permitted.
- Unrealized dollar gain/loss may be calculated when a usable market price exists.
- Unrealized gain/loss percentages require a cost basis greater than zero.
- Holdings with zero cost basis shall return an unavailable percentage.
- Portfolios with total cost basis equal to zero shall return an unavailable percentage.
- Undefined percentages shall not be represented as numeric values.

**Resolved Business Rule: Portfolio Reporting Currency**

- USD shall be the sole reporting currency for Version 1.
- Holding and portfolio monetary amounts shall be calculated in USD.
- Portfolio summaries shall explicitly identify USD as the reporting currency.
- No currency conversion shall be performed.
- Existing monetary precision and HALF_EVEN rounding rules shall apply to USD value