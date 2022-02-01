package com.psc.sample.r103.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@ConfigurationProperties(prefix = "spring.db2.datasource")
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory2",
        transactionManagerRef = "transactionManager2",
        basePackages = {"com.psc.sample.r103.db2"})
public class DbConfig2 extends HikariConfig {

    @Bean(name = "dataSource2")
    public DataSource dataSource2() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    @Bean(name="entityManagerFactory2")
    public EntityManagerFactory entityManagerFactory2() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(this.dataSource2());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto", "update",
                "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect",
                "hibernate.show_sql", "true"
        ));



        factory.setPackagesToScan("com.psc.sample.r103.db2");
        factory.setPersistenceUnitName("db2");
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager2() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory2());
        return tm;
    }
}
