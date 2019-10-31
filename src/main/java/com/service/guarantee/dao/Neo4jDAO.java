package com.service.guarantee.dao;

import com.service.guarantee.config.ConfigProperties;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Neo4jDAO implements AutoCloseable, InitializingBean {

	private static final Logger logger = Logger.getLogger(Neo4jDAO.class);

	public static Neo4jDAO neo4jClient = null;

	private static final Config DRIVER_CONF = Config.builder()
			.withMaxConnectionPoolSize(ConfigProperties.MAX_CONNOCTION_POOL_SIZE)
			.build();

	private Driver driver;
	private String boltAddress;
	private String userName;
	private String password;
	private static String neo4jServer = "server1";

	public Neo4jDAO() {
		super();
	}

	public Neo4jDAO(String boltAddress, String userName, String password) {
		super();
		try {
			this.boltAddress = boltAddress;
			this.userName = userName;
			this.password = password;
			this.driver = GraphDatabase.driver(this.boltAddress, AuthTokens.basic(this.userName, this.password),
					DRIVER_CONF);
			logger.info(String.format("Success Initialing Neo4j, boltAddress: %s", boltAddress));
		} catch (Exception th) {
			String error = boltAddress + "&" + userName + "&" + password;
			logger.error("Neo4j Connect Error: " + error);
		}
	}

	public String getBoltAddress() {
		return boltAddress;
	}

	public void setBoltAddress(String boltAddress) {
		this.boltAddress = boltAddress;
	}

	public Driver getDriver() {
		return driver;
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	@Override
	public void afterPropertiesSet() {
	    setProperties();
	}

	public static void setProperties() {
        neo4jClient = new Neo4jDAO(ConfigProperties.NEO4J_BOLT_ADDRESS, ConfigProperties.NEO4J_UESRNAME,
                ConfigProperties.NEO4J_PASSWORD);

        if (neo4jClient.driver == null) {
            logger.info("Start HA Neo4j...");
            neo4jClient = new Neo4jDAO(ConfigProperties.NEO4J_HA_BOLT_ADDRESS, ConfigProperties.NEO4J_HA_UESRNAME,
                    ConfigProperties.NEO4J_HA_PASSWORD);
            neo4jServer = "server2";
        }
    }

	public static void checkNeo4jService() {
		try (Session session = neo4jClient.driver.session(AccessMode.READ)) {
			String statement = "MATCH (n) RETURN n LIMIT 1";
			session.run(statement);
		} catch (Exception e) {
			logger.info("Start Neo4j Switch...");
			if (neo4jServer.equals("server1")) {
				neo4jClient = new Neo4jDAO(ConfigProperties.NEO4J_HA_BOLT_ADDRESS, ConfigProperties.NEO4J_HA_UESRNAME,
						ConfigProperties.NEO4J_HA_PASSWORD);
				neo4jServer = "server2";
				logger.info(String.format("Now Neo4j server is %s", ConfigProperties.NEO4J_HA_BOLT_ADDRESS));
			} else {
				neo4jClient = new Neo4jDAO(ConfigProperties.NEO4J_BOLT_ADDRESS, ConfigProperties.NEO4J_UESRNAME,
						ConfigProperties.NEO4J_PASSWORD);
				neo4jServer  ="server1";
				logger.info(String.format("Now Neo4j server is %s", ConfigProperties.NEO4J_BOLT_ADDRESS));
			}
		}

	}
}
