package main;

import junit.framework.TestCase;
import model.Field;
import model.Page;
import model.Site;
import model.Status;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Jsoup;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ContextConfiguration(initializers = {SiteIndexatorTest.Initializer.class})
public class SiteIndexatorTest extends TestCase {

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
    @Transactional
    public void testSiteIndexator() {
        List<Field> fields = new ArrayList<>();
        Field title = new Field("title", "title", 1);
        fieldRepository.save(title);
        fields.add(title);
        Field body = new Field("body", "body", (float) 0.8);
        fieldRepository.save(body);
        fields.add(body);

        String userAgent = "Mozilla/5.0 (compatible; BakoBot/1.0;)";
        Site site = new Site(Status.INDEXED, new Date(), "", "https://aquatoria.com/", "aquatoria");
        siteRepository.save(site);

        SiteIndexator siteIndexator = new SiteIndexator(site,"/", userAgent, fields, siteRepository, indexRepository, lemmaRepository, pageRepository, LogManager.getLogger("SearchEngineInfo"), LogManager.getRootLogger(), true);
        siteIndexator.run();
        siteIndexator.interrupt();

        assertEquals(9, pageRepository.findAll().size());
    }
}
