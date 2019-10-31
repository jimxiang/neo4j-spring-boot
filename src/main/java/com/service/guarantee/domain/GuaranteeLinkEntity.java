package com.service.guarantee.domain;

import java.util.ArrayList;
import java.util.List;

public class GuaranteeLinkEntity {
    private int total;
    private List<GuaranteeNodeEntity> nodes = new ArrayList<>();
    private List<GuaranteeRelationEntity> edges = new ArrayList<>();

    public GuaranteeLinkEntity() {
        super();
    }

    public GuaranteeLinkEntity(int total, List<GuaranteeNodeEntity> nodes, List<GuaranteeRelationEntity> edges) {
        this.total = total;
        this.nodes = nodes;
        this.edges = edges;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GuaranteeNodeEntity> getNodes() {
        return nodes;
    }

    public void setNodes(List<GuaranteeNodeEntity> nodes) {
        this.nodes = nodes;
    }

    public List<GuaranteeRelationEntity> getEdges() {
        return edges;
    }

    public void setEdges(List<GuaranteeRelationEntity> edges) {
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "GuaranteeLinkEntity{" +
                "total=" + total +
                ", nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
