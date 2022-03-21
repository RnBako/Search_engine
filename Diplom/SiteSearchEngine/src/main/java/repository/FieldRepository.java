package repository;

import model.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for fields
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Repository
public interface FieldRepository extends CrudRepository<Field, Integer> {
    public List<Field> findAll();
}
