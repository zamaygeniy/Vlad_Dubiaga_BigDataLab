package com.epam.crimes.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;

public class DatabaseConnection {

    private static class DatabaseConnectionHolder {
        public static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    private DatabaseConnection(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/crimes");
        config.setUsername("postgres");
        config.setPassword("reverse");
        config.setSchema("crimes_schema");
        dataSource = new HikariDataSource(config);
        fluentJdbc = new FluentJdbcBuilder()
                .connectionProvider(dataSource)
                .build();
    }

    public static DatabaseConnection getInstance(){
        return  DatabaseConnectionHolder.INSTANCE;
    }

    private final HikariDataSource dataSource;
    private final FluentJdbc fluentJdbc;

    public HikariDataSource getDataSource(){
        return dataSource;
    }

    public FluentJdbc getFluentJdbc(){
        return fluentJdbc;
    }

}
