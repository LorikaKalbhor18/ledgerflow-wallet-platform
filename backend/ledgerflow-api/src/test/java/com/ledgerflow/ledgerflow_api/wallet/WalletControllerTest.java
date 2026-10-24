package com.ledgerflow.ledgerflow_api.wallet;


import com.ledgerflow.ledgerflow_api.common.config.SecurityConfig;
import com.ledgerflow.ledgerflow_api.common.exception.GlobalExceptionHandler;
import com.ledgerflow.ledgerflow_api.common.exception.WalletNotFoundException;
import com.ledgerflow.ledgerflow_api.wallet.constants.WalletStatus;
import com.ledgerflow.ledgerflow_api.wallet.controller.WalletController;
import com.ledgerflow.ledgerflow_api.wallet.entity.CreateWalletRequest;
import com.ledgerflow.ledgerflow_api.wallet.entity.WalletResponse;
import com.ledgerflow.ledgerflow_api.wallet.service.WalletService;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WalletService walletService;

    @Test
    void createWalletShouldReturn201WhenRequestIsValid() throws Exception {
        UUID walletId = UUID.randomUUID();

        WalletResponse response = new WalletResponse(
                walletId,
                "CUST-101",
                "INR",
                BigDecimal.ZERO,
                WalletStatus.ACTIVE,
                OffsetDateTime.now()
        );

        when(walletService.createWallet(any(CreateWalletRequest.class)))
                .thenReturn(response);

        CreateWalletRequest request =
                new CreateWalletRequest("CUST-101", "INR");

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.customerId").value("CUST-101"))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createWalletShouldReturn400WhenCustomerIdIsBlank() throws Exception {
        String request = """
                {
                    "customerId": " ",
                    "currency": "INR"
                }
                """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWalletShouldReturn400WhenCurrencyIsLowercase() throws Exception {
        String request = """
                {
                    "customerId": "CUST-101",
                    "currency": "inr"
                }
                """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
void getWalletShouldReturn404WhenWalletDoesNotExist() throws Exception {
    UUID walletId = UUID.randomUUID();

    when(walletService.getWallet(walletId))
            .thenThrow(new com.ledgerflow.ledgerflow_api.common.exception
                    .WalletNotFoundException(walletId));

    mockMvc.perform(
                    org.springframework.test.web.servlet.request
                            .MockMvcRequestBuilders
                            .get("/api/v1/wallets/{walletId}", walletId))
            .andExpect(
                    org.springframework.test.web.servlet.result
                            .MockMvcResultMatchers.status().isNotFound());
}
}
