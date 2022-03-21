package repository;

import java.util.List;

/**
 * Customized lemma interface
 * @author Roman Barsuchenko
 * @version 1.0
 */
public interface CustomizedLemma<Lemma>{
    /**
     * Query lemmas by frequency and list
     * @param frequency - Frequency of lemmas
     * @param lemmaList - Ids lemma list
     * @return returns lemmas list
     */
    List<Lemma> findByFrequencyAndLemmaIn(Integer frequency, String lemmaList);

    /**
     * Query lemmas by site, frequency and list
     * @param siteId - Site id
     * @param frequency - Frequency of lemmas
     * @param lemmaList - Ids lemma list
     * @return returns lemmas list
     */
    List<Lemma> findBySiteAndFrequencyAndLemmaIn(Integer siteId, Integer frequency, String lemmaList);
}
