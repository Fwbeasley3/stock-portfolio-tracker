Client / Postman
|
| HTTPS REST / JSON + Bearer JWT
v
Portfolio Service (Spring Boot)
- REST controllers and HTTP error responses
- JWT authentication and portfolio ownership authorization
- portfolio/holding business rules and transactional persistence
- portfolio valuation and gRPC client
  |                                   |
  | JPA / JDBC                        | unary gRPC GetQuote
  v                                   | server-streaming gRPC StreamPrices
  PostgreSQL                               v
  Market Data Service (Spring Boot)
  - internal deterministic mock quotes
  - background simulated price updates
  - gRPC unary + server-streaming server