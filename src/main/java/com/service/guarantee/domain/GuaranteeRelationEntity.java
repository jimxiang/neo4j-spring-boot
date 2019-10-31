package com.service.guarantee.domain;

import java.util.List;

public class GuaranteeRelationEntity {
    private String srcId;
    private String dstId;
    private List<AttrEntity> attrs;
    private int times;
    private double amount;
    private String relType;
    private String srcName;
    private String dstName;

    public GuaranteeRelationEntity() {
        super();
    }

    public GuaranteeRelationEntity(String srcId, String dstId, List<AttrEntity> attrs, int times, double amount, String relType, String srcName, String dstName) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.attrs = attrs;
        this.times = times;
        this.amount = amount;
        this.relType = relType;
        this.srcName = srcName;
        this.dstName = dstName;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getDstId() {
        return dstId;
    }

    public void setDstId(String dstId) {
        this.dstId = dstId;
    }

    public List<AttrEntity> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrEntity> attrs) {
        this.attrs = attrs;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDstName() {
        return dstName;
    }

    public void setDstName(String dstName) {
        this.dstName = dstName;
    }

    @Override
    public String toString() {
        return "GuaranteeRelationEntity{" +
                "src='" + srcId + '\'' +
                ", dst='" + dstId + '\'' +
                ", attrs=" + attrs +
                ", times=" + times +
                ", amount=" + amount +
                ", relType='" + relType + '\'' +
                ", srcName='" + srcName + '\'' +
                ", dstName='" + dstName + '\'' +
                '}';
    }
}
