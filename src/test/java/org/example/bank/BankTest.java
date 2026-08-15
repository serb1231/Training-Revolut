package org.example.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Random;
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
        assertEquals(1, bank.getAccount(1).id);
    }

    // test that depositing money into a non-existing wallet gives error
    @Test
    public void depositAmount_IntoNonExistentWallet() {
        assertThatThrownBy(() -> bank.depositAmount(2, 20)).isInstanceOf(AccountNonExistentException.class);
    }

    // test that getting money from a non-existing wallet gives error
    @Test
    public void withdrawAmount_IntoNonExistentWallet() {
        assertThatThrownBy(() -> bank.withdrawAmount(2, 20)).isInstanceOf(AccountNonExistentException.class);
    }

    // test that depositing money into account increases value
    @Test
    public void depositAmount_IntoExistentWallet() {
        bank.depositAmount(1, 20);

        assertEquals(20, bank.getAccount(1).getAmount());
    }

    // test that withdrawing money from account increases value
    @Test
    public void withdrawAmount_IntoExistentWallet() {
        bank.depositAmount(1, 20);
        bank.withdrawAmount(1, 10);

        assertEquals(10, bank.getAccount(1).getAmount());
    }

    // test that non-existing accounts throw error
    @Test
    public void transfer_NonExistingAccount_ThrowError() {
        bank.createAccount(2);

        assertThatThrownBy(() -> bank.transferMoney(1, 3, 20)).isInstanceOf(AccountNonExistentException.class);
        assertThatThrownBy(() -> bank.transferMoney(3, 1, 20)).isInstanceOf(AccountNonExistentException.class);
    }

    // test that sending money modifies the account balances
    @Test
    public void transfer_ModifiesAccountBalances() {
        bank.createAccount(2);

        bank.depositAmount(1, 20);
        bank.depositAmount(2, 20);
        bank.transferMoney(1, 2, 20);

        assertEquals(0, bank.getAccount(1).getAmount());
        assertEquals(40, bank.getAccount(2).getAmount());
    }

    // putting a large sum of money into an account by 100 threads
    @Test
    @Timeout(5)
    public void deposit_Multithreaded() throws InterruptedException {
        int threadCount = 10000;

        try (var executor = Executors.newFixedThreadPool(16)) {
            var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                            bank.depositAmount(1, 10);
                            latch.countDown();
                    }
                );
            }
            latch.await();
        }
        assertEquals(10 * threadCount, bank.getAccount(1).getAmount());
    }

    @Test
    @Timeout(5)
    public void transfer_Multithreaded() throws InterruptedException {
        int threadCount = 1;
        bank.createAccount(2);
        bank.depositAmount(1, 1000);
        bank.depositAmount(2, 1000);
        Random random = new Random();
        random.ints(1,3);

        try (var executor = Executors.newFixedThreadPool(8)) {
            var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    int nextInt = random.nextInt();
                    try {
                        if (nextInt == 1) {
                            bank.transferMoney(1, 2, 10);
                        } else {
                            bank.transferMoney(2, 1, 10);
                        }
                    } catch (InsufficientClassException ignored) {}
                    latch.countDown();
                });
            }
            latch.await();
        }
        assertEquals(2000, bank.getAccount(1).getAmount() + bank.getAccount(2).getAmount());
    }
}