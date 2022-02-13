package repository;

import model.Lemma;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedLemmaImpl implements CustomizedLemma{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Lemma> findByFrequencyAndLemmaIn(Integer frequency, String lemmaList) {
        return em.createQuery("from model.Lemma l where l.frequency <= " + frequency + " and l.lemma in (" + lemmaList + ") order by l.frequency", Lemma.class).getResultList();
    }

    @Override
    public List<Lemma> findBySiteAndFrequencyAndLemmaIn(Integer siteId, Integer frequency, String lemmaList) {
        return em.createQuery("from model.Lemma l where l.site = " + siteId + " and l.frequency <= " + frequency + " and l.lemma in (" + lemmaList + ") order by l.frequency", Lemma.class).getResultList();
    }
}
