public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        container.addCount(5672);
        System.out.println(container.getCount());

        // TODO: ниже напишите код для выполнения задания:
        //  С помощью цикла и преобразования чисел в символы найдите все коды
        //  букв русского алфавита — заглавных и строчных, в том числе буквы Ё.

        for (int i = 0; i < 65536; i++) {
            if (i == (int)'Ё' || i == (int)'ё') {
                System.out.println(i + " - " + (char) i);
            } else  if (i >= (int)'А' && i <= (int)'я' ) {
                System.out.println(i + " - " + (char) i);
            }
        }

//        for (char c = 'Ё'; c <= 'ё'; c++) {
//            System.out.println(c + " - " + (int) c);
//        }
    }
}
