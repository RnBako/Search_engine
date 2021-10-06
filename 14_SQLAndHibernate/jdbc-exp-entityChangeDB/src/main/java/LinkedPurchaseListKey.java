import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LinkedPurchaseListKey implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false, insertable = false, updatable = false)
    private Student student;


    @ManyToOne ( fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false, insertable = false, updatable = false)
    private Course course;

    public LinkedPurchaseListKey() {
        super();
    }

    public Student getStudent() {
        return student;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedPurchaseListKey that = (LinkedPurchaseListKey) o;
        return student.equals(that.student) && course.equals(that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
