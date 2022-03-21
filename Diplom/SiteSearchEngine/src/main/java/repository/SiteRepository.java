package repository;

import model.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for sites
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {
    public List<Site> findAll();

    public List<Site> findByUrl(String url);
}
