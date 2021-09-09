public class Main {

  public static void main(String[] args) {

    String text = "Вася заработал 5000 рублей, Петя - 7563 рубля, а Маша - 30000 рублей";
    //TODO: напишите ваш код, результат вывести в консоль
    String salaryCharSet = "руб";

    String vasyaSalary = text.substring(text.substring(0, text.indexOf(salaryCharSet)).trim().lastIndexOf(" "), text.indexOf(salaryCharSet)-1);
    String remainingText = text.substring(text.indexOf(salaryCharSet) + 3);

    String petyaSalary = remainingText.substring(remainingText.substring(0, remainingText.indexOf(salaryCharSet)).trim().lastIndexOf(" "), remainingText.indexOf(salaryCharSet)-1);
    remainingText = remainingText.substring(remainingText.indexOf(salaryCharSet) + 3);

    String mashaSalary = remainingText.substring(remainingText.substring(0, remainingText.indexOf(salaryCharSet)).trim().lastIndexOf(" "), remainingText.indexOf(salaryCharSet)-1);

    System.out.println("Сумма заработка всех друзей: " + (Integer.parseInt(vasyaSalary.trim()) + Integer.parseInt(petyaSalary.trim()) + Integer.parseInt(mashaSalary.trim())) + " рублей");
  }

}