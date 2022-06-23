import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static long calculateFolderSize(String path) {
        long size = 0;
        try (Stream<Path> walk = Files.walk(Path.of(path))) {
            size = walk.filter(Files::isRegularFile).mapToLong(p -> {
                try {
                    return Files.size(p);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return 0L;
                }
            }).sum();
        } catch ( IOException ex) {
            ex.printStackTrace();
        }

        return size;
    }
}
