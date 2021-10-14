
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Начали создание карты!");
        Node root = new ForkJoinPool().invoke(new SiteMapCreator("https://skillbox.ru/"));
        System.out.println("Завершили создание карты! Начали генерацию файла!");
        StringBuilder siteMap = new StringBuilder();
        siteMap = generateSiteMap(root, siteMap, 0);

        try {
            Files.write(Path.of("C:\\Users\\AVK\\Documents\\Java\\java_basics\\15_Multithreading\\SiteMap\\siteMap.txt"), siteMap.toString().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Завершили генерацию файла!");
    }

    public static StringBuilder generateSiteMap (Node node, StringBuilder siteMap, int tabCount) {
        String tab = "";
        for (int i = 0; i < tabCount; i++) tab = tab + "\t";
        if (node == null) {
            return siteMap;
        } if (node.getChildren().size() == 0) {
            siteMap.append(tab);
            siteMap.append(node.getValue());
            siteMap.append("\n");
            return siteMap;
        }  else {
            siteMap.append(tab);
            siteMap.append(node.getValue());
            siteMap.append("\n");
            for (Node child : node.getChildren()) {
                if (child != null) {
                    siteMap = generateSiteMap(child, siteMap, tabCount + 1);
                }
            }
            return siteMap;
        }
    }
}
