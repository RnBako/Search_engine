package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("controller")
@EntityScan("model")
@EnableJpaRepositories("repository")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        try {
//            System.out.println("Начали индексацию!");
//            long start = System.currentTimeMillis();
//            long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//            System.out.println(SearchSystem.searchPage("Служба доставки работает каждый день"));
//            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
//
//            Session session = sessionFactory.openSession();
//            model.Page root = new ForkJoinPool().invoke(new main.SiteIndexator("https://www.playback.ru", session));
//            sessionFactory.close();
//            long workingTime = (System.currentTimeMillis() - start) / 1000;
//            usageMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - usageMemory) / 1048576;
//            System.out.println("Завершили индексацию! Время выполнения " + workingTime + " с., использовано RAM " + usageMemory + " Мбайт.");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
