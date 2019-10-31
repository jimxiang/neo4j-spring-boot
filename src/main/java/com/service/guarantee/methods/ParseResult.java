package com.service.guarantee.methods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.service.guarantee.domain.*;
import com.service.guarantee.domain.*;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.*;

class ParseResult {
    private static final Logger logger = Logger.getLogger(ParseResult.class);

    static GuaranteeLinkEntity getGuaranteeLinkEntity(StatementResult statementResult, int total) {
        GuaranteeLinkEntity guaranteeLinkEntity = new GuaranteeLinkEntity();
        List<GuaranteeNodeEntity> nodeEntityList = new ArrayList<>();
        List<GuaranteeRelationEntity> edgeEntityList = new ArrayList<>();
        List<String> sourceIdList = new ArrayList<>();
        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            if (!record.get("source").isNull()) {
                Node source = record.get("source").asNode();
                String sourceId = source.get("nodeId").asString();
                if (!sourceIdList.contains(sourceId)) {
                    sourceIdList.add(sourceId);
                    nodeEntityList.add(getGuaranteeNodeEntity(source));
                }
            }
            List<Node> nodeList = record.get("nodeList").asList(Value::asNode);
            List<Relationship> edgeList = record.get("edgeList").asList(Value::asRelationship);
            for (Node node : nodeList) {
                nodeEntityList.add(getGuaranteeNodeEntity(node));
            }
            for (Relationship edge : edgeList) {
                edgeEntityList.add(getGuaranteeRelationEntity(edge));
            }
        }
        guaranteeLinkEntity.setNodes(nodeEntityList);
        guaranteeLinkEntity.setEdges(edgeEntityList);
        guaranteeLinkEntity.setTotal(total);
        return guaranteeLinkEntity;
    }

    static PathSearchEntity getGuaranteeLinkEntity(StatementResult statementResult) {
        PathSearchEntity pathSearchEntity = new PathSearchEntity();
        List<GuaranteeNodeEntity> nodeEntityList = new ArrayList<>();
        List<GuaranteeRelationEntity> edgeEntityList = new ArrayList<>();

        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            Value value = record.get("path");
            Path path = value.asPath();
            Iterable<Node> pathNodes = path.nodes();
            for (Node node : pathNodes) {
                GuaranteeNodeEntity nodeEntity = nodeToGuaranteeNodeEntity(node);
                nodeEntityList.add(nodeEntity);
            }
            Iterable<Relationship> pathRelationships = path.relationships();
            for (Relationship relationship : pathRelationships) {
                GuaranteeRelationEntity relationEntity = relationshipToGuaranteeRelationEntity(relationship);
                edgeEntityList.add(relationEntity);
            }
        }
        pathSearchEntity.setNodes(RemoveDuplicateNode(nodeEntityList));
        pathSearchEntity.setEdges(edgeEntityList);
        return pathSearchEntity;
    }

    static List<HasNextEntity> getHasNextEntity(StatementResult statementResult) {
        List<HasNextEntity> hasNextEntityList = new ArrayList<>();

        while (statementResult.hasNext()) {
            HasNextEntity hasNextEntity = new HasNextEntity();
            Record record = statementResult.next();
            int total = Integer.parseInt(record.get("total").toString());
            boolean hasNext = total > 1;
            String nodeId = record.get("nodeId").asString();
            hasNextEntity.setNodeId(nodeId);
            hasNextEntity.setHasNext(hasNext);
            hasNextEntityList.add(hasNextEntity);
        }
        return hasNextEntityList;
    }

    static int getTotal(StatementResult statementResult) {
        int total = 0;
        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            total = Integer.parseInt(record.get("total").toString());
        }
        return total;
    }

    private static GuaranteeNodeEntity getGuaranteeNodeEntity(Node node) {
        Value eid = node.get("eid");
        Value nodeId = node.get("nodeId");
        Value nodeName = node.get("nodeName");
        Value nodeType = node.get("nodeType");
        Value corpId = node.get("corpId");
        Value corpName = node.get("corpName");
        Value corpCnt = node.get("corpCnt");
        GuaranteeNodeEntity nodeEntity = new GuaranteeNodeEntity();
        if (eid.isNull()) {
            nodeEntity.setEid(null);
        } else {
            nodeEntity.setEid(eid.asString());
        }
        if (nodeId.isNull()) {
            nodeEntity.setNodeId(null);
        } else {
            nodeEntity.setNodeId(nodeId.asString());
        }
        if (nodeName.isNull()) {
            nodeEntity.setNodeName(null);
        } else {
            nodeEntity.setNodeName(nodeName.asString());
        }
        if (nodeType.isNull()) {
            nodeEntity.setNodeType(null);
        } else {
            nodeEntity.setNodeType(nodeType.asString());
        }
        if (corpId.isNull()) {
            nodeEntity.setCorpId(null);
        } else {
            nodeEntity.setCorpId(corpId.asString());
        }
        if (corpName.isNull()) {
            nodeEntity.setCorpName(null);
        } else {
            nodeEntity.setCorpName(corpName.asString());
        }
        if (corpCnt.isNull()) {
            nodeEntity.setCorpCnt(0);
        } else {
            nodeEntity.setCorpCnt(corpCnt.asInt());
        }
        return nodeEntity;
    }

    private static GuaranteeRelationEntity getGuaranteeRelationEntity(Relationship edge) {
        Value srcId = edge.get("src");
        Value dstId = edge.get("dst");
        Value attrs = edge.get("attrs");
        Value times = edge.get("times");
        Value amount = edge.get("amount");
        Value relType = edge.get("relType");
        Value srcName = edge.get("srcName");
        Value dstName = edge.get("dstName");
        GuaranteeRelationEntity edgeEntity = new GuaranteeRelationEntity();
        if (srcId.isNull()) {
            edgeEntity.setSrcId(null);
        } else {
            edgeEntity.setSrcId(srcId.asString());
        }
        if (dstId.isNull()) {
            edgeEntity.setDstId(null);
        } else {
            edgeEntity.setDstId(dstId.asString());
        }
        if (attrs.isNull()) {
            edgeEntity.setAttrs(null);
        } else {
            List<String> attrList = attrs.asList(Value::asString);
            if (!CollectionUtils.isEmpty(attrList)) {
                List<AttrEntity> attrEntityList = parseAttrs(attrList);
                edgeEntity.setAttrs(attrEntityList);
            }
        }
        if (times.isNull()) {
            edgeEntity.setTimes(0);
        } else {
            edgeEntity.setTimes(times.asInt());
        }
        if (amount.isNull()) {
            edgeEntity.setAmount(0);
        } else {
            edgeEntity.setAmount(amount.asDouble());
        }
        if (relType.isNull()) {
            edgeEntity.setRelType(null);
        } else {
            edgeEntity.setRelType(relType.asString());
        }
        if (srcName.isNull()) {
            edgeEntity.setSrcName(null);
        } else {
            edgeEntity.setSrcName(srcName.asString());
        }
        if (dstName.isNull()) {
            edgeEntity.setSrcName(null);
        } else {
            edgeEntity.setDstName(dstName.asString());
        }
        return edgeEntity;
    }

    private static List<AttrEntity> parseAttrs(List<String> attrList) {
        List<AttrEntity> attrEntityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attrList)) {
            for (String attr : attrList) {
                AttrEntity attrEntity = new AttrEntity();
                JSONObject dataJsonObject = JSON.parseObject(attr);

                float cnvCnyGuaranteeAmount = Float.parseFloat(dataJsonObject.get("cnv_cny_guaranteeamount").toString());
                int guaranteeType = Integer.parseInt(dataJsonObject.get("guaranteetype").toString());
                int dataSrc = Integer.parseInt(dataJsonObject.get("data_src").toString());
                String sgnDt = dataJsonObject.get("sgn_dt").toString();

                attrEntity.setCnvCnyGuaranteeAmount(cnvCnyGuaranteeAmount);
                attrEntity.setGuaranteeType(guaranteeType);
                attrEntity.setDataSrc(dataSrc);
                attrEntity.setSgnDt(sgnDt);

                attrEntityList.add(attrEntity);
            }
        }
        return attrEntityList;
    }

    private static GuaranteeNodeEntity nodeToGuaranteeNodeEntity(Node node) {
        GuaranteeNodeEntity nodeEntity = new GuaranteeNodeEntity();
        Map<String, Object> nodeProperty = node.asMap();

        if (nodeProperty.get("eid") != null) {
            nodeEntity.setEid(nodeProperty.get("eid").toString());
        }
        if (nodeProperty.get("nodeId") != null) {
            nodeEntity.setNodeId(nodeProperty.get("nodeId").toString());
        }
        if (nodeProperty.get("nodeName") != null) {
            nodeEntity.setNodeName(nodeProperty.get("nodeName").toString());
        }
        if (nodeProperty.get("nodeType") != null) {
            nodeEntity.setNodeType(nodeProperty.get("nodeType").toString());
        }
        if (nodeProperty.get("corpId") != null) {
            nodeEntity.setCorpId(nodeProperty.get("corpId").toString());
        }
        if (nodeProperty.get("corpName") != null) {
            nodeEntity.setCorpName(nodeProperty.get("corpName").toString());
        }
        if (nodeProperty.get("corpCnt") != null) {
            nodeEntity.setCorpCnt(Integer.parseInt(nodeProperty.get("corpCnt").toString()));
        }

        return nodeEntity;

    }

    private static GuaranteeRelationEntity relationshipToGuaranteeRelationEntity(Relationship relationship) {
        GuaranteeRelationEntity relationEntity = new GuaranteeRelationEntity();
        Map<String, Object> relationProperty = relationship.asMap();
        if (relationProperty.get("src") != null) {
            relationEntity.setSrcId(relationProperty.get("src").toString());
        }
        if (relationProperty.get("dst") != null) {
            relationEntity.setDstId(relationProperty.get("dst").toString());
        }
        List<String> attrs = (List<String>) relationProperty.get("attrs");
        if (!CollectionUtils.isEmpty(attrs)) {
            relationEntity.setAttrs(parseAttrs(attrs));
        }
        if (relationProperty.get("times") != null) {
            relationEntity.setTimes(Integer.parseInt(relationProperty.get("times").toString()));
        }
        if (relationProperty.get("amount") != null) {
            relationEntity.setAmount(Double.valueOf(relationProperty.get("amount").toString()));
        }
        if (relationProperty.get("relType") != null) {
            relationEntity.setRelType(relationProperty.get("relType").toString());
        }
        if (relationProperty.get("srcName") != null) {
            relationEntity.setSrcName(relationProperty.get("srcName").toString());
        }
        if (relationProperty.get("dstName") != null) {
            relationEntity.setDstName(relationProperty.get("dstName").toString());
        }
        return relationEntity;

    }

    private static List<GuaranteeNodeEntity> RemoveDuplicateNode(List<GuaranteeNodeEntity> nodeEntityList) {
        Set<GuaranteeNodeEntity> nodeSet = new TreeSet<>(Comparator.comparing(GuaranteeNodeEntity::getNodeId));
        nodeSet.addAll(nodeEntityList);
        return new ArrayList<>(nodeSet);
    }
}
