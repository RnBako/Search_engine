package controller;

import model.SiteFromProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "indexing")
public class SiteConfig {
    private List<SiteFromProperties> sites;

    public List<SiteFromProperties> getSites() {
        return sites;
    }

    public void setSites(List<SiteFromProperties> sites) {
        this.sites = sites;
    }
}
