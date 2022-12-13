package com.vrp.demo.service.imp;

//import org.flywaydb.core.Flyway;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service("multiTenantService")
public class MultiTenantService {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Async
    public void initDatabase(String schema) {
        Flyway flyway = Flyway.configure()
                .locations("db/migration/tenants")
                .dataSource(dataSource)
                .schemas(schema)
                .load();
        flyway.migrate();
    }

    public void createCollectionForUser(String userName) {
        mongoTemplate.createCollection(userName);
        return;
    }

}
