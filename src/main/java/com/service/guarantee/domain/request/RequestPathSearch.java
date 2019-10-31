package com.service.guarantee.domain.request;

import org.hibernate.validator.constraints.NotBlank;

public class RequestPathSearch {
    @NotBlank(message = "不能为空")
    private String src;
    @NotBlank(message = "不能为空")
    private String dst;

    public RequestPathSearch() {
        super();
    }

    public RequestPathSearch(@NotBlank(message = "不能为空") String src, @NotBlank(message = "不能为空") String dst) {
        this.src = src;
        this.dst = dst;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "PathSearch Request Body {" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                '}';
    }
}
