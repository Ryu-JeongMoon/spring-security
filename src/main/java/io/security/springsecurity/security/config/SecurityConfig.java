package io.security.springsecurity.security.config;

import io.security.springsecurity.security.common.FormAuthenticationDetailsSource;
import io.security.springsecurity.security.handler.FormAccessDeniedHandler;
import io.security.springsecurity.security.handler.FormAuthenticationFailureHandler;
import io.security.springsecurity.security.handler.FormAuthenticationSuccessHandler;
import io.security.springsecurity.security.provider.FormAuthenticationProvider;
import io.security.springsecurity.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Log4j2
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final FormAuthenticationProvider customAuthenticationProvider;
    private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    private final FormAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final FormAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final FormAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService);
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/login", "/accounts").permitAll()
            .antMatchers("/my-page").hasRole("USER")
            .antMatchers("/messages").hasRole("MANAGER")
            .antMatchers("/config").hasRole("ADMIN")
            .anyRequest().authenticated()

            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login_proc")
            .authenticationDetailsSource(formAuthenticationDetailsSource)
//            .defaultSuccessUrl("/")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .permitAll()

            .and()
            .exceptionHandling()
            .accessDeniedHandler(customAccessDeniedHandler);
    }
}

/*
loginProcessingUrl("/login_proc") -> process 담당할 url 지정해주는 것일뿐 구현할 필요 없다

successHandler, failureHandler 로 로그인 성공 또는 실패 후의 행동을 지정해주면 defaultSuccessUrl 은 의미가 없다
-> defaultSuccessUrl custom handler 사용하지 않을 때 필요

.antMatchers("/", "/login", "/accounts").permitAll()
/login 허용시켜줘야 오류 없이 바로 failureHandler 로 넘어갈 수 있다
허용 안 해주니까 param 없이 로그인 페이지 간 후 다시 한번 클릭해야 param 가지고 /login?~~로 이동함

AJAX 흐름
AjaxAuthenticationFilter
-> AjaxAuthenticationToken
-> AuthenticationManager
-> AuthenticationProvider
-> SuccessHandler or FailureHandler
== Authentication ==

-> FilterSecurityInterceptor 등등..
== Authorization ==

 */