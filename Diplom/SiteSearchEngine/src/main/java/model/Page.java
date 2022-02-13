package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "`page`")
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
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Site.class)
    @JoinColumn(name="site_id")
    private Site site;
    @Transient
    private final Collection<Page> children = new ArrayList<>();

    public Page() {super();}

    public Page(String path, int code, String content, Site site) {
        this.path = path;
        this.code = code;
        this.content = content;
        this.site = site;
    }

    public int getId() {
        return id;
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}