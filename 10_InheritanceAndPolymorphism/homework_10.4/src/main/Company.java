package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
    private int income = 0;
    public List<Employee> employees = new ArrayList<>();

    public void hire(Employee employee) {
        employees.add(employee);
        income = (int) (Math.random() * (20_000_000 - 10_000_000) + 10_000_000);
    }

    public void hireAll(List<Employee> employees) {
        for (Employee e : employees) { hire(e); }
    }

    public void  fire (Employee employee) {
        employees.remove(employee);
    }

    public int getIncome () {
        return income;
    }

    public List<Employee> getTopSalaryStaff(int count) {
        Collections.sort(employees, new EmployeeComparator());
        Collections.reverse(employees);
        return employees.subList(0, count);
    }

    public List<Employee> getLowestSalaryStaff(int count) {
        Collections.sort(employees, new EmployeeComparator());
        return employees.subList(0, count);
    }
}
