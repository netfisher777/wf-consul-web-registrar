package com.magicvalleyworks.wfwebconsulregistrar.regconf.api;

import java.util.List;

public class WebServiceRegConfig {
    private String name;
    private List<String> tags;
    private Meta meta;
    private String httpHealthCheckPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getHttpHealthCheckPath() {
        return httpHealthCheckPath;
    }

    public void setHttpHealthCheckPath(String httpHealthCheckPath) {
        this.httpHealthCheckPath = httpHealthCheckPath;
    }

    public static class Meta {
        private String path;

        public String getPath() {
            return path;
        }

        public Meta setPath(String path) {
            this.path = path;
            return this;
        }
    }
}
