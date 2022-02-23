package model;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
public class Lemma {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "lemma")
    private String lemma;
    @Column(name = "frequency")
    private int frequency;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Site.class)
    @JoinColumn(name="site_id")
    private Site site;

    public Lemma() {}

    public Lemma(String lemma, int frequency, Site site) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.site = site;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
