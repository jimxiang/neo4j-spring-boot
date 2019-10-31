package com.service.guarantee.domain.request;

import org.hibernate.validator.constraints.NotBlank;

public class RequestGetRelationInCorp {
    @NotBlank(message = "不能为空")
    private String corpId;
    private int start = 0;
    private int limit = 10;

    public RequestGetRelationInCorp() {
        super();
    }

    public RequestGetRelationInCorp(@NotBlank(message = "不能为空") String corpId, int start, int limit) {
        this.corpId = corpId;
        if (start < 0) {
            this.start = 0;
        } else {
            this.start = start;
        }
        if (limit <= 0) {
            this.limit = 10;
        } else {
            this.limit = limit;
        }
    }

    public String getCorpId() {
        return corpId;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "GetRelationInCorp Request Body {" +
                "corpId='" + corpId + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }

}
