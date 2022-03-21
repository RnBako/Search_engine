package model;

import javax.persistence.*;

/**
 * Index class
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Entity
@Table(name = "`index`")
public class Index {
    /** Index id*/
    @Id
    @GeneratedValue
    private int id;
    /** Reference to page id*/
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Page.class)
    @JoinColumn(name="page_id")
    private Page page;
    /** Reference to lemma id*/
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Lemma.class)
    @JoinColumn(name="lemma_id")
    private Lemma lemma;
    /** Link rank of page and lemma*/
    @Column(name = "`rank`")
    private float rank;

    /**
     * Basic constructor
     * @see Index#Index(Page, Lemma, float)
     */
    public Index() { super(); }

    /**
     * Creating a new index object
     * @param page - Page object
     * @param lemma - Lemma object
     * @param rank - Link rank of page and lemma
     * @see Index#Index()
     */
    public Index(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    /**
     * Index id get method
     * @return return index id
     */
    public int getId() {
        return id;
    }

    /**
     * Index rank get method
     * @return return index rank
     */
    public float getRank() {
        return rank;
    }

    /**
     * Index rank set method
     * @param rank - rank for index
     */
    public void setRank(float rank) {
        this.rank = rank;
    }

    /**
     * Page get method
     * @return return page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Lemma get method
     * @return return lemma
     */
    public Lemma getLemma() {
        return lemma;
    }
}
