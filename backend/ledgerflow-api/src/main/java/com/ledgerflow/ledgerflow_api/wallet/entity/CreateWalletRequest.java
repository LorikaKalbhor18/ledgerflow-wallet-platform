package com.ledgerflow.ledgerflow_api.wallet.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateWalletRequest(
    @NotBlank(message = "Customer ID is required")
        @Size(max = 100, message = "Customer ID must not exceed 100 characters")
        String customerId,

        @NotBlank(message = "Currency is required")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Currency must be a 3-letter uppercase code, such as INR"
        )
        String currency
) {

}
