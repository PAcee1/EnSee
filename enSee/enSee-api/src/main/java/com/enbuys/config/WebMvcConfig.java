package com.enbuys.config;

import com.enbuys.utils.RedisOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:D:/Projects/EnSee-vedios/");
    }

    @Bean
    public MyFilter myFilter(){
        return new MyFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyFilter())
                .addPathPatterns("/user/getUserInfo","/user/uploadFace","/user/logout","/user/addFollowFans","/user/reduceFollowFans")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/report/**")
                .addPathPatterns("/comment/save")
                .addPathPatterns("/video/upload");
        super.addInterceptors(registry);
    }
}
