package com.efit.hrms.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Set max file size to 100MB
        factory.setMaxFileSize(DataSize.ofMegabytes(100));

        // Set max request size to 100MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));

        return factory.createMultipartConfig();
    }
}


