package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi //会自动开启配置，启动类不需要加任何注解
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return Docket是swagger全局配置对象
     */
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("标准接口")
                .apiInfo(apiInfo("Swagger2测试项目", "1.0"))
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select() // 指定扫描的包，不指定会扫描出 spring 框架的接口，指定错误会导致接口扫描不出来
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))//这里写的是API接口所在的包位置
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问原始地址：http:ip/swagger-ui/index.html
     * 访问添加好ui之后的地址： http:ip/doc.html
     *
     * @return
     */
    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title) // 文档标题
                .description("ZIME Online Judge") // 文档描述内容
                .termsOfServiceUrl("") // 服务Url
                .contact(new Contact("ZIME", "http://www.baidu.com", "")) // 作者信息
                .version(version) // 文档版本号
                .build();
    }
}
