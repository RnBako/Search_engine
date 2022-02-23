package main;

import model.Lemma;
import model.Site;
import model.Status;
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
import repository.LemmaRepository;
import repository.SiteRepository;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ContextConfiguration(initializers = {LemmaRepositoryTest.Initializer.class})
public class LemmaRepositoryTest {

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
    private LemmaRepository lemmaRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void findByFrequencyAndLemmaInTest() {
        Site site = new Site(Status.INDEXED, new Date(), "", "https://dimonvideo.ru", "dimonvideo");
        siteRepository.save(site);
        Lemma lemma = new Lemma("фильм", 1, site);
        lemmaRepository.save(lemma);

        String lemmas = "\'фильм\'";
        List<Lemma> lemmaList= lemmaRepository.findByFrequencyAndLemmaIn(1, lemmas);
        assertEquals(lemma, lemmaList.get(0));
    }

    @Test
    public void findBySiteAndFrequencyAndLemmaInTest() {
        Site site = new Site(Status.INDEXED, new Date(), "", "https://dimonvideo.ru", "dimonvideo");
        siteRepository.save(site);
        Lemma lemma = new Lemma("фильм", 1, site);
        lemmaRepository.save(lemma);

        String lemmas = "\'фильм\'";
        List<Lemma> lemmaList= lemmaRepository.findBySiteAndFrequencyAndLemmaIn(1, 1, lemmas);
        assertEquals(lemma, lemmaList.get(0));
    }
}
