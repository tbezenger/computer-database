package com.excilys.formation.tbezenger.cdb.springconfig;

import java.util.Collections;
import java.util.Properties;

import com.excilys.formation.tbezenger.cdb.model.Company;
import com.excilys.formation.tbezenger.cdb.model.Computer;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hibernate.cfg.Environment.*;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.excilys.formation.tbezenger.cdb.model")
public class HibernateConf {

    @Autowired
    private Environment env;

    @Bean
    public EntityManager createEntityManager() {

        MutablePersistenceUnitInfo mutablePersistenceUnitInfo = new MutablePersistenceUnitInfo() {
            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };

        mutablePersistenceUnitInfo.setPersistenceUnitName("persistence");
        mutablePersistenceUnitInfo.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName());

        Properties props = new Properties();
        props.put("hibernate.connection.driver", env.getProperty("jdbc.driverClassName"));
        props.put("hibernate.connection.url", env.getProperty("jdbc.url"));
        props.put("hibernate.connection.username", env.getProperty("jdbc.username"));
        props.put("hibernate.connection.password", env.getProperty("jdbc.password"));

        mutablePersistenceUnitInfo.setProperties(props);

        mutablePersistenceUnitInfo.addManagedClassName(Computer.class.getName());
        mutablePersistenceUnitInfo.addManagedClassName(Company.class.getName());

        PersistenceUnitDescriptor persistenceUnitDescriptor = new PersistenceUnitInfoDescriptor(
                mutablePersistenceUnitInfo);

        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(
                persistenceUnitDescriptor, Collections.EMPTY_MAP);

        EntityManagerFactory entityManagerFactory = entityManagerFactoryBuilder.build();

        return entityManagerFactory.createEntityManager();
    }
}