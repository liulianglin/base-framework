package com.ldzhn.baseframework.utils.poi.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author : llliu
 * @Despriction : 字段
 * @Date : create on 2018/9/20 10:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellField {
    /**
     * 文件列名
     * @return
     */
    String name() default "";
}
