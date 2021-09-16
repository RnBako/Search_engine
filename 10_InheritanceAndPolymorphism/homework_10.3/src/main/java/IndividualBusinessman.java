public class IndividualBusinessman extends Client {
    public void printAmountInfo () {
        System.out.println("Условия пополнения: 1% , если сумма меньше 1 000 рублей. И пополнение с комиссией 0,5%, если сумма больше либо равна 1 000 рублей.");
        System.out.println("Условия снятия: Без комиссии");
        System.out.println("Баланс: " + super.getAmount());
    }
    public void put(double amountToPut) {
        super.put((amountToPut < 1000) ? amountToPut * 0.99 : amountToPut * 0.995);
    }
}
