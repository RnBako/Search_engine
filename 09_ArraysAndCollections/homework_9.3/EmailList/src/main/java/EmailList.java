import java.util.*;

public class EmailList {
    private final TreeSet<String> emailList = new TreeSet<>();
    public void add(String email) {
        // TODO: валидный формат email добавляется
        email = email.toLowerCase(Locale.ROOT);
        if (email.matches("[A-z0-9]*@[A-z0-9]*\\.[A-z]{2,3}")) {
            emailList.add(email);
        } else { System.out.println(Main.WRONG_EMAIL_ANSWER); }
    }

    public List<String> getSortedEmails() {
        // TODO: возвращается список электронных адресов в алфавитном порядке
        return new ArrayList<String>(emailList);
    }

}
