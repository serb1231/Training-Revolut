package org.example.Bank;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    // test initial amound is 0
    @Test
    public void initialAmountIs0() {
        Wallet wallet = new Wallet(1);
        assertEquals(0.0, wallet.getAmount());
    }

    // test that depositing money increases value
    @Test
    public void depositMoneyIncreasesValue() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(100);
        assertEquals(100.0, wallet.getAmount());
    }

    // test that subtracting money decreases value
    @Test
    public void subtractMoneyDecreasesValue() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(100);
        wallet.withdrawAmount(20);
        assertEquals(100-20, wallet.getAmount());
    }

    // test that subtracting too much raises error
    @Test
    @DisplayName("substractTooMuch() should through an exception if funds are unsufficient")
    public void subtractTooMuch() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(20);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            wallet.withdrawAmount(40);
        });

        // verify that the exception matches
        assertEquals("Insufficient funds", exception.getMessage());

        // verify wallet wasn't modified
        assertEquals(20, wallet.getAmount());
    }

    // test that moving money from one part to the other modifies the values
}