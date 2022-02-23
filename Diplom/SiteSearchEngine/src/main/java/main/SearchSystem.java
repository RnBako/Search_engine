package main;

import model.*;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import repository.FieldRepository;
import repository.IndexRepository;
import repository.LemmaRepository;
import repository.SiteRepository;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchSystem {
    public static List<SearchResult> searchPage(String query, String site, LemmaRepository lemmaRepository, SiteRepository siteRepository, IndexRepository indexRepository, FieldRepository fieldRepository, Integer maxFrequency, Logger loggerInfo, boolean isLogging) throws IOException {
        if (isLogging) {
            loggerInfo.info("[searchPage] Begin.");
        }

        HashMap<String, Integer> lemmaSearchLine = Lemmatizer.normalizeText(query, loggerInfo, isLogging);
        StringBuilder lemmaList = new StringBuilder();
        for (String lemma : lemmaSearchLine.keySet()) {
            lemmaList.append((lemmaList.length() > 0) ? ",'" : "'").append(lemma).append("'");
        }

        if (isLogging) {
            loggerInfo.info("[searchPage] maxFrequency " + maxFrequency + ", lemmaList - " + lemmaList);
        }

        List<Lemma> lemmas;
        if (site == null) {
            lemmas = lemmaRepository.findByFrequencyAndLemmaIn(maxFrequency, lemmaList.toString());
        } else {
            List<Site> siteList = siteRepository.findByUrl(site);
            lemmas = lemmaRepository.findBySiteAndFrequencyAndLemmaIn(siteList.get(0).getId(), maxFrequency, lemmaList.toString());
        }

        if (isLogging) {
            loggerInfo.info("[searchPage] Lemmas count " + lemmas.size());
        }

        StringBuilder lemmaIdList = new StringBuilder();
        for (Lemma lemma : lemmas) {
            lemmaIdList.append((lemmaIdList.length() > 0) ? "," : "").append(lemma.getId());
        }
        List<Index> indexes = indexRepository.findByLemmaAndLemmaSize(lemmaIdList.toString(), lemmas.size());

        if (isLogging) {
            loggerInfo.info("[searchPage] Indexes count " + indexes.size());
        }

        List<SearchResult> searchResults = new ArrayList<>();
        Map<Page, List<Index>> groupedIndexes = indexes.stream().collect(Collectors.groupingBy(Index::getPage));
        double maxRelevance = 0;
        for (Map.Entry<Page, List<Index>> item : groupedIndexes.entrySet()) {
            maxRelevance = Math.max(maxRelevance, item.getValue().stream().mapToDouble(Index::getRank).sum());
            searchResults.add(new SearchResult(item.getKey(), item.getValue().stream().mapToDouble(Index::getRank).sum(), 0));
        }

        if (isLogging) {
            loggerInfo.info("[searchPage] Search results count" + searchResults.size());
        }

        Iterable<Field> fieldIterable = fieldRepository.findAll();
        List<Field> fields = new ArrayList<>();
        fieldIterable.forEach(fields::add);
        Document document;

        for (SearchResult searchResult : searchResults) {
            searchResult.setRelativeRelevance(searchResult.getAbsoluteRelevance() / maxRelevance);
            document = Jsoup.parse(searchResult.getPage().getContent());
            Element snippet = new Element("p");
            for (Field field : fields) {
                Element element = document.select(field.getName()).first();
                if (element != null) generateSnippet(element, lemmaSearchLine, snippet, loggerInfo, isLogging);
            }
            searchResult.setTitle(document.select("title").text());
            searchResult.setSnippet(snippet);
        }
        searchResults.sort(Collections.reverseOrder(SearchResult.COMPARE_BY_RELATIVE_RELEVANCE));

        if (isLogging) {
            loggerInfo.info("[searchPage] Snippet added.");
        }

        return searchResults;
    }

    private static void generateSnippet(Element elementForGenerate, HashMap<String, Integer> lemmaSearchLine, Element snippet, Logger loggerInfo, boolean isLogging) throws IOException {
        HashMap<String, Collection<Integer>> textMap = new HashMap<>();

        if (isLogging) {
            loggerInfo.info("[generateSnippet] Snippet for element - " + elementForGenerate.toString());
        }

        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        List<String> wordBaseForms;

        String elementText = elementForGenerate.text();
        String regex = "[А-ё’]+[\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(elementText);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String word = elementText.substring(start, end).trim().toLowerCase(Locale.ROOT);
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
                if (textMap.get(word) == null) {
                    Collection <Integer> wordIndex = new ArrayList<>();
                    wordIndex.add(start);
                    textMap.put(word, wordIndex);
                } else {
                    textMap.get(word).add(start);
                }
            }
        }

        for (String searchWord : lemmaSearchLine.keySet()) {
            if (textMap.get(searchWord) != null) {
                int index = textMap.get(searchWord).stream().findFirst().get();
                if (index > 0) {
                    String temp = elementForGenerate.text().substring(0, index);
                    for (int i = 0; i < 6; i++) {
                        temp = temp.substring(0, temp.lastIndexOf(" "));
                    }
                    String previousText = elementForGenerate.text().substring(temp.length(), index);
                    temp = elementForGenerate.text().substring(index);
                    for (int i = 0; i < 6; i++) {
                        temp = temp.substring(temp.indexOf(" ")).trim();
                    }
                    String subsequentText = elementForGenerate.text().substring(index, elementForGenerate.text().length() - temp.length());
                    String boldWord = subsequentText.substring(0, subsequentText.indexOf(" "));
                    subsequentText = subsequentText.substring(subsequentText.indexOf(" "));
                    snippet.appendText(previousText);
                    snippet.appendElement("b").appendText(boldWord);
                    snippet.appendText(subsequentText);
                } else {
                    String temp = elementForGenerate.text();
                    for (int i = 0; i < 6; i++) {
                        temp = temp.substring(temp.indexOf(" ")).trim();
                    }
                    String subsequentText = elementForGenerate.text().substring(index, elementForGenerate.text().length() - temp.length());
                    String boldWord = subsequentText.substring(0, subsequentText.indexOf(" "));
                    subsequentText = subsequentText.substring(subsequentText.indexOf(" "));
                    snippet.appendElement("b").appendText(boldWord);
                    snippet.appendText(subsequentText);
                }
            }
        }

        if (isLogging) {
            loggerInfo.info("[generateSnippet] Snippet for element - " + elementForGenerate.toString() + " generated.");
        }
    }

}
