package controller;

import model.Lemma;
import model.Page;
import model.Site;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/statistics")
    public JSONObject statistics() {
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
