package com.ldzhn.baseframework.context;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/8/1 17:33
 */
public class RequestThreadLocal {

    public static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

    /**
     * 删除请求
     */
    public static void removeRequest() {
        threadLocal.remove();
    }

    /**
     * 获取请求
     */
    public static HttpServletRequest getRequest() {
        return threadLocal.get();
    }

    /**
     * 设置请求
     *
     * @param req 请求
     */
    public static void setRequest(HttpServletRequest req) {
        threadLocal.set(req);
    }

}
