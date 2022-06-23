import java.time.LocalDate;

class DepositAccount extends BankAccount {

    protected LocalDate lastIncome;

    public void put(double amountToPut) {
        super.put(amountToPut);
        lastIncome = LocalDate.now();
    }

    protected void take(double amountToTake) {
        if (LocalDate.now().isAfter(lastIncome.plusMonths(1))) { super.take(amountToTake); }
    }

}
