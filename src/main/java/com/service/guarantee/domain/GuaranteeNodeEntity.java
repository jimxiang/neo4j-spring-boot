package com.service.guarantee.domain;

public class GuaranteeNodeEntity {
    private String eid;
    private String nodeId;
    private String nodeName;
    private String nodeType;
    private String corpId;
    private String corpName;
    private int corpCnt;

    public GuaranteeNodeEntity() { super(); }

    public GuaranteeNodeEntity(String eid, String nodeId, String nodeName, String nodeType, String corpId, String corpName, int corpCnt) {
        this.eid = eid;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.corpId = corpId;
        this.corpName = corpName;
        this.corpCnt = corpCnt;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
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

    @Override
    public String toString() {
        return "GuaranteeNodeEntity{" +
                "eid='" + eid + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCnt=" + corpCnt +
                '}';
    }
}
