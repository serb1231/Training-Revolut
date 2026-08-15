package org.example.bank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    // test that withdrawing money decreases value
    @Test
    public void withdrawMoneyDecreasesValue() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(100);
        wallet.withdrawAmount(20);
        assertEquals(100-20, wallet.getAmount());
    }

    // test that withdrawing too much raises error
    @Test
    @DisplayName("substractTooMuch() should through an exception if funds are unsufficient")
    public void withdrawTooMuch() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(20);

        assertThatThrownBy(() -> wallet.withdrawAmount(40)).isInstanceOf(InsufficientClassException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getAmount());
    }

    @Test
    public void deposit_NegativeAmount() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(20);

        assertThatThrownBy(() -> wallet.depositAmount(-40)).isInstanceOf(IllegalArgumentException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getAmount());
    }

    @Test
    public void withdraw_NegativeAmount() {
        Wallet wallet = new Wallet(1);
        wallet.depositAmount(20);

        assertThatThrownBy(() -> wallet.withdrawAmount(-40)).isInstanceOf(IllegalArgumentException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getAmount());
    }
}