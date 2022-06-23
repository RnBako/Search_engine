import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LinkedPurchaseList")
@IdClass(LinkedPurchaseListKey.class)
public class LinkedPurchaseList  implements Serializable {
    @EmbeddedId
    private LinkedPurchaseListKey id;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;


    @ManyToOne (optional=false, cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
