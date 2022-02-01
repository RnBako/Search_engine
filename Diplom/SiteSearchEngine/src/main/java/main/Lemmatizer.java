package main;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lemmatizer {
    public static HashMap<String, Integer> normalizeText (String inputText) throws IOException {
        String cleanText = inputText.replaceAll("[^А-ё]"," ").toLowerCase(Locale.ROOT) + " ";
        String regex = "[А-ё’]+[\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanText);
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        List<String> wordBaseForms;
        HashMap<String, Integer> outputMap = new HashMap<>();
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
            }
        }

        return  outputMap;
    }
}
