package com.magicvalleyworks.wfconsulwebregistrar.context.impl;

import com.magicvalleyworks.wfconsulwebregistrar.context.api.ConsulRegistrationContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsulRegistrationContextImpl implements ConsulRegistrationContext {
    private String webServicesConfigurationFileName;
    private String consulHostOverride;
    private String consulPortOverride;
    private String webApplicationContextPath;

    @Override
    public String getWebServicesConfigurationFileName() {
        return webServicesConfigurationFileName;
    }

    @Override
    public void setWebServicesConfigurationFileName(String fileName) {
        this.webServicesConfigurationFileName = fileName;
    }

    @Override
    public String getConsulHostOverride() {
        return consulHostOverride;
    }

    @Override
    public void setConsulHostOverride(String host) {
        this.consulHostOverride = host;
    }

    @Override
    public String getConsulPortOverride() {
        return consulPortOverride;
    }

    @Override
    public void setConsulPortOverride(String port) {
        this.consulPortOverride = port;
    }

    @Override
    public String getWebApplicationContextPath() {
        return webApplicationContextPath;
    }

    @Override
    public void setWebApplicationContextPath(String contextPath) {
        this.webApplicationContextPath = contextPath;
    }
}
