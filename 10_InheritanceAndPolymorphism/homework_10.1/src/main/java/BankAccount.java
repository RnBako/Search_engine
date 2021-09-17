public class BankAccount {

  private double amount;

  protected BankAccount() {
    amount = 0;
  }

  protected double getAmount() {
    return amount;
  }

  protected void put(double amountToPut) {
    if (amountToPut > 0) { amount += amountToPut; }
  }

  protected void take(double amountToTake) {
    if (amountToTake < amount) { amount -= amountToTake; }
  }
}
