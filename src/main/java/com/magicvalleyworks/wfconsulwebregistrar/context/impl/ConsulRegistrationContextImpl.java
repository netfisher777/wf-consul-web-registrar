package com.magicvalleyworks.wfconsulwebregistrar.context.impl;

import com.magicvalleyworks.wfconsulwebregistrar.context.api.ConsulRegistrationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.function.Consumer;

@ApplicationScoped
public class ConsulRegistrationContextImpl implements ConsulRegistrationContext {
    private static final Logger logger = LoggerFactory.getLogger(ConsulRegistrationContextImpl.class);
    private static final String WS_REG_CONFIG_FILE_NAME_INIT_PARAM = "WEB_SERVICES_REGISTRATION_CONFIGURATION_FILE_NAME";
    private static final String CONSUL_HOST_OVERRIDE_SYSTEM_PROPERTY = "consul.host.default.override";
    private static final String CONSUL_PORT_OVERRIDE_SYSTEM_PROPERTY = "consul.port.default.override";
    private static final String IGNORE_EXCEPTIONS_ON_STARTUP_SYSTEM_PROPERTY = "wfconsulwebregistrar.startup.ignoreexceptions";

    @Inject
    private ServletContext servletContext;

    private String webServicesConfigurationFileName;
    private String consulHostOverride;
    private String consulPortOverride;
    private String webApplicationContextPath;
    private Boolean ignoreExceptionsOnStartup;

    @PostConstruct
    private void initialize() {
        setupStringFieldFromServletContextParams(WS_REG_CONFIG_FILE_NAME_INIT_PARAM, this::setWebServicesConfigurationFileName);
        setupStringFieldFromSystemProperties(CONSUL_HOST_OVERRIDE_SYSTEM_PROPERTY, this::setConsulHostOverride);
        setupStringFieldFromSystemProperties(CONSUL_PORT_OVERRIDE_SYSTEM_PROPERTY, this::setConsulPortOverride);
        setupBooleanFieldFromSystemProperties(IGNORE_EXCEPTIONS_ON_STARTUP_SYSTEM_PROPERTY, this::setIgnoreExceptionsOnStartup);
        String contextPath = this.servletContext.getContextPath();
        if (contextPath != null) {
            logger.info(String.format("Was found context path for the application in ServletContext = %s", contextPath));
            this.setWebApplicationContextPath(contextPath);
        } else {
            logger.warn("Was not found context path for the application in ServletContext");
            throw new IllegalStateException();
        }
    }

    private void setupStringFieldFromServletContextParams(String initParam, Consumer<String> stringConsumer) {
        String value = this.servletContext.getInitParameter(initParam);
        if (value != null) {
            logger.info(String.format("Was found %s parameter in servlet context with value = %s", initParam, value));
            stringConsumer.accept(value);
        } else {
            logger.warn(String.format("Was not found %s parameter in servlet context. Just ignore this if this is ok", initParam));
        }
    }

    private void setupStringFieldFromSystemProperties(String propertyName, Consumer<String> stringConsumer) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            logger.info(String.format("Was found %s property in system properties with value = %s", propertyName, value));
            stringConsumer.accept(value);
        } else {
            logger.warn(String.format("Was not found %s property in system properties. Just ignore this message if you have not configured it", propertyName));
        }
    }

    private void setupBooleanFieldFromSystemProperties(String propertyName, Consumer<Boolean> stringConsumer) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            logger.info(String.format("Was found %s property in system properties with value = %s", propertyName, value));
            stringConsumer.accept(Boolean.valueOf(value));
        } else {
            logger.warn(String.format("Was not found %s property in system properties. Just ignore this message if you have not configured it", propertyName));
        }
    }

    @Override
    public String getWebServicesConfigurationFileName() {
        return webServicesConfigurationFileName;
    }

    private void setWebServicesConfigurationFileName(String fileName) {
        this.webServicesConfigurationFileName = fileName;
    }

    @Override
    public String getConsulHostOverride() {
        return consulHostOverride;
    }

    private void setConsulHostOverride(String host) {
        this.consulHostOverride = host;
    }

    @Override
    public String getConsulPortOverride() {
        return consulPortOverride;
    }

    private void setConsulPortOverride(String port) {
        this.consulPortOverride = port;
    }

    @Override
    public String getWebApplicationContextPath() {
        return webApplicationContextPath;
    }

    private void setWebApplicationContextPath(String contextPath) {
        this.webApplicationContextPath = contextPath;
    }

    @Override
    public Boolean getIgnoreExceptionsOnStartup() {
        return ignoreExceptionsOnStartup;
    }

    public void setIgnoreExceptionsOnStartup(Boolean ignoreExceptionsOnStartup) {
        this.ignoreExceptionsOnStartup = ignoreExceptionsOnStartup;
    }
}
