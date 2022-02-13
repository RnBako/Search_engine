package repository;

import java.util.List;

public interface CustomizedLemma<Lemma>{
    List<Lemma> findByFrequencyAndLemmaIn(Integer frequency, String lemmaList);

    List<Lemma> findBySiteAndFrequencyAndLemmaIn(Integer siteId, Integer frequency, String lemmaList);
}
