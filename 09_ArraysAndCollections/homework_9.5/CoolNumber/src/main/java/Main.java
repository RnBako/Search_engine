import net.sf.saxon.functions.Trace;

import java.util.*;

public class Main {
    /*
    TODO:
     - реализовать методы класса CoolNumbers
     - посчитать время поиска введимого номера в консоль в каждой из структуры данных
     - проанализоровать полученные данные
     */

    public static void main(String[] args) {
        List<String> list = CoolNumbers.generateCoolNumbers();
        Collections.sort(list);
        HashSet<String> hashList = new HashSet<>(list);
        TreeSet<String> treeList = new TreeSet<>(list);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите номер в формате Х000ХХ000 или 0 для выхода: ");
            String number = scanner.nextLine();

            if (number.matches("[АВЕКМНОРСТУХ]{1}[0-9]{3}[АВЕКМНОРСТУХ]{2}[0-9]{3}")) {
                long startTime = System.nanoTime();
                boolean isFind = CoolNumbers.bruteForceSearchInList(list, number);
                long finishTime = System.nanoTime();
                System.out.println("Поиск перебором: номер " + (isFind ? "найден" : "не найден") + ", поиск занял " + (finishTime - startTime) + "нс");

                startTime = System.nanoTime();
                isFind = CoolNumbers.binarySearchInList(list, number);
                finishTime = System.nanoTime();
                System.out.println("Бинарный поиск: номер " + (isFind ? "найден" : "не найден") + ", поиск занял " + (finishTime - startTime) + "нс");

                startTime = System.nanoTime();
                isFind = CoolNumbers.searchInHashSet(hashList, number);
                finishTime = System.nanoTime();
                System.out.println("Поиск в HashSet: номер " + (isFind ? "найден" : "не найден") + ", поиск занял " + (finishTime - startTime) + "нс");

                startTime = System.nanoTime();
                isFind = CoolNumbers.searchInTreeSet(treeList, number);
                finishTime = System.nanoTime();
                System.out.println("Поиск в TreeSet: номер " + (isFind ? "найден" : "не найден") + ", поиск занял " + (finishTime - startTime) + "нс");
            } else if (number.equals("0")) {
                break;
            } else {
                System.out.println("Не верный формат номера. Попробуйте еще раз.");
            }
        }
    }
}
