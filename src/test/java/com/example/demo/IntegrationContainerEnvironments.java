package com.example.demo;

import org.junit.ClassRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.Wait;

import java.util.Optional;


@ContextConfiguration(initializers = IntegrationContainerEnvironments.SpringContextInitializer.class)
public abstract class IntegrationContainerEnvironments {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationContainerEnvironments.class);


    private static final Boolean droneEnvironment
            = Optional.ofNullable(System.getenv("DRONE"))
            .map(value -> value.equalsIgnoreCase("true"))
            .orElse(false);

    @ClassRule
    public static final GenericContainer RABBIT_CONTAINER = setupRabbitContainer();

    private static final int RABBIT_PORT = 5672;

    private static final int MONGO_EXPOSED_PORT = 27017;

    @ClassRule
    public static final GenericContainer MONGO_CONTAINER = setupMongoContainer();

    private static final String RABBIT_IMAGE = "rabbitmq:latest";

    private static final String MONGO_IMAGE = "mongo:3.6";

    @ClassRule
    public static final PostgreSQLContainer POSTGRES_CONTAINER = setupPostgresContainer();


    public static class SpringContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            if (!droneEnvironment) {
                LOGGER.info("Setting up test environment context...");
                EnvironmentTestUtils.addEnvironment("testcontainers", configurableApplicationContext.getEnvironment(),
                        "spring.rabbitmq.host=" + RABBIT_CONTAINER.getContainerIpAddress()
                        ,"spring.rabbitmq.port=" + RABBIT_CONTAINER.getMappedPort(RABBIT_PORT)
                        ,"spring.rabbitmq.username=rabbit"
                        ,"spring.rabbitmq.login=mq"
                        ,"spring.data.mongodb.host=" + MONGO_CONTAINER.getContainerIpAddress()
                        ,"spring.data.mongodb.port=" + MONGO_CONTAINER.getMappedPort(MONGO_EXPOSED_PORT)
                        ,"spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl()
                        ,"spring.datasource.username=" + POSTGRES_CONTAINER.getUsername()
                        ,"spring.datasource.password=" + POSTGRES_CONTAINER.getPassword()
                        ,"spring.jpa.hibernate.ddl-auto=create"
                );
            }
        }
    }

    private static GenericContainer setupRabbitContainer() {

        return !droneEnvironment ? new GenericContainer(RABBIT_IMAGE)
                .withExposedPorts(RABBIT_PORT)
                .withEnv("RABBITMQ_DEFAULT_USER", "rabbit")
                .withEnv("RABBITMQ_DEFAULT_PASS","mq")
                .waitingFor(Wait.forListeningPort()) : null;

    }

    private static GenericContainer setupMongoContainer() {

        return !droneEnvironment ? new GenericContainer(MONGO_IMAGE).withExposedPorts(MONGO_EXPOSED_PORT) : null;

    }

    private static PostgreSQLContainer setupPostgresContainer() {
        return !droneEnvironment ? new PostgreSQLContainer() : null;
    }



    public static class NoOpGenericContainer extends GenericContainer {
        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    return;
                }
            };
        }
    }


}
