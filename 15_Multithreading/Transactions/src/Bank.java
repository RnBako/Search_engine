import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bank {

    private Map<String, Account> accounts;
    private final Random random = new Random();

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
        throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        try {
            System.out.println("Перевод с " + fromAccountNum + " на " + toAccountNum);
            if (accounts.get(fromAccountNum) == null || accounts.get(toAccountNum) == null) {
                System.out.println("Один или оба счета не найдены!");
            } else if (accounts.get(fromAccountNum).getBlocked() || accounts.get(toAccountNum).getBlocked()) {
                System.out.println("Транзакция не возможна, один или оба счета заблокированы!");
            } else if (amount <= 50_000) {
                if ((accounts.get(fromAccountNum).getMoney() - amount) > 0) {
                    synchronized (accounts.get(fromAccountNum)) {
                        accounts.get(fromAccountNum).setMoney(accounts.get(fromAccountNum).getMoney() - amount);
                    }
                    synchronized (accounts.get(toAccountNum)) {
                        accounts.get(toAccountNum).setMoney(accounts.get(toAccountNum).getMoney() + amount);
                        System.out.println("Транзакция выполнена!");
                    }
                } else System.out.println("Недостаточно средств на счете!");
            } else {
                if ((accounts.get(fromAccountNum).getMoney() - amount) > 0) {
                    if (this.isFraud(fromAccountNum, toAccountNum, amount)) {
                        synchronized (accounts.get(fromAccountNum)) {
                            accounts.get(fromAccountNum).setMoney(accounts.get(fromAccountNum).getMoney() - amount);
                        }
                        synchronized (accounts.get(toAccountNum)) {
                            accounts.get(toAccountNum).setMoney(accounts.get(toAccountNum).getMoney() + amount);
                        }
                        accounts.get(fromAccountNum).setBlocked(true);
                        accounts.get(toAccountNum).setBlocked(true);
                        System.out.println("Мошенническая транзакция! Счета заблокированы!");
                    } else {
                        synchronized (accounts.get(fromAccountNum)) {
                            accounts.get(fromAccountNum).setMoney(accounts.get(fromAccountNum).getMoney() - amount);
                        }
                        synchronized (accounts.get(toAccountNum)) {
                            accounts.get(toAccountNum).setMoney(accounts.get(toAccountNum).getMoney() + amount);
                        }
                        System.out.println("Транзакция выполнена!");
                    }
                } else System.out.println("Недостаточно средств на счете!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        synchronized (accounts.get(accountNum)) {
            return accounts.get(accountNum).getMoney();
        }
    }

    public long getSumAllAccounts() {
        return accounts.values().stream().mapToLong(money -> money.getMoney()).reduce(0,(x, y) -> x + y);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void addAccount(String accountNumber, Account account) {
        this.accounts.put(accountNumber, account);
    }
}
