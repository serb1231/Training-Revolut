package org.example.bank;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private final ConcurrentHashMap<Integer, Wallet> wallets = new ConcurrentHashMap<>();
    public void createAccount(int id) {

        Wallet wallet = new Wallet(id);
        wallets.putIfAbsent(id, wallet);
    }

    private Wallet internalGetAccount(int id) {
        Wallet w = wallets.get(id);
        if (w == null) {
            throw new AccountNonExistentException("Id: " + id);
        }
        return  w;
    }

    public void deposit(int id, long amount) {
        // if id doesn't exist
        var w = internalGetAccount(id);

        w.deposit(amount);
    }

    public void withdraw(int id, long amount) {
        var w = internalGetAccount(id);

        w.withdraw(amount);
    }

    public Wallet getAccount(int id) {

        return internalGetAccount(id);
    }

    public void transfer(int from, int to, long amount) {

        if (from == to) throw new SelfTransferException("Tried to transfer from and to the same account");
        var fromAcc = internalGetAccount(from);
        var toAcc = internalGetAccount(to);

        boolean fromFirst = to < from;
        Wallet first = fromFirst ? toAcc : fromAcc;
        Wallet second = fromFirst ? fromAcc : toAcc;

        first.lockAccount();
        try {
                second.lockAccount();
            try {
                fromAcc.withdraw(amount);
                toAcc.deposit(amount);
            } finally {
                second.unlockAccount();
            }
        } finally {
            first.unlockAccount();
        }
    }


}
