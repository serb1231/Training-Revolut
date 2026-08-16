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
        assertEquals(0.0, wallet.getBalance());
    }

    // test that depositing money increases value
    @Test
    public void depositMoneyIncreasesValue() {
        Wallet wallet = new Wallet(1);
        wallet.deposit(100);
        assertEquals(100.0, wallet.getBalance());
    }

    // test that withdrawing money decreases value
    @Test
    public void withdrawMoneyDecreasesValue() {
        Wallet wallet = new Wallet(1);
        wallet.deposit(100);
        wallet.withdraw(20);
        assertEquals(100-20, wallet.getBalance());
    }

    // test that withdrawing too much raises error
    @Test
    @DisplayName("substractTooMuch() should through an exception if funds are unsufficient")
    public void withdrawTooMuch() {
        Wallet wallet = new Wallet(1);
        wallet.deposit(20);

        assertThatThrownBy(() -> wallet.withdraw(40)).isInstanceOf(InsufficientFundsException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getBalance());
    }

    @Test
    public void deposit_NegativeAmount() {
        Wallet wallet = new Wallet(1);
        wallet.deposit(20);

        assertThatThrownBy(() -> wallet.deposit(-40)).isInstanceOf(IllegalArgumentException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getBalance());
    }

    @Test
    public void withdraw_NegativeAmount() {
        Wallet wallet = new Wallet(1);
        wallet.deposit(20);

        assertThatThrownBy(() -> wallet.withdraw(-40)).isInstanceOf(IllegalArgumentException.class);

        // verify wallet wasn't modified
        assertEquals(20, wallet.getBalance());
    }
}