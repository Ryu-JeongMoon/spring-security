package io.security.springsecurity.aopsecurity.method;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AopMethodService {

    public void methodTest() {
        log.debug("methodTest");
    }

    public void methodTest2(AopMethodService methodService) {
        methodService.innerCallMethodTest();
        log.debug("methodTest2");
    }

    public void methodTest3() {
        log.debug(this.getClass().getSimpleName());
        this.innerCallMethodTest();
        log.debug("methodTest2");
    }

    public void innerCallMethodTest() {
        log.debug("innerCallMethodTest");
    }

}
