package com.service.guarantee.apps;

import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.GuaranteeLinkEntity;
import com.service.guarantee.domain.HasNextEntity;
import com.service.guarantee.domain.PathSearchEntity;
import com.service.guarantee.domain.response.ResultEntity;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.methods.CommonNeo4jQueries;
import com.service.guarantee.domain.request.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "guaranteeApps")
public class GuaranteeApps {
    private static final Logger logger = Logger.getLogger(GuaranteeApps.class);

    public ResultEntity getTargetRelation(Neo4jDAO neo4jDAO, RequestGetTargetRelation requestData) {
        ResultEntity result = new ResultEntity();
        logger.info(requestData.toString());

        try {
            GuaranteeLinkEntity guaranteeLinkEntity = CommonNeo4jQueries.getTargetRelation(neo4jDAO, requestData);
            result.setData(guaranteeLinkEntity);
        } catch (Exception e) {
            result.setMessage("GetTargetRelation Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }

    public ResultEntity expandNodePath(Neo4jDAO neo4jDAO, RequestExpandNodePath requestData) {
        ResultEntity result = new ResultEntity();
        logger.info(requestData.toString());

        try {
            GuaranteeLinkEntity guaranteeLinkEntity = CommonNeo4jQueries.expandNodePath(neo4jDAO, requestData);
            result.setData(guaranteeLinkEntity);
        } catch (Exception e) {
            result.setMessage("ExpandNodePath Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }

    public ResultEntity getRelationInCorp(Neo4jDAO neo4jDAO, RequestGetRelationInCorp requestData) {
        ResultEntity result = new ResultEntity();
        logger.info(requestData.toString());

        try {
            GuaranteeLinkEntity guaranteeLinkEntity = CommonNeo4jQueries.getRelationInCorp(neo4jDAO, requestData);
            result.setData(guaranteeLinkEntity);
        } catch (Exception e) {
            result.setMessage("GetRelationInCorp Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }

    public ResultEntity getCorpRelatedDetail(Neo4jDAO neo4jDAO, RequestGetCorpRelatedDetail requestData) {
        ResultEntity result = new ResultEntity();
        logger.info(requestData.toString());

        try {
            GuaranteeLinkEntity guaranteeLinkEntity = CommonNeo4jQueries.getCorpRelatedDetail(neo4jDAO, requestData);
            result.setData(guaranteeLinkEntity);
        } catch (Exception e) {
            result.setMessage("GetCorpRelatedDetail Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }

    public ResultEntity hasNextBatchQuery(Neo4jDAO neo4jDAO, List<String> nodeIds) {
        ResultEntity result = new ResultEntity();
        logger.info(nodeIds);

        try {
            List<HasNextEntity> hasNextEntityLists = CommonNeo4jQueries.hasNextBatchQuery(neo4jDAO, nodeIds);
            result.setData(hasNextEntityLists);
        } catch (Exception e) {
            result.setMessage("HasNextBatchQuery Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }

    public ResultEntity pathSearch(Neo4jDAO neo4jDAO, RequestPathSearch requestData) {
        ResultEntity result = new ResultEntity();
        logger.info(requestData);

        try {
            PathSearchEntity pathSearchEntity = CommonNeo4jQueries.pathSearch(neo4jDAO, requestData);
            result.setData(pathSearchEntity);
        } catch (Exception e) {
            result.setMessage("PathSearch Compute Error, " + e.getMessage());
            result.setStatus(500);
        }

        return result;
    }
}
