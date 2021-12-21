import javax.persistence.*;

@Entity
@Table(name = "field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "selector")
    private String selector;
    @Column(name = "weight")
    private float weight;

    public String getName() {
        return name;
    }

    public String getSelector() {
        return selector;
    }

    public float getWeight() {
        return weight;
    }
}
