package com.hankuper.securedapi;

import com.hankuper.securedapi.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SecuredApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuredApiApplication.class, args);
    }

}
