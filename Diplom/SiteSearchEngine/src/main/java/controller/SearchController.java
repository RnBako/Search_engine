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

/**
 * Class of search page controller
 * @author Roman Barsuchenko
 * @version 1.0
 */
@RestController
public class SearchController {

    /** Repository of lemmas*/
    @Autowired
    private LemmaRepository lemmaRepository;

    /** Repository of sites*/
    @Autowired
    private SiteRepository siteRepository;

    /** Repository of indexes*/
    @Autowired
    private IndexRepository indexRepository;

    /** Repository of fields*/
    @Autowired
    private FieldRepository fieldRepository;

    /** Max frequency properties from configuration*/
    @Value("${max-frequency}")
    private String maxFrequency;

    /** Logging level properties from configuration*/
    @Value("${logging-level}")
    private String loggingLevel;

    /** Logger for info logging*/
    private static Logger loggerInfo;
    /** Logger for debug logging*/
    private static Logger loggerDebug;

    /**
     * Method for searching given text on indexed pages
     * @param query - Text for search
     * @param site - Site to search
     * @param offset - Offset from 0 for paging
     * @param limit - Number of results to display
     * @return return JSON object with search result for search page
     */
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
                response.put("error", "An empty search term has been specified");
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
