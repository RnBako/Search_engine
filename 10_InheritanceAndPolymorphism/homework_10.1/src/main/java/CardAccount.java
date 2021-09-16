public class CardAccount extends BankAccount {

    public void take(double amountToTake) {
        super.take(amountToTake * 1.01);
    }
}
