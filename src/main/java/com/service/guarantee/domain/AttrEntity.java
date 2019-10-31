package com.service.guarantee.domain;

public class AttrEntity {
    private float cnvCnyGuaranteeAmount;
    private int guaranteeType;
    private int dataSrc;
    private String sgnDt;

    public AttrEntity() {
        super();
    }

    public AttrEntity(float cnvCnyGuaranteeAmount, int guaranteeType, int dataSrc, String sgnDt) {
        this.cnvCnyGuaranteeAmount = cnvCnyGuaranteeAmount;
        this.guaranteeType = guaranteeType;
        this.dataSrc = dataSrc;
        this.sgnDt = sgnDt;
    }

    public float getCnvCnyGuaranteeAmount() {
        return cnvCnyGuaranteeAmount;
    }

    public void setCnvCnyGuaranteeAmount(float cnvCnyGuaranteeAmount) {
        this.cnvCnyGuaranteeAmount = cnvCnyGuaranteeAmount;
    }

    public int getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(int guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public int getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(int dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getSgnDt() {
        return sgnDt;
    }

    public void setSgnDt(String sgnDt) {
        this.sgnDt = sgnDt;
    }

    @Override
    public String toString() {
        return "AttrEntity{" +
                "cnvCnyGuaranteeAmount=" + cnvCnyGuaranteeAmount +
                ", guaranteeType=" + guaranteeType +
                ", dataSrc=" + dataSrc +
                ", sgnDt='" + sgnDt + '\'' +
                '}';
    }
}
