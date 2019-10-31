package com.service.guarantee.service;

import com.service.guarantee.apps.GuaranteeApps;
import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.domain.response.ResultEntity;

import java.util.List;

public interface GuaranteeService {
    /**
     * 担保接口
     * @param guaranteeApps neo4j query function
     * @param neo4jClient neo4j client
	 * @param requestData request data
     * @return ResultEntity
     */
	ResultEntity getTargetRelation(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, RequestGetTargetRelation requestData);

	ResultEntity expandNodePath(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, RequestExpandNodePath requestData);

	ResultEntity getRelationInCorp(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, RequestGetRelationInCorp requestData);

    ResultEntity getCorpRelatedDetail(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, RequestGetCorpRelatedDetail requestData);

    ResultEntity hasNextBatchQuery(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, List<String> nodeIds);

    ResultEntity pathSearch(GuaranteeApps guaranteeApps, Neo4jDAO neo4jClient, RequestPathSearch requestData);

}
