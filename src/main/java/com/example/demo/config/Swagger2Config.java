package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
  * @Title:Swagger2Config.java
  * @Description:TODO
  * @Author:82322156@qq.com
  * @Date:2020年2月7日下午3:23:26
  * @Version:1.0
  * Copyright 2020  Internet  Products Co., Ltd.
*/
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户及人员管理中心接口文档")
                .description("使用RESTful风格的API，接口文档地址：http://localhost:8781/swagger-ui.html#")
                .termsOfServiceUrl("http://localhost:8087/swagger-ui.html#")
                .version("1.0")
                .build();
    }
}
