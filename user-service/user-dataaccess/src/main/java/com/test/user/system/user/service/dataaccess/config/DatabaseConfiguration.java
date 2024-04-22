package com.test.user.system.user.service.dataaccess.config;

import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.DatabaseProps;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.test.user.system.user.service.dataaccess",
        entityManagerFactoryRef = "multiEntityManager",
        transactionManagerRef = "multiTransactionManager"
)
@EntityScan(basePackages = "com.test.user.system.user.service.dataaccess")
public class DatabaseConfiguration {
    private final String PACKAGE_SCAN = "com.test.user.system.user.service.dataaccess";

    private final ConfigurableListableBeanFactory beanFactory;

    private final DatabaseConnectionProperties properties;
    private final ApplicationContext applicationContext;

    @Bean
    public List<DataSource> dataSources() {
        return properties.getDataSources().stream()
                .map(this::buildAndRegisterDataSource)
                .collect(Collectors.toList());
    }

    @Primary
    @Bean(name = "multiRoutingDataSource")
    public DataSource multiRoutingDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<DataSource> dataSources = dataSources();
        properties.getDataSources()
                .forEach(databaseProps -> targetDataSources.put(
                        databaseProps.getName(),
                        applicationContext.getBean(databaseProps.getName()))
                );
        MultiRoutingDataSource multiRoutingDataSource = new MultiRoutingDataSource();
        multiRoutingDataSource.setDefaultTargetDataSource(dataSources.get(0));
        multiRoutingDataSource.setTargetDataSources(targetDataSources);
        return multiRoutingDataSource;
    }

    @Bean(name = "multiEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(multiRoutingDataSource());
        entityManager.setPackagesToScan(PACKAGE_SCAN);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        entityManager.setJpaProperties(hibernateProperties());
        return entityManager;
    }

    @Bean(name = "multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                multiEntityManager().getObject());
        return transactionManager;
    }

    @Primary
    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean dbSessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(multiRoutingDataSource());
        sessionFactoryBean.setPackagesToScan(PACKAGE_SCAN);
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        return sessionFactoryBean;
    }

    private DataSource buildAndRegisterDataSource(DatabaseProps props) {
        var dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName("org.postgresql.Driver")
                .url(props.getUrl())
                .username(props.getUser())
                .password(props.getPassword())
                .build();
        beanFactory.registerSingleton(props.getName(), dataSource);
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.id.new_generator_mappings", false);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", true);
        return properties;
    }
}
