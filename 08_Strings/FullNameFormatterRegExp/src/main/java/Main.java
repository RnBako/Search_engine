import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

      String regex1 = "[А-я,-]{1,}[\\s][А-я]{1,}[\\s][А-я]{1,}";
      Pattern pattern = Pattern.compile(regex1);
      Matcher matcher = pattern.matcher(input);
      if (matcher.find() && input.length() == matcher.end()) {
        String regex2 = "[А-я,-]{1,}";
        pattern = Pattern.compile(regex2);
        matcher = pattern.matcher(input);
        if (matcher.find()) { System.out.println("Фамилия: " + input.substring(matcher.start(), matcher.end())); }
        if (matcher.find()) { System.out.println("Имя: " + input.substring(matcher.start(), matcher.end())); }
        if (matcher.find()) { System.out.println("Отчество: " + input.substring(matcher.start(), matcher.end())); }
      } else {
        System.out.println("Введенная строка не является ФИО");
        continue;
      }
    }
  }

}