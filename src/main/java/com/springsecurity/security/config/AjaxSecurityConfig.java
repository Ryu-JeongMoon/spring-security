package com.springsecurity.security.config;

import com.springsecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import com.springsecurity.security.filter.AjaxLoginProcessingFilter;
import com.springsecurity.security.handler.AjaxAccessDeniedHandler;
import com.springsecurity.security.handler.AjaxAuthenticationFailureHandler;
import com.springsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import com.springsecurity.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(0)
@Configuration
@RequiredArgsConstructor
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider authenticationProvider;
    private final AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    private final AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    private final AjaxLoginAuthenticationEntryPoint ajaxLoginAuthenticationEntryPoint;
    private final AjaxAccessDeniedHandler ajaxAccessDeniedHandler;

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
        return ajaxLoginProcessingFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(ajaxLoginAuthenticationEntryPoint)
                .accessDeniedHandler(ajaxAccessDeniedHandler);
    }


}
