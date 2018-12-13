package com.ldzn.baseframework.persistence.cache.redis.publish;

/**
 * @Author : llliu
 * @Despriction : 消息生产者服务接口
 * @Date : create on 2018/11/21 22:45
 */
public interface MessagePublisher {
    boolean sendMessage(String message);
}
