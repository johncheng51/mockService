package com.jm.config;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(postPaths())
             .build();
            }

    private Predicate<String> postPaths(){
        return Predicates.or(regex("/api.*"));
    }
 
    
   
    
    private ApiInfo apiInfo() {
            String description = "Model Mock MICRO SERVICE";
            return new ApiInfoBuilder()
                    .title("Hibernate Mocking DB")
                    .description(description)
                    .license("John Cheng")
                    .licenseUrl("")
                    .version("1.0")
                    .build();
        }


}
