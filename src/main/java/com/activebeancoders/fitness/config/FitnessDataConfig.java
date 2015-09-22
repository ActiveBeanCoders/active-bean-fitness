package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.util.Assert;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
public class FitnessDataConfig {

    @Value("${hibernate.url}") private String url;
    @Value("${hibernate.dialect}") private String dialect;
    @Value("${hibernate.username}") private String username;
    @Value("${hibernate.password}") private String password;
    @Value("${hibernate.show_sql}") private String showSql;
    @Value("${hibernate.format_sql}") private String formatSql;

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager bean = new HibernateTransactionManager(sessionFactory());
        return bean;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder.addAnnotatedClasses(getAnnotatedClasses().toArray(new Class<?>[getAnnotatedClasses().size()]));
        builder.setProperty("hibernate.dialect", getHibernateDialect());
        builder.setProperty("hibernate.show_sql", getHibernateShowSql());
        builder.setProperty("hibernate.format_sql", getHibernateFormatSql());
        builder.setProperty("hibernate.hbm2ddl.auto", getHibernateHbmToDdlAuto());
        builder.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        return builder.buildSessionFactory();
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(getHibernateDriverClass());
            dataSource.setIdleConnectionTestPeriod(600);
            dataSource.setMinPoolSize(getHibernateMinPoolSize());
            dataSource.setMaxPoolSize(getHibernateMaxPoolSize());
            dataSource.setInitialPoolSize(getHibernateInitialPoolSize());
            dataSource.setJdbcUrl(getHibernateUrl());
            dataSource.setUser(getHibernateUsername());
            dataSource.setPassword(getHibernatePassword());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Could not configure DataSource", e);
        }

        return dataSource;
    }

    public String getHibernateDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

    public Integer getHibernateInitialPoolSize() {
        return 1;
    }

    public Integer getHibernateMinPoolSize() {
        return 5;
    }

    public Integer getHibernateMaxPoolSize() {
        return 1;
    }

    public String getHibernateDialect() {
        return dialect;
    }

    public String getHibernateFormatSql() {
        return "false";
    }

    public String getHibernateHbmToDdlAuto() {
//        return "create";
        return "validate";
    }

    public String getHibernateSchemaUpdate() {
        return formatSql;
    }

    public String getHibernateShowSql() {
        return showSql;
    }

    public List<Class<?>> getAnnotatedClasses() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(Activity.class);
        return classes;
    }

    public Integer getIdleConnectionTestPeriod() {
        return 600;
    }

    public String getPreferredTestQuery() {
        return "select 1";
    }

    public String getHibernateUrl() {
        return url;
    }

    public String getHibernateUsername() {
        return username;
    }

    public String getHibernatePassword() {
        return password;
    }

    // protected methods
    // ````````````````````````````````````````````````````````````````````````

    @PostConstruct
    protected void init() {
        Assert.assertStringIsInitialized(url);
        Assert.assertStringIsInitialized(dialect);
        Assert.assertStringIsInitialized(username);
        Assert.assertStringIsInitialized(password);
        Assert.assertStringIsInitialized(showSql);
        Assert.assertStringIsInitialized(formatSql);
    }

}

