import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Main {

    private static final String STAFF_TXT = "C:/Users/AVK/Documents/Java/java_basics/11_AdvancedOOPFeatures/homework_11.2/Employees/data/staff.txt";

    public static void main(String[] args) {
        List<Employee> staff = Employee.loadStaffFromFile(STAFF_TXT);
        Employee employeeMaxSalary = findEmployeeWithHighestSalary(staff, 2017);
        System.out.println(employeeMaxSalary);
    }

    public static Employee findEmployeeWithHighestSalary(List<Employee> staff, int year) {
        //TODO Метод должен вернуть сотрудника с максимальной зарплатой среди тех,
        // кто пришёл в году, указанном в переменной year
        return staff.stream().filter(e -> e.getWorkStart().after(new Date(year - 1900, 00, 00))
                                    && e.getWorkStart().before(new Date(year - 1900, 11, 31)))
                             .max(Comparator.comparing(Employee::getSalary)).get();
    }
}