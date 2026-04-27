package com.test.api.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configurations/config.properties")
public interface Configuration extends Config {

    @Key("baseUrl")
    String baseUrl();

    @Key("basePath")
    String basePath();
}