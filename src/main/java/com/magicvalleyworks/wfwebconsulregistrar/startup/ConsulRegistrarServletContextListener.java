package com.magicvalleyworks.wfwebconsulregistrar.startup;

import com.magicvalleyworks.wfwebconsulregistrar.context.api.ConsulRegistrationContext;
import com.magicvalleyworks.wfwebconsulregistrar.reg.api.ConsulRegistrar;
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
    private static final String CONSUL_HOST_OVERRIDE_INIT_PARAM = "CONSUL_HOST_OVERRIDE";
    private static final String CONSUL_PORT_OVERRIDE_INIT_PARAM = "CONSUL_PORT_OVERRIDE";

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
        setupFieldFromServletContextParams(servletContext, CONSUL_HOST_OVERRIDE_INIT_PARAM, consulRegistrationContext::setConsulHostOverride);
        setupFieldFromServletContextParams(servletContext, CONSUL_PORT_OVERRIDE_INIT_PARAM, consulRegistrationContext::setConsulPortOverride);
        String contextPath = servletContext.getContextPath();
        if (contextPath != null) {
            logger.info(String.format("Was found context path for the application in ServletContext = %s", contextPath));
            consulRegistrationContext.setWebApplicationContextPath(contextPath);
        } else {
            logger.warn("Was not found context path for the application in ServletContext");
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
}
