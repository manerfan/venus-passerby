package com.manerfan.venus

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/**
 * @author manerfan
 * @date 2018/5/24
 */

@SpringBootApplication
class App

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
            .title("Venus")
            .description("全国城市列表查询服务")
            .contact(Contact("ManerFan", "https://github.com/manerfan/venus-passerby", "manerfan.china@gmail.com"))
            .version("1.0.0")
            .build()

    @Bean
    fun docket(): Docket = Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.manerfan.venus"))
            .paths(PathSelectors.any())
            .build()
}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}