import java.util.*;

public class CoolNumbers {

    public static List<String> generateCoolNumbers() {
        String[] chars = {"А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х"};
        TreeSet<String> carCoolNumbers = new TreeSet<>();
        for (String c : chars){
            for (int region = 1; region <= 199; region++){
                for (int number = 0; number <= 999; number++) {
                    carCoolNumbers.add(String.format("%s%03d%s%s%03d", c, number, c, c, region));
                }
            }
        }
        return new ArrayList<>(carCoolNumbers);
    }

    public static boolean bruteForceSearchInList(List<String> list, String number) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(number)) { return true; }
        }
        return false;
    }

    public static boolean binarySearchInList(List<String> sortedList, String number) {
        return (Collections.binarySearch(sortedList, number) >= 0 );
    }


    public static boolean searchInHashSet(HashSet<String> hashSet, String number) {
        return  (hashSet.contains(number));
    }

    public static boolean searchInTreeSet(TreeSet<String> treeSet, String number) {
        return treeSet.contains(number);
    }

}
