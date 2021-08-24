package com.center.aurora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayDeque;
import java.util.Deque;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();


    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Aurora SNS API 문서")
                .description("Aurora SNS API 설명 문서입니다.")
//                .license("license")
//                .licenseUrl("licenseUrl")
//                .contact(new Contact("name", "url", "email"))
//                .termsOfServiceUrl("termsOfServiceUrl")
                .version("0.0.2")
                .build();
    }
}