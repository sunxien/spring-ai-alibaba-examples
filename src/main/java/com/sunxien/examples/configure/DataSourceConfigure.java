package com.sunxien.examples.configure;

import com.sunxien.examples.custom.EncryptDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Configuration(proxyBeanMethods = false)
public class DataSourceConfigure {

    /**
     * @return DataSource
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = false)
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(EncryptDataSource.class).build();
    }

    /**
     * @param dataSource
     * @return JdbcTemplate
     */
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
