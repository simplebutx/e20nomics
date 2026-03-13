package com.htm.e20nomics.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().removeIf(
                converter -> converter instanceof StringHttpMessageConverter
        );
        restTemplate.getMessageConverters().add(    //utf-8로 설정
                0, new StringHttpMessageConverter(StandardCharsets.UTF_8)
        );

        return restTemplate;
    }
}

// RestTemplate = 자바에서 HTTP 요청을 보내는 도구
// 브라우저에서는 fetch나 axios쓰는 것 처럼