
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "page")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "path")
    private String path;
    @Column(name = "code")
    private int code;
    @Column(name = "content")
    private String content;
    @Transient
    private final Collection<Page> children = new ArrayList<>();

    public Page() {super();}

    public Page(String path, int code, String content) {
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public void addChildren(Page child) {
        children.add(child);
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}