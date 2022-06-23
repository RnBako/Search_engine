package Elevator;

public class Elevator {
    private int currentFloor = 1;
    private int minFloor;
    private int maxFloor;

    //Конструктор, задающий начальный и последний этаж
    public Elevator (int minFloor, int maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    //Возвращаем текущий этаж
    private int getCurrentFloor() { return currentFloor; }

    //Перемещаем на этаж вниз
    private void moveDown() { currentFloor = currentFloor > minFloor ? currentFloor - 1 : currentFloor; }

    //Перемещаем на этаж вверх
    private void moveUp() { currentFloor = currentFloor < maxFloor ? currentFloor + 1 : currentFloor; }

    //Перемещение на заданный этаж
    public void move(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            System.out.println("Этаж задан не верно!");
            return;
        } else if (floor > currentFloor) {
            while (floor > currentFloor) {
                moveUp();
                System.out.println("Текущий этаж - " + getCurrentFloor());
            }
        } else if (floor < currentFloor) {
            while (floor < currentFloor) {
                moveDown();
                System.out.println("Текущий этаж - " + getCurrentFloor());
            }
        }

    }
}
