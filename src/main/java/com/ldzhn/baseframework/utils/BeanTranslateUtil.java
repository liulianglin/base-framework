package com.ldzhn.baseframework.utils;

import java.util.*;

/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/8/19 18:34
 */
public class BeanTranslateUtil {
    /**
     * List<实体>转为list<Object>
     *
     * @param t
     * @return
     */
    public List<Object> beanToObject(List<?> t) {
        List<Object> o = new ArrayList<Object>();
        Iterator<?> it = t.iterator();
        while (it.hasNext()) {
            o.add(it.next());
        }
        return o;
    }

}
