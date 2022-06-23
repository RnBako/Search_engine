import main.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Company company = new Company();

        //Создаем 180 операторов
        for (int i = 0; i < 180; i++) { company.hire(new Operator());}

        //Создаем 80 менеджеров
        List<Employee> managers = new ArrayList<>();
        for (int i = 0; i < 80; i++) { managers.add(new Manager());}
        company.hireAll(managers);

        //Создаем 10 топ-менеджеров
        for (int i = 0; i < 180; i++) { company.hire(new TopManager(company));}

        System.out.println("Топ 10 зарплат для " + company.employees.size() + " сотрудников");
        for (Employee topStaff : company.getTopSalaryStaff(10)) {
            System.out.println(topStaff.getMonthSalary());
        }

        System.out.println("_________________________________________");
        System.out.println("Топ 30 минимальных зарплат " + company.employees.size() + " сотрудников");
        for (Employee lowStaff : company.getLowestSalaryStaff(30)) {
            System.out.println(lowStaff.getMonthSalary());
        }

        // Увольняем 50% сотрудников
        int countForFire = (int) (company.employees.size() * 0.5);
        for (int i = 0; i < countForFire; i++) { company.fire(company.employees.get(i)); }

        System.out.println("_________________________________________");
        System.out.println("После увольнения");

        System.out.println("Топ 10 зарплат " + company.employees.size() + " сотрудников");
        for (Employee topStaff : company.getTopSalaryStaff(10)) {
            System.out.println(topStaff.getMonthSalary());
        }

        System.out.println("_________________________________________");
        System.out.println("Топ 30 минимальных зарплат " + company.employees.size() + " сотрудников");
        for (Employee lowStaff : company.getLowestSalaryStaff(30)) {
            System.out.println(lowStaff.getMonthSalary());
        }
    }

}