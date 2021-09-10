import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    System.out.println(System.lineSeparator() + splitTextIntoWords(input));
  }

  public static String splitTextIntoWords(String text) {
    //TODO реализуйте метод
    String cleanText = text.replaceAll("[^A-z’]"," ");

    String regex = "[A-z’]{1,}[\\s]";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(cleanText);
    String textResult = "";
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      textResult = textResult + ((textResult.isEmpty()) ? "" : System.lineSeparator()) + cleanText.substring(start, end).trim();
    }
    return textResult;
  }

}