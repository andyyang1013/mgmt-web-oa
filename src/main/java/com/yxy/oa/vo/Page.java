package com.yxy.oa.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分页查询  入参统一接收类,vo可以继承该类
 **/
public class Page {

    /**
     * 当前页  从1开始
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer pageNum = 0;

    /**
     * 页面大小
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer pageSize = 10;

    /**
     * 查询起始行  从0开始【mysql】
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer startIndex = 0;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum < 0) {
            this.pageNum = 0;
        } else {
            this.pageNum = pageNum;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Integer getStartIndex() {
        if (this.pageNum == null || this.pageSize == null || this.pageNum < 1 || this.pageSize < 1) {
            this.startIndex = null;
        } else {
            this.startIndex = (pageNum - 1) * pageSize;
        }
        return this.startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        if (startIndex == null || startIndex < 0) {
            this.startIndex = 0;
        } else {
            this.startIndex = startIndex;
        }
    }
}
