package io.security.springsecurity.aopsecurity.pointcut;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AopPointcutService {

    public void aopService() {
        log.debug("AopFirstService");
    }
}
