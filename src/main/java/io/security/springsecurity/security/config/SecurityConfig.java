package io.security.springsecurity.security.config;

import io.security.springsecurity.security.common.FormAuthenticationDetailsSource;
import io.security.springsecurity.security.filter.PermitAllFilter;
import io.security.springsecurity.security.handler.FormAccessDeniedHandler;
import io.security.springsecurity.security.handler.FormAuthenticationFailureHandler;
import io.security.springsecurity.security.handler.FormAuthenticationSuccessHandler;
import io.security.springsecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.security.springsecurity.security.provider.FormAuthenticationProvider;
import io.security.springsecurity.security.service.CustomUserDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

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
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    private final List<String> permitAllResources = List.of("/", "/login", "/user/login/**");

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
        permitAllFilter.setAccessDecisionManager(new AffirmativeBased(List.of(new RoleVoter())));
        permitAllFilter.setAuthenticationManager(authenticationManagerBean());

        return permitAllFilter;
    }

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
            .antMatchers("/mypage").hasRole("USER")
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
            .accessDeniedHandler(customAccessDeniedHandler)

            .and()
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }
}

/*
loginProcessingUrl("/login_proc") -> process ????????? url ??????????????? ????????? ????????? ?????? ??????

successHandler, failureHandler ??? ????????? ?????? ?????? ?????? ?????? ????????? ??????????????? defaultSuccessUrl ??? ????????? ??????
-> defaultSuccessUrl custom handler ???????????? ?????? ??? ??????

.antMatchers("/", "/login", "/accounts").permitAll()
/login ?????????????????? ?????? ?????? ?????? failureHandler ??? ????????? ??? ??????
?????? ??? ???????????? param ?????? ????????? ????????? ??? ??? ?????? ?????? ???????????? param ????????? /login?~~??? ?????????

AJAX ??????
AjaxAuthenticationFilter
-> AjaxAuthenticationToken
-> AuthenticationManager
-> AuthenticationProvider
-> SuccessHandler or FailureHandler
== Authentication ==

-> FilterSecurityInterceptor ??????..
== Authorization ==

 */