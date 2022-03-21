package repository;

import model.Lemma;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Customized lemma class for complex queries
 * @author Roman Barsuchenko
 * @version 1.0
 */
public class CustomizedLemmaImpl implements CustomizedLemma{

    @PersistenceContext
    private EntityManager em;

    /**
     * Query lemmas by frequency and list
     * @param frequency - Frequency of lemmas
     * @param lemmaList - Ids lemma list
     * @return returns lemmas list
     */
    @Override
    public List<Lemma> findByFrequencyAndLemmaIn(Integer frequency, String lemmaList) {
        return em.createQuery("from model.Lemma l where l.frequency <= " + frequency + " and l.lemma in (" + lemmaList + ") order by l.frequency", Lemma.class).getResultList();
    }

    /**
     * Query lemmas by site, frequency and list
     * @param siteId - Site id
     * @param frequency - Frequency of lemmas
     * @param lemmaList - Ids lemma list
     * @return returns lemmas list
     */
    @Override
    public List<Lemma> findBySiteAndFrequencyAndLemmaIn(Integer siteId, Integer frequency, String lemmaList) {
        return em.createQuery("from model.Lemma l where l.site = " + siteId + " and l.frequency <= " + frequency + " and l.lemma in (" + lemmaList + ") order by l.frequency", Lemma.class).getResultList();
    }
}
