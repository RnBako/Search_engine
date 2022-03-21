package repository;


import java.util.List;

/**
 * Customized index interface
 * @author Roman Barsuchenko
 * @version 1.0
 */
public interface CustomizedIndex<Index>{
    /**
     * Query indexes by lemmas list
     * @param lemmaList - lemmas list
     * @param lemmasSize - count lemmas in list
     * @return returns indexes list corresponding to lemmas
     */
    List<Index> findByLemmaAndLemmaSize(String lemmaList, Integer lemmasSize);
}
