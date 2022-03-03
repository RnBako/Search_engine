package main.Unit;

import junit.framework.TestCase;
import main.Lemmatizer;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

public class LemmatizerTest extends TestCase {

    public void testNormalizeText() {
        try {
            HashMap<String, Integer> lemmasActual = Lemmatizer.normalizeText("Поиск для тестирования", LogManager.getLogger("SearchEngineInfo"), false);
            HashMap<String, Integer> lemmasExpected = new HashMap<>();
            lemmasExpected.put("поиск", 1);
            lemmasExpected.put("тестирование", 1);
            assertEquals(lemmasExpected, lemmasActual);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
