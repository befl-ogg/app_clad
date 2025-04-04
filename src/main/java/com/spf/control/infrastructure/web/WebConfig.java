package com.spf.control.infrastructure.web;

import com.spf.control.infrastructure.filter.CustomHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

public class WebConfig {

    @Bean
    public FilterRegistrationBean<CustomHeadersFilter> cacheControlFilter() {
        FilterRegistrationBean<CustomHeadersFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setFilter(new CustomHeadersFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
