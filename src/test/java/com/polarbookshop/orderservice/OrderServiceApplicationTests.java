package com.polarbookshop.orderservice;

import com.polarbookshop.orderservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@Import(DataConfig.class)
class OrderServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:16.1"));

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.r2dbc.url", OrderServiceApplicationTests::r2dbcUrl);
        propertyRegistry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        propertyRegistry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s",
            postgreSQLContainer.getHost(),
            postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
            postgreSQLContainer.getDatabaseName());
    }

    @Test
    void contextLoads() {
    }

}
