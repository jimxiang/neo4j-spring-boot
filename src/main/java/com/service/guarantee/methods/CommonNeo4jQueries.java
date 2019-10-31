package com.service.guarantee.methods;

import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.GuaranteeLinkEntity;
import com.service.guarantee.domain.HasNextEntity;
import com.service.guarantee.domain.PathSearchEntity;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.domain.request.*;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonNeo4jQueries {
    private static final Logger logger = Logger.getLogger(CommonNeo4jQueries.class);

    public static GuaranteeLinkEntity getTargetRelation(Neo4jDAO neo4jDAO, RequestGetTargetRelation requestData) {
        String eid = requestData.getEid();
        int start = requestData.getStart();
        int limit = requestData.getLimit();

        String statement = String.format("MATCH (source:Guarantee)-[rel:CorpAggRel|:GuaranteeRel*1]-(target) " +
                "WHERE source.eid = '%s' " +
                "WITH source, target, rel " +
                "UNWIND rel AS r " +
                "MATCH (source)-[r]-(target) " +
                "WHERE NOT (target.corpCnt > 1 AND (source)-[r]-(target)-[:BelongRel]->(:Corp)) " +
                "WITH source, target, r " +
                "ORDER BY r.amount DESC " +
                "SKIP %d " +
                "LIMIT %d " +
                "RETURN COLLECT(DISTINCT(target)) AS nodeList, source AS source, COLLECT(r) AS edgeList", eid, start, limit);

        GuaranteeLinkEntity guaranteeLinkEntity = new GuaranteeLinkEntity();
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            int total = getTargetRelationCount(neo4jDAO, eid);
            StatementResult statementResult = session.run(statement);
            guaranteeLinkEntity = ParseResult.getGuaranteeLinkEntity(statementResult, total);
            return guaranteeLinkEntity;
        } catch (Exception e) {
            logger.error("Execute GetTargetRelation Cypher Query Error, " + e.getMessage());
            return guaranteeLinkEntity;
        }
    }

    public static GuaranteeLinkEntity expandNodePath(Neo4jDAO neo4jDAO, RequestExpandNodePath requestData) {
        String nodeId = requestData.getNodeId();
        String mode = requestData.getMode();
        int start = requestData.getStart();
        int limit = requestData.getLimit();
        String statement = "";

        if (mode.equals("1")) {
            statement = String.format("MATCH (source:Guarantee)-[rel:GuaranteeRel]-(target:Guarantee) " +
                            "WHERE source.nodeId = '%s' " +
                            "WITH source, target, rel " +
                            "ORDER BY rel.amount DESC " +
                            "SKIP %d " +
                            "LIMIT %d " +
                            "RETURN COLLECT(target) AS nodeList, source AS source, COLLECT(rel) AS edgeList "
                    , nodeId, start, limit);
        }

        if (mode.equals("2")) {
            statement = String.format("MATCH (source)-[rel:CorpAggRel|:CorpRel|:GuaranteeRel]-(target) " +
                            "WHERE source.nodeId = '%s' " +
                            "    AND source.nodeId <> target.nodeId " +
                            "WITH source, target, rel " +
                            "MATCH (source)-[rel]-(target) " +
                            "WHERE NOT (target.corpCnt > 1 AND (source)-[rel]-(target)-[:BelongRel]->(:Corp)) " +
                            "WITH source, target, rel " +
                            "ORDER BY rel.amount DESC " +
                            "SKIP %d " +
                            "LIMIT %d " +
                            "RETURN COLLECT(target) AS nodeList, source AS source, COLLECT(rel) AS edgeList "
                    , nodeId, start, limit);
        }

        GuaranteeLinkEntity guaranteeLinkEntity = new GuaranteeLinkEntity();
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            int total = expandNodePathCount(neo4jDAO, nodeId, mode);
            StatementResult statementResult = session.run(statement);
            guaranteeLinkEntity = ParseResult.getGuaranteeLinkEntity(statementResult, total);
            return guaranteeLinkEntity;
        } catch (Exception e) {
            logger.error("Execute ExpandNodePath Cypher Query Error, " + e.getMessage());
            return guaranteeLinkEntity;
        }
    }

    public static GuaranteeLinkEntity getRelationInCorp(Neo4jDAO neo4jDAO, RequestGetRelationInCorp requestData) {
        String corpId = requestData.getCorpId();
        int start = requestData.getStart();
        int limit = requestData.getLimit();
        String statement = String.format("MATCH (c:Corp) WHERE c.corpId = '%s' " +
                "WITH c.corpList AS NodeList " +
                "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n) " +
                "WHERE m.nodeId IN NodeList AND n.nodeId IN NodeList " +
                "WITH m, n, r " +
                "SKIP %d " +
                "LIMIT %d " +
                "RETURN COLLECT(n) AS nodeList, m AS source, COLLECT(r) AS edgeList", corpId, start, limit);

        GuaranteeLinkEntity guaranteeLinkEntity = new GuaranteeLinkEntity();
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            int total = getRelationInCorpCount(neo4jDAO, corpId);
            StatementResult statementResult = session.run(statement);
            guaranteeLinkEntity = ParseResult.getGuaranteeLinkEntity(statementResult, total);
            return guaranteeLinkEntity;
        } catch (Exception e) {
            logger.error("Execute GetRelationInCorp Cypher Query Error, " + e.getMessage());
            return guaranteeLinkEntity;
        }
    }

    public static GuaranteeLinkEntity getCorpRelatedDetail(Neo4jDAO neo4jDAO, RequestGetCorpRelatedDetail requestData) {
        String src = requestData.getSrc();
        String dst = requestData.getDst();
        String mode = requestData.getMode();
        int start = requestData.getStart();
        int limit = requestData.getLimit();

        String statement = "";
        if (mode.equals("1")) {
            statement = String.format("MATCH (c1:Corp)-[r1:CorpRel]->(c2:Corp) " +
                    "WHERE c1.corpId = '%s' AND c2.corpId = '%s' " +
                    "WITH c1.corpList AS SrcList, c2.corpList AS DstList " +
                    "MATCH (m:Guarantee)-[r2:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId IN SrcList AND n.nodeId IN DstList " +
                    "WITH m, n, r2 " +
                    "SKIP %d " +
                    "LIMIT %d " +
                    "RETURN COLLECT(m) + COLLECT(n) AS nodeList, COLLECT(r2) AS edgeList", src, dst, start, limit);
        }
        if (mode.equals("2")) {
            statement = String.format("MATCH (source:Guarantee)-[rel:CorpAggRel]->(target:Corp) " +
                    "WHERE source.nodeId = '%s' AND target.nodeId = '%s' " +
                    "WITH source.nodeId AS src, rel.nodeIds AS DstList " +
                    "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId = src AND n.nodeId in DstList " +
                    "WITH m, n, r " +
                    "SKIP %d " +
                    "LIMIT %d " +
                    "RETURN COLLECT(m) + COLLECT(n) AS nodeList, COLLECT(r) AS edgeList", src, dst, start, limit);
        }
        if (mode.equals("3")) {
            statement = String.format("MATCH (source:Corp)-[rel:CorpAggRel]->(target:Guarantee) " +
                    "WHERE source.nodeId = '%s' AND target.nodeId = '%s' " +
                    "WITH rel.nodeIds AS SrcList, target.nodeId AS dst " +
                    "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId in SrcList AND n.nodeId = dst " +
                    "WITH m, n, r " +
                    "SKIP %d " +
                    "LIMIT %d " +
                    "RETURN COLLECT(m) + COLLECT(n) AS nodeList, COLLECT(r) AS edgeList", src, dst, start, limit);
        }

        GuaranteeLinkEntity guaranteeLinkEntity = new GuaranteeLinkEntity();
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            int total = getCorpRelatedDetailCount(neo4jDAO, src, dst, mode);
            StatementResult statementResult = session.run(statement);
            guaranteeLinkEntity = ParseResult.getGuaranteeLinkEntity(statementResult, total);
            return guaranteeLinkEntity;
        } catch (Exception e) {
            logger.error("Execute GetCorpRelatedDetail Cypher Query Error, " + e.getMessage());
            return guaranteeLinkEntity;
        }
    }

    public static List<HasNextEntity> hasNextBatchQuery(Neo4jDAO neo4jDAO, List<String> nodeIds) {
        String statement = "MATCH (n)-[r:CorpAggRel|:CorpRel|:GuaranteeRel]-(m) " +
                "WHERE n.nodeId in {nodeIds} " +
                "WITH n, m, r " +
                "MATCH (n)-[r]-(m) " +
                "WHERE NOT (m.corpCnt > 1 AND (n)-[r]-(m)-[:BelongRel]->(:Corp)) " +
                "RETURN COUNT(*) AS total, n.nodeId as nodeId";
        List<HasNextEntity> hasNextEntityList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("nodeIds", nodeIds);
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            StatementResult statementResult = session.run(statement, params);
            hasNextEntityList = ParseResult.getHasNextEntity(statementResult);
            return hasNextEntityList;
        } catch (Exception e) {
            logger.error("Execute HasNextBatchQuery Cypher Query Error, " + e.getMessage());
            return hasNextEntityList;
        }
    }

    public static PathSearchEntity pathSearch(Neo4jDAO neo4jDAO, RequestPathSearch requestData) {
        String src = requestData.getSrc();
        String dst = requestData.getDst();
        String statement = String.format("MATCH (source:Guarantee {eid: '%s'}) " +
                "MATCH (target:Guarantee {eid: '%s'}) " +
                "CALL apoc.algo.allSimplePaths(source, target, 'GuaranteeRel>', 6) YIELD path " +
                "RETURN path ", src, dst);

        PathSearchEntity pathSearchEntity = new PathSearchEntity();
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            StatementResult statementResult = session.run(statement);
            pathSearchEntity = ParseResult.getGuaranteeLinkEntity(statementResult);
            return pathSearchEntity;
        } catch (Exception e) {
            logger.error("Execute GetRelationInCorp Cypher Query Error, " + e.getMessage());
            return pathSearchEntity;
        }
    }

    private static int getTargetRelationCount(Neo4jDAO neo4jDAO, String eid) {
        String statement = String.format("MATCH (source:Guarantee)-[rel:CorpAggRel|:GuaranteeRel*1]-(target) " +
                "WHERE source.eid = '%s' " +
                "WITH source, target, rel " +
                "UNWIND rel AS r " +
                "MATCH (source)-[r]-(target) " +
                "WHERE NOT (target.corpCnt > 1 AND (source)-[r]-(target)-[:BelongRel]->(:Corp)) " +
                "RETURN COUNT(*) AS total ", eid);

        return getTotal(neo4jDAO, statement);
    }

    private static int expandNodePathCount(Neo4jDAO neo4jDAO, String nodeId, String mode) {
        String statement = "";
        if (mode.equals("1")) {
            statement = "MATCH (source:Guarantee)-[rel:GuaranteeRel]-(target:Guarantee) " +
                    "WHERE source.nodeId = '" + nodeId + "' " +
                    "RETURN COUNT(*) AS total ";
        }
        if (mode.equals("2")) {
            statement = String
                    .format("MATCH (source)-[rel:CorpAggRel|:CorpRel|:GuaranteeRel]-(target) " +
                            "WHERE source.nodeId = '%s' " +
                            "    AND source.nodeId <> target.nodeId " +
                            "WITH source, target, rel " +
                            "MATCH (source)-[rel]-(target) " +
                            "WHERE NOT (target.corpCnt > 1 AND (source)-[rel]-(target)-[:BelongRel]->(:Corp)) " +
                            "RETURN COUNT(*) AS total ", nodeId);
        }
        return getTotal(neo4jDAO, statement);
    }

    private static int getRelationInCorpCount(Neo4jDAO neo4jDAO, String corpId) {
        String statement = String.format("MATCH (c:Corp) WHERE c.corpId = '%s' " +
                "WITH c.corpList AS NodeList " +
                "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n) " +
                "WHERE m.nodeId IN NodeList AND n.nodeId IN NodeList " +
                "RETURN COUNT(*) AS total", corpId);

        return getTotal(neo4jDAO, statement);
    }

    private static int getCorpRelatedDetailCount(Neo4jDAO neo4jDAO, String src, String dst, String mode) {
        String statement = "";
        if (mode.equals("1")) {
            statement = String.format("MATCH (c1:Corp)-[r1:CorpRel]->(c2:Corp) " +
                    "WHERE c1.corpId = '%s' AND c2.corpId = '%s' " +
                    "WITH c1.corpList AS SrcList, c2.corpList AS DstList " +
                    "MATCH (m:Guarantee)-[r2:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId IN SrcList AND n.nodeId IN DstList " +
                    "RETURN COUNT(*) AS total", src, dst);
        }
        if (mode.equals("2")) {
            statement = String.format("MATCH (source:Guarantee)-[rel:CorpAggRel]->(target:Corp) " +
                    "WHERE source.nodeId = '%s' AND target.nodeId = '%s' " +
                    "WITH source.nodeId AS src, rel.nodeIds AS DstList " +
                    "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId = src AND n.nodeId in DstList " +
                    "RETURN COUNT(*) AS total", src, dst);
        }
        if (mode.equals("3")) {
            statement = String.format("MATCH (source:Corp)-[rel:CorpAggRel]->(target:Guarantee) " +
                    "WHERE source.nodeId = '%s' AND target.nodeId = '%s' " +
                    "WITH rel.nodeIds AS SrcList, target.nodeId AS dst " +
                    "MATCH (m:Guarantee)-[r:GuaranteeRel]->(n:Guarantee) " +
                    "WHERE m.nodeId in SrcList AND n.nodeId = dst " +
                    "RETURN COUNT(*) AS total", src, dst);
        }

        return getTotal(neo4jDAO, statement);
    }

    private static int getTotal(Neo4jDAO neo4jDAO, String statement) {
        int total = 0;
        try (Session session = neo4jDAO.getDriver().session(AccessMode.READ)) {
            StatementResult statementResult = session.run(statement);
            total = ParseResult.getTotal(statementResult);
        }
        return total;
    }
}
