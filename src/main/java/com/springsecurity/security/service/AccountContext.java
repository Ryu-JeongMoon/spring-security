package com.springsecurity.security.service;

import com.springsecurity.domain.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AccountContext extends User {

    @Autowired
    private final Account account;

    /** There is no PasswordEncoder mapped for the id "null" Error 시에 비밀번호 앞에 {noop} 이 붙어 있지 않아서! */
    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }
}
