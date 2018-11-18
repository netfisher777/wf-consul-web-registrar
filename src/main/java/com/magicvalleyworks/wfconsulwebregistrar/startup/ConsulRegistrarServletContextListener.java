package com.magicvalleyworks.wfconsulwebregistrar.startup;

import com.magicvalleyworks.wfconsulwebregistrar.context.api.ConsulRegistrationContext;
import com.magicvalleyworks.wfconsulwebregistrar.reg.api.ConsulRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.function.Consumer;

public class ConsulRegistrarServletContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ConsulRegistrarServletContextListener.class);
    private static final String WS_REG_CONFIG_FILE_NAME_INIT_PARAM = "WEB_SERVICES_REGISTRATION_CONFIGURATION_FILE_NAME";
    private static final String CONSUL_HOST_OVERRIDE_SYSTEM_PROPERTY = "consul.host.default.override";
    private static final String CONSUL_PORT_OVERRIDE_SYSTEM_PROPERTY = "consul.port.default.override";

    @Inject
    private ConsulRegistrar consulRegistrar;

    @Inject
    private ConsulRegistrationContext consulRegistrationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Web services registration was started");
        try {
            ServletContext servletContext = sce.getServletContext();
            setupConsulRegistrationContext(servletContext);
            consulRegistrar.registerWebServices();
        } catch (Throwable ex) {
            logger.error("Can't register web services due to unexpected exception", ex);
        }

        logger.info("Web services registration was ended");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        consulRegistrar.deregisterWebServices();
    }

    // Must be called first in contextInitialized(). Assuming that all @PostConstruct methods will be lazy loaded
    private void setupConsulRegistrationContext(ServletContext servletContext) {
        setupFieldFromServletContextParams(servletContext, WS_REG_CONFIG_FILE_NAME_INIT_PARAM, consulRegistrationContext::setWebServicesConfigurationFileName);
        setupFieldFromSystemProperties(CONSUL_HOST_OVERRIDE_SYSTEM_PROPERTY, consulRegistrationContext::setConsulHostOverride);
        setupFieldFromSystemProperties(CONSUL_PORT_OVERRIDE_SYSTEM_PROPERTY, consulRegistrationContext::setConsulPortOverride);
        String contextPath = servletContext.getContextPath();
        if (contextPath != null) {
            logger.info(String.format("Was found context path for the application in ServletContext = %s", contextPath));
            consulRegistrationContext.setWebApplicationContextPath(contextPath);
        } else {
            logger.warn("Was not found context path for the application in ServletContext");
            throw new IllegalStateException();
        }

    }

    private void setupFieldFromServletContextParams(ServletContext servletContext, String initParam, Consumer<String> stringConsumer)  {
        String value = servletContext.getInitParameter(initParam);
        if (value != null) {
            logger.info(String.format("Was found %s parameter in servlet context with value = %s", initParam, value));
            stringConsumer.accept(value);
        } else {
            logger.warn(String.format("Was not found %s parameter in servlet context. Just ignore this if this is ok", initParam));
        }
    }

    private void setupFieldFromSystemProperties(String propertyName, Consumer<String> stringConsumer)  {
        String value = System.getProperty(propertyName);
        if (value != null) {
            logger.info(String.format("Was found %s property in system properties with value = %s", propertyName, value));
            stringConsumer.accept(value);
        } else {
            logger.warn(String.format("Was not found %s property in system properties. Just ignore this message if you have not configured it", propertyName));
        }
    }
}
