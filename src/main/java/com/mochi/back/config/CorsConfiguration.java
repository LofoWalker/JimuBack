package com.mochi.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedOrigins("*")
                .allowedHeaders("origin", "content-type", "accept", "x-requested-with", "www-authenticate", "authorization", "rp")
                .exposedHeaders("www-authenticate")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
