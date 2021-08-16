package com.springsecurity.security.provider;

import com.springsecurity.security.common.FormWebAuthenticationDetails;
import com.springsecurity.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        /** authentication 에 getName() or getPrincipal() 둘 다 이름 가져오는 듯 */
        System.out.println("authentication.getName() = " + authentication.getName());
        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());

        AccountContext ac = (AccountContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, ac.getAccount().getPassword()))
            throw new BadCredentialsException("유효하지 않은 비밀번호!");

        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = details.getSecretKey();

        if (secretKey == null || !"secret".equals(secretKey))
            throw new InsufficientAuthenticationException("invalid secret key");

        return new UsernamePasswordAuthenticationToken(ac.getAccount(), null, ac.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
