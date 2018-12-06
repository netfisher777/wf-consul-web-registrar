package com.magicvalleyworks.wfconsulwebregistrar.context.api;

public interface ConsulRegistrationContext {
    String getWebServicesConfigurationFileName();
    String getConsulHostOverride();
    String getConsulPortOverride();
    String getWebApplicationContextPath();
    Boolean getIgnoreExceptionsOnStartup();
}
