package com.ldzhn.baseframework.business.httpclient;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/9/1 10:33
 */
//@Configuration
@PropertySource(value = "classpath:httpclient.properties")
public class HttpClientConfiguration {
    @Value("${spring.http.maxTotal}")
    private Integer maxTotal;

    @Value("${spring.http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${spring.http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${spring.http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${spring.http.socketTimeout}")
    private Integer socketTimeout;

    @Value("${spring.http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isStaleConnectionCheckEnabled() {
        return staleConnectionCheckEnabled;
    }

    public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
    }
}
