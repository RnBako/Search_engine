import javax.persistence.*;

@Entity
@Table(name = "`index`")
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Page page;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Lemma lemma;
    @Column(name = "`rank`")
    private float rank;

    public Index() { super(); }

    public Index(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public Page getPage() {
        return page;
    }
}
