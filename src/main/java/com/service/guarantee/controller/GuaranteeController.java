package com.service.guarantee.controller;

import com.alibaba.fastjson.JSONObject;
import com.service.guarantee.apps.GuaranteeApps;
import com.service.guarantee.callback.*;
import com.service.guarantee.config.ConfigProperties;
import com.service.guarantee.dao.Neo4jDAO;
import com.service.guarantee.domain.response.ResultEntity;
import com.service.guarantee.domain.request.*;
import com.service.guarantee.service.GuaranteeService;
import com.service.guarantee.callback.*;
import com.service.guarantee.domain.request.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.service.guarantee.config.ConfigProperties.SINGLE_THREAD_TIMEOUT;

@RestController
@RequestMapping("/api")
public class GuaranteeController {

    private static final Logger logger = Logger.getLogger(GuaranteeController.class);

    private final static List<String> interfaceName = Arrays.asList("getTargetRelation", "expandNodePath",
            "getRelationInCorp", "getCorpRelatedDetail", "hasNextBatchQuery");

    @Resource(name = "guaranteeService")
    private GuaranteeService guaranteeService;

    @Resource(name = "guaranteeApps")
    private GuaranteeApps guaranteeApps;


    public static String neo4jValidBoltAddress = ConfigProperties.NEO4J_BOLT_ADDRESS;

    @RequestMapping(value = "/")
    public String home() throws Exception {
        logger.info("This is home method");
        String apiList = "";
        for (String item : interfaceName) {
            apiList += "/guarantee/api/" + item + "\r\n";
        }
        return apiList;
    }

    @RequestMapping(value = "/getTargetRelation", method = RequestMethod.POST)
    public ResultEntity getTargetRelation(@RequestBody @Valid RequestGetTargetRelation data) {
        Neo4jDAO.checkNeo4jService();
        GetTargetRelationCallback callback = new GetTargetRelationCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, data);
        return extracted(callback, "getTargetRelation");
    }

    @RequestMapping(value = "/expandNodePath", method = RequestMethod.POST)
    public ResultEntity expandNodePath(@RequestBody @Valid RequestExpandNodePath data) {
        Neo4jDAO.checkNeo4jService();
        ExpandNodePathCallback callback = new ExpandNodePathCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, data);
        return extracted(callback, "expandNodePath");

    }

    @RequestMapping(value = "/getRelationInCorp", method = RequestMethod.POST)
    public ResultEntity inCorpRelation(@RequestBody @Valid RequestGetRelationInCorp data) {
        Neo4jDAO.checkNeo4jService();
        GetRelationInCorpCallback callback = new GetRelationInCorpCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, data);
        return extracted(callback, "getRelationInCorp");
    }

    @RequestMapping(value = "/getCorpRelatedDetail", method = RequestMethod.POST)
    public ResultEntity getCorpRelatedDetail(@RequestBody @Valid RequestGetCorpRelatedDetail data) {
        Neo4jDAO.checkNeo4jService();
        GetCorpRelatedDetailCallback callback = new GetCorpRelatedDetailCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, data);
        return extracted(callback, "getCorpRelatedDetail");
    }

    @RequestMapping(value = "/hasNextBatchQuery", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ResultEntity hasNextBatchQuery(@RequestBody JSONObject jsonObject) {
        try {
            @SuppressWarnings("unchecked")
            List<String> nodeIds = (List<String>) jsonObject.get("nodeIds");
            Neo4jDAO.checkNeo4jService();
            HasNextBatchQueryCallback callback = new HasNextBatchQueryCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, nodeIds);
            return extracted(callback, "hasNextBatchQuery");
        } catch (Exception e) {
            ResultEntity responseResult;
            String message = "Query exception, " + e.getMessage();
            responseResult = new ResultEntity(null, message, 500);
            return responseResult;
        }
    }

    @RequestMapping(value = "/pathSearch", method = RequestMethod.POST)
    public ResultEntity pathSearch(@RequestBody @Valid RequestPathSearch data) {
        Neo4jDAO.checkNeo4jService();
        String requestURI = "pathSearch";
        PathSearchCallback callback = new PathSearchCallback(guaranteeService, guaranteeApps, Neo4jDAO.neo4jClient, data);
        return extracted(callback, requestURI);
    }

    private ResultEntity extracted(Callable<ResultEntity> callback, String methodName) {
        long startTime = System.currentTimeMillis();

        ResultEntity responseResult = new ResultEntity();
        int status = 200;

        String message = "";
        ExecutorService excutor = Executors.newSingleThreadExecutor();
        try {
            try {
                Future<ResultEntity> future = excutor.submit(callback);
                responseResult = future.get(SINGLE_THREAD_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                message = String.format("Compute Timeout, processed exceed %d ms", SINGLE_THREAD_TIMEOUT);
                status = 500;
                responseResult = new ResultEntity(null, message, status);
            }
        } catch (Exception e) {
            message = "Thread Executor Exception, " + e.getMessage();
            status = 500;
            responseResult = new ResultEntity(null, message, status);
        } finally {
            excutor.shutdownNow();
        }

        logger.info(String.format("Interface: %s, Response Status: %d, Process Time: %d, Neo4j Bolt: %s", methodName,
                status, System.currentTimeMillis() - startTime, Neo4jDAO.neo4jClient.getBoltAddress()));

        return responseResult;
    }

}
