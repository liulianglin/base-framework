package com.ldzhn.baseframework.utils;

import net.sf.cglib.beans.BeanCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llliu on 2018/3/14.
 */
public class BeanCopyUtils {

    private static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<String, BeanCopier>();

    private static Logger logger = LoggerFactory.getLogger(BeanCopyUtils.class);

    /**
     * 功能描述:当源和目标类的属性类型及字段相同时，才能拷贝
     *
     * @param srcObj
     * @param destObj void
     */
    public static void copy(Object srcObj, Object destObj) {
        if (null != srcObj && null != destObj) {
            String key = genKey(srcObj.getClass(), destObj.getClass());
            BeanCopier copier = null;
            if (BEAN_COPIERS.containsKey(key)) {
                copier = BEAN_COPIERS.get(key);
            } else {
                copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
                BEAN_COPIERS.put(key, copier);
            }
            copier.copy(srcObj, destObj, null);
        }
    }

    /**
     * 功能描述: 当源和目标类的属性类型及字段相同时，才能拷贝(集合)
     *
     * @param srcList
     * @param destList
     * @param destClazz void
     */
    public static void copyList(List srcList, List destList, Class<?> destClazz) {
        if (null != srcList && !srcList.isEmpty() && null != destList) {
            try {
                Object deskObj = null;
                for (Object obj : srcList) {
                    deskObj = destClazz.newInstance();
                    BeanCopyUtils.copy(obj, deskObj);
                    destList.add(deskObj);
                }
            } catch (Exception e) {
                logger.error("复制list出错：" + destClazz.getName(), e);
            }
        }

    }

    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }

}
