package com.mochi.back;

import com.mochi.back.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class JimuBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimuBackApplication.class, args);
    }

}
