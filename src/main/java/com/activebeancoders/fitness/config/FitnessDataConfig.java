package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.entity.Activity;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories // TODO: not ready yet, need to generate metamodel
public class FitnessDataConfig {

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
        return "org.hibernate.dialect.MySQLDialect";
    }

    public String getHibernateFormatSql() {
        return "false";
    }

    public String getHibernateHbmToDdlAuto() {
//        return "create";
        return "validate";
    }

    public String getHibernateSchemaUpdate() {
        return "false";
    }

    public String getHibernateShowSql() {
        return "true";
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
        return "jdbc:mysql://localhost:3306/active_bean_fitness";
    }

    public String getHibernateUsername() {
        return "root";
    }

    public String getHibernatePassword() {
        return "";
    }
}

