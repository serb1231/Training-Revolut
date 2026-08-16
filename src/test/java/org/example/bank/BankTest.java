package org.example.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank;
    @BeforeEach
    public void setup() {
        bank = new Bank();

        bank.createAccount(1);
    }
    // test that after creating an account, it exists
    @Test
    public void createAccount() {
        assertEquals(1, bank.getAccount(1).getId());
    }

    // test that depositing money into a non-existing wallet gives error
    @Test
    public void depositAmount_IntoNonExistentWallet() {
        assertThatThrownBy(() -> bank.deposit(2, 20)).isInstanceOf(AccountNonExistentException.class);
    }

    // test that getting money from a non-existing wallet gives error
    @Test
    public void withdrawAmount_IntoNonExistentWallet() {
        assertThatThrownBy(() -> bank.withdraw(2, 20)).isInstanceOf(AccountNonExistentException.class);
    }

    // test that depositing money into account increases value
    @Test
    public void depositAmount_IntoExistentWallet() {
        bank.deposit(1, 20);

        assertEquals(20, bank.getAccount(1).getBalance());
    }

    // test that withdrawing money from account increases value
    @Test
    public void withdrawAmount_IntoExistentWallet() {
        bank.deposit(1, 20);
        bank.withdraw(1, 10);

        assertEquals(10, bank.getAccount(1).getBalance());
    }

    // test that non-existing accounts throw error
    @Test
    public void transfer_NonExistingAccount_ThrowError() {
        bank.createAccount(2);

        assertThatThrownBy(() -> bank.transfer(1, 3, 20)).isInstanceOf(AccountNonExistentException.class);
        assertThatThrownBy(() -> bank.transfer(3, 1, 20)).isInstanceOf(AccountNonExistentException.class);

        assertEquals(0, bank.getAccount(1).getBalance());
        assertEquals(0, bank.getAccount(2).getBalance());
    }

    // test that sending money modifies the account balances
    @Test
    public void transfer_ModifiesAccountBalances() {
        bank.createAccount(2);

        bank.deposit(1, 20);
        bank.deposit(2, 20);
        bank.transfer(1, 2, 20);

        assertEquals(0, bank.getAccount(1).getBalance());
        assertEquals(40, bank.getAccount(2).getBalance());
    }

    // putting a large amount of money into an account by 100 threads
    @Test
    @Timeout(5)
    public void deposit_Multithreaded() throws InterruptedException {
        int threadCount = 10000;

        try (var executor = Executors.newFixedThreadPool(16)) {
            var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                            bank.deposit(1, 10);
                            latch.countDown();
                    }
                );
            }
            latch.await();
        }
        assertEquals(10 * threadCount, bank.getAccount(1).getBalance());
    }

    @Test
    @Timeout(5)
    public void transfer_Multithreaded() throws InterruptedException {
        int threadCount = 10000;
        bank.createAccount(2);
        bank.deposit(1, 1000);
        bank.deposit(2, 1000);

        try (var executor = Executors.newFixedThreadPool(8)) {
            var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                int to = i % 2 + 1;
                int from = 3 - to;
                executor.submit(() -> {
                    try {
                            bank.transfer(to, from, 10);
                    } catch (InsufficientFundsException ignored) {}
                    latch.countDown();
                });
            }
            latch.await();
        }
        assertEquals(2000, bank.getAccount(1).getBalance() + bank.getAccount(2).getBalance());
    }

    public void transfer_SameAccount_ThrowsException() {
        assertThatThrownBy(() -> bank.transfer(1, 1, 10)).isInstanceOf(SelfTransferException.class);

        assertEquals(0, bank.getAccount(1).getBalance());
    }
}