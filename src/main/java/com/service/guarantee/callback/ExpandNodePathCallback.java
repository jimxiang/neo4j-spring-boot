package com.service.guarantee.callback;

import com.service.guarantee.apps.GuaranteeApps;
import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.response.ResultEntity;
import com.service.guarantee.domain.request.RequestExpandNodePath;
import com.service.guarantee.service.GuaranteeService;

import java.util.concurrent.Callable;

public class ExpandNodePathCallback implements Callable<ResultEntity> {
    private GuaranteeService guaranteeService;
    private GuaranteeApps guaranteeApps;
    private Neo4jDAO neo4jDAO;
    private RequestExpandNodePath requestData;

    public ExpandNodePathCallback(GuaranteeService guaranteeService, GuaranteeApps guaranteeApps,
                                     Neo4jDAO neo4jDAO, RequestExpandNodePath requestData) {
        super();
        this.guaranteeService = guaranteeService;
        this.guaranteeApps = guaranteeApps;
        this.neo4jDAO = neo4jDAO;
        this.requestData = requestData;
    }

    @Override
    public ResultEntity call() throws Exception {
        return guaranteeService.expandNodePath(guaranteeApps, neo4jDAO, requestData);
    }
}
