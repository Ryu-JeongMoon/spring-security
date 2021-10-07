package io.security.springsecurity.security.init;

import io.security.springsecurity.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    private final SecurityResourceService securityResourceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        securityResourceService;securityResourceService
    }
}
