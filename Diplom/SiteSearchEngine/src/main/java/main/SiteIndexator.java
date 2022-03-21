package main;

import model.*;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import repository.IndexRepository;
import repository.LemmaRepository;
import repository.PageRepository;
import repository.SiteRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Site indexing class
 * @author Roman Barsuchenko
 * @version 1.0
 */
public class SiteIndexator implements Runnable{

    /** Indexator activity parameter*/
    private boolean isActive;
    /** Root site*/
    private final Site root;
    /** User agent for site indexator*/
    private static String userAgent;
    /** Repository of sites*/
    private static SiteRepository siteRepository;
    /** Repository of indexes*/
    private static IndexRepository indexRepository;
    /** Repository of lemmas*/
    private static LemmaRepository lemmaRepository;
    /** Repository of pages*/
    private static PageRepository pageRepository;
    /** List of fields*/
    private static List<Field> fields;
    /** List of unique pages in progress*/
    private final Map<String, Page> pageList = new ConcurrentHashMap<>();
    /** List of unique lemmas in progress*/
    private final Map<String, Lemma> lemmaList = new ConcurrentHashMap<>();
    /** List of unique indexes in progress*/
    private final Map<String, Index> indexList = new ConcurrentHashMap<>();
    /** Path to first page*/
    private static String firstPagePath;
    /** Logger for info logging*/
    private static Logger loggerInfo;
    /** Logger for debug logging*/
    private static Logger loggerException;
    /** To log or not */
    private static boolean isLogging;

    /**
     * New object constructor
     * @param root - Root site
     * @param firstPagePath - Path to first page
     * @param userAgent - User-agent properties from configuration
     * @param fields - List of fields
     * @param siteRepository - Repository of sites
     * @param indexRepository - Repository of indexes
     * @param lemmaRepository - Repository of lemmas
     * @param pageRepository - Repository of pages
     * @param loggerInfo - Logger for info logging
     * @param loggerException - Logger for debug logging
     * @param isLogging - To log or not
     */
    public SiteIndexator (Site root, String firstPagePath, String userAgent, List<Field> fields, SiteRepository siteRepository, IndexRepository indexRepository, LemmaRepository lemmaRepository, PageRepository pageRepository, Logger loggerInfo, Logger loggerException, boolean isLogging) {
        isActive = true;
        this.root = root;
        SiteIndexator.firstPagePath = firstPagePath;
        SiteIndexator.userAgent = userAgent;
        SiteIndexator.siteRepository = siteRepository;
        SiteIndexator.indexRepository = indexRepository;
        SiteIndexator.lemmaRepository = lemmaRepository;
        SiteIndexator.pageRepository = pageRepository;
        SiteIndexator.fields = fields;
        this.loggerInfo = loggerInfo;
        this.loggerException = loggerException;
        this.isLogging = isLogging;
    }

    /**
     * Overridden method for running on a thread
     */
    @Override
    public void run() {
        try {
            Page firstPage = new Page((firstPagePath == "") ? "/" : firstPagePath, 200, Jsoup.connect(root.getUrl())
                    .userAgent(userAgent)
                    .referrer("http://www.google.com")
                    .maxBodySize(0).get().toString(),
                    root);
            setSiteStatus(root, Status.INDEXING, "");
            Page page = toIndex(firstPage, loggerInfo, isLogging);
            setSiteStatus(root, Status.INDEXED, "");
        } catch (Exception ex) {
            isActive = false;
            setSiteStatus(root, Status.FAILED, ex.getMessage());
            loggerException.debug(ex.getStackTrace());
        }
    }

    /**
     * Thread interrupt method
     */
    public void interrupt() {
        isActive = false;
    }

    /**
     * Site status change method
     * @param site - Site for change status
     * @param status - Status to be changed to
     * @param error - Error text, if any
     */
    private void setSiteStatus(Site site, Status status, String error) {
        site.setStatus(status);
        site.setStatusTime(new Date());
        site.setLastError(error);
        siteRepository.save(site);
    }

    /**
     * Page indexing method
     * @param page - Page for indexing
     * @param loggerInfo - Logger object for info logging
     * @param isLogging - To log or not
     * @return Return indexed page
     * @throws Exception
     */
    private synchronized Page toIndex(Page page, Logger loggerInfo, boolean isLogging) throws Exception{
        if (isLogging) {
            loggerInfo.info("Call for " + page.getPath() + " - started! PageList size - " + pageList.size() + ". root - " + root.getUrl());
        }

        if (pageList.get(page.getPath()) == null && isActive) {
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
                    lemmaMap = Lemmatizer.normalizeText(element.text(), loggerInfo, isLogging);
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
        if (hrefUI.size() < 1) {
            if (isLogging) {
                loggerInfo.info("Call for page " + page.getPath() + ", root -" + root.getUrl() + " - finished!");
            }
            return page;
        }

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
                page.addChildren(toIndex(childPage, loggerInfo, isLogging));
            }
        }

        if (isLogging) {
            loggerInfo.info("Call for page " + page.getPath() + ", root -" + root.getUrl() + " - finished!");
        }

        return page;
    }
}
