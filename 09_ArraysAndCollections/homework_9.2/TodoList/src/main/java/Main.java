
import java.util.Scanner;

public class Main {
    private static TodoList todoList = new TodoList();

    public static void main(String[] args) {
        // TODO: написать консольное приложение для работы со списком дел todoList
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите комманду (доступны LIST, ADD, EDIT, DELETE, EXIT): ");
            String command = scanner.nextLine();
            if (command.matches("LIST")) {
                for (int i = 0; i < todoList.getTodos().size(); i++) {
                    System.out.println(i + " - " + todoList.getTodos().get(i));
                }
            } else if (command.matches("ADD [0-9]*\\s[0-ё\\s]+")) {
                command = command.substring(command.indexOf(" ")).trim();
                todoList.add(Integer.parseInt(command.substring(0,command.indexOf(" ")).trim()), command.substring(command.indexOf(" ")).trim());
            } else if (command.matches("ADD [0-ё\\s]+")) {
                todoList.add(command.substring(4));
            } else if (command.matches("EDIT [0-9]*\\s[0-ё\\s]+")) {
                command = command.substring(command.indexOf(" ")).trim();
                todoList.edit(command.substring(command.indexOf(" ")).trim(), Integer.parseInt(command.substring(0,command.indexOf(" ")).trim()));
            } else if (command.matches("DELETE [0-9]*")) {
                command = command.substring(command.indexOf(" ")).trim();
                todoList.delete(Integer.parseInt(command));
            } else {
                System.out.println("Введена не верная команда, попробуйте еще раз.");
            }
        }
    }
}
