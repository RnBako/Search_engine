package model;

/**
 * SiteFromProperties class
 * @author Roman Barsuchenko
 * @version 1.0
 */
public class SiteFromProperties {
    /** Site URL*/
    private String url;
    /** Site name*/
    private String name;

    /**
     * Site URL get method
     * @return return Site URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Site URL set method
     * @param url - URL for site from properties
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Site name get method
     * @return return Site name
     */
    public String getName() {
        return name;
    }

    /**
     * Site name set method
     * @param name - Name for site from properties
     */
    public void setName(String name) {
        this.name = name;
    }
}
