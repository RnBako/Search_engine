package controller;

import model.SiteFromProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Class for getting sites list from configuration
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "indexing")
public class SiteConfig {
    /** Parameter for sites list*/
    private List<SiteFromProperties> sites;

    /**
     * Method for getting sites list from configuration
     * @return Return sites list
     */
    public List<SiteFromProperties> getSites() {
        return sites;
    }

    /**
     * Method for set sites list from configuration
     * @param sites - Sites list for change
     */
    public void setSites(List<SiteFromProperties> sites) {
        this.sites = sites;
    }
}
