package com.withme.api.config;

import com.fasterxml.classmate.TypeResolver;
import com.withme.api.controller.dto.ExceptionResponseDto;
import com.withme.api.controller.dto.MyPageResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
                .additionalModels(
                        typeResolver.resolve(ExceptionResponseDto.class)
                        , typeResolver.resolve(MyPageResponseDto.class)
                )
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(this.securityContext()))
                .securitySchemes(Arrays.asList(this.apiKey()))
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.withme.api.controller"))
                    .paths(PathSelectors.any())
                    .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(this.defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
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
