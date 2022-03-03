package main.Unit;

import controller.DefaultController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DefaultController.class)
public class DefaultControllerTest {

    @Autowired
    private DefaultController defaultController;

    @Test
    public void testIndex() throws Exception{
        assertThat(defaultController).isNotNull();
    }
}
