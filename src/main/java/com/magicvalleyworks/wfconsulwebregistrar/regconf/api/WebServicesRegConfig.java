package com.magicvalleyworks.wfconsulwebregistrar.regconf.api;

import java.util.List;

public class WebServicesRegConfig {
    private List<WebServiceRegConfig> webServiceRegistrationConfigurations;

    public List<WebServiceRegConfig> getWebServiceRegistrationConfigurations() {
        return webServiceRegistrationConfigurations;
    }

    public void setWebServiceRegistrationConfigurations(List<WebServiceRegConfig> webServiceRegistrationConfigurations) {
        this.webServiceRegistrationConfigurations = webServiceRegistrationConfigurations;
    }
}
