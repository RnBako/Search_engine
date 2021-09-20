import java.util.Collections;
import java.util.List;

public class Main {

    public static final String STAFF_TXT = "C:/Users/AVK/Documents/Java/java_basics/11_AdvancedOOPFeatures/homework_11.1/data/staff.txt";

    public static void main(String[] args) {
        List<Employee> staff = Employee.loadStaffFromFile(STAFF_TXT);
        sortBySalaryAndAlphabet(staff);
        System.out.println(staff);
    }

    public static void sortBySalaryAndAlphabet(List<Employee> staff) {
        Collections.sort(staff, ((o1, o2) -> {
            if (o1.getSalary() > o2.getSalary()) { return 1; }
            else if (o1.getSalary() < o2.getSalary()) { return -1; }
            else {
                return o1.getName().compareTo(o2.getName());
            }
        }));
    }
}