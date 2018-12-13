package com.ldzhn.baseframework.utils;
import java.io.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : llliu
 * @Despriction : 配置文件读取工具类
 *  用途： 如果不想通过spring boot的方式处理配置文件，可以使用该工具类进行处理
 * @Date : create on 2018/11/26 23:18
 */
public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;

    synchronized static private void loadProps(String properties){
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            //通过类加载器进行获取properties文件流
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(properties);
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    /**
     * 根据key获取配置
     * @param key               key
     * @param propertiesFile    配置文件
     * @return
     */
    public static String getProperty(String key,String propertiesFile){
        if(null == props) {
            loadProps(propertiesFile);
        }
        return props.getProperty(key);
    }

    /**
     * 根据key获取配置，如果没有使用默认值
     * @param key               key
     * @param defaultValue      默认值
     * @param propertiesFile    配置文件
     * @return
     */
    public static String getProperty(String key, String defaultValue,String propertiesFile) {
        if(null == props) {
            loadProps(propertiesFile);
        }
        return props.getProperty(key, defaultValue);
    }

}
