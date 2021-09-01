public class Arithmetic {
    int a = 0;
    int b = 0;

    public Arithmetic (int a, int b) {
        this.a = a;
        this.b = b;
    }

    //Сумма чисел
    public void summ() {
        System.out.println("Сумма чисел: " + (a + b));
    }

    //Произведение чисел
    public void multiplication() {
        System.out.println("Произведение чисел: " + (a * b));
    }

    //Максимальное из двух чисел
    public void maxNumbers() {
        System.out.println("Максимальное из чисел: " + ((a > b) ? a : b));
    }

    //Минимальное из двух чисел
    public void minNumbers() {
        System.out.println("Минимальное из чисел: " + ((a > b) ? b : a));
    }
}
