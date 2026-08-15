package org.example.bank;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    ConcurrentHashMap<Integer, Wallet> wallets = new ConcurrentHashMap<>();
    public void createAccount(int id) {

        Wallet wallet = new Wallet(id);
        wallets.putIfAbsent(id, wallet);
    }

    private Wallet existingAccount(int id) {
        Wallet w = wallets.get(id);
        if (w == null) {
            throw new AccountNonExistentException("");
        }
        return  w;
    }

    public void depositAmount(int id, long sum) {
        // if id doesn't exist
        var w = existingAccount(id);

        w.depositAmount(sum);
    }

    public void withdrawAmount(int id, long sum) {
        var w = existingAccount(id);

        w.withdrawAmount(sum);
    }

    public Wallet getAccount(int id) {

        return existingAccount(id);
    }

    public void transferMoney (int from, int to, long sum) {

        if (from == to) throw new SelfTransferException("Tried to transfer from and to the same account");
        var fromAcc = existingAccount(from);
        var toAcc = existingAccount(to);

        Wallet first = to < from ? toAcc : fromAcc;
        Wallet second = from < to ? toAcc : fromAcc;

        synchronized (first) {
            synchronized (second) {
                fromAcc.withdrawAmount(sum);
                toAcc.depositAmount(sum);
            }
        }
    }


}
