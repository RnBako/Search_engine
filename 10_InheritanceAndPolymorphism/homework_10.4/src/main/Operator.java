package main;

public class Operator implements Employee{
    private int salary = 50_000;

    @Override
    public int getMonthSalary() {
        return salary;
    }

}
