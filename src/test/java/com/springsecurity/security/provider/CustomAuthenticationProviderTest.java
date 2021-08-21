package com.springsecurity.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class CustomAuthenticationProviderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void passwordTest() {
        String input = "1234";
        String encodedPassword = passwordEncoder.encode(input);

        log.info("input = {}", input);
        log.info("encodedPassword = {}", encodedPassword);

        assertThat(passwordEncoder.matches(input, encodedPassword));
    }
}