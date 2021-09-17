package main;

import java.util.Comparator;

public class EmployeeComparator implements Comparator <Employee> {

    public int compare(Employee employee1, Employee employee2) {
        if (employee1.getMonthSalary() > employee2.getMonthSalary()) { return 1; }
        if (employee1.getMonthSalary() < employee2.getMonthSalary()) { return -1; }
        return 0;
    }
}
