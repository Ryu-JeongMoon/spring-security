package io.security.springsecurity.security;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
public class SecurityConfigTest {

    @Test
    void logTest() {
        log.info("log.getClass() = {}", log.getClass());
    }
}