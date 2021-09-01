public class Arithmetic {
    int a;
    int b;

    public Arithmetic (int a, int b) {
        this.a = a;
        this.b = b;
    }

    //Сумма чисел
    public int  summ() {
        return a + b;
    }

    //Произведение чисел
    public int multiplication() {
        return a * b;
    }

    //Максимальное из двух чисел
    public int maxNumbers() {
        return (a > b) ? a : b;
    }

    //Минимальное из двух чисел
    public int minNumbers() {
        return (a > b) ? b : a;
    }
}
