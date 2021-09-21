package io.security.springsecurity.service;

import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(Account account) {
        userRepository.save(account);
    }
}
