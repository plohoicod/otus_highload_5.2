package ru.otus.highload.homework.fifth.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywaySlaveInitializer {

    @Value("${db.url}")
    String firstDatasourceUrl;
    @Value("${db.user}")
    String firstDatasourceUser;
    @Value("${db.password}")
    String firstDatasourcePassword;

    @Value("${db.url.second}")
    String secondDatasourceUrl;
    @Value("${db.user.second}")
    String secondDatasourceUser;
    @Value("${db.password.second}")
    String secondDatasourcePassword;

    @PostConstruct
    public void migrateFlyway() {
        Flyway flywayIntegration = Flyway.configure()
                .dataSource(firstDatasourceUrl, firstDatasourceUser, firstDatasourcePassword)
                .defaultSchema("otus_5")
                .load();


        Flyway flywayPhenom = Flyway.configure()
                .dataSource(secondDatasourceUrl, secondDatasourceUser, secondDatasourcePassword)
                .defaultSchema("otus_5")
                .load();

        flywayIntegration.migrate();
        flywayPhenom.migrate();
    }
}
