package com.service.guarantee.domain;

import java.util.ArrayList;
import java.util.List;

public class PathSearchEntity {
    private List<GuaranteeNodeEntity> nodes = new ArrayList<>();
    private List<GuaranteeRelationEntity> edges = new ArrayList<>();

    public PathSearchEntity() {
        super();
    }

    public PathSearchEntity(List<GuaranteeNodeEntity> nodes, List<GuaranteeRelationEntity> edges) {
        this.nodes = nodes;
        this.edges = edges;
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
        return "PathSearchEntity{" +
                ", nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
