package com.mochi.back;

import com.mochi.back.config.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})public class JimuBackApplication {

	private static final Logger logger = LoggerFactory.getLogger(JimuBackApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JimuBackApplication.class, args);
	}

}
