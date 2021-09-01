public class Arithmetic {
    int a;
    int b;

    public Arithmetic (int a, int b) {
        this.a = a;
        this.b = b;
    }

    //Сумма чисел
    public int  summ() {
        int summ = a + b;
        return summ;
    }

    //Произведение чисел
    public int multiplication() {
        int multiplication = a * b;
        return multiplication;
    }

    //Максимальное из двух чисел
    public int maxNumbers() {
        int maxNumbers = (a > b) ? a : b;
        return maxNumbers;
    }

    //Минимальное из двух чисел
    public int minNumbers() {
        int minNumbers = (a > b) ? b : a;
        return minNumbers;
    }
}
