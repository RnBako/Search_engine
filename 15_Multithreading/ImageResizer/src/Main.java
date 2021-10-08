import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    private static  int newWidth = 300;

    public static void main(String[] args) {
        String srcFolder = "C:/$D/src";
        String dstFolder = "C:/$D/dst";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();
        int coreCount = Runtime.getRuntime().availableProcessors();

        for (int i = 1; i <= coreCount; i++) {
            int divider = files.length / coreCount;
            File[] filesForCore = (i != coreCount) ? new File[divider] : new File[files.length - ((divider) * (coreCount - 1))];
            System.arraycopy(files, (i-1) * divider, filesForCore, 0, filesForCore.length);
            ImageResizer resizer = new ImageResizer(filesForCore, newWidth, dstFolder, start);
            resizer.start();
        }
    }
}
