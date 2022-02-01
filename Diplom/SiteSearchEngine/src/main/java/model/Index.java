package model;

import javax.persistence.*;

@Entity
@Table(name = "`index`")
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Page.class)
    @JoinColumn(name="page_id")
    private Page page;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Lemma.class)
    @JoinColumn(name="lemma_id")
    private Lemma lemma;
    @Column(name = "`rank`")
    private float rank;

    public Index() { super(); }

    public Index(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    public int getId() {
        return id;
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

    public Lemma getLemma() {
        return lemma;
    }
}
