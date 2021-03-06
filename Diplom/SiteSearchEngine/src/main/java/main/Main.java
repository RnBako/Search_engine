package main;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

/**
 * Main class of BakoSearch project
 * @author Roman Barsuchenko
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan("controller")
@EntityScan("model")
@EnableJpaRepositories("repository")
public class Main {
    private static Logger loggerInfo;

    public static void main(String[] args) {
        loggerInfo = LogManager.getLogger("SearchEngineInfo");
        SpringApplication.run(Main.class, args);
        loggerInfo.info("Search engine is started");
    }
}
