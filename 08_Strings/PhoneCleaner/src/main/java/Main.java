import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (input.equals("0")) {
        break;
      }
      //TODO:напишите ваш код тут, результат вывести в консоль.
      String cleanPhone = input.replaceAll("[^0-9]","");
      if (cleanPhone.length() == 10) {
        cleanPhone = "7" + cleanPhone;
        System.out.println(cleanPhone);
      } else if (!cleanPhone.matches("[7,8]{1}[0-9]{10}")) {
        System.out.println("Неверный формат номера");
      } else if (cleanPhone.matches("[8]{1}[0-9]{10}")) {
        cleanPhone = cleanPhone.replace("8", "7");
        System.out.println(cleanPhone);
      } else {
        System.out.println(cleanPhone);
      }
    }
  }

}
