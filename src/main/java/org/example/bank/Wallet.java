package org.example.bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    private long amount;
    private final int id;
    private final Lock lock;
    Wallet(int id) {
        amount = 0;
        this.id = id;
        lock = new ReentrantLock();
    }

    public int getId() {
        return id;
    }

    public void lockAccount() {
        lock.lock();
    }

    public void unlockAccount() {
        lock.unlock();
    }

    void deposit(long amount) {
        try {
            lockAccount();

            if (amount < 0) {
                throw new IllegalArgumentException("Cannot deposit negative amount");
            }
            this.amount += amount;
        }
        finally {
            unlockAccount();
        }
    }

    void withdraw(long amount) {
        try {
            lockAccount();
            if (amount < 0) {
                throw new IllegalArgumentException("Cannot withdraw negative funds");
            }
            if (this.amount >= amount) {
                this.amount -= amount;
            } else {
                throw new InsufficientFundsException("Insufficient funds");
            }
        }
        finally {
            unlockAccount();
        }
    }

    public long getBalance() {
        long currentAmount;
        lockAccount();
        try {
            currentAmount = amount;
        } finally {
            unlockAccount();
        }

        return  currentAmount;
    }
}
