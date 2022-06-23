import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PhoneBook {
    private TreeMap<String, String> phoneBook = new TreeMap<>();

    public void addContact(String phone, String name) {
        // проверьте корректность формата имени и телефона
        // если такой номер уже есть в списке, то перезаписать имя абонента
        if (phone.matches("7[0-9]{10}") && name.matches("[A-zА-я]+")) {
            phoneBook.put(phone, name);
        }  else { System.out.println("Не верный формат имени или телефона.");}
    }

    public String getNameByPhone(String phone) {
        // формат одного контакта "Имя - Телефон"
        // если контакт не найдены - вернуть пустую строку
        if (phoneBook.containsKey(phone)) { return phoneBook.get(phone) + " - " + phone; }
        else { return ""; }
    }

    public Set<String> getPhonesByName(String name) {
        // формат одного контакта "Имя - Телефон"
        // если контакт не найден - вернуть пустой TreeSet
        TreeSet <String> phoneSet = new TreeSet<>();
        if (phoneBook.containsValue(name)) {
            String nameRecord = "";
            for (String phone : phoneBook.keySet()) {
                if (phoneBook.get(phone).equals(name) && phoneSet.isEmpty()) {
                    nameRecord = phoneBook.get(phone) + " - " + phone;
                } else if (phoneBook.get(phone).equals(name)) {
                    nameRecord = nameRecord + ", " + phone;
                }
            }
            phoneSet.add(nameRecord);
            return phoneSet;
        } else { return phoneSet; }
    }

    public Set<String> getAllContacts() {
        // формат одного контакта "Имя - Телефон"
        // если контактов нет в телефонной книге - вернуть пустой TreeSet
        TreeSet <String> allContacts = new TreeSet<>();
        if (phoneBook.isEmpty()) { return allContacts; }
        else {
            String previousName = "";
            String nameRecord = "";
            for (String phone : phoneBook.keySet()) {
                if (allContacts.isEmpty() && previousName.isEmpty()) {
                    previousName = phoneBook.get(phone);
                    nameRecord = phoneBook.get(phone) + " - " + phone;
                } else if (!phoneBook.get(phone).equals(previousName)) {
                    previousName = phoneBook.get(phone);
                    allContacts.add(nameRecord);
                    nameRecord = phoneBook.get(phone) + " - " + phone;
                } else {
                    previousName = phoneBook.get(phone);
                    nameRecord = nameRecord + ", " + phone;
                }
            }
            allContacts.add(nameRecord);
            return allContacts;
        }
    }

}