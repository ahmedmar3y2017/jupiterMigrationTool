package com.asset.jupiter.JUPITERMEDIA.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(
        entityManagerFactoryRef = "anotherEntityManagerFactory",
        transactionManagerRef = "anotherTransactionManager",
        basePackages = {"com.asset.jupiter.JUPITERMEDIA.Dao"})
public class AnotherRepositoryConfig {
    @Autowired
    JpaVendorAdapter jpaVendorAdapter;

    @Value("${med.datasource.url}")
    private String databaseUrl;

    @Value("${med.datasource.username}")
    private String username;

    @Value("${med.datasource.password}")
    private String password;

    @Value("${med.datasource.driver-class-name}")
    private String driverClassName;


    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernatedialect;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String hibernateformat_sql;
    @Value("${spring.jpa.show-sql}")
    private String jpashowsql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Value("${med.datasource.tomcat.max-wait}")
    private String max_wait;
    @Value("${med.datasource.tomcat.max-active}")
    private String max_active;
    @Value("${med.datasource.tomcat.max-idle}")
    private String max_idle;
    @Value("${med.datasource.tomcat.min-idle}")
    private String min_idle;

    @Value("${med.datasource.hikari.connection-timeout}")
    private String connection_timeout;
    @Value("${med.datasource.hikari.maximum-pool-size}")
    private String maximum_pool_size;


//    @Value("${med.datasource.hibernate.dialect}")
//    private String dialect;

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(databaseUrl, username, password);
        Properties properties = new Properties();

//        med.datasource.hikari.connection-timeout=60000
//        med.datasource.hikari.maximum-pool-size=5
        properties.setProperty("med.datasource.hikari.connection-timeout", connection_timeout);
        properties.setProperty("med.datasource.hikari.maximum-pool-size", maximum_pool_size);

        properties.setProperty("spring.datasource.tomcat.max-wait", max_wait);
        properties.setProperty("spring.datasource.tomcat.max-active", max_active);
        properties.setProperty("spring.datasource.tomcat.max-idle", max_idle);
        properties.setProperty("spring.datasource.tomcat.min-idle", min_idle);

        dataSource.setConnectionProperties(properties);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean(name = "anotherEntityManager")
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Bean(name = "anotherEntityManagerFactory")

    public EntityManagerFactory entityManagerFactory() {
        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", dialect);
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("spring.jpa.properties.hibernate.dialect", hibernatedialect);
        properties.setProperty("spring.jpa.properties.hibernate.format_sql", hibernateformat_sql);
        properties.setProperty("spring.jpa.show-sql", jpashowsql);


        properties.setProperty("spring.jpa.hibernate.ddl-auto", ddl);


        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.asset.jupiter.JUPITERMEDIA.Model.Entities");   // <- package for entities
        emf.setPersistenceUnitName("anotherPersistenceUnit");
        emf.setJpaProperties(properties);
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean(name = "anotherTransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }
}
