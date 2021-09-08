import java.util.Scanner;

public class Main {

    private static final int MAX_CONTAINER_IN_TRUCK = 12;
    private static final int MAX_BOXES_IN_CONTAINER = 27;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String boxes = scanner.nextLine();

        //Получаем количество контейнеров
        int containers = (Integer.parseInt(boxes) / MAX_BOXES_IN_CONTAINER) +
                         (((Integer.parseInt(boxes) % MAX_BOXES_IN_CONTAINER) > 0) ? 1 : 0);

        //Получаем количество грузовиков
        int trucks = (containers / MAX_CONTAINER_IN_TRUCK) + (((containers % MAX_CONTAINER_IN_TRUCK) > 0) ? 1 : 0);

        // TODO: вывести в консоль коробки разложенные по грузовикам и контейнерам
        // пример вывода при вводе 2
        // для отступа используйте табуляцию - \t

        /*
        Грузовик: 1
            Контейнер: 1
                Ящик: 1
                Ящик: 2
        Необходимо:
        грузовиков - 1 шт.
        контейнеров - 1 шт.
        */

        int loadedTrucks = 0;
        int loadedContainers = 0;
        int loadedBoxes = 0;

        for (int i = 1; i <= trucks; i++) {
            System.out.print("Грузовик: " + i + System.lineSeparator());
            if (i != trucks) {
                for (int j = 1; j <= MAX_CONTAINER_IN_TRUCK; j++) {
                    System.out.print("\t" + "Контейнер: " + (j + loadedTrucks * MAX_CONTAINER_IN_TRUCK)
                                     + System.lineSeparator());
                    for (int z = 1; z <= MAX_BOXES_IN_CONTAINER; z++) {
                        System.out.print("\t\t" + "Ящик: " + (z + loadedContainers * MAX_BOXES_IN_CONTAINER)
                                         + System.lineSeparator());
                        loadedBoxes++;
                    }
                    loadedContainers++;
                }
            } else {
                for (int j = loadedContainers + 1; j <= containers; j++) {
                    System.out.print("\t" + "Контейнер: " + j + System.lineSeparator());
                    if (j != containers) {
                        for (int z = loadedBoxes + 1; z <= MAX_BOXES_IN_CONTAINER; z++) {
                            System.out.print("\t\t" + "Ящик: " + z + System.lineSeparator());
                            loadedBoxes++;
                        }
                    } else {
                        for (int z = loadedBoxes + 1; z <= Integer.parseInt(boxes); z++) {
                            System.out.print("\t\t" + "Ящик: " + z + System.lineSeparator());
                            loadedBoxes++;
                        }
                    }
                    loadedContainers++;
                }
            }
            loadedTrucks++;
        }
        System.out.print("Необходимо:" + System.lineSeparator());
        System.out.print("грузовиков - " + trucks + " шт." + System.lineSeparator());
        System.out.print("контейнеров - " + containers + " шт." + System.lineSeparator());
    }

}
