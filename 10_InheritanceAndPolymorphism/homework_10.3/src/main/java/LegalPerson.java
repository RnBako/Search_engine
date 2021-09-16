public class LegalPerson extends Client {
    public void printAmountInfo () {
        System.out.println("Условия пополнения: Без комиссии");
        System.out.println("Условия снятия: Комиссия 1%");
        System.out.println("Баланс: " + super.getAmount());
    }

    public void take(double amountToTake) {
        super.take(amountToTake * 1.01);
    }
}
