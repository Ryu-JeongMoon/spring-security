package io.security.springsecurity.security.config;

import io.security.springsecurity.security.common.AjaxAuthenticationEntryPoint;
import io.security.springsecurity.security.filter.AjaxAuthenticationFilter;
import io.security.springsecurity.security.handler.AjaxAccessDeniedHandler;
import io.security.springsecurity.security.handler.AjaxAuthenticationFailureHandler;
import io.security.springsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import io.security.springsecurity.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
@Configuration
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;
    private final AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    private final AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    private final AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;
    private final AjaxAccessDeniedHandler ajaxAccessDeniedHandler;

    @Bean
    public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        AjaxAuthenticationFilter ajaxAuthenticationFilter = new AjaxAuthenticationFilter();
        ajaxAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxAuthenticationFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
        ajaxAuthenticationFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
        return ajaxAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider);
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
            .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

            .exceptionHandling()
            .authenticationEntryPoint(ajaxAuthenticationEntryPoint)
            .accessDeniedHandler(ajaxAccessDeniedHandler);
    }
}

/*
?????? ????????? ?????? ?????? ???????????? ?????????
SecurityConfig ??? ?????? ?????? ?????? ????????? ????????? ????????? ??????
AjaxSecurity ??? ??? ?????? ????????? ??? ???????????? ??????????????? @Configuration ??????????????? ?????????
????????? ?????? ??????????????? ???????????? ????????? ???????????? ?????? ??????
 */