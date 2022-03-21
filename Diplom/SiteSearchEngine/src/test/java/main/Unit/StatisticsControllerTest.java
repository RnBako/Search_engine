package main.Unit;

import controller.StatisticsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("file:C:/Users/AVK/Documents/Java/java_basics/application.yaml")
public class StatisticsControllerTest {

    @Autowired
    private StatisticsController statisticsController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testStatistics() {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/statistics", HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }
}
