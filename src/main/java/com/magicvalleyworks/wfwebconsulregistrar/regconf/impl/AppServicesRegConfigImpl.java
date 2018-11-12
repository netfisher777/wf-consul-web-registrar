package com.magicvalleyworks.wfwebconsulregistrar.regconf.impl;

import com.magicvalleyworks.wfwebconsulregistrar.context.api.ConsulRegistrationContext;
import com.magicvalleyworks.wfwebconsulregistrar.regconf.api.AppServicesRegConfig;
import com.magicvalleyworks.wfwebconsulregistrar.regconf.api.WebServicesRegConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class AppServicesRegConfigImpl implements AppServicesRegConfig {
    private WebServicesRegConfig webServicesRegConfig;
    private static final Logger logger = LoggerFactory.getLogger(AppServicesRegConfigImpl.class);

    @Inject
    private ConsulRegistrationContext consulRegistrationContext;

    @PostConstruct
    private void initialize() {
        loadWebServicesRegistrationConfiguration();
    }

    private void loadWebServicesRegistrationConfiguration() {
        String webServicesConfigurationFileName = consulRegistrationContext.getWebServicesConfigurationFileName();
        if (webServicesConfigurationFileName != null) {
            this.webServicesRegConfig = loadObjectFromJsonFileInClasspath(webServicesConfigurationFileName, WebServicesRegConfig.class);
        } else {
            logger.warn("ConsulRegistrationContext doesn't contain web services configuration file name. Nothing to load");
        }
    }

    private <T> T loadObjectFromJsonFileInClasspath(String filename, Class<T> type) {
        T deserializedObject = null;
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream != null) {
                logger.info(String.format("File with name %s was found in classpath. Trying to load", filename));
                Jsonb jsonb = JsonbBuilder.create();
                deserializedObject = jsonb.fromJson(inputStream, type);
                logger.info(String.format("Data from %s was successfully loaded", filename));
            } else {
                logger.warn(String.format("File with name %s was not found in classpath. Nothing to load", filename));
            }
        } catch (JsonbException ex) {
            logger.error(String.format("JsonbException: Can't serialize data from %s", filename), ex);
        } catch (IOException ex) {
            logger.error(String.format("IOException: Can't load data from %s", filename), ex);
        }
        return deserializedObject;
    }

    @Override
    public WebServicesRegConfig getWebServicesRegConfig() {
        return webServicesRegConfig;
    }
}
