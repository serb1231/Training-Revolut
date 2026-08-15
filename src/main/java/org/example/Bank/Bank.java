package org.example.Bank;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    ConcurrentHashMap<Integer, Wallet> wallets = new ConcurrentHashMap<>();
    public void createAccount(int id) {

        Wallet wallet = new Wallet(id);
        wallets.putIfAbsent(id, wallet);
    }

    public void depositAmount(int id, long sum) {
        // if id doesn't exist
        if(!wallets.containsKey(id)) {
            throw new RuntimeException("Id doesn't exist");
        }

        wallets.get(id).depositAmount(sum);
    }

    public void subtractAmount(int id, long sum) {
        // if id doesn't exist
        if(!wallets.containsKey(id)) {
            throw new RuntimeException("Id doesn't exist");
        }

        wallets.get(id).withdrawAmount(sum);
    }

    public Wallet getAccount(int id) {
        // if id doesn't exist
        if(!wallets.containsKey(id)) {
            throw new RuntimeException("Id doesn't exist");
        }

        return wallets.get(id);
    }

    public void transferMoney (int from, int to, long sum) {
        // if accounts don't exist, throw error
        if(!wallets.containsKey(from) || !wallets.containsKey(to)) {
            throw new RuntimeException("Id's of wallets don't exist");
        }

        subtractAmount(from, sum);

        try {
            depositAmount(to, sum);
        } catch (RuntimeException e) {
            depositAmount(from, sum);
            throw new RuntimeException("Transfer failed, replenishing funds back to sender",e);
        }
    }
}
