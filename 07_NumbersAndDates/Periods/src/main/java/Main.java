import java.time.LocalDate;

public class Main {

  public static void main(String[] args) {
    System.out.println(getPeriodFromBirthday(LocalDate.of(1995, 5, 23)));
  }

  private static String getPeriodFromBirthday(LocalDate birthday) {
    int periodFromBirthday = (int) (LocalDate.now().toEpochDay() - birthday.toEpochDay());
    return periodFromBirthday / 365 + " years, " +
            (periodFromBirthday % 365) / 30 + " months, " +
            ((periodFromBirthday % 365) % 30) + " days ";
  }

}
