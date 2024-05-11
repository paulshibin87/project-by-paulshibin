package com.paulshibin.boutiquebackend;

import com.paulshibin.boutiquebackend.security.config.userConfig.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class BoutiqueBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoutiqueBackEndApplication.class, args);
    }

}
