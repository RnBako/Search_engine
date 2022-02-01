package repository;

import model.Index;
import model.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends CrudRepository<Index, Integer> {
    public List<Index> findAll();

    public List<Index> findByPageIn(Iterable<Page> pageIterable);
}
