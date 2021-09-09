import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Main {

    public static final DateTimeFormatter FORMATTER_FOR_BIRTHDAY = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    public static final DateTimeFormatter FORMATTER_FOR_DAY = DateTimeFormatter.ofPattern("EEE").localizedBy(new Locale("us"));

    public static void main(String[] args) {

        int day = 31;
        int month = 12;
        int year = 1990;

        System.out.println(collectBirthdays(year, month, day));

    }

    public static String collectBirthdays(int year, int month, int day) {
        LocalDate birthday = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();
        //TODO реализуйте метод для построения строки в следующем виде
        //0 - 31.12.1990 - Mon
        //1 - 31.12.1991 - Tue
        String birthdayCollection = "";
        for (int i = 0; i <=today.compareTo(birthday); i++) {
            if (birthday.plusYears(i).isBefore(today) || birthday.plusYears(i).isEqual(today)) {
                birthdayCollection = birthdayCollection + i + " - " +
                                     FORMATTER_FOR_BIRTHDAY.format(birthday.plusYears(i)) + " - " +
                                     FORMATTER_FOR_DAY.format(birthday.plusYears(i)) +
                                     System.lineSeparator();
            }
        }

        return birthdayCollection;
    }
}
