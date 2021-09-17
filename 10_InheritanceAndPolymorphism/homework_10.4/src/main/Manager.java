package main;

public class Manager implements Employee{
    private int salary = 70_000;
    private int sales;

    public Manager() {
        sales = (int) (Math.random() * ((140_000 - 115_000) + 115_000));
        salary += (int) (sales * 0.05);
    }

    @Override
    public int getMonthSalary() {
        return salary;
    }

}
