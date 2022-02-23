package controller;

import model.SearchResult;
import main.SearchSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.FieldRepository;
import repository.IndexRepository;
import repository.LemmaRepository;
import repository.SiteRepository;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Value("${max-frequency}")
    private String maxFrequency;

    @Value("${logging-level}")
    private String loggingLevel;

    private static Logger loggerInfo;
    private static Logger loggerDebug;

    @GetMapping("/search")
    public JSONObject search (String query, String site, Integer offset, Integer limit) {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        loggerDebug = LogManager.getRootLogger();

        JSONObject response = new JSONObject();
        try {
            if (query == null){
                if (loggingLevel.equals("info")) {
                    loggerInfo.info("[search] Query is null.");
                }

                response.put("result", false);
                response.put("error", "Задан пустой поисковый запрос");
                return response;
            }

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[search] For query - " + query + ", site - " + site + " search start.");
            }

            List<SearchResult> searchResults = SearchSystem.searchPage(query, site, lemmaRepository, siteRepository, indexRepository, fieldRepository, Integer.valueOf(maxFrequency), loggerInfo, loggingLevel.equals("info"));

            if (loggingLevel.equals("info")) {
                loggerInfo.info("[search] For query - " + query + ", site - " + site + " search results - " + searchResults.size());
            }

            response.put("result", true);
            response.put("count", searchResults.size());

            JSONArray jsonSearchResults = new JSONArray();
            for (SearchResult searchResult : searchResults) {
                JSONObject jsonSearchResult = new JSONObject();
                jsonSearchResult.put("site", searchResult.getPage().getSite().getUrl());
                jsonSearchResult.put("siteName", searchResult.getPage().getSite().getName());
                jsonSearchResult.put("uri", searchResult.getPage().getPath());
                jsonSearchResult.put("title", searchResult.getTitle());
                jsonSearchResult.put("snippet", searchResult.getSnippet().toString());
                jsonSearchResult.put("relevance", searchResult.getRelativeRelevance());
                jsonSearchResults.add(jsonSearchResult);
            }
            response.put("data", jsonSearchResults);
        } catch (Exception ex) {
            loggerDebug.debug(ex.getStackTrace());
            response.put("result", false);
            response.put("error", ex.getMessage());
        }

        return response;
    }
}
