CREATE TABLE app_user
(
    id               BIGSERIAL PRIMARY KEY,
    external_auth_id VARCHAR(255) NOT NULL UNIQUE,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255)

);

CREATE TABLE portfolio
(
    id               BIGSERIAL PRIMARY KEY,
    owner_id         BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    CONSTRAINT fk_portfolio_owner
        FOREIGN KEY (owner_id)
            REFERENCES app_user (id)
);

CREATE TABLE holding
(
    id               BIGSERIAL PRIMARY KEY,
    portfolio_id     BIGINT         NOT NULL,
    symbol           VARCHAR(255)   NOT NULL,
    quantity         NUMERIC(20, 8) NOT NULL,
    average_cost     NUMERIC(19, 4) NOT NULL,
    version          BIGINT         NOT NULL DEFAULT 0,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    CONSTRAINT fk_holding_portfolio
        FOREIGN KEY (portfolio_id)
            REFERENCES portfolio (id),
    CONSTRAINT uq_holding_portfolio
        UNIQUE (portfolio_id, symbol)
);

CREATE TABLE stock_transaction
(
    id               BIGSERIAL PRIMARY KEY,
    holding_id       BIGINT         NOT NULL,
    type             VARCHAR(10)    NOT NULL,
    quantity         NUMERIC(20, 8) NOT NULL,
    price_per_share  NUMERIC(19, 4) NOT NULL,
    executed_at      TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    CONSTRAINT fk_stock_transaction_holding
        FOREIGN KEY (holding_id)
            REFERENCES holding (id),
    CONSTRAINT chk_stock_transaction_type
        CHECK (type IN ('BUY', 'SELL')),
    CONSTRAINT chk_stock_transaction_quantity
        CHECK (quantity > 0),
    CONSTRAINT chk_stock_transaction_price
        CHECK (price_per_share >= 0)
);



CREATE INDEX idx_fk_portfolio_owner_id
    ON portfolio (owner_id);

CREATE INDEX idx_stock_transaction_holding_executed
    ON stock_transaction (holding_id, executed_at DESC);



