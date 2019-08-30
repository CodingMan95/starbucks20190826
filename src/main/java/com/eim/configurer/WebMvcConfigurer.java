package com.eim.configurer;

import com.eim.interceptor.CrossDomainInterceptor;
import com.eim.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //为所有接口统一配置请求头
        registry.addInterceptor(new CrossDomainInterceptor()).addPathPatterns("/**");
        //对接口进行拦截，鉴权通过则放行
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin-user/**", "/store-admin/**", "/active-admin/**");
    }

}
