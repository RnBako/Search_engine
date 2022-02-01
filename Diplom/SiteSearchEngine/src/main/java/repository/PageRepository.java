package repository;

import model.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    public List<Page> findAll();

    public List<Page> findBySiteId(Integer siteId);

    public List<Page> findBySiteIdAndPath(Integer siteId, String path);
}
