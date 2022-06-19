package com.withme.api.config;

import com.fasterxml.classmate.TypeResolver;
import com.withme.api.controller.dto.ExceptionResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
                .additionalModels(
                        typeResolver.resolve(ExceptionResponseDto.class)
                )
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.withme.api.controller"))
                    .paths(PathSelectors.any())
                    .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WithMe")
                .description("WithMe API")
                .version("0.1")
                .contact(new Contact("[WithMe] API Docs", "https://github.com/Team-WithMe", "hansh9501@naver.com"))
//                .termsOfServiceUrl("https://github.com/Team-WithMe")
                .build();
    }
}
