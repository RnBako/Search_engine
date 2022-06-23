public class TwoDimensionalArray {
    public static char symbol = 'X';

    public static char[][] getTwoDimensionalArray(int size) {

        //TODO: Написать метод, который создаст двумерный массив char заданного размера.
        // массив должен содержать символ symbol по диагоналям, пример для size = 3
        // [X,  , X]
        // [ , X,  ]
        // [X,  , X]
        char[][] cross = new char[size][size];
        for (int i = 0; i < cross.length; i++) {
            for (int j = 0; j < cross[i].length; j++){
                cross[i][j] = (i == j || (j == cross[i].length - 1 - i)) ? symbol : ' ';
            }
        }
        return cross;
    }
}
