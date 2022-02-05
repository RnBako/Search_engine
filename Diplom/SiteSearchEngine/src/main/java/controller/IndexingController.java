package controller;

import main.SiteIndexator;
import model.*;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
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

@RestController
public class IndexingController {
    @Autowired
    SiteService siteService;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Value("${user-agent}")
    private String userAgent;

    private static ExecutorService executorService = null;
    private static Future[] futures;

    @GetMapping("/startIndexing")
    public JSONObject startIndexing() {
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
            response.put("result", false);
            response.put("error", "Индексация уже запущена");
        } else {
            System.out.println("Begin - " + sitesFromDB.size());
            List<Field> fields = getFields();

            executorService = Executors.newWorkStealingPool();
            Callable<Page>[] tasks = new Callable[sitesFromDB.values().size()];
            futures = new Future[sitesFromDB.values().size()];
            int j = 0;
            for (Site site : sitesFromDB.values()) {
                Callable<Page> task = () -> {
                    System.out.println("Поток - " + Thread.currentThread().getName() + ", для - " + site.getUrl());
                    try {
                        Iterable<Page> pageIterable = pageRepository.findBySiteId(site.getId());
                        Iterable<Index> indexIterable = indexRepository.findByPageIn(pageIterable);
                        Iterable<Lemma> lemmaIterable = lemmaRepository.findBySiteId(site.getId());
                        indexRepository.deleteAll(indexIterable);
                        pageRepository.deleteAll(pageIterable);
                        lemmaRepository.deleteAll(lemmaIterable);

                        setSiteStatus(site, Status.INDEXING, "");

                        Page firstPage = new Page("/", 200, Jsoup.connect(site.getUrl())
                                .userAgent(userAgent)
                                .referrer("http://www.google.com")
                                .maxBodySize(0).get().toString(),
                                site);
                        Page root = new SiteIndexator(site, userAgent, fields, indexRepository, lemmaRepository, pageRepository).toIndex(firstPage);

                        setSiteStatus(site, Status.INDEXED, "");
                        System.out.println("Завершили - " + site.getUrl());
                        return root;
                    } catch (Exception ex) {
                        setSiteStatus(site, Status.FAILED, ex.getMessage());

                        System.out.println("Отвалились - " + ex.getMessage());
                        throw new RuntimeException(ex.getMessage());
                    }
                };

                tasks[j] = task;
                j++;
            }

            j = 0;
            for (Callable<Page> task : tasks) {
                futures[j] = executorService.submit(task);
                j++;
            }
            executorService.shutdown();
            response.put("result", true);
        }
        return response;
    }

    @GetMapping("/stopIndexing")
    public JSONObject stopIndexing() {
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
            for (Future future : futures) future.cancel(true);
            executorService.shutdownNow();
            response.put("result", true);
        } else {
            response.put("result", false);
            response.put("error", "Индексация не запущена");
        }
        return response;
    }

    @PostMapping("/indexPage")
    public JSONObject indexPage(String url) {
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        syncSitePropertiesAndDB(sitesFromDB);

        String rootOfURL = "";
        String pathOfURL = "";
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            rootOfURL = url.substring(matcher.start(), matcher.end()-1).trim();
            pathOfURL = url.substring(rootOfURL.length());
        }
        boolean isURLinProperties = false;
        Site siteForIndexing = null;
        for (Site site : sitesFromDB.values()) {
            if (!rootOfURL.isEmpty() && site.getUrl().contains(rootOfURL)) {
                isURLinProperties = true;
                siteForIndexing = sitesFromDB.get(site.getUrl());
            }
        }

        JSONObject response = new JSONObject();
        if (isURLinProperties) {
            System.out.println("Site - " + siteForIndexing.getName());
            System.out.println("Path - " + pathOfURL);

            List<Field> fields = getFields();

            Iterable<Page> pageIterable = pageRepository.findBySiteIdAndPath(siteForIndexing.getId(), url.substring(rootOfURL.length()));
            Iterable<Index> indexIterable = indexRepository.findByPageIn(pageIterable);
            List<Lemma> lemmaList = new ArrayList<>();
            indexIterable.forEach(index -> lemmaList.add(index.getLemma()));
            Iterable<Lemma> lemmaIterable = lemmaList;
            indexRepository.deleteAll(indexIterable);
            pageRepository.deleteAll(pageIterable);
            lemmaRepository.deleteAll(lemmaIterable);

            setSiteStatus(siteForIndexing, Status.INDEXING, "");

            try {
                Page firstPage = new Page(pathOfURL, 200, Jsoup.connect(siteForIndexing.getUrl())
                        .userAgent(userAgent)
                        .referrer("http://www.google.com")
                        .maxBodySize(0).get().toString(),
                        siteForIndexing);

                Page root = new SiteIndexator(siteForIndexing, userAgent, fields, indexRepository, lemmaRepository, pageRepository).toIndex(firstPage);

                setSiteStatus(siteForIndexing, Status.INDEXED, "");
                System.out.println("Завершили - " + siteForIndexing.getUrl());
            } catch (Exception ex) {
                System.out.println("Отвалились - " + ex.getMessage());
                setSiteStatus(siteForIndexing, Status.FAILED, ex.getMessage());
            }

            response.put("result", true);
        } else {
            response.put("result", false);
            response.put("error", "Данная страница находится за пределами сайтов, указанных в конфигурационном файле");
        }
        return response;
    }

    private void syncSitePropertiesAndDB(HashMap<String, Site> sitesFromDB) {
        List<SiteFromProperties> sitesFromProperties = siteService.getSites();
        Iterable<Site> siteIterable = siteRepository.findAll();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));
        for (SiteFromProperties siteFromProperties : sitesFromProperties) {
            if (sitesFromDB.get(siteFromProperties.getUrl()) == null) {
                Site site = new Site(Status.FAILED, new Date(), "", siteFromProperties.getUrl(), siteFromProperties.getName());
                sitesFromDB.put(siteFromProperties.getUrl(), siteRepository.save(site));
            }
        }
    }

    private void setSiteStatus(Site site, Status status, String error) {
        site.setStatus(status);
        site.setStatusTime(new Date());
        site.setLastError(error);
        siteRepository.save(site);
    }

    private List<Field> getFields() {
        Iterable<Field> fieldIterable = fieldRepository.findAll();
        List<Field> fields = new ArrayList<>();
        fieldIterable.forEach(fields::add);
        return fields;
    }
}
