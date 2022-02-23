package main;

import model.Field;
import model.Page;
import model.Site;
import model.Status;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SearchSystemTest {
    @Autowired
    private static LemmaRepository lemmaRepository;
    @Autowired
    private static SiteRepository siteRepository;
    @Autowired
    private static IndexRepository indexRepository;
    @Autowired
    private static FieldRepository fieldRepository;
    @Autowired
    private static PageRepository pageRepository;
    private String userAgent = "Mozilla/5.0 (compatible; BakoBot/1.0;)";

    @Test
    public void testSearchPage() {
        Iterable<Site> siteIterable = siteRepository.findAll();
        HashMap<String, Site> sitesFromDB = new HashMap<>();
        siteIterable.forEach(site -> sitesFromDB.put(site.getUrl(), site));
        String siteForSearch;
        if (sitesFromDB.get("https://dimonvideo.ru") == null) {
            Site site = new Site(Status.FAILED, new Date(), "", "https://dimonvideo.ru", "demonvideo");
            sitesFromDB.put("https://dimonvideo.ru", siteRepository.save(site));

            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            SiteIndexator[] tasks = new SiteIndexator[1];
            Future[] futures = new Future[1];

            Iterable<Field> fieldIterable = fieldRepository.findAll();
            List<Field> fields = new ArrayList<>();
            fieldIterable.forEach(fields::add);

            try {
                SiteIndexator siteIndexator = new SiteIndexator(site,"/usernews/3/1/dateD/0", userAgent, fields, siteRepository, indexRepository, lemmaRepository, pageRepository, LogManager.getLogger("SearchEngineInfo"), LogManager.getRootLogger(), false);
                tasks[0] = siteIndexator;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            futures[0] = executorService.submit(tasks[0]);
            executorService.shutdown();
        }
        siteForSearch =  "https://dimonvideo.ru";

        try {
            List<SearchResult> searchResultsActual = SearchSystem.searchPage("Фильм", siteForSearch, lemmaRepository, siteRepository, indexRepository, fieldRepository, 250, LogManager.getLogger("SearchEngineInfo"), false);
            Page pageActual = null;
            for (SearchResult searchResult : searchResultsActual) {
                if (searchResult.getPage().getPath() == "/usernews/3/1/dateD/0") pageActual = searchResult.getPage();
            }

            Iterable<Page> pageIterable = pageRepository.findBySiteIdAndPath(sitesFromDB.get("https://dimonvideo.ru").getId(), "/usernews/3/1/dateD/0");
            List<Page> pages = new ArrayList<>();
            pageIterable.forEach(pages::add);
            Page pageExpected = pages.get(0);

            assertThat(pageActual).isEqualTo(pageExpected);

//            assertEquals(pageExpected, pageActual);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
