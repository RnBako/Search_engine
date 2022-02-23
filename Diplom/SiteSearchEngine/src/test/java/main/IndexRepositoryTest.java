package main;

import junit.framework.TestCase;
import model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.IndexRepository;
import repository.LemmaRepository;
import repository.PageRepository;
import repository.SiteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IndexRepositoryTest extends TestCase {

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private LemmaRepository lemmaRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void findByLemmaAndLemmaSizeTest() {
        Site site = new Site(Status.INDEXED, new Date(), "", "https://dimonvideo.ru", "dimonvideo");
        siteRepository.save(site);
        Lemma lemma = new Lemma("фильм", 1, site);
        lemmaRepository.save(lemma);
        Page page = new Page("/", 200, "<html> <head></head> <body></body> </html>", site);
        pageRepository.save(page);
        Index index = new Index(page, lemma, (float) 0.9);
        indexRepository.save(index);

        List<Index> indexList = indexRepository.findByLemmaAndLemmaSize("1", 1);
//        Iterable<Index> indexIterable = indexRepository.findAll();
//        List<Index> indexList = new ArrayList<>();
//        indexIterable.forEach(indexList::add);
        assertEquals(index, indexList.get(0));
    }
}
