import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchSystem {
    public static String searchPage(String searchLine) throws IOException {
        HashMap<String, Integer> lemmaSearchLine = Lemmatizer.normalizeText(searchLine);
        StringBuilder lemmaList = new StringBuilder();
        for (String lemma : lemmaSearchLine.keySet()) {
            lemmaList.append((lemmaList.length() > 0) ? ",'" : "'").append(lemma).append("'");
        }

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

        List<Lemma> lemmas = session.createQuery("from Lemma l where l.frequency <= 250 and l.lemma in (" + lemmaList.toString() + ") order by l.frequency").list();
        StringBuilder lemmaIdList = new StringBuilder();
        for (Lemma lemma : lemmas) {
            lemmaIdList.append((lemmaIdList.length() > 0) ? "," : "").append(lemma.getId());
        }

        List<Index> indexes = session.createSQLQuery("select i.* from `index` i where i.page_id in (select gi.page_id " +
                                                        "from (select i.page_id, count(i.lemma_id) cnt from `index` i where i.lemma_id in (" + lemmaIdList + ") " +
                                                        "group by i.page_id) gi where gi.cnt=" + lemmas.size() + ") " +
                                                        "and i.lemma_id in (" + lemmaIdList + ")").addEntity(Index.class).list();

        List<SearchResult> searchResults = new ArrayList<>();
        Map<Page, List<Index>> groupedIndexes = indexes.stream().collect(Collectors.groupingBy(Index::getPage));
        double maxRelevance = 0;
        for (Map.Entry<Page, List<Index>> item : groupedIndexes.entrySet()) {
            maxRelevance = Math.max(maxRelevance, item.getValue().stream().mapToDouble(Index::getRank).sum());
            searchResults.add(new SearchResult(item.getKey(), item.getValue().stream().mapToDouble(Index::getRank).sum(), 0));
        }

        List<Field> fields = session.createQuery("from Field f").list();
        Document document = new Document("");

        for (SearchResult searchResult : searchResults) {
            searchResult.setRelativeRelevance(searchResult.getAbsoluteRelevance() / maxRelevance);
            document = Jsoup.parse(searchResult.getPage().getContent());
            Element snippet = new Element("description");
            for (Field field : fields) {
                Element element = document.select(field.getName()).first();
                snippet = generateSnippet(element, lemmaSearchLine, snippet);
            }
            System.out.println(searchResult.getPage().getPath() + "; " + document.select("title").text() + "; Сниппет: " + snippet + "; rel - " + searchResult.getRelativeRelevance());
        }

        sessionFactory.close();

        return lemmaList.toString();
    }

    private static Element generateSnippet(Element elementForGenerate, HashMap<String, Integer> lemmaSearchLine, Element snippet) throws IOException {
        HashMap<String, Collection<Integer>> textMap = new HashMap<>();

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
                snippet.appendElement("p").appendText(previousText);
                snippet.appendElement("b").appendText(boldWord);
                snippet.appendElement("p").appendText(subsequentText);
            }
        }
        return snippet;
        //Перебор элементов рекурсивно работает медленно, т.к. на каждом вызывается лемматайзер.
//        if (elementForGenerate == null || !elementForGenerate.hasText()) {
//            return snippet;
//        } else if (elementForGenerate.childrenSize() == 0) {
//            snippetText = elementForGenerate.text() + " ";
//            if (!snippetText.trim().isEmpty()) {
//                lemmaMap = Lemmatizer.normalizeText(snippetText);
//                Set<String> matchedLemma = lemmaMap.keySet();
//                matchedLemma.retainAll(lemmaSearchLine.keySet());
//                if (!matchedLemma.isEmpty()) {
//                    for (String l : matchedLemma) {
//                        String regex = "[A-ё’]+[\\s]";
//                        Pattern pattern = Pattern.compile(regex);
//                        Matcher matcher = pattern.matcher(snippetText);
//                        while (matcher.find()) {
//                            int start = matcher.start();
//                            int end = matcher.end();
//                            String word = snippetText.substring(start, end).trim();
//                            Set<String> wordSet = Lemmatizer.normalizeText(word).keySet();
//                            if (!wordSet.isEmpty() && l.equals(wordSet.stream().findFirst().get())) {
//                                snippet.appendElement("b").appendText(word + " ");
//                            } else {
//                                snippet.appendText(word + " ");
//                            }
//                        }
//                    }
//                }
//            }
//            return snippet;
//        } else {
//            for (Element child : elementForGenerate.children()) {
//                if (child != null) {
//                    snippet = generateSnippet(child, lemmaSearchLine, snippet);
//                }
//            }
//            return snippet;
//        }
    }

}
