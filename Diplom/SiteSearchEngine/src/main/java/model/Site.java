package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`site`")
public class Site {
    @Id
    @GeneratedValue
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "`status`",columnDefinition = "enum('INDEXING','INDEXED','FAILED')")
    private Status status;
    @Column(name = "status_time")
    private Date statusTime;
    @Column(name = "last_error")
    private String lastError;
    @Column(name = "url")
    private String url;
    @Column(name = "`name`")
    private String name;

    public Site () {super();}

    public Site(Status status, Date statusTime, String lastError, String url, String name) {
        this.status = status;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public String getLastError() {
        return lastError;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
}
