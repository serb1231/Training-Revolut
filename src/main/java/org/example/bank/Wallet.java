package org.example.bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    private long amount;
    public int id;
    private final Lock lock;
    Wallet(int id) {
        amount = 0;
        this.id = id;
        lock = new ReentrantLock();
    }

    void depositAmount(long sum) {
        try {
            lock.lock();

            if (sum < 0) {
                throw new IllegalArgumentException("Cannot deposit negative sum");
            }
            amount += sum;
        }
        finally {
            lock.unlock();
        }
    }

    void withdrawAmount(long sum) {
        try {
            lock.lock();
            if (sum < 0) {
                throw new IllegalArgumentException("Cannot withdraw negative funds");
            }
            if (amount >= sum) {
                amount -= sum;
            } else {
                throw new InsufficientClassException("Insufficient funds");
            }
        }
        finally {
            lock.unlock();
        }
    }

    public float getAmount() {
        long currentAmount;
        lock.lock();
        currentAmount = amount;
        lock.unlock();

        return  currentAmount;
    }
}
