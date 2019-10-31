package com.service.guarantee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.service.guarantee")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(Application.class);
    	application.run(args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	setRegisterErrorPageFilter(false);
    	return builder.sources(Application.class);
    }
}
