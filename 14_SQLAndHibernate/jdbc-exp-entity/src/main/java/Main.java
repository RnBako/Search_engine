import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

//        List<Course> courses = session.createQuery("from Course c").list();
//        courses.forEach(course -> System.out.println("На курсе \"" + course.getName() + "\" " + course.getStudentsCount() + " студентов. Учит " + course.getTeacher().getName()));

//        List<Student> students = session.createQuery("from Student s").list();
//        students.forEach(student -> System.out.println(student.getName()));

//        List<Teacher> teachers = session.createQuery("from Teacher t").list();
//        teachers.forEach(teacher -> System.out.println(teacher  .getName()));

//        List<PurchaseList> purchaseLists = session.createQuery("from PurchaseList pl").list();
//        purchaseLists.forEach(purchase -> System.out.println(purchase.getStudentName() + " - " + purchase.getCourseName()));

        List<Subscription> subscriptions = session.createQuery("from Subscription s").list();
        subscriptions.forEach(subscription -> System.out.println(subscription.getStudent().getName() + " - " + subscription.getCourse().getName()));

        sessionFactory.close();
    }
}
