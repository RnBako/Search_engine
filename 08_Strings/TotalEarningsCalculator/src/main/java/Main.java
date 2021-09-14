public class Main {

  public static void main(String[] args) {

    String text = "Вася заработал 5000 рублей, Петя - 7563 рубля, а Маша - 30000 рублей";
    //TODO: напишите ваш код, результат вывести в консоль

    int sumSalary = 0;
    while (text.contains(" ")) {
      if ((int) (text.substring(0, text.indexOf(" ")).toCharArray()[0]) > 47 &&
              (int) (text.substring(0, text.indexOf(" ")).toCharArray()[0]) < 58) {
        sumSalary = sumSalary + Integer.parseInt(text.substring(0, text.indexOf(" ")));
      }
      text = text.substring(text.indexOf(" ")).trim();
    }

    if ((int) (text.toCharArray()[0]) > 47 &&
            (int) (text.toCharArray()[0]) < 58) {
      sumSalary = sumSalary + Integer.parseInt(text);
    }
    System.out.println(sumSalary);

  }

}