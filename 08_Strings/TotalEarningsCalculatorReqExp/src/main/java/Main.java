import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    System.out.println("Сумма заработка всех друзей: " + calculateSalarySum(input));
  }

  public static int calculateSalarySum(String text){
    //TODO: реализуйте метод
    String regex = "[\\s][0-9]{1,9}[\\sруб]";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    int salarySum = 0;
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      salarySum += Integer.parseInt(text.substring(start, end).trim());
    }
    return salarySum;
  }

}