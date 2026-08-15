package org.example.Bank;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    // test that after creating an account, it exists
    @Test
    public void createAccount() {
        Bank bank = new Bank();

        bank.createAccount(1);
        assertEquals(1, bank.getAccount(1).id);
    }

    // test that depositing money into a non-existing wallet gives error
    @Test
    public void depositAmount_IntoNonExistentWallet() {
        Bank bank = new Bank();

        bank.createAccount(1);
        Exception exception =  assertThrows(RuntimeException.class,  () -> bank.depositAmount(2, 20));

        assertEquals("Id doesn't exist", exception.getMessage());
    }

    // test that getting money from a non-existing wallet gives error
    @Test
    public void withdrawAmount_IntoNonExistentWallet() {
        Bank bank = new Bank();

        bank.createAccount(1);
        Exception exception =  assertThrows(RuntimeException.class,  () -> bank.subtractAmount(2, 20));

        assertEquals("Id doesn't exist", exception.getMessage());
    }

    // test that depositing money into account increases value
    @Test
    public void depositAmount_IntoExistentWallet() {
        Bank bank = new Bank();

        bank.createAccount(1);
        bank.depositAmount(1, 20);

        assertEquals(20, bank.getAccount(1).getAmount());
    }

    // test that subtracting money from account increases value
    @Test
    public void subtractAmount_IntoExistentWallet() {
        Bank bank = new Bank();

        bank.createAccount(1);
        bank.depositAmount(1, 20);
        bank.subtractAmount(1, 10);

        assertEquals(10, bank.getAccount(1).getAmount());
    }

    // test that non-existing accounts throw error
    @Test
    public void transfer_NonExistingAccount_ThrowError() {
        Bank bank = new Bank();
        bank.createAccount(1);
        bank.createAccount(2);

        Exception exception = assertThrows(RuntimeException.class, () -> bank.transferMoney(1, 3, 20));

        assertEquals("Id's of wallets don't exist",exception.getMessage());


        exception = assertThrows(RuntimeException.class, () -> bank.transferMoney(3, 2, 20));

        assertEquals("Id's of wallets don't exist", exception.getMessage());
    }

    // test that sending money to an account that would overflow breaks
    @Test
    public void transfer_ExceedingAmount() {
        Bank bank = new Bank();
        bank.createAccount(1);
        bank.createAccount(2);

        bank.depositAmount(1, Long.MAX_VALUE - 20);
        bank.depositAmount(2, 21);
        Exception exception = assertThrows(RuntimeException.class, () -> bank.transferMoney(1, 2, Long.MAX_VALUE - 20));

        assertEquals("Transfer failed, replenishing funds back to sender", exception.getMessage());
    }

    // test that sending money modifies the account balances
    @Test
    public void transfer_ModifiesAccountBalances() {
        Bank bank = new Bank();
        bank.createAccount(1);
        bank.createAccount(2);

        bank.depositAmount(1, 20);
        bank.depositAmount(2, 20);
        bank.transferMoney(1, 2, 20);

        assertEquals(0, bank.getAccount(1).getAmount());
        assertEquals(40, bank.getAccount(2).getAmount());
    }

    // putting a large sum of money into an account by 100 threads
    @Test
    public void deposit_Multithreaded() {
        int threadCount = 100;
        Bank bank = new Bank();
        bank.createAccount(1);

        try (var executor = Executors.newFixedThreadPool(16)) {
            var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                            bank.depositAmount(1, 10);
                            latch.countDown();
                    }
                );
            }
        }
        assertEquals(1000, bank.getAccount(1).getAmount());
    }
}