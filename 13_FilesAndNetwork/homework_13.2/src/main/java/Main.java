import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Введите путь к папке-источнику в формате \"C:\\Папка\\:");
            Scanner scanner = new Scanner(System.in);
            String pathSource = scanner.nextLine().trim();
            System.out.println("Введите путь к папке-приёмнику в формате \"C:\\Папка\\:");
            String pathDestination = scanner.nextLine().trim();
            FileUtils.copyFolder(pathSource, pathDestination);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
