package org.opentides.config;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.bind.Evaluation;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.config.RuleBuilder;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.Domain;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.opentides.persistence.hibernate.MultiTenantConnectionProviderImpl;
import org.opentides.persistence.hibernate.MultiTenantIdentifierResolver;
import org.opentides.persistence.user.AuthenticationDaoJdbcImpl;
import org.opentides.util.MultitenancyUtil;
import org.opentides.web.security.MultitenantSessionFilter;

/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * AbstractInboundAuthenticationConfigurationProvider.java
 * May 15, 2015
 *
 */

/**
 * <p>
 * This is the default inbound configuration for the OCPSoft's Rewrite servlet
 * filter that will be responsible for parsing and rewriting the URL for user
 * authentication of multi-tenant applicants.
 * </p>
 * <p>
 * To make an Opentides3 application multi-tenant, this configuration provider
 * should be implemented and annotated with the Rewrite annotation
 * {@link RewriteConfiguration}. This provider will parse and extract the
 * account/tenant name from the following URL patterns:
 * 
 * <pre>
 * 1.) /{path};a={account}
 * </pre>
 * 
 * <pre>
 * 2.) {account}.{domain}.{tld}/{path}
 * </pre>
 * 
 * </p>
 * <p>
 * After extracting the account name, the filter will strip out the account name
 * from the URL and pass it to {@link AuthenticationDaoJdbcImpl} and will be
 * used to switch the database schema in user authentication .
 * </p>
 * <p>
 * Once authenticated, {@link MultiTenantIdentifierResolver} and
 * {@link MultiTenantConnectionProviderImpl} will be responsible for the schema
 * switching.
 * </p>
 * 
 * @author Jeric
 *
 * @deprecated This URL filter is now replaced by the more robust
 *             {@link MultitenantSessionFilter}. This new Spring Security filter
 *             also looks for the account name in the URL similar to this
 *             filter. However, it stores the account not only in the thread
 *             local but in the session as well. This ensures that the
 *             parameters survive the URL redirects/forwards.
 */

@Deprecated
public abstract class AbstractInboundAuthenticationConfigurationProvider extends
		HttpConfigurationProvider {

	protected static final Logger _log = Logger
			.getLogger(AbstractInboundAuthenticationConfigurationProvider.class);

	protected String loginPath = "/login";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ocpsoft.common.pattern.Weighted#priority()
	 */
	@Override
	public abstract int priority();

	/**
	 * An implementation of {@link Operation} that is responsible for setting
	 * the account name in the {@link MultitenancyUtil} parsed from the URL by
	 * the given rules.
	 * 
	 * @author Jeric
	 *
	 */
	protected static final class StoreTenantNameOperation implements Operation {
		@Override
		public void perform(final Rewrite event, final EvaluationContext context) {
			final String account = (String) Evaluation.property("account")
					.retrieve(event, context);
			_log.info("Setting tenant name " + account);
			MultitenancyUtil.setTenantName(account);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ocpsoft.rewrite.config.ConfigurationProvider#getConfiguration(java
	 * .lang.Object)
	 */
	@Override
	public final Configuration getConfiguration(final ServletContext context) {
		_log.info("Setting up configuration for Multitenancy.");
		final StoreTenantNameOperation op = new StoreTenantNameOperation();

		final Rule parameterRule = RuleBuilder
				.define()
				.when(Direction.isInbound().and(
						Path.matches(loginPath + "{*}{*}a={account}")))
				.perform(op);

		final Rule subDomainRule = RuleBuilder
				.define()
				.when(Direction.isInbound().and(
						Domain.matches("{account}.{*}.{*}").or(
								Domain.matches("{account}.{*}.{*}.{*}"))))
				.perform(op);

		// Rules for processing:
		// (1) Path matches are /path...a=<account> and <account>.domain.com
		final Configuration configuration = ConfigurationBuilder.begin()
				.addRule(parameterRule).addRule(subDomainRule);

		return configuration;
	}

	/**
	 * @param loginPath
	 *            the loginPath to set
	 */
	public void setLoginPath(final String loginPath) {
		this.loginPath = loginPath;
	}
}
