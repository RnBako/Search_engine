package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Site class
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Entity
@Table(name = "`site`")
public class Site {
    /** Site id*/
    @Id
    @GeneratedValue
    private int id;
    /** Site status*/
    @Enumerated(EnumType.STRING)
    @Column(name = "`status`",columnDefinition = "enum('INDEXING','INDEXED','FAILED')")
    private Status status;
    /** Site status time*/
    @Column(name = "status_time")
    private Date statusTime;
    /** Site last error*/
    @Column(name = "last_error")
    private String lastError;
    /** Site url*/
    @Column(name = "url")
    private String url;
    /** Site name*/
    @Column(name = "`name`")
    private String name;

    /**
     * Basic constructor
     * @see Site#Site(Status, Date, String, String, String)
     */
    public Site () {super();}

    /**
     * Creating a new site object
     * @param status - Site stetus
     * @param statusTime - Site status time
     * @param lastError - Site last error
     * @param url - Site url
     * @param name - Site name
     * @see Site#Site()
     */
    public Site(Status status, Date statusTime, String lastError, String url, String name) {
        this.status = status;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }

    /**
     * Site id get method
     * @return return site id
     */
    public int getId() {
        return id;
    }

    /**
     * Site status get method
     * @return return site status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Site status time get method
     * @return return site status time
     */
    public Date getStatusTime() {
        return statusTime;
    }

    /**
     * Site last error get method
     * @return return site last error
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Site url get method
     * @return return site url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Site name get method
     * @return return site name
     */
    public String getName() {
        return name;
    }

    /**
     * Site status set method
     * @param status - status for site
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Site status time set method
     * @param statusTime - Status time for site
     */
    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    /**
     * Site last error set method
     * @param lastError - Last error for site
     */
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
}
