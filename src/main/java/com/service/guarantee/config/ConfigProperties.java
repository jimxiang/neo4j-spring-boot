package com.service.guarantee.config;

import com.service.guarantee.utils.ApplicationConfig;

public class ConfigProperties {
    public static final String NEO4J_BOLT_ADDRESS = ApplicationConfig.getValue("neo4j.bolt.address", "");
    public static final String NEO4J_UESRNAME = ApplicationConfig.getValue("neo4j.user.name", "");
    public static final String NEO4J_PASSWORD = ApplicationConfig.getValue("neo4j.user.password", "");

    public static final int SINGLE_THREAD_TIMEOUT = ApplicationConfig.getValue("single.thread.timeout", 20000);

    // neo4j driver配置
    public static final int MAX_CONNOCTION_POOL_SIZE = ApplicationConfig.getValue("neo4j.driver.withMaxConnectionPoolSize", 100);

    // neo4j HA
    public static final String NEO4J_HA_BOLT_ADDRESS = ApplicationConfig.getValue("neo4j.ha.bolt.address", "");
    public static final String NEO4J_HA_UESRNAME = ApplicationConfig.getValue("neo4j.ha.user.name", "");
    public static final String NEO4J_HA_PASSWORD = ApplicationConfig.getValue("neo4j.ha.user.password", "");
}
