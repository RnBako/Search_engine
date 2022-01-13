import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class SiteIndexator extends RecursiveTask<Page> {

    private final Page page;
    private static final Map<String, Page> siteList = new ConcurrentHashMap<>();
    private static String root;
    private static Session session;
    private static List<Field> fields;
    private static final Map<String, Lemma> lemmaList = new ConcurrentHashMap<>();
    private static final Map<String, Index> indexList = new ConcurrentHashMap<>();

    public SiteIndexator(String path, int code, String content) {
        this.page = new Page(path, code, content);

    }

    public SiteIndexator (String root, Session session) throws IOException {
        this("/", 200, Jsoup.connect(root)
                .userAgent("Mozilla/5.0 (compatible; BakoBot/1.0;)")
                .referrer("http://www.google.com")
                .maxBodySize(0).get().toString());
        this.root = root;
        this.session = session;
        this.fields = session.createQuery("from Field f").list();
    }

    @Override
    protected Page compute() {
        System.out.println("Fork for " + page.getPath() + " - started! SiteList size - " + siteList.size());

        if (siteList.get(page.getPath()) == null) {
            siteList.put(page.getPath(), page);
        } else {
            return page;
        }

        List<SiteIndexator> taskList = new ArrayList<>();
        try {
            Thread.sleep(120);
            Document document = Jsoup.connect((Objects.equals(page.getPath(), "/")) ? root : (root + page.getPath()))
                    .userAgent("Mozilla/5.0 (compatible; BakoBot/1.0;)")
                    .referrer("http://www.google.com")
                    .maxBodySize(0).get();

            HashMap<String, Integer> lemmaMap;
            for (Field field : fields) {
                Elements elements = document.select(field.getName());
                for (Element element : elements) {
                    lemmaMap = Lemmatizer.normalizeText(element.text());
                    for (String lemmaKey : lemmaMap.keySet()) {
                        if (lemmaList.get(lemmaKey) == null) lemmaList.put(lemmaKey, new Lemma(lemmaKey, 1));
                        else {
                            lemmaList.get(lemmaKey).setFrequency(lemmaList.get(lemmaKey).getFrequency() + 1);
                        }

                        String indexKey = page.getPath() + " - " + lemmaKey;
                        if (indexList.get(indexKey) == null) {
                            indexList.put(indexKey, new Index(page,
                                    lemmaList.get(lemmaKey),
                                    field.getWeight() * lemmaMap.get(lemmaKey)));
                        } else {
                            indexList.get(indexKey).setRank(indexList.get(indexKey).getRank() + field.getWeight() * lemmaMap.get(lemmaKey));
                        }
                    }
                }
            }

            Elements hrefUI = document.select("a");
            for (org.jsoup.nodes.Element element : hrefUI) {
                if (siteList.get(element.attr("href")) == null &&
                        !element.attr("href").isEmpty() &&
                        element.attr("href").charAt(0) == '/' &&
                        !element.attr("href").contains(".jpg") &&
                        !element.attr("href").contains(".pdf")) {
                    String path = element.attr("href");
                    String address = root + path;

                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int code = connection.getResponseCode();

                    Document childDocument = Jsoup.connect(address)
                            .userAgent("Mozilla/5.0 (compatible; BakoBot/1.0;)")
                            .referrer("http://www.google.com")
                            .maxBodySize(0).get();
                    SiteIndexator task = new SiteIndexator(path, code, childDocument.toString());
                    task.fork();
                    taskList.add(task);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (SiteIndexator task : taskList) {
            Page child = task.join();
            page.addChildren(child);
        }

        System.out.println("Fork for " + page.getPath() + " - finished!");

        if (Objects.equals(page.getPath(), "/")) {
            session.beginTransaction();
            session.clear();
            int bulkSize = 0;
            for (Index i : indexList.values()){
                if (bulkSize == 250) {
                    session.getTransaction().commit();
                    session.beginTransaction();
                    session.clear();
                    bulkSize = 0;
                }
                session.save(i);
                bulkSize++;
            }
            session.getTransaction().commit();
        }

        return page;
    }
}
