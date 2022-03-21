package controller;

import main.SiteIndexator;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class of index page controller
 * @author Roman Barsuchenko
 * @version 1.0
 */

@RestController
public class IndexingController {
    /** Parameter to get sites list from the configuration*/
    @Autowired
    SiteService siteService;

    /** Repository of fields*/
    @Autowired
    private FieldRepository fieldRepository;

    /** Repository of indexes*/
    @Autowired
    private IndexRepository indexRepository;

    /** Repository of lemmas*/
    @Autowired
    private LemmaRepository lemmaRepository;

    /** Repository of pages*/
    @Autowired
    private PageRepository pageRepository;

    /** Repository of sites*/
    @Autowired
    private SiteRepository siteRepository;

    /** User-agent properties from configuration*/
    @Value("${user-agent}")
    private String userAgent;

    /** Logging level properties from configuration*/
    @Value("${logging-level}")
    private String loggingLevel;

    /** Logger for info logging*/
    private static Logger loggerInfo;
    /** Logger for debug logging*/
    private static Logger loggerDebug;

    /** ExecutorService for multithreaded indexing*/
    private static ExecutorService executorService = null;
    /** SiteIndexator for multithreaded indexing*/
    private static SiteIndexator[] tasks;
    /** Threads for SiteIndexator*/
    private static Future[] futures;

    /**
     * Sites indexing start method
     * @return return JSON object with result start indexing for index page
     */
    @GetMapping("/startIndexing")
    public JSONObject startIndexing() {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        syncSitePropertiesAndDB(sitesFromDB);

        boolean isIndexing = false;
        for (Site site : sitesFromDB.values()) {
            if (site.getStatus() == Status.INDEXING) {
                isIndexing = true;
                break;
            }
        }

        JSONObject response = new JSONObject();
        if (isIndexing) {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[startIndexing] Indexing started");
            }
            response.put("result", false);
            response.put("error", "Indexing is running");
        } else {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[startIndexing] Indexing begin. Sites count - " + sitesFromDB.size());
            }
            List<Field> fields = getFields();

            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            tasks = new SiteIndexator[sitesFromDB.values().size()];
            futures = new Future[sitesFromDB.values().size()];
            int j = 0;
            for (Site site : sitesFromDB.values()) {
                try {
                    if (loggingLevel.equals("info")) {
                        loggerInfo.info("[startIndexing] Site - " + site.getName() + " start.");
                    }

                    Iterable<Page> pageIterable = pageRepository.findBySiteId(site.getId());
                    Iterable<Index> indexIterable = indexRepository.findByPageIn(pageIterable);
                    Iterable<Lemma> lemmaIterable = lemmaRepository.findBySiteId(site.getId());
                    indexRepository.deleteAll(indexIterable);
                    pageRepository.deleteAll(pageIterable);
                    lemmaRepository.deleteAll(lemmaIterable);

                    if (loggingLevel.equals("info")) {
                        loggerInfo.info("[startIndexing] Cleanup completed for the site - " + site.getName());
                    }

                    SiteIndexator siteIndexator = new SiteIndexator(site, "", userAgent, fields, siteRepository, indexRepository, lemmaRepository, pageRepository, loggerInfo, loggerDebug, loggingLevel.equals("info"));

                    if (loggingLevel.equals("info")) {
                        loggerInfo.info("[startIndexing] For site - " + site.getName() + " indexator is created.");
                    }

                    tasks[j] = siteIndexator;
                } catch (Exception ex) {
                    loggerDebug.debug(ex.getStackTrace());
                    setSiteStatus(site, Status.FAILED, ex.getMessage());
                }
                j++;
            }

