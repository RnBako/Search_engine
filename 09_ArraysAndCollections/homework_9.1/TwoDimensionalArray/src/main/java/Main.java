public class Main {
    public static void main(String[] args) {
        //Распечатайте сгенерированный в классе TwoDimensionalArray.java двумерный массив
        char[][] cross = TwoDimensionalArray.getTwoDimensionalArray(7);
        for (int i = 0; i < cross.length; i++) {
            for (int j = 0; j < cross[i].length; j++){
                System.out.print(cross[i][j]);
            }
            System.out.println();
        }
    }
}
