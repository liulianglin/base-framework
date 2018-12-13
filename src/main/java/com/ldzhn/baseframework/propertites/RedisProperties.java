package com.ldzhn.baseframework.propertites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * @Author : llliu
 * @Despriction : redis配置文件
 * @Date : create on 2018/9/3 16:25
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisProperties {
    /**
     * 集群节点信息
     */
    private String nodes;

    /**
     * 执行命令超时时间15S
     */
    private Integer commandTimeout;

    /**
     * 重试次数
     */
    private Integer maxAttempts;

    /**
     * 跨集群执行命令时要遵循的最大重定向数量
     */
    private Integer maxRedirects;

    /**
     * 连接池最大连接数（使用负值表示没有限制）
     */
    private Integer maxActive;

    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     */
    private Integer maxWait;

    /**
     *  连接池中的最大空闲连接
     */
    private Integer maxIdle;

    /**
     * 连接池中的最小空闲连接
     */
    private Integer minIdle;

    /**
     * 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
     */
    private boolean testOnBorrow;
}
