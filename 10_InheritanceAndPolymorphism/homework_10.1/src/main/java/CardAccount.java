class CardAccount extends BankAccount {

    protected void take(double amountToTake) {
        super.take(amountToTake * 1.01);
    }
}
