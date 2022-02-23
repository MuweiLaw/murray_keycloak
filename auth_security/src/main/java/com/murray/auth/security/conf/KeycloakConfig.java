package com.murray.auth.security.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author Murray Law
 * KeycloakConfigResolver并注入Spring IoC，有两种实现方式,此处为方式一
 * 复用Spring Boot Adapter配置
 * 直接复用Spring Boot的配置形式，先声明Spring Boot的KeycloakConfigResolver实现
 */
@KeycloakConfiguration
public class KeycloakConfig {
    /**
     * 复用spring boot 的方法
     *
     * @return the keycloak config resolver
     */
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * 自己写解析
     *
     * @return the keycloak config resolver
     */

    @Bean
    public KeycloakConfigResolver fileKeycloakConfigResolver() {
        return request -> {
            // json 文件放到resources 文件夹下
            ClassPathResource classPathResource = new ClassPathResource("./keycloak.json");
            AdapterConfig adapterConfig = null;
            try {
                adapterConfig = new ObjectMapper().readValue(classPathResource.getFile(), AdapterConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return KeycloakDeploymentBuilder.build(adapterConfig);
        };
    }

}
