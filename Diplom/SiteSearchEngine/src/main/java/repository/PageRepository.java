package repository;

import model.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for pages
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    public List<Page> findAll();

    public List<Page> findBySiteId(Integer siteId);

    public List<Page> findBySiteIdAndPath(Integer siteId, String path);
}
