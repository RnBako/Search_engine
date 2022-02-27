package main;

import junit.framework.TestCase;
import model.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.FieldRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FieldRepositoryTest extends TestCase {

    @Autowired
    private FieldRepository fieldRepository;

    @Test
    public void testFindAll() {
        Iterable<Field> fields = fieldRepository.findAll();

        int countOfFields = 0;
        assertThat(fields).hasSize(countOfFields);
    }
}
