package com.zelu.authorizecode.confige;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author wangqiang
 * @Date: 2021/9/16 09:43
 */
@Configuration
public class TemplateConfige {
    //RestTemplate的3种REST-Client的封装
    @Bean("urlConnection")
    public RestTemplate urlConnectionRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        return restTemplate;
    }

//    @Bean("httpClient")
//    public RestTemplate httpClientRestTemplate(){
//        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//        return restTemplate;
//    }

//    @Bean("OKHttp3")
//    public RestTemplate OKHttp3RestTemplate(){
//        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
//        return restTemplate;
//    }
}
