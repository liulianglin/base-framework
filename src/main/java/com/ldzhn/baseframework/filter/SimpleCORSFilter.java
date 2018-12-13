package com.ldzhn.baseframework.filter;/**
 * Created by admin on 2018/6/23.
 */

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description://mytodo
 * @author:xlwang
 * @create:2018/6/23
 */
public class SimpleCORSFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        filterChain.doFilter(servletRequest,servletResponse );
    }

    @Override
    public void destroy() {

    }
}
