package com.service.guarantee.callback;

import com.service.guarantee.apps.GuaranteeApps;
import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.response.ResultEntity;
import com.service.guarantee.service.GuaranteeService;

import java.util.List;
import java.util.concurrent.Callable;

public class HasNextBatchQueryCallback implements Callable<ResultEntity> {
    private GuaranteeService guaranteeService;
    private GuaranteeApps guaranteeApps;
    private Neo4jDAO neo4jDAO;
    private List<String> nodeIds;

    public HasNextBatchQueryCallback(GuaranteeService guaranteeService, GuaranteeApps guaranteeApps,
                                  Neo4jDAO neo4jDAO, List<String> nodeIds) {
        super();
        this.guaranteeService = guaranteeService;
        this.guaranteeApps = guaranteeApps;
        this.neo4jDAO = neo4jDAO;
        this.nodeIds = nodeIds;
    }

    @Override
    public ResultEntity call() throws Exception {
        return guaranteeService.hasNextBatchQuery(guaranteeApps, neo4jDAO, nodeIds);
    }
}
