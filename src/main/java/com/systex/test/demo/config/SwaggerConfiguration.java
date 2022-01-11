package com.systex.test.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Jerry Cheng
 * @CreateTime: 2022-01-01 19:11
 * Swagger 配置類
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurationSupport {
    /**
     * apis():指定掃描的介面
     *  RequestHandlerSelectors:設定要掃描介面的方式
     *       basePackage:指定要掃描的包
     *       any:掃面全部
     *       none:不掃描
     *       withClassAnnotation:掃描類上的註解(引數是類上註解的class物件)
     *       withMethodAnnotation:掃描方法上的註解(引數是方法上的註解的class物件)
     */
    /**
     * paths():過濾路徑
     *  PathSelectors:設定過濾的路徑
     *      any:過濾全部路徑
     *      none:不過濾路徑
     *      ant:過濾指定路徑:按照按照Spring的AntPathMatcher提供的match方法進行匹配
     *      regex:過濾指定路徑:按照String的matches方法進行匹配
     */
    @Bean
    public Docket createRestApi(){
        //設定要設定的Swagger環境
        return new Docket(DocumentationType.SWAGGER_2) // DocumentationType.SWAGGER_2 固定的，代表swagger2
                .apiInfo(apiInfo()) // 用於生成API資訊
                .enable(true)//設定是否啟動swagger,預設為true
                .select() // select()函式返回一個ApiSelectorBuilder例項,用來控制介面被swagger做成文件
                .apis(RequestHandlerSelectors.basePackage("com.systex.test")) // 用於指定掃描哪個Package下的介面
                .paths(PathSelectors.regex("(?!/index).+")) // 選擇所有的API,若想只為部分API生成文件，可以配置這裡
                .build();
    }
    private ApiInfo apiInfo(){
        //設定Swagger資訊
        return new ApiInfoBuilder()
                .title("標題: Spring Boot中使用Swagger2構建RESTFul APIs") // 可以用來自定義API的主標題
                .description("相關說明") // 可以用來描述整體的API
                .termsOfServiceUrl("com.systex.test.demo") // 用於定義服務的域名
                .version("1.0") // 可以用來定義版本。
                .build();
    }
}