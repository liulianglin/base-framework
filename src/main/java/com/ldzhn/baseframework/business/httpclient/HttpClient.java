package com.ldzhn.baseframework.business.httpclient;

import lombok.Data;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/8/31 18:02
 */
//@Service
@Data
public class HttpClient {
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    @Autowired
    HttpClientConfiguration httpClientConfiguration;

    public HttpClient(){
        logger.info("++++++++ HttpClient被加载。。。。。");
    }

    /**
     * 首先实例化一个连接池管理器，设置最大连接数、并发连接数
     * @return
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        logger.info("开始加载连接池管理器PoolingHttpClientConnectionManager");
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(httpClientConfiguration.getMaxTotal());
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(httpClientConfiguration.getDefaultMaxPerRoute());
        logger.info("连接池管理器PoolingHttpClientConnectionManager加载完毕");
        return httpClientConnectionManager;
    }

    /**
     * 实例化连接池，设置连接池管理器。
     * 这里需要以参数形式注入上面实例化的连接池管理器
     * @param httpClientConnectionManager
     * @return
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){
        logger.info("开始实例化连接池，设置连接池管理器");
        //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        logger.info("实例化连接池，设置连接池管理器完毕");
        return httpClientBuilder;
    }

    /**
     * 注入连接池，用于获取httpClient
     * @param httpClientBuilder
     * @return
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        logger.info("开始注入连接池");
        return httpClientBuilder.build();
    }

    /**
     * Builder是RequestConfig的一个内部类
     * 通过RequestConfig的custom方法来获取到一个Builder对象
     * 设置builder的连接信息
     * 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
     * @return
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(httpClientConfiguration.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientConfiguration.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientConfiguration.getSocketTimeout())
                .setStaleConnectionCheckEnabled(httpClientConfiguration.isStaleConnectionCheckEnabled());
    }

    /**
     * 使用builder构建一个RequestConfig对象
     * @param builder
     * @return
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
        return builder.build();
    }

}
