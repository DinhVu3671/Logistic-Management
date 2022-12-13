package com.vrp.demo.configuration.multitenancy;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.vrp.demo.configuration.mongodb.CascadingMongoEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MultiTenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {
//    private static final String url = "mongodb+srv://zovivo:123khongbiet123@cluster0.eloqz.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static final String url = "mongodb://localhost:27017/";
    private static final String DEFAULT_DB = "common";

    private static Logger logger = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);


    public MultiTenantMongoDbFactory() {
        super(url + DEFAULT_DB);
    }

    public MultiTenantMongoDbFactory(String databaseName) {
        super(url + databaseName);
    }

    @Override
    protected MongoDatabase doGetMongoDatabase(String dbName) {
        return mongoDatabaseCurrentTenantResolver();
    }

    public MongoDatabase mongoDatabaseCurrentTenantResolver() {
        try {
            final String tenantId = TenantContext.getCurrentTenant();
            String tenantConnection = url;
            MongoClient client = MongoClients.create(tenantConnection);
            logger.info("Get connection for Datasource {}", tenantId);
            return client.getDatabase(tenantId);
        } catch (NullPointerException exception) {
            System.err.println("Tenant Datasource alias not found.");
        }
        return null;
    }

    @Bean
    public CascadingMongoEventListener cascadingMongoEventListener() {
        return new CascadingMongoEventListener();
    }
}
