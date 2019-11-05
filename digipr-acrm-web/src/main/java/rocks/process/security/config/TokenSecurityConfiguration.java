/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import rocks.process.security.model.Token;
import rocks.process.security.service.TokenService;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TokenSecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsServiceImpl;
    @Autowired
    private TokenSecurityProperties tokenSecurityProperties;
    @Autowired
    DataSource dataSource;
    @Autowired
    JpaVendorAdapter vendorAdapter;
    @Autowired
    private Environment env;

    @Bean
    public TokenService tokenService() {
        return new TokenService(tokenBlacklistRepository(), userDetailsServiceImpl, tokenSecurityProperties.getSecret());
    }

    @Bean
    public EntityManager entityManagerSecurity(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("rocks.process.security");
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
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "none"));
        return properties;
    }
}
