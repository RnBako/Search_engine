import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.concurrent.ForkJoinPool;

public class SearchEngine {

    public static void main(String[] args) {
        try {
            System.out.println("Начали индексацию!");
            long start = System.currentTimeMillis();
            long usageMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println(SearchSystem.searchPage("Служба доставки работает каждый день"));
//            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
//
//            Session session = sessionFactory.openSession();
//            Page root = new ForkJoinPool().invoke(new SiteIndexator("https://www.playback.ru", session));
//            sessionFactory.close();
            long workingTime = (System.currentTimeMillis() - start) / 1000;
            usageMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - usageMemory) / 1048576;
            System.out.println("Завершили индексацию! Время выполнения " + workingTime + " с., использовано RAM " + usageMemory + " Мбайт.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
