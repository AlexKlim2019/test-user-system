package com.test.user.system.user.service.dataaccess.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "database-connection")
public class DatabaseConnectionProperties {
    private List<DatabaseProps> dataSources;

    @Getter
    @Setter
    public static class DatabaseProps {
        private String name;
        private String url;
        private String table;
        private String user;
        private String password;
        private ColumnMapping mapping;
    }

    @Getter
    @Setter
    public static class ColumnMapping {
        private String id;
        private String username;
        private String name;
        private String surname;
    }
}
