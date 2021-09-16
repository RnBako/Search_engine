import java.time.LocalDate;

public class DepositAccount extends BankAccount {

    private LocalDate lastIncome;

    public void put(double amountToPut) {
        super.put(amountToPut);
        lastIncome = LocalDate.now();
    }

    public void take(double amountToTake) {
        if (LocalDate.now().isAfter(lastIncome.plusMonths(1))) { super.take(amountToTake); }
    }

}