            j = 0;
            for (SiteIndexator task : tasks) {
                futures[j] = executorService.submit(task);
                if (loggingLevel.equals("info")) {
                    loggerInfo.info("[startIndexing] For task - " + task + " indexator is started.");
                }
                j++;
            }
            executorService.shutdownNow();
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[startIndexing] Indexing end.");
            }
            response.put("result", true);
        }
        return response;
    }

    /**
     * Sites indexing stop method
     * @return return JSON object with result stop indexing for index page
     */
    @GetMapping("/stopIndexing")
    public JSONObject stopIndexing() {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        JSONObject response = new JSONObject();
        Iterable<Site> siteIterable = siteRepository.findAll();
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));

        boolean isIndexing = false;
        for (Site site : sitesFromDB.values()) {
            if (site.getStatus() == Status.INDEXING) {
                isIndexing = true;
                setSiteStatus(site, Status.INDEXED, "");
            }
        }

        if (isIndexing && executorService != null) {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[stopIndexing] Stop indexing start.");
            }
            for (SiteIndexator siteIndexator : tasks) {
                if (loggingLevel.equals("info")) {
                    loggerInfo.info("[stopIndexing] " + siteIndexator.hashCode() + " interrupted.");
                }
                siteIndexator.interrupt();
            }
            for (Future future : futures) future.cancel(true);
            executorService.shutdownNow();

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[stopIndexing] Stop indexing end.");
            }

            response.put("result", true);
        } else {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[stopIndexing] Indexing is not starting.");
            }

            response.put("result", false);
            response.put("error", "Indexing is not running");
        }
        return response;
    }

    /**
     * Site or page indexing start method
     * @param url - page URL to be indexed
     * @return return JSON object with result start indexing for index page
     */
    @PostMapping("/indexPage")
    public JSONObject indexPage(String url) {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        HashMap<String, Site> sitesFromDB = new HashMap<>();
        syncSitePropertiesAndDB(sitesFromDB);

        if (loggingLevel.equals("info")) {
            loggerInfo.info("[indexPage] Index page begin.");
        }

        String rootOfURL = "";
        String pathOfURL = "";
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            rootOfURL = url.substring(matcher.start(), matcher.end()-1).trim();
            pathOfURL = url.substring(rootOfURL.length());

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[indexPage] Root - " + rootOfURL + ", path - " + pathOfURL);
            }
        }

        boolean isURLinProperties = false;
        Site siteForIndexing = null;
        for (Site site : sitesFromDB.values()) {
            if (!rootOfURL.isEmpty() && site.getUrl().contains(rootOfURL)) {
                isURLinProperties = true;
                siteForIndexing = sitesFromDB.get(site.getUrl());

                if (loggingLevel.equals("info")) {
                    loggerInfo.info("[indexPage] URL " + site.getUrl() + " is in properties.");
                }
            }
        }

        JSONObject response = new JSONObject();
        if (isURLinProperties) {
            List<Field> fields = getFields();

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[indexPage] Site - " + siteForIndexing.getName() + " start.");
            }

            Iterable<Page> pageIterable = pageRepository.findBySiteIdAndPath(siteForIndexing.getId(), url.substring(rootOfURL.length()));
            Iterable<Index> indexIterable = indexRepository.findByPageIn(pageIterable);
            List<Lemma> lemmaList = new ArrayList<>();
            indexIterable.forEach(index -> lemmaList.add(index.getLemma()));
            Iterable<Lemma> lemmaIterable = lemmaList;
            indexRepository.deleteAll(indexIterable);
            pageRepository.deleteAll(pageIterable);
            lemmaRepository.deleteAll(lemmaIterable);

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[indexPage] Cleanup completed for the site - " + siteForIndexing.getName());
            }

            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            tasks = new SiteIndexator[1];
            futures = new Future[1];

            try {
                SiteIndexator siteIndexator = new SiteIndexator(siteForIndexing, url.substring(rootOfURL.length()), userAgent, fields, siteRepository, indexRepository, lemmaRepository, pageRepository, loggerInfo, loggerDebug, loggingLevel.equals("info"));
                tasks[0] = siteIndexator;
            } catch (Exception ex) {
                loggerDebug.debug(ex.getStackTrace());
                setSiteStatus(siteForIndexing, Status.FAILED, ex.getMessage());
            }

            futures[0] = executorService.submit(tasks[0]);
            executorService.shutdown();

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[indexPage] Indexing end.");
            }

            response.put("result", true);
        } else {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[indexPage] " + siteForIndexing.getName() + " is outside the sites specified in the configuration file");
            }

            response.put("result", false);
            response.put("error", "This page is outside the sites specified in the configuration file");
        }
        return response;
    }

    /**
     * Method for synchronisation sites list in database and configuration
     * @param sitesFromDB - Sites list from database
     */
    private void syncSitePropertiesAndDB(HashMap<String, Site> sitesFromDB) {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        List<SiteFromProperties> sitesFromProperties = siteService.getSites();
        Iterable<Site> siteIterable = siteRepository.findAll();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));
        for (SiteFromProperties siteFromProperties : sitesFromProperties) {
            if (sitesFromDB.get(siteFromProperties.getUrl()) == null) {
                Site site = new Site(Status.FAILED, new Date(), "", siteFromProperties.getUrl(), siteFromProperties.getName());
                sitesFromDB.put(siteFromProperties.getUrl(), siteRepository.save(site));
                if (loggingLevel.equals("info")) {
                    loggerInfo.info("Sync site in properties and db. Added - " + site.getName());
                }
            }
        }
    }

    /**
     * Site status change method
     * @param site - Site for change status
     * @param status - Status to be changed to
     * @param error - Error text, if any
     */
    private void setSiteStatus(Site site, Status status, String error) {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        if (loggingLevel.equals("info")) {
            loggerInfo.info("For - " + site.getName()+ " status is " + status);
        }

        site.setStatus(status);
        site.setStatusTime(new Date());
        site.setLastError(error);
        siteRepository.save(site);
    }

    /**
     * Method for getting fields
     * @return Returns a list of fields from the database
     */
    private List<Field> getFields() {
        Iterable<Field> fieldIterable = fieldRepository.findAll();
        List<Field> fields = new ArrayList<>();
        fieldIterable.forEach(fields::add);
        return fields;
    }
}
