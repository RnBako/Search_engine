package controller;

import model.SiteFromProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * for getting sites list from configuration
 * @author Roman Barsuchenko
 * @version 1.0
 */
@Service
public class SiteService {
    /** Parameter for sites list*/
    private final List<SiteFromProperties> sites;

    /**
     * Method for getting sites list from configuration
     * @param siteConfig - Configuration from which we get the list of sites
     */
    @Autowired
    public SiteService(SiteConfig siteConfig){
        this.sites = siteConfig.getSites();
    }

    /**
     * Method that returns sites list from configuration
     * @return Return sites list
     */
    public List<SiteFromProperties> getSites() {
        return  this.sites;
    }
}
