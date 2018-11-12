package com.magicvalleyworks.wfwebconsulregistrar.serversettings.impl;

import com.magicvalleyworks.wfwebconsulregistrar.serversettings.api.CurrentServerNodeSettings;

import javax.enterprise.context.ApplicationScoped;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class CurrentServerNodeSettingsWildflyImpl implements CurrentServerNodeSettings {
    @Override
    public String getWebServiceHost() {
        return getPlatformMBeanServerAttributeValue(String.class, "jboss.ws:service=ServerConfig", "WebServiceHost");
    }

    @Override
    public Integer getWebServicePort() {
        return  getPlatformMBeanServerAttributeValue(Integer.class, "jboss.ws:service=ServerConfig", "WebServicePort");
    }

    private <T> T getPlatformMBeanServerAttributeValue(Class<T> valueType, String objectName, String attributeName) {
        T attributeValue = null;

        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            Object attributeObject = mBeanServer.getAttribute(new ObjectName(objectName), attributeName);

            attributeValue = attributeObject != null ? (valueType.cast(attributeObject)) : null;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        return attributeValue;
    }
}
