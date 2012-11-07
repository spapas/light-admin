package org.lightadmin.core.config.management.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.reporting.ProblemReporterFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GlobalConfigurationManagementRMIService implements GlobalConfigurationManagementService {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainConfigurationSourceFactory domainConfigurationSourceFactory;

	@Autowired
	private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

	@Autowired
	private DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory;

	@Override
	@SuppressWarnings( "unchecked" )
	public void registerDomainTypeConfiguration( final ConfigurationUnits configurationUnits ) {
		final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource( configurationUnits );

		final DomainConfigurationSourceValidator configurationSourceValidator = configurationSourceValidatorFactory.getValidator();

		configurationSourceValidator.validate( configurationSource, ProblemReporterFactory.failFastReporter() );

		final DomainTypeAdministrationConfiguration administrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration( configurationSource );

		globalAdministrationConfiguration.registerDomainTypeConfiguration( administrationConfiguration );
	}

	@Override
	public void removeDomainTypeAdministrationConfiguration( final Class<?> domainType ) {
		globalAdministrationConfiguration.removeDomainTypeConfiguration( domainType );
	}

	@Override
	public void removeAllDomainTypeAdministrationConfigurations() {
		globalAdministrationConfiguration.removeAllDomainTypeAdministrationConfigurations();
	}
}