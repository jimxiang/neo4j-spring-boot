package com.service.guarantee.domain.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class RequestExpandNodePath {
    @NotBlank(message = "不能为空")
    private String nodeId;
    @NotBlank(message = "不能为空")
    @Pattern(regexp = "1|2", message = "参数不再可选范围内")
    private String mode;
    private int start = 0;
    private int limit = 10;

    public RequestExpandNodePath() {
        super();
    }

    public RequestExpandNodePath(@NotBlank(message = "不能为空") String nodeId, @NotBlank(message = "不能为空") @Pattern(regexp = "1|2", message = "参数不再可选范围内") String mode, int start, int limit) {
        this.nodeId = nodeId;
        this.mode = mode;
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
        return "ExpandNodePath Request Body {" +
                "nodeId='" + nodeId + '\'' +
                ", mode='" + mode + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }

}
