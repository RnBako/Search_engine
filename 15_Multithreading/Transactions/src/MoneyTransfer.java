public class MoneyTransfer  extends Thread{

    private Bank bank;
    String fromAccountNum;
    String toAccountNum;
    long amount;
    String action;

    public MoneyTransfer(Bank bank, String fromAccountNum, String toAccountNum, long amount, String action) {
        this.bank = bank;
        this.fromAccountNum = fromAccountNum;
        this.toAccountNum = toAccountNum;
        this.amount = amount;
        this.action = action;
    }

    @Override
    public void run () {
        if (action.contains("transfer")) {
            bank.transfer(fromAccountNum, toAccountNum, amount);
        } else {
            System.out.println("Баланс на счете " + fromAccountNum + ": " + bank.getBalance(fromAccountNum));
        }
    }
}
