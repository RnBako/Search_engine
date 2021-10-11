public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        for (int i = 0; i < 1000; i++) {
            String accountNumber = String.format("%.0f", (Math.random() * 1000_000_000_000L));
            bank.addAccount(accountNumber, new Account(accountNumber, (long) (Math.random() * 100_000)));
        }

        for (int i = 1; i <= 100; i++) {
            String fromAccountNum = bank.getAccounts().keySet().stream().filter(account -> account.contains(String.format("%.0f", (Math.random() * 1000)))).findFirst().get();
            String toAccountNum = bank.getAccounts().keySet().stream().filter(account -> account.contains(String.format("%.0f", (Math.random() * 1000)))).findFirst().get();

            MoneyTransfer moneyTransfer = new MoneyTransfer(bank,
                    fromAccountNum,
                    toAccountNum,
                    (long) ((Math.random() * 52100) + 10000),
                    (Long.parseLong(fromAccountNum) % 2 == 0) ? "transfer":"getBalance");
            moneyTransfer.start();
        }

        System.out.println("Сумма: " + bank.getSumAllAccounts());
    }
}
