package com.mju.management.global.config.jwtinterceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;
    private final Environment environment;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] activeProfiles = environment.getActiveProfiles();
        if(activeProfiles.length > 0 && !activeProfiles[0].equals("test"))
            registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**");
    }
}