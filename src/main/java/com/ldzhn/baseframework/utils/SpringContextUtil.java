package com.ldzhn.baseframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;


/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/9/2 15:55
 */
public class SpringContextUtil{
    private static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);
    /**
     * Spring应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文
     * @param applicationContext
     * @throws BeansException
     */
    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("b-SpringContextUtil开始注入....");
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static Object getBean(String name, Class requiredType)
            throws BeansException {

        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name)
            throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    public static Class getType(String name)
            throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    public static String[] getAliases(String name)
            throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

}
