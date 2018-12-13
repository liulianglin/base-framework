package com.ldzhn.baseframework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author : llliu
 * @Despriction : swaager配置文件，引入swagger，方便进行测试后台的restful形式的接口
 * @Date : create on 2018/8/1 13:45
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hstc.resultpush.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("APP应用分析后台接口")
                .description("rest api 文档构建利器")
                .termsOfServiceUrl("http://www.baidu.com")
                .contact("llliu")
                .version("1.0")
                .build();
    }


}
