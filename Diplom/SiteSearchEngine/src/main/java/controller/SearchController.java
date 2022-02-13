package controller;

import main.SearchResult;
import main.SearchSystem;
import model.Lemma;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/search")
    public JSONObject search (String query, String site, Integer offset, Integer limit) {
        JSONObject response = new JSONObject();
        try {
            List<SearchResult> searchResults = SearchSystem.searchPage(query, site, lemmaRepository, siteRepository, indexRepository, fieldRepository);

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
            ex.printStackTrace();
            response.put("result", false);
            response.put("error", "Задан пустой поисковый запрос");
        }

        return response;
    }
}
