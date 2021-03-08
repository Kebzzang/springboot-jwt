package com.keb.jwt.config;


import com.keb.jwt.filter.MyFilter1;
import com.keb.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

@Configuration
public class FilterConfig {



    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean=new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); //모든 요청에서 필터를 걸어라
        bean.setOrder(1); //낮은 번호가 필터 중에서 가장 먼저 실행!!
        return bean;
    }
    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean=new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); //모든 요청에서 필터를 걸어라
        bean.setOrder(0); //낮은 번호가 필터 중에서 가장 먼저 실행!!
        return bean;
    }
}
