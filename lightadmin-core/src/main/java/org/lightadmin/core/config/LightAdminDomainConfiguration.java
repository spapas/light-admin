package org.lightadmin.core.config;

import org.lightadmin.core.config.bootstrap.GlobalAdministrationConfigurationProcessor;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.management.rmi.DataManipulationService;
import org.lightadmin.core.config.management.rmi.DataManipulationServiceImpl;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementServiceImpl;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.context.support.ServletContextResourceLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class LightAdminDomainConfiguration {

    @Autowired
    private Environment environment;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ServletContextResourceLoader servletContextResourceLoader;

    @Bean
    public DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver() {
        return new JpaDomainTypeEntityMetadataResolver(entityManager);
    }

    @Bean
    @Autowired
    public DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory(TransactionInterceptor transactionInterceptor) {
        return new DynamicJpaRepositoryFactory(entityManager, transactionInterceptor);
    }

    @Bean
    @Autowired
    public DomainConfigurationSourceFactory domainConfigurationSourceFactory(AutowireCapableBeanFactory beanFactory) {
        return new DomainConfigurationSourceFactory(domainTypeEntityMetadataResolver(), beanFactory);
    }

    @Bean
    public DomainConfigurationSourceValidatorFactory domainConfigurationSourceValidatorFactory() {
        return new DomainConfigurationSourceValidatorFactory(domainTypeEntityMetadataResolver(), servletContextResourceLoader);
    }

    @Bean
    @Autowired
    public DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigFactory(DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory, DomainTypeEntityMetadataResolver entityMetadataResolver) {
        return new DomainTypeAdministrationConfigurationFactory(dynamicJpaRepositoryFactory, entityMetadataResolver);
    }

    @Bean
    @Autowired
    public GlobalAdministrationConfiguration globalAdministrationConfiguration(DomainTypeAdministrationConfigurationFactory domainTypeConfigurationFactory) {
        return new GlobalAdministrationConfiguration(domainTypeConfigurationFactory);
    }

    @Bean
    public GlobalConfigurationManagementService globalConfigurationManagementService() {
        return new GlobalConfigurationManagementServiceImpl();
    }

    @Bean
    public DataManipulationService dataManipulationService() {
        return new DataManipulationServiceImpl();
    }

    @Bean
    @Autowired
    public GlobalAdministrationConfigurationProcessor globalAdministrationConfigurationProcessor(DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory, AutowireCapableBeanFactory beanFactory) {
        return new GlobalAdministrationConfigurationProcessor(domainTypeAdministrationConfigurationFactory, domainConfigurationSourceFactory(beanFactory), domainConfigurationSourceValidatorFactory(), environment);
    }
}
