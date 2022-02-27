package controller;

import model.Lemma;
import model.Page;
import model.Site;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.LemmaRepository;
import repository.PageRepository;
import repository.SiteRepository;

import java.util.List;


@RestController
public class StatisticsController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private LemmaRepository lemmaRepository;

    @Value("${logging-level}")
    private String loggingLevel;

    private static Logger loggerInfo;
    private static Logger loggerDebug;

    @GetMapping("/statistics")
//    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONObject statistics() {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        if (loggingLevel.equals("info")) {
            loggerInfo.info("[statistics] Begin.");
        }

        JSONObject response = new JSONObject();
        response.put("result",  true);

        JSONObject total = new JSONObject();
        total.put("sites", siteRepository.count());
        total.put("pages", pageRepository.count());
        total.put("lemmas", lemmaRepository.count());
        total.put("isIndexing", true);

        JSONObject statistics = new JSONObject();
        statistics.put("total", total);

        JSONArray detailed = new JSONArray();
        Iterable<Site> siteIterable = siteRepository.findAll();
        for (Site site : siteIterable) {
            if (loggingLevel.equals("info")) {
                loggerInfo.info("[statistics] For site " + site.getName() + " status " + site.getStatus());
            }

            JSONObject objDetailed = new JSONObject();
            objDetailed.put("url", site.getUrl());
            objDetailed.put("name", site.getName());
            objDetailed.put("status", site.getStatus());
            objDetailed.put("statusTime", site.getStatusTime());
            objDetailed.put("error", site.getLastError());
            List<Page> pageList = pageRepository.findBySiteId(site.getId());
            objDetailed.put("pages", pageList.size());
            List<Lemma> lemmaList = lemmaRepository.findBySiteId(site.getId());
            objDetailed.put("lemmas", lemmaList.size());
            detailed.add(objDetailed);
        }

        statistics.put("detailed", detailed);
        response.put("statistics", statistics);

        return response;
    }
}
