import java.util.HashMap;
import java.util.Map;

public class CustomerStorage {
    private final Map<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;

        String[] components = data.split("\\s+");
        if (components.length !=4) { throw new IllegalArgumentException("Некорректный формат. В строке не 4 элемента"); }
        else if (!components[INDEX_PHONE].matches("\\+7[0-9]{10}")) { throw new IllegalArgumentException("Некорректный формат номера телефона."); }
        else if (!components[INDEX_EMAIL].matches("[A-z0-9]*@[A-z0-9]*\\.[A-z]{2,3}")) { throw new IllegalArgumentException("Некорректный формат EMAIL."); }
        String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
        storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        storage.remove(name);
    }

    public Customer getCustomer(String name) {
        return storage.get(name);
    }

    public int getCount() {
        return storage.size();
    }
}