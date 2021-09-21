package io.security.springsecurity.security.service;

import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("없는 회원!"));

        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

//        role 여러개일 때 사용
//        account.getUserRoles()
//            .forEach(userRole -> roles.add(new SimpleGrantedAuthority(userRole.toString())));

        return new AccountContext(account, roles);
    }
}
