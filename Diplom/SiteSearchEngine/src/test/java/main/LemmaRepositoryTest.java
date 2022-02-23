package main;

import junit.framework.TestCase;
import model.Lemma;
import model.Site;
import model.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.LemmaRepository;
import repository.SiteRepository;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LemmaRepositoryTest extends TestCase {

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

        List<Lemma> lemmaList= lemmaRepository.findByFrequencyAndLemmaIn(1, "1");
        assertEquals(lemma, lemmaList.get(0));
    }

    @Test
    public void findBySiteAndFrequencyAndLemmaInTest() {
        Site site = new Site(Status.INDEXED, new Date(), "", "https://dimonvideo.ru", "dimonvideo");
        siteRepository.save(site);
        Lemma lemma = new Lemma("фильм", 1, site);
        lemmaRepository.save(lemma);

        List<Lemma> lemmaList= lemmaRepository.findBySiteAndFrequencyAndLemmaIn(1, 1, "1");
        assertEquals(lemma, lemmaList.get(0));
    }
}
