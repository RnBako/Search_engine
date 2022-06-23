public class PhysicalPerson extends Client {
    public void printAmountInfo () {
        System.out.println("Условия пополнения: Без комиссии");
        System.out.println("Условия снятия: Без комиссии");
        System.out.println("Баланс: " + super.getAmount());
    }
}
