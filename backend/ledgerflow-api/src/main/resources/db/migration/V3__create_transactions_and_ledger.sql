
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    transaction_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    source_wallet_id UUID,
    destination_wallet_id UUID,
    idempotency_key VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_transaction_type
        CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),

    CONSTRAINT chk_transaction_status
        CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),

    CONSTRAINT chk_transaction_amount
        CHECK (amount > 0),

    CONSTRAINT chk_transaction_currency
        CHECK (currency ~ '^[A-Z]{3}$'),

    CONSTRAINT fk_transaction_source_wallet
        FOREIGN KEY (source_wallet_id) REFERENCES wallets(id),

    CONSTRAINT fk_transaction_destination_wallet
        FOREIGN KEY (destination_wallet_id) REFERENCES wallets(id),

    CONSTRAINT uq_transaction_idempotency_key
        UNIQUE (idempotency_key)
);

CREATE INDEX idx_transactions_source_wallet
    ON transactions(source_wallet_id);

CREATE INDEX idx_transactions_destination_wallet
    ON transactions(destination_wallet_id);

CREATE INDEX idx_transactions_created_at
    ON transactions(created_at);


CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    wallet_id UUID NOT NULL,
    entry_type VARCHAR(10) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id),

    CONSTRAINT fk_ledger_wallet
        FOREIGN KEY (wallet_id) REFERENCES wallets(id),

    CONSTRAINT chk_ledger_entry_type
        CHECK (entry_type IN ('DEBIT', 'CREDIT')),

    CONSTRAINT chk_ledger_amount
        CHECK (amount > 0),

    CONSTRAINT chk_ledger_currency
        CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE INDEX idx_ledger_entries_transaction
    ON ledger_entries(transaction_id);

CREATE INDEX idx_ledger_entries_wallet_created
    ON ledger_entries(wallet_id, created_at);
