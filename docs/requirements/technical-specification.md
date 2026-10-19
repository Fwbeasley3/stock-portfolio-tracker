REST Client
|
| HTTP/JSON + Bearer JWT
v
API Gateway (Spring Boot)
| - validates token and request shape
| - converts JSON DTOs to Protobuf messages
| - enforces RPC deadlines, maps errors to HTTP
| - propagates cancellation and trace context
|
+---- unary gRPC ----> Portfolio Service ----> PostgreSQL
|                        - tenant-scoped queries
|                        - transactional CRUD/upsert
|                        - portfolio valuation aggregation
|
+---- unary gRPC ----> Market Data Service
|                        - internal simulated price store
|                        - deterministic mock price generator
|
+---- server-streaming gRPC (internal) ----> Market Data Service
- scheduled simulated price ticks