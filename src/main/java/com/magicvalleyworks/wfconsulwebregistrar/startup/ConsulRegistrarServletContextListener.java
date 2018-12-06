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

    @Inject
    private ConsulRegistrar consulRegistrar;

    @Inject
    private ConsulRegistrationContext consulRegistrationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Web services registration was started");
        try {
            consulRegistrar.registerWebServices();
        } catch (Throwable ex) {
            Boolean ignoreExceptionsOnStartup = consulRegistrationContext.getIgnoreExceptionsOnStartup();
            ignoreExceptionsOnStartup = ignoreExceptionsOnStartup == null ? false : ignoreExceptionsOnStartup;
            logger.error("Can't register web services due to unexpected exception", ex);
            if (!ignoreExceptionsOnStartup) {
                throw ex;
            }
        }

        logger.info("Web services registration was ended");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        consulRegistrar.deregisterWebServices();
    }
}
