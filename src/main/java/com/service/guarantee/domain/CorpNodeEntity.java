package com.service.guarantee.domain;

import java.util.ArrayList;
import java.util.List;

public class CorpNodeEntity {
    private String corpId;
    private String corpName;
    private int corpCnt;
    private List<String> corpList = new ArrayList<>();

    public CorpNodeEntity() { super(); }

    public CorpNodeEntity(String corpId, String corpName, int corpCnt, List<String> corpList) {
        this.corpId = corpId;
        this.corpName = corpName;
        this.corpCnt = corpCnt;
        this.corpList = corpList;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public int getCorpCnt() {
        return corpCnt;
    }

    public void setCorpCnt(int corpCnt) {
        this.corpCnt = corpCnt;
    }

    public List<String> getCorpList() {
        return corpList;
    }

    public void setCorpList(List<String> corpList) {
        this.corpList = corpList;
    }

    @Override
    public String toString() {
        return "CorpNodeEntity{" +
                "corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCnt=" + corpCnt +
                ", corpList=" + corpList +
                '}';
    }
}
