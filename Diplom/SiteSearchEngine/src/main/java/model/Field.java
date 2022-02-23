package model;

import javax.persistence.*;

@Entity
@Table(name = "field")
public class Field {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "selector")
    private String selector;
    @Column(name = "weight")
    private float weight;

    public Field() { super(); }

    public Field(String name, String selector, float weight) {
        this.name = name;
        this.selector = selector;
        this.weight = weight;
    }

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
