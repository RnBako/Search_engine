import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Loader {

    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private static HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
    private static HashMap<Voter, Integer> voterCounts = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String fileName = "C:\\Users\\AVK\\Documents\\Java\\java_basics\\19_Performance\\VoteAnalyzer\\res\\data-1572M.xml";

        long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long start = System.currentTimeMillis();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();
        parser.parse(new File(fileName), handler);
        usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - usageMemory;
        System.out.println("Потребление памяти - " + usageMemory);
        System.out.println("Время выполнения - " + (System.currentTimeMillis() - start) + " мс");

        start = System.currentTimeMillis();
        handler.printDuplicatedVoters();
        System.out.println("Время запроса дублей - " + (System.currentTimeMillis() - start) + " мс");
    }
}