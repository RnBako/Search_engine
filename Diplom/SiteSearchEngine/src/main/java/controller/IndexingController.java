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

    private static final ExecutorService executorService = Executors.newWorkStealingPool();

    @GetMapping("/startIndexing")
    public JSONObject startIndexing() {
        List<SiteFromProperties> sitesFromProperties = siteService.getSites();
        Iterable<Site> siteIterable = siteRepository.findAll();
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));
        for (SiteFromProperties siteFromProperties : sitesFromProperties) {
            if (sitesFromDB.get(siteFromProperties.getUrl()) == null) {
                Site site = new Site(Status.FAILED, new Date(), "", siteFromProperties.getUrl(), siteFromProperties.getName());
                sitesFromDB.put(siteFromProperties.getUrl(), siteRepository.save(site));
            }
        }

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
            return response;
        } else {
            System.out.println("Begin - " + sitesFromDB.size());
            Iterable<Field> fieldIterable = fieldRepository.findAll();
            List<Field> fields = new ArrayList<>();
            fieldIterable.forEach(fields::add);

            Callable<Page>[] tasks = new Callable[sitesFromDB.values().size()];
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

                        site.setStatus(Status.INDEXING);
                        site.setStatusTime(new Date());
                        site.setLastError("");
                        siteRepository.save(site);

                        Page firstPage = new Page("/", 200, Jsoup.connect(site.getUrl())
                                .userAgent(userAgent)
                                .referrer("http://www.google.com")
                                .maxBodySize(0).get().toString(),
                                site);
                        Page root = new SiteIndexator(site, userAgent, fields, indexRepository, lemmaRepository, pageRepository).toIndex(firstPage);

                        site.setStatus(Status.INDEXED);
                        site.setStatusTime(new Date());
                        site.setLastError("");
                        siteRepository.save(site);
                        System.out.println("Завершили - " + site.getUrl());
                        return root;
                    } catch (Exception ex) {
                        System.out.println("Отвалились - " + ex.getMessage());
                        site.setStatus(Status.FAILED);
                        site.setStatusTime(new Date());
                        site.setLastError(ex.getMessage());
                        siteRepository.save(site);
                        return new Page("/", 200, Jsoup.connect(site.getUrl())
                                .userAgent(userAgent)
                                .referrer("http://www.google.com")
                                .maxBodySize(0).get().toString(),
                                site);
                    }
                };
                tasks[j] = task;
                j++;
            }

            for (Callable<Page> task : tasks) executorService.submit(task);
            executorService.shutdown();
            response.put("result", true);
            return response;
        }
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
                site.setStatus(Status.INDEXED);
                siteRepository.save(site);
            }
        }

        if (isIndexing) {
            executorService.shutdown();
            response.put("result", true);
        } else {
            response.put("result", false);
            response.put("error", "Индексация не запущена");
        }
        return response;
    }

    @PostMapping("/indexPage")
    public JSONObject indexPage(String url) {
        List<SiteFromProperties> sitesFromProperties = siteService.getSites();
        Iterable<Site> siteIterable = siteRepository.findAll();
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));

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
        for (SiteFromProperties siteFromProperties : sitesFromProperties) {
            if (sitesFromDB.get(siteFromProperties.getUrl()) == null) {
                Site site = new Site(Status.FAILED, new Date(), "", siteFromProperties.getUrl(), siteFromProperties.getName());
                sitesFromDB.put(siteFromProperties.getUrl(), siteRepository.save(site));
            }
            if (!rootOfURL.isEmpty() && siteFromProperties.getUrl().contains(rootOfURL)) {
                isURLinProperties = true;
                siteForIndexing = sitesFromDB.get(siteFromProperties.getUrl());
            }
        }

        JSONObject response = new JSONObject();
        if (isURLinProperties) {
            System.out.println("Site - " + siteForIndexing.getName());
            System.out.println("Path - " + pathOfURL);

            Iterable<Field> fieldIterable = fieldRepository.findAll();
            List<Field> fields = new ArrayList<>();
            fieldIterable.forEach(fields::add);

            Iterable<Page> pageIterable = pageRepository.findBySiteIdAndPath(siteForIndexing.getId(), url.substring(rootOfURL.length()));
            Iterable<Index> indexIterable = indexRepository.findByPageIn(pageIterable);
            List<Lemma> lemmaList = new ArrayList<>();
            indexIterable.forEach(index -> lemmaList.add(index.getLemma()));
            Iterable<Lemma> lemmaIterable = lemmaList;
            indexRepository.deleteAll(indexIterable);
            pageRepository.deleteAll(pageIterable);
            lemmaRepository.deleteAll(lemmaIterable);

            siteForIndexing.setStatus(Status.INDEXING);
            siteForIndexing.setStatusTime(new Date());
            siteForIndexing.setLastError("");
            siteRepository.save(siteForIndexing);

            try {
                Page firstPage = new Page(pathOfURL, 200, Jsoup.connect(siteForIndexing.getUrl())
                        .userAgent(userAgent)
                        .referrer("http://www.google.com")
                        .maxBodySize(0).get().toString(),
                        siteForIndexing);

                Page root = new SiteIndexator(siteForIndexing, userAgent, fields, indexRepository, lemmaRepository, pageRepository).toIndex(firstPage);
                siteForIndexing.setStatus(Status.INDEXED);
                siteForIndexing.setStatusTime(new Date());
                siteForIndexing.setLastError("");
                siteRepository.save(siteForIndexing);
                System.out.println("Завершили - " + siteForIndexing.getUrl());
            } catch (Exception ex) {
                System.out.println("Отвалились - " + ex.getMessage());
                siteForIndexing.setStatus(Status.FAILED);
                siteForIndexing.setStatusTime(new Date());
                siteForIndexing.setLastError(ex.getMessage());
                siteRepository.save(siteForIndexing);
            }

            response.put("result", true);
            return response;
        } else {
            response.put("result", false);
            response.put("error", "Данная страница находится за пределами сайтов, указанных в конфигурационном файле");
            return response;
        }
    }
}
