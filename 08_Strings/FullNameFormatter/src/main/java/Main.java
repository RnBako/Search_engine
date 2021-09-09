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
      //При невалидном ФИО вывести в консоль: Введенная строка не является ФИО

      int spaceCount = 0;
      boolean isNotCyrillic = false;
      for (int i = 0; i < input.length(); i++) {
        if ((int)input.toCharArray()[i] < 1040 || (int)input.toCharArray()[i] > 1103 ) {
          if (input.toCharArray()[i] == ' ') { spaceCount++; }
          else if (input.toCharArray()[i] == '-') { continue; }
          else {
            isNotCyrillic = true;
            break;
          }
        }
      }

      if (isNotCyrillic || spaceCount != 2) {
        System.out.println("Введенная строка не является ФИО");
        continue;
      }

      System.out.println("Фамилия: " + input.substring(0,input.indexOf(" ")));
      System.out.println("Имя: " + input.substring(input.indexOf(" "), input.lastIndexOf(" ")).trim());
      System.out.println("Отчество: " + input.substring(input.lastIndexOf(" ")).trim());
    }
  }

}