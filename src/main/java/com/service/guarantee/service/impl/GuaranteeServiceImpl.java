package com.service.guarantee.service.impl;

import com.service.guarantee.apps.GuaranteeApps;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.domain.response.ResultEntity;
import org.springframework.stereotype.Service;

import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.service.GuaranteeService;

import java.util.List;

@Service(value = "guaranteeService")
public class GuaranteeServiceImpl implements GuaranteeService {
	@Override
	public ResultEntity getTargetRelation(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, RequestGetTargetRelation requestData) {
		return guaranteeApps.getTargetRelation(neo4jDAO, requestData);
	}

	@Override
	public ResultEntity expandNodePath(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, RequestExpandNodePath requestData) {
		return guaranteeApps.expandNodePath(neo4jDAO, requestData);
	}

	@Override
	public ResultEntity getRelationInCorp(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, RequestGetRelationInCorp requestData) {
		return guaranteeApps.getRelationInCorp(neo4jDAO, requestData);
	}

    @Override
    public ResultEntity getCorpRelatedDetail(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, RequestGetCorpRelatedDetail requestData) {
        return guaranteeApps.getCorpRelatedDetail(neo4jDAO, requestData);
    }

    @Override
    public ResultEntity hasNextBatchQuery(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, List<String> nodeIds) {
        return guaranteeApps.hasNextBatchQuery(neo4jDAO, nodeIds);
    }

	@Override
	public ResultEntity pathSearch(GuaranteeApps guaranteeApps, Neo4jDAO neo4jDAO, RequestPathSearch requestData) {
		return guaranteeApps.pathSearch(neo4jDAO, requestData);
	}
}
