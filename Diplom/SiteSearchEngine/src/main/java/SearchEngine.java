import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
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
            System.out.println(SearchSystem.searchPage("Служба доставки работает каждый день"));
//            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
//
//            Session session = sessionFactory.openSession();
//            Node root = new ForkJoinPool().invoke(new SiteIndexator("https://www.playback.ru", session));
//            sessionFactory.close();
            System.out.println("Завершили индексацию!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
