package com.service.guarantee.domain;

public class HasNextEntity {
    private String nodeId;
    private boolean hasNext;

    public HasNextEntity() { super(); }

    public HasNextEntity(String nodeId, boolean hasNext) {
        this.nodeId = nodeId;
        this.hasNext = hasNext;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "HasNextEntity{" +
                "nodeId='" + nodeId + '\'' +
                ", hasNext=" + hasNext +
                '}';
    }
}
