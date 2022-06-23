import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

//        List<Course> courses = session.createQuery("from Course c").list();
//        List<Student> students = session.createQuery("from Student s").list();
//        List<PurchaseList> purchaseList = session.createQuery("from PurchaseList pl").list();


        List<LinkedPurchaseList> linkedPurchaseList = session.createSQLQuery("select s.id as student_id, c.id as course_id " +
                "from purchaseList pl left join students s on s.name=pl.student_name left join courses c on c.name=pl.course_name").addEntity(LinkedPurchaseList.class).list();


        linkedPurchaseList.forEach(lp -> {
            session.beginTransaction();
            session.clear();
            System.out.println(lp.getStudent().getName() + " - " + lp.getCourse().getName());
            LinkedPurchaseList linkedPurchase = new LinkedPurchaseList();
            linkedPurchase.setStudent(lp.getStudent());
            linkedPurchase.setCourse(lp.getCourse());
            session.save(linkedPurchase);
            session.getTransaction().commit();
        });
//
//        purchaseList.forEach(purchase -> {
//            LinkedPurchaseList linkedPurchase = new LinkedPurchaseList();
//            linkedPurchase.setStudent(students.stream().filter(student -> student.getName().equals(purchase.getStudentName())).collect(Collectors.toList()).get(0));
//            linkedPurchase.setCourse(courses.stream().filter(course -> course.getName().equals(purchase.getCourseName())).collect(Collectors.toList()).get(0));
//            System.out.println(linkedPurchase.getStudent().getName() + " - " + linkedPurchase.getCourse().getName());
//            session.save(linkedPurchase);
//        });

        sessionFactory.close();
    }
}
