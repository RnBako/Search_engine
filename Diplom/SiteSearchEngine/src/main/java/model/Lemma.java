package model;

import javax.persistence.*;

/**
 * Lemma class
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Entity
@Table(name = "lemma")
public class Lemma {
    /** Lemma id*/
    @Id
    @GeneratedValue
    private int id;
    /** Lemma name*/
    @Column(name = "lemma")
    private String lemma;
    /** Lemma frequency*/
    @Column(name = "frequency")
    private int frequency;
    /** Reference to site id*/
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Site.class)
    @JoinColumn(name="site_id")
    private Site site;

    /**
     * Basic constructor
     * @see Lemma#Lemma(String, int, Site)
     */
    public Lemma() {}

    /**
     * Creating a new lemma object
     * @param lemma - Lemma name
     * @param frequency - Lemma frequency
     * @param site - Site object
     * @see Lemma#Lemma()
     */
    public Lemma(String lemma, int frequency, Site site) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.site = site;
    }

    /**
     * Lemma id get method
     * @return return lemma id
     */
    public int getId() {
        return id;
    }

    /**
     * Lemma frequency get method
     * @return return lemma frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Lemma frequency set method
     * @param frequency - frequency for lemma
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Lemma Site set method
     * @param site - site for lemma
     */
    public void setSite(Site site) {
        this.site = site;
    }
}
