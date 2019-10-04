package com.asset.jupiter.JUPITER.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.asset.jupiter.JUPITER.Dao"})
public class RepositoryConfig {
    @Autowired
    JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    DataSource dataSource;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernatedialect;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String hibernateformat_sql;
    @Value("${spring.jpa.show-sql}")
    private String jpashowsql;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Bean(name = "entityManager")
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("spring.jpa.properties.hibernate.dialect", hibernatedialect);
        properties.setProperty("spring.jpa.properties.hibernate.format_sql", hibernateformat_sql);
        properties.setProperty("spring.jpa.show-sql", jpashowsql);
        properties.setProperty("spring.jpa.hibernate.ddl-auto", ddl);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.asset.jupiter.JUPITER.Model.Entities");
        emf.setPersistenceUnitName("default");   // <- giving 'default' as name
        emf.setJpaProperties(properties);   // set properties
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory());
        return tm;
    }
}