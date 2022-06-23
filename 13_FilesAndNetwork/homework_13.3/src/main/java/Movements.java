import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Movements {
    public List<Operation> operationsList = new ArrayList<>();

    public Movements(String pathMovementsCsv) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(pathMovementsCsv));
            List<String[]> lines = csvReader.readAll();
            lines.forEach(elements -> {
                try {
                    operationsList.add(new Operation(elements[0],
                            elements[1],
                            elements[2],
                            LocalDate.parse(elements[3], DateTimeFormatter.ofPattern("dd.MM.yy")),
                            elements[4],
                            elements[5],
                            Double.parseDouble(elements[6].replace(',', '.')),
                            Double.parseDouble(elements[7].replace(',', '.'))
                    ));
                } catch (Exception ex) {
                    System.out.println("Wrong line! " + ex.getMessage() + " - " + ex.getCause());
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public double getExpenseSum() {
        return operationsList.stream().mapToDouble(Operation::getExpense).sum();
    }

    public double getIncomeSum() {
        return operationsList.stream().mapToDouble(Operation::getIncome).sum();
    }

    public void printExpensesByOrganization() {
        Map<String, List<Operation>> groupOperations = operationsList.stream().collect(Collectors.groupingBy(o -> {
            String regex = "[\\d]{2}\\.[\\d]{2}\\.[\\d]{2}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(o.getOperationDescription());
            if (matcher.find()) return o.getOperationDescription().substring(19, matcher.start()).trim();
            else return o.getOperationDescription().substring(19).trim();
            }));
        for (Map.Entry<String, List<Operation>> item : groupOperations.entrySet()) {
            List<Operation> operation = item.getValue();
            System.out.println(item.getKey() + ": " + operation.stream().mapToDouble(Operation::getExpense).sum() + " руб.");
        }
    }
}
