package org.example.Bank;

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
        lock.lock();
        if(amount > Long.MAX_VALUE - sum) {
            throw new RuntimeException("New Amount would surpass max value");
        }
        amount += sum;
        lock.unlock();
    }

    void withdrawAmount(long sum) {
        lock.lock();
        if (amount >= sum) {
            amount -= sum;
        } else {
            throw new RuntimeException("Insufficient funds");
        }
        lock.unlock();
    }

    public double getAmount() {
        long currentAmount;
        lock.lock();
        currentAmount = amount;
        lock.unlock();
        return  currentAmount;
    }
}
