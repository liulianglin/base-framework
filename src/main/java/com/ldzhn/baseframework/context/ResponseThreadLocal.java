package com.ldzhn.baseframework.context;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : llliu
 * @Despriction :
 * @Date : create on 2018/8/1 17:33
 */
public class ResponseThreadLocal {

    public static ThreadLocal<HttpServletResponse> threadLocal = new ThreadLocal<HttpServletResponse>();

    /**
     * 删除响应
     */
    public static void removeResponse() {
        threadLocal.remove();
    }

    /**
     * 获取响应
     */
    public static HttpServletResponse getResponse() {
        return threadLocal.get();
    }

    /**
     * 设置响应
     *
     * @param res 请求
     */
    public static void setResponse(HttpServletResponse res) {
        threadLocal.set(res);
    }

}
