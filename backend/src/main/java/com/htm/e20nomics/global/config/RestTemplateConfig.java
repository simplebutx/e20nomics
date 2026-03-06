package com.htm.e20nomics.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// RestTemplate = 자바에서 HTTP 요청을 보내는 도구
// 브라우저에서는 fetch나 axios쓰는 것 처럼