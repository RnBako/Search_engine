package model;

import javax.persistence.*;

/**
 * Field class
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Entity
@Table(name = "field")
public class Field {
    /** Field id*/
    @Id
    @GeneratedValue
    private int id;
    /** Field name*/
    @Column(name = "name")
    private String name;
    /** Field selector*/
    @Column(name = "selector")
    private String selector;
    /** Field weight*/
    @Column(name = "weight")
    private float weight;

    /**
     * Basic constructor
     * @see Field#Field(String, String, float)
     */
    public Field() { super(); }

    /**
     * Creating a new field object
     * @param name - Field name
     * @param selector - Field selector
     * @param weight - Field weight
     * @see Field#Field()
     */
    public Field(String name, String selector, float weight) {
        this.name = name;
        this.selector = selector;
        this.weight = weight;
    }

    /**
     * Field mame get method
     * @return return field name
     */
    public String getName() {
        return name;
    }

    /**
     * Field selector get method
     * @return return field selector
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Field weight get method
     * @return return field weight
     */
    public float getWeight() {
        return weight;
    }
}
