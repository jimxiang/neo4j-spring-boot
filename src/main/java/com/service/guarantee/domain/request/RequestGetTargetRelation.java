package com.service.guarantee.domain.request;

import org.hibernate.validator.constraints.NotBlank;

public class RequestGetTargetRelation {
    @NotBlank(message = "不能为空")
    private String eid;
    private int start = 0;
    private int limit = 10;

    public RequestGetTargetRelation() {
        super();
    }

    public RequestGetTargetRelation(String eid, int start, int limit) {
        this.eid = eid;
        this.start = start;
        this.limit = limit;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "GetTargetRelation Request Body {" +
                "eid='" + eid + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }
}

