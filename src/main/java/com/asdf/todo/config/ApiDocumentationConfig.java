package com.asdf.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 스프링 컨텍스트에 등록된 컨트롤러 빈을 대상으로 대상으로 문서 생성
@Configuration
public class ApiDocumentationConfig {

    @Bean
    public OpenAPI apiDocumentation() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("TODO List API")
                                .version("2.0")
                                .description("Spring Boot3을 이용한 TODO List API 문서"));
    }
}
