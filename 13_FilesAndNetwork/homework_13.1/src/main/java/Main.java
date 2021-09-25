import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Введите путь к папке в формате \"C:\\Папка\\:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine().trim();
            System.out.println("Размер папки " + path + " составляет " + recalculateSize(FileUtils.calculateFolderSize(path)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String recalculateSize(long size) {
        double sizeInKBytes = (double) size / (1024);
        if (sizeInKBytes < 1024) return (String.format("%.2f", sizeInKBytes) + " Кбайт");
        double sizeInMBytes = (double) sizeInKBytes / 1024;
        if (sizeInMBytes < 1024) return (String.format("%.2f", sizeInMBytes) + " Мбайт");
        double sizeInGBytes = (double) sizeInMBytes / 1024;
        return (String.format("%.2f", sizeInGBytes) + " Гбайт");
    }
}
