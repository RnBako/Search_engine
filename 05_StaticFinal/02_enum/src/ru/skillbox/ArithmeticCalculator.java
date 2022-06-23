package ru.skillbox;

public class ArithmeticCalculator {
    private int a;
    private int b;

    public ArithmeticCalculator(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int calculate(Operation operation) {
        if (operation == Operation.ADD) { return a + b; }
        else if (operation == Operation.SUBTRACT) { return a - b; }
        else if (operation == Operation.MULTIPLY) { return  a * b; }
        return 0;
    }
}
