package com.ledgerflow.ledgerflow_api.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.ledgerflow.ledgerflow_api.wallet.constants.WalletStatus;
import com.ledgerflow.ledgerflow_api.wallet.entity.CreateWalletRequest;
import com.ledgerflow.ledgerflow_api.wallet.entity.Wallet;
import com.ledgerflow.ledgerflow_api.wallet.entity.WalletResponse;
import com.ledgerflow.ledgerflow_api.wallet.repository.WalletRepository;
import com.ledgerflow.ledgerflow_api.wallet.service.WalletServiceImpl;


@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock 
    private WalletRepository walletRepository;


    private WalletServiceImpl walletService;  
    
    @BeforeEach 
    void setUp() {
        walletService = new WalletServiceImpl(walletRepository);
    }

    @Test
    void createWalletShouldCreateWalletWithZeroBalance() {
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
    CreateWalletRequest request =
                new CreateWalletRequest(" CUST-101 ", "INR");
        WalletResponse response = walletService.createWallet(request);

        assertNotNull(response.id());
        assertEquals("CUST-101", response.customerId());
        assertEquals("INR", response.currency());
        assertEquals(0, BigDecimal.ZERO.compareTo(response.balance()));
        assertEquals(WalletStatus.ACTIVE, response.status());

        verify(walletRepository, times(1)).save(any(Wallet.class));
    
    }

      @Test
    void getWalletShouldReturnWalletWhenItExists() {
        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(
                walletId,
                "CUST-101",
                "INR",
                BigDecimal.ZERO,
                WalletStatus.ACTIVE,
                null,
                null
        );

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        WalletResponse response = walletService.getWallet(walletId);

        assertEquals(walletId, response.id());
        assertEquals("CUST-101", response.customerId());
        assertEquals(WalletStatus.ACTIVE, response.status());

        verify(walletRepository).findById(walletId);
    }

    @Test
    void getWalletShouldThrowExceptionWhenWalletDoesNotExist() {
        UUID walletId = UUID.randomUUID();

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.empty());

        assertThrows(
                com.ledgerflow.ledgerflow_api.common.exception.WalletNotFoundException.class,
                () -> walletService.getWallet(walletId)
        );

        verify(walletRepository).findById(walletId);
    }


}
