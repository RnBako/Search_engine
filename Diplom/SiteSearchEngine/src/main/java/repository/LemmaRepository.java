package repository;

import model.Lemma;
import model.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemmaRepository extends CrudRepository<Lemma, Integer>, CustomizedLemma<Lemma> {
    public List<Lemma> findAll();

    public List<Lemma> findBySiteId(Integer siteId);

}
