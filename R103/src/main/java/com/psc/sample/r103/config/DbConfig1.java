package com.psc.sample.r103.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
@Configuration
@ConfigurationProperties(prefix = "spring.db1.datasource")
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory1",
       transactionManagerRef = "transactionManager1",
        basePackages = {"com.psc.sample.r103.db1"})   // repository
public class DbConfig1 extends HikariConfig {

    @Bean
    @Primary
    public DataSource dataSource1() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    @Bean(name="entityManagerFactory1")
    @Primary
    public EntityManagerFactory entityManagerFactory1() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(this.dataSource1());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto", "update",
                "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect",
                "hibernate.show_sql", "true"
        ));


        factory.setPackagesToScan("com.psc.sample.r103.db1"); // domain
        factory.setPersistenceUnitName("db1");
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager1() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory1());
        return tm;
    }
}