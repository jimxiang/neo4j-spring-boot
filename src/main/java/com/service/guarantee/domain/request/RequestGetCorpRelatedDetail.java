package com.service.guarantee.domain.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class RequestGetCorpRelatedDetail {
    @NotBlank(message = "不能为空")
    private String src;
    @NotBlank(message = "不能为空")
    private String dst;
    @NotBlank(message = "不能为空")
    @Pattern(regexp = "1|2|3", message = "参数不再可选范围内")
    private String mode;
    private int start = 0;
    private int limit = 10;

    public RequestGetCorpRelatedDetail() {
        super();
    }

    public RequestGetCorpRelatedDetail(@NotBlank(message = "不能为空") String src, @NotBlank(message = "不能为空") String dst, @NotBlank(message = "不能为空") @Pattern(regexp = "1|2|3", message = "参数不再可选范围内") String mode, int start, int limit) {
        this.src = src;
        this.dst = dst;
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

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public String getMode() {
        return mode;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "RequestGetCorpRelatedDetail {" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", mode='" + mode + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }
}
