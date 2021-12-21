import jdk.jshell.Snippet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
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
        HashMap<String, Integer> lemmaMap;
        String snippetText = "";

        for (SearchResult searchResult : searchResults) {
            searchResult.setRelativeRelevance(searchResult.getAbsoluteRelevance() / maxRelevance);
            Element snippet = new Element("p");
            document = Jsoup.parse(searchResult.getPage().getContent());
            for (Field field : fields) {
                Elements elements = document.select(field.getName());
                for (Element element : elements) {
                    snippetText = element.text();
                    lemmaMap = Lemmatizer.normalizeText(element.text());
                    Set<String> matchedLemma = lemmaMap.keySet();
                    matchedLemma.retainAll(lemmaSearchLine.keySet());
                    if (!matchedLemma.isEmpty()) {
                        for (String l : matchedLemma) {
                            //Не меняет т.к. регистр и лемма. Надо в StringBuilder перебрать текст по словам, сравнивая их с леммами. При совпадении выделяем жирным тегом, ставим "...", запускаем итерацию по следующей лемме
                            snippetText = snippetText.replaceAll(l, "<b>" + l + "</b>");
                        }

                        snippet.html();
                        snippet.appendText(snippetText);
                        System.out.println(searchResult.getPage().getPath() + " - " + snippetText);
                    }
                }
            }
            System.out.println(searchResult.getPage().getPath() + "; " + document.select("title").text() + "; Сниппет: " + snippet.toString() + "; rel - " + searchResult.getRelativeRelevance());
        }

        sessionFactory.close();

        return lemmaList.toString();
    }


}
