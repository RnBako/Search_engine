import java.io.PrintWriter;

public class Loader extends Thread{
    private String srcDir;
    private int regionCodeStart;
    private int regionCodeEnd;

    public Loader(String srcDir, int regionCodeStart, int regionCodeEnd) {
        this.srcDir = srcDir;
        this.regionCodeStart = regionCodeStart;
        this.regionCodeEnd = regionCodeEnd;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        try {
            PrintWriter writer = new PrintWriter(srcDir + "\\numbers_" + regionCodeStart + "_" + regionCodeEnd + ".txt");

            char letters[] = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
            for (int regionCode = regionCodeStart; regionCode <= regionCodeEnd; regionCode++) {
                StringBuilder regionBuilder = new StringBuilder();
                for (int number = 1; number < 1000; number++) {
                    for (char firstLetter : letters) {
                        for (char secondLetter : letters) {
                            for (char thirdLetter : letters) {
                                regionBuilder.append(firstLetter);
                                regionBuilder.append(padNumber(number, 3));
                                regionBuilder.append(secondLetter);
                                regionBuilder.append(thirdLetter);
                                regionBuilder.append(padNumber(regionCode, 2));
                                regionBuilder.append('\n');
                            }
                        }
                    }
                }
                writer.write(regionBuilder.toString());
            }

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println((System.currentTimeMillis() - start) + " ms");
    }

    private static String padNumber(int number, int numberLength) {
        StringBuilder numberStr = new StringBuilder();
        numberStr.append(number);
        int padSize = numberLength - numberStr.length();

        for (int i = 0; i < padSize; i++) {
            numberStr.insert(0, '0');
        }

        return numberStr.toString();
    }
}
