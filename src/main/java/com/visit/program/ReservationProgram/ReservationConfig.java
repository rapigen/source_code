package com.visit.program.ReservationProgram;
import com.visit.program.ReservationProgram.domain.interceptor.LoginInterceptor;
import com.visit.program.ReservationProgram.domain.interceptor.MobileOrWebInterceptor;
import com.visit.program.ReservationProgram.domain.interceptor.SessionInterceptor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
public class ReservationConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/m/**","/reservation","/reservation/**","/reservation/info/**").order(1).excludePathPatterns("/reservation/info/all/rapigen_employee","/reservation/info/all/rapigen_security");
        registry.addInterceptor(new MobileOrWebInterceptor()).addPathPatterns("/**").order(2).excludePathPatterns("/");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/m/reservation/info/update/*","/m/reservation/info/delete/*","/reservation/info/update/*","/reservation/info/delete/*").order(3);
    }

    @Bean
    public ServletContextInitializer clearJsession(){
        return servletContext -> {
            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));

            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
            sessionCookieConfig.setHttpOnly(true);
        };
    }




}
