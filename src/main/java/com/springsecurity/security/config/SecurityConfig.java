package com.springsecurity.security.config;

import com.springsecurity.security.common.FormWebAuthenticationDetailsSource;
import com.springsecurity.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Order(1)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final FormWebAuthenticationDetailsSource formWebAuthenticationDetailsSource;
    private final AuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler formAuthenticationFailureHandler;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new FormAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    /** 정적 파일에 대해 Filter 자체를 거치지 않게 하기 위함 (비용적 측면을 고려) */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/", "/users", "/login*")
            .permitAll()
            .antMatchers("/mypage")
            .hasRole("USER")
            .antMatchers("/messages")
            .hasRole("MANAGER")
            .antMatchers("/config")
            .hasRole("ADMIN")
            .anyRequest()
            .authenticated()

            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login_proc")
            .authenticationDetailsSource(formWebAuthenticationDetailsSource)
            .successHandler(formAuthenticationSuccessHandler)
            .failureHandler(formAuthenticationFailureHandler)
            .permitAll()

            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler);
    }
}
