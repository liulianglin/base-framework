package com.ldzhn.baseframework.bean;

import java.util.List;

/**
 * Created by MaJingcao on 2018/3/9.
 * Copyright by syswin
 */
public class Page<T> extends BaseBean {

    private long startRow = 0; //开始记录
    private Integer pageSize = 20; //页面大小
    private long totalRow; //总记录条数
    private long totalPage; //总页数
    private long curPage = 1; //当前页码
    private List<T> list;

    public long getStartRow() {
        return startRow;
    }

    public void setStartRow(long startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
