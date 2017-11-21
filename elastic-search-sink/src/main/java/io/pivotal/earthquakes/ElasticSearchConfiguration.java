package io.pivotal.earthquakes;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("vcap.services.elastic-search.credentials")
public class ElasticSearchConfiguration {

    private String _url;

    public ElasticSearchConfiguration() {
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }
}
