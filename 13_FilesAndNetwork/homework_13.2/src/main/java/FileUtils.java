import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtils {
    public static void copyFolder(String sourceDirectory, String destinationDirectory) {
        // TODO: write code copy content of sourceDirectory to destinationDirectory
        try (Stream<Path> walk = Files.walk(Path.of(sourceDirectory))) {
            walk.forEach(p -> {
                try {
                    if (!sourceDirectory.equals(p.toString())) {
                        System.out.println("Скопировали: " + Files.copy(p, Path.of(destinationDirectory + p.toString().substring(sourceDirectory.length())), StandardCopyOption.REPLACE_EXISTING));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
