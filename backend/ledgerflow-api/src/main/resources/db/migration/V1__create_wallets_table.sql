CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(100) NOT NULL,
    currency CHAR(3) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_wallet_balance_non_negative
        CHECK (balance >= 0),

    CONSTRAINT chk_wallet_status
        CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED')),

    CONSTRAINT chk_wallet_currency
        CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE INDEX idx_wallets_customer_id
    ON wallets(customer_id);
