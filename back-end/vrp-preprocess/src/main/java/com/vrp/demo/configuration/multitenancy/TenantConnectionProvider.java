package com.vrp.demo.configuration.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component(value = "multiTenantConnectionProviderImpl")
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

    private static Logger logger = LoggerFactory.getLogger(TenantConnectionProvider.class);
    private DataSource datasource;

    public TenantConnectionProvider(DataSource dataSource) {
        this.datasource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        logger.info("Get connection for tenant {}", tenantIdentifier);
        final Connection connection = getAnyConnection();
//        connection.setSchema(tenantIdentifier);
        try {
            connection.createStatement().execute("USE `" + tenantIdentifier + "`");
        } catch (SQLException e) {
            logger.info("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]");
            logger.info("Get JDBC connection to default schema [" + TenantContext.DEFAULT_TENANT + "]");
            connection.createStatement().execute("USE " + TenantContext.DEFAULT_TENANT);
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        logger.info("Release connection for tenant {}", tenantIdentifier);
        try {
            connection.createStatement().execute("USE " + TenantContext.DEFAULT_TENANT);
        } catch (SQLException e) {
            logger.info("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }
//        connection.setSchema(DEFAULT_TENANT);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
