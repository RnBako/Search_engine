import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Course> courses = session.createQuery("from Course c").list();
        List<Student> students = session.createQuery("from Student s").list();
        List<PurchaseList> purchaseList = session.createQuery("from PurchaseList pl").list();

        purchaseList.forEach(purchase -> {
            LinkedPurchaseList linkedPurchase = new LinkedPurchaseList();
            linkedPurchase.setStudent(students.stream().filter(student -> student.getName().equals(purchase.getStudentName())).collect(Collectors.toList()).get(0));
            linkedPurchase.setCourse(courses.stream().filter(course -> course.getName().equals(purchase.getCourseName())).collect(Collectors.toList()).get(0));
            System.out.println(linkedPurchase.getStudent().getId() + " - " + linkedPurchase.getCourse().getId());
            session.save(linkedPurchase);
        });

        session.getTransaction().commit();
        sessionFactory.close();
    }
}
