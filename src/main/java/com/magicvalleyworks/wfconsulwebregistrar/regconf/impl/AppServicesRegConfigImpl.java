package com.magicvalleyworks.wfconsulwebregistrar.regconf.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.magicvalleyworks.wfconsulwebregistrar.context.api.ConsulRegistrationContext;
import com.magicvalleyworks.wfconsulwebregistrar.regconf.api.AppServicesRegConfig;
import com.magicvalleyworks.wfconsulwebregistrar.regconf.api.WebServicesRegConfig;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
            this.webServicesRegConfig = loadObjectFromConfigurationFileInClasspath(webServicesConfigurationFileName, WebServicesRegConfig.class);
        } else {
            logger.warn("ConsulRegistrationContext doesn't contain web services configuration file name. Nothing to load");
        }
    }

    private <T> T loadObjectFromConfigurationFileInClasspath(String filename, Class<T> type) {
        T deserializedObject = null;
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream != null) {
                logger.info(String.format("File with name %s was found in classpath. Trying to load", filename));
                deserializedObject = loadObjectFromInputStreamByExtension(inputStream, filename, type);
            } else {
                logger.warn(String.format("File with name %s was not found in classpath. Nothing to load", filename));
            }
        } catch (Exception ex) {
            logger.error(String.format("Can't load data from %s", filename), ex);
        }
        return deserializedObject;
    }

    private <T> T loadObjectFromInputStreamByExtension(InputStream inputStream, String filename, Class<T> type) throws IOException {
        T result = null;
        String extension = FilenameUtils.getExtension(filename);
        if ("json".equals(extension.toLowerCase())) {
            Jsonb jsonb = JsonbBuilder.create();
            result = jsonb.fromJson(inputStream, type);
            logger.info(String.format("Data from %s was successfully loaded", filename));
        } else if (List.of("yaml", "yml").contains(extension.toLowerCase())) {
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            result = yamlMapper.readValue(inputStream, type);
            logger.info(String.format("Data from %s was successfully loaded", filename));
        } else {
            logger.info(String.format("Can't load data from %s. File extension is not supported", filename));
        }

        return result;
    }

    @Override
    public WebServicesRegConfig getWebServicesRegConfig() {
        return webServicesRegConfig;
    }
}
