package com.sparta.todo.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer globalHeader() {
        return (operation, handlerMethod) -> { //operation: API 작업(operation) 을 나타내는 객체 - 대개 HTTP 요청과 관련된 작업
            operation.addParametersItem(new Parameter() //addParametersItem: List 를 만들어 반환한 것에 Parameter 추가
                    .in(ParameterIn.HEADER.toString())//in(매개변수가 전달되는 위치)
                    .allowEmptyValue(false) //빈 값 비허용
                    .schema(new StringSchema().name("Refresh-Token"))//Refresh-Token 이라는 이름을 가진 문자열 스키마를 생성하여 정의
                    .name("Refresh-Token"));//이름이 두 번?
            return operation;
        };
    }
}
