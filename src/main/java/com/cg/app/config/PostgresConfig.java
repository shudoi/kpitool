package com.cg.app.config;

import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresConfig {

    @Bean
    public DataSource dataSource() {
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("postgres");
        source.setServerName("ec2-107-21-113-16.compute-1.amazonaws.com");
        source.setDatabaseName("daptgnea3lae61");
        source.setUser("emkbuqlelmiywz");
        source.setPassword("2ff73f7cfd04c64e14e4b0efcaa6b13f1b089d3757cb5424ce03e0ffff9a38f3");
        source.setPortNumber(5432);
        source.setMaxConnections(10);
        source.setSsl(true);
        //source.setLoginTimeout(5);
        //source.setSocketTimeout(5);
        source.setSslfactory("org.postgresql.ssl.NonValidatingFactory");
        return source;
    }
}
