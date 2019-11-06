/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import rocks.process.security.model.Token;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TokenSecurityConfiguration {
    private DataSource dataSource;
    private JpaVendorAdapter vendorAdapter;
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public TokenSecurityConfiguration(DataSource dataSource, JpaVendorAdapter vendorAdapter, EntityManagerFactory entityManagerFactory) {
        this.dataSource = dataSource;
        this.vendorAdapter = vendorAdapter;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public EntityManager entityManagerSecurity(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("rocks.process.security.model");
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean.getNativeEntityManagerFactory().createEntityManager();
    }

    @Bean
    public SimpleJpaRepository<Token, String> tokenBlacklistRepository(){
        return new SimpleJpaRepository<>(Token.class, entityManagerSecurity());
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.putAll(entityManagerFactory.getProperties());
        properties.remove("hibernate.transaction.coordinator_class"); //Spring Data issue
        return properties;
    }
}
