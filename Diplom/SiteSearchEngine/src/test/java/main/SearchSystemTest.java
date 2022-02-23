package main;

import model.*;
import org.apache.logging.log4j.LogManager;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ContextConfiguration(initializers = {SearchSystemTest.Initializer.class})
public class SearchSystemTest {

    @ClassRule
    public static MySQLContainer<?> database = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("springboot")
            .withUsername("springboot")
            .withPassword("springboot");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword(),
                    "spring.jpa.hibernate.ddl-auto = update"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Test
    public void searchPageTest() {
        String userAgent = "Mozilla/5.0 (compatible; BakoBot/1.0;)";
        Site site = new Site(Status.INDEXED, new Date(), "", "https://dimonvideo.ru", "demonvideo");
        siteRepository.save(site);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        SiteIndexator[] tasks = new SiteIndexator[1];
        Future[] futures = new Future[1];

        List<Field> fields = new ArrayList<>();
        Field title = new Field("title", "title", 1);
        fieldRepository.save(title);
        fields.add(title);
        Field body = new Field("body", "body", (float) 0.8);
        fieldRepository.save(body);
        fields.add(body);

        try {
            SiteIndexator siteIndexator = new SiteIndexator(site,"/usernews/3/1/dateD/0", userAgent, fields, siteRepository, indexRepository, lemmaRepository, pageRepository, LogManager.getLogger("SearchEngineInfo"), LogManager.getRootLogger(), false);
            tasks[0] = siteIndexator;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        futures[0] = executorService.submit(tasks[0]);
        executorService.shutdown();

        try {
            List<SearchResult> searchResultsActual = SearchSystem.searchPage("Фильм", site.getUrl(), lemmaRepository, siteRepository, indexRepository, fieldRepository, 250, LogManager.getLogger("SearchEngineInfo"), false);
            Page pageActual = null;
            for (SearchResult searchResult : searchResultsActual) {
                if (searchResult.getPage().getPath() == "/usernews/3/1/dateD/0") pageActual = searchResult.getPage();
            }

            Iterable<Page> pageIterable = pageRepository.findBySiteIdAndPath(site.getId(), "/usernews/3/1/dateD/0");
            List<Page> pages = new ArrayList<>();
            pageIterable.forEach(pages::add);
            Page pageExpected = pages.get(0);

            assertEquals(pageExpected, pageActual);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
