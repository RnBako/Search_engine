package main;

import org.apache.logging.log4j.Logger;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for text lemmatization
 * @author Roman Barsuchenko
 * @version 1.0
 */
public class Lemmatizer {

    /**
     * Method for text lemmatization
     * @param inputText - Text for lemmatization
     * @param loggerInfo - Logger object for info logging
     * @param isLogging - To log or not
     * @return Returns map of text lemmas
     * @throws IOException Exception when working with text
     */
    public static HashMap<String, Integer> normalizeText (String inputText, Logger loggerInfo, boolean isLogging) throws IOException {
        if (isLogging) {
            loggerInfo.info("[normalizeText] Begin.");
        }
        String cleanText = inputText.replaceAll("[^А-ё]"," ").toLowerCase(Locale.ROOT) + " ";
        String regex = "[А-ё]+[\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanText);
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        List<String> wordBaseForms;
        HashMap<String, Integer> outputMap = new HashMap<>();

        if (isLogging) {
            loggerInfo.info("[normalizeText] Clean text - " + cleanText);
        }

        while (matcher.find()) {
            String word = cleanText.substring(matcher.start(), matcher.end()).trim();
            wordBaseForms = luceneMorphology.getMorphInfo(word);
            boolean isAncillaryPart = false;
            for (String wbf : wordBaseForms) {
                String morphInfo = wbf.substring(wbf.lastIndexOf(" ") + 1);
                if (morphInfo.equals("ЧАСТ") || morphInfo.equals("СОЮЗ") || morphInfo.equals("ПРЕДЛ") || morphInfo.equals("МЕЖД")) {
                    isAncillaryPart = true;
                    break;
                }
                word = wbf.substring(0, wbf.indexOf('|'));
            }
            if (!isAncillaryPart) {
                outputMap.merge(word, 1, Integer::sum);

                if (isLogging) {
                    loggerInfo.info("[normalizeText] Work with " + word + ".");
                }
            }
        }

        return  outputMap;
    }
}
