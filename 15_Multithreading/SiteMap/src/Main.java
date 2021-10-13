
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) {
        System.out.println("Начали создание карты!");
        Node root = new ForkJoinPool().invoke(new SiteMapCreator("https://skillbox.ru/"));
        System.out.println("Завершили создание карты! Начали генерацию файла!");
        StringBuilder siteMap = new StringBuilder();
        siteMap.append(root.getValue() + "\n");
        System.out.println(root.getValue());
        for (Node childLevelOne : root.getChildren()) {
            siteMap.append("    " + childLevelOne.getValue() + "\n");
            System.out.println("    " + childLevelOne.getValue());
            for (Node childLevelTwo : childLevelOne.getChildren()) {
                siteMap.append("        " + childLevelTwo.getValue() + "\n");
                System.out.println("        " + childLevelTwo.getValue());
                for (Node childLevelThree : childLevelTwo.getChildren()) {
                    siteMap.append("           " + childLevelThree.getValue() + "\n");
                    System.out.println("           " + childLevelThree.getValue());
                }
            }
        }
        try {
            Files.write(Path.of("C:\\Users\\AVK\\Documents\\Java\\java_basics\\15_Multithreading\\SiteMap\\siteMap.txt"), siteMap.toString().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Завершили генерацию файла!");
    }
}
