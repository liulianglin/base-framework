package com.ldzhn.baseframework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Author : llliu
 * @Despriction : 基础Bean，封装了基础的equals、hashcode、tostring，当普通实体类可根据实际需要继承该类
 * @Date : create on 2018/8/1 17:33
 */
public class BaseBean {

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * 可以详细显示对象的各个属性值
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
