package repository;

import model.Index;
import model.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for indexes
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Repository
public interface IndexRepository extends CrudRepository<Index, Integer>, CustomizedIndex<Index> {
    public List<Index> findAll();

    public List<Index> findByPageIn(Iterable<Page> pageIterable);

    /**
     * Query indexes by lemmas list
     * @param lemmaList - lemmas list
     * @param lemmasSize - count lemmas in list
     * @return returns indexes list corresponding to lemmas
     */
    @Query(value = "select i.* from `index` i where i.page_id in (select gi.page_id \n" +
            " from \n" +
            " (select i.page_id, \n" +
            " count(i.lemma_id) cnt \n" +
            " from `index` i \n" +
            " where i.lemma_id in (?1) \n" +
            " group by i.page_id) gi where gi.cnt=?2) \n" +
            " and i.lemma_id in (?1)", nativeQuery = true)
    List<Index> findByLemmaAndLemmaSize(String lemmaList, Integer lemmasSize);
}
