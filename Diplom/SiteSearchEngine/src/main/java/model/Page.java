package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Page class
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Entity
@Table(name = "`page`")
public class Page {
    /** Page id*/
    @Id
    @GeneratedValue
    private int id;
    /** Page path*/
    @Column(name = "path", columnDefinition = "text")
    private String path;
    /** Response code*/
    @Column(name = "code")
    private int code;
    /** Page content*/
    @Column(name = "content", columnDefinition = "mediumtext")
    private String content;
    /** Reference to site id*/
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=Site.class)
    @JoinColumn(name="site_id")
    private Site site;
    /** Child page list, not loading into database*/
    @Transient
    private final Collection<Page> children = new ArrayList<>();

    /**
     * Basic constructor
     * @see Page#Page(String, int, String, Site)
     */
    public Page() {super();}

    /**
     * Creating a new page object
     * @param path - Path to page from root site
     * @param code - Response code
     * @param content - Page content
     * @param site - Site to which page belongs
     * @see Page#Page()
     */
    public Page(String path, int code, String content, Site site) {
        this.path = path;
        this.code = code;
        this.content = content;
        this.site = site;
    }

    /**
     * Page id get method
     * @return return page id
     */
    public int getId() {
        return id;
    }

    /**
     * Adding a child page
     * @param child - child page
     */
    public void addChildren(Page child) {
        children.add(child);
    }

    /**
     * Page path get method
     * @return return page path
     */
    public String getPath() {
        return path;
    }

    /**
     * Page content get method
     * @return return page content
     */
    public String getContent() {
        return content;
    }

    /**
     * Page site get method
     * @return return page site
     */
    public Site getSite() {
        return site;
    }

    /**
     * Page site set method
     * @param site - Site to page
     */
    public void setSite(Site site) {
        this.site = site;
    }
}