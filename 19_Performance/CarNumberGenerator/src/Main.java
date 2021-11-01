public class Main {
    public static void main(String[] args) {
        String srcDir = "C:\\Users\\AVK\\Documents\\Java\\java_basics\\19_Performance\\CarNumberGenerator\\res";
        int regionCodeBulkSize = 10;

        for (int regionCodeBulk = 0; regionCodeBulk < 10; regionCodeBulk++) {
            Loader loader = new Loader(srcDir,
                    (regionCodeBulk * regionCodeBulkSize) + 1,
                    (regionCodeBulk * regionCodeBulkSize) + regionCodeBulkSize);
            loader.start();
        }
    }
}
