import java.text.DecimalFormat;

public class Main {

    private static final String PATH_MOVEMENTS_CSV = "C:\\Users\\AVK\\Documents\\Java\\java_basics\\13_FilesAndNetwork\\homework_13.3\\src\\test\\resources\\movementList.csv";

    public static void main(String[] args) {
        Movements movements = new Movements(PATH_MOVEMENTS_CSV);
        System.out.println("Сумма расходов: " + (new DecimalFormat("###,###.##").format(movements.getExpenseSum())) + " руб.");
        System.out.println("Сумма доходов: " + (new DecimalFormat("###,###.##").format(movements.getIncomeSum())) + " руб.");
        System.out.println();
        System.out.println();
        System.out.println("Суммы расходов по организациям:");
        movements.printExpensesByOrganization();
    }
}
