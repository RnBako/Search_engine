package main;

import model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import repository.IndexRepository;
import repository.LemmaRepository;
import repository.PageRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SiteIndexator {

    private final Site root;
    private static String userAgent;
    private static IndexRepository indexRepository;
    private static LemmaRepository lemmaRepository;
    private static PageRepository pageRepository;
    private static List<Field> fields;
    private final Map<String, Page> pageList = new ConcurrentHashMap<>();
    private final Map<String, Lemma> lemmaList = new ConcurrentHashMap<>();
    private final Map<String, Index> indexList = new ConcurrentHashMap<>();


    public SiteIndexator (Site root, String userAgent, List<Field> fields, IndexRepository indexRepository, LemmaRepository lemmaRepository, PageRepository pageRepository) throws Exception {
        this.root = root;
        SiteIndexator.userAgent = userAgent;
        SiteIndexator.indexRepository = indexRepository;
        SiteIndexator.lemmaRepository = lemmaRepository;
        SiteIndexator.pageRepository = pageRepository;
        SiteIndexator.fields = fields;
    }


    public synchronized Page toIndex(Page page) throws Exception{
        System.out.println("Call for " + page.getPath() + " - started! PageList size - " + pageList.size() + ". root - " + root.getUrl());

        if (pageList.get(page.getPath()) == null) {
            pageList.put(page.getPath(), page);
            page.setSite(root);
            pageRepository.save(page);
        } else {
            return page;
        }


        //Thread.sleep(120);
        Document document = Jsoup.connect((Objects.equals(page.getPath(), "/")) ? root.getUrl() : (root.getUrl() + page.getPath()))
                .userAgent(userAgent)
                .referrer("http://www.google.com")
                .maxBodySize(0).get();

        HashMap<String, Integer> lemmaMap;
        for (Field field : fields) {
            Elements elements = document.select(field.getName());
            for (Element element : elements) {
                    lemmaMap = Lemmatizer.normalizeText(element.text());
                    for (String lemmaKey : lemmaMap.keySet()) {
                        if (lemmaList.get(lemmaKey) == null) {
                            Lemma lemma = new Lemma(lemmaKey, 1, root);
                            lemmaList.put(lemmaKey, lemma);
                            //lemma.setSite(root);
                            lemmaRepository.save(lemma);
                        } else {
                            lemmaList.get(lemmaKey).setFrequency(lemmaList.get(lemmaKey).getFrequency() + 1);
                        }

                        String indexKey = page.getPath() + " - " + lemmaKey;
                        if (indexList.get(indexKey) == null) {
                            Index index = new Index(page,
                                    lemmaList.get(lemmaKey),
                                    field.getWeight() * lemmaMap.get(lemmaKey));
                            indexList.put(indexKey, index);
                            indexRepository.save(index);
                        } else {
                            indexList.get(indexKey).setRank(indexList.get(indexKey).getRank() + field.getWeight() * lemmaMap.get(lemmaKey));
                        }
                    }
            }
        }

        Elements hrefUI = document.select("a");
        if (hrefUI.size() < 1) return page;
        for (org.jsoup.nodes.Element element : hrefUI) {
            if (pageList.get(element.attr("href")) == null &&
                    !element.attr("href").isEmpty() &&
                    element.attr("href").charAt(0) == '/' &&
                    !element.attr("href").contains(".jpg") &&
                    !element.attr("href").contains(".pdf")) {
                String path = element.attr("href");
                String address = root.getUrl() + path;

                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int code = connection.getResponseCode();

                Document childDocument = Jsoup.connect(address)
                        .userAgent(userAgent)
                        .referrer("http://www.google.com")
                        .maxBodySize(0).get();
                Page childPage = new Page(path, code, childDocument.toString(), root);
                page.addChildren(toIndex(childPage));
            }
        }

        System.out.println("Call for page " + page.getPath() + ", root -" + root.getUrl() + " - finished!");

        return page;
    }
}
