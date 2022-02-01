package controller;

import model.SiteFromProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {
    private final List<SiteFromProperties> sites;

    @Autowired
    public SiteService(SiteConfig siteConfig){
        this.sites = siteConfig.getSites();
    }

    public List<SiteFromProperties> getSites() {
        return  this.sites;
    }
}
