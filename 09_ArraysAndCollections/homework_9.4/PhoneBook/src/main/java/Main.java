import java.util.Scanner;

public class Main {
    public static final PhoneBook phoneBook = new PhoneBook();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите номер, имя или команду: ");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                break;
            }

            if (input.matches("LIST")) {
                for (String record : phoneBook.getAllContacts()) {
                    System.out.println(record);
                }
            } else if (input.matches("7[0-9]{10}") && phoneBook.getNameByPhone(input).isEmpty()) {
                System.out.println("Такого номера нет в телефонной книге.");
                System.out.println("Введите имя абонента для номера \""+ input + "\":");
                String name = scanner.nextLine();
                phoneBook.addContact(input, name);
                System.out.println("Контакт сохранен!");
            } else if (input.matches("7[0-9]{10}") && !phoneBook.getNameByPhone(input).isEmpty()) {
                System.out.println("Такой номер есть в телефонной книге у " + phoneBook.getNameByPhone(input) + ". Имя будет перезаписано");
                String name = scanner.nextLine();
                phoneBook.addContact(input, name);
                System.out.println("Контакт сохранен!");
            } else if (input.matches("[A-zА-я]+") && phoneBook.getPhonesByName(input).isEmpty()) {
                System.out.println("Такого имени в телефонной книге нет.");
                System.out.println("Введите номер телефона для абонента \""+ input + "\":");
                String phone = scanner.nextLine();
                phoneBook.addContact(phone, input);
                System.out.println("Контакт сохранен!");
            } else if (input.matches("[A-zА-я]+") && !phoneBook.getPhonesByName(input).isEmpty()) {
                System.out.println("Такое имя в телефонной книге есть у " + phoneBook.getPhonesByName(input) + ". Номер будет перезаписан");
                System.out.println("Введите номер телефона для абонента \""+ input + "\":");
                String phone = scanner.nextLine();
                phoneBook.addContact(phone, input);
                System.out.println("Контакт сохранен!");
            } else {
                System.out.println("Введена не верная команда, попробуйте еще раз.");
            }
        }
    }
}
