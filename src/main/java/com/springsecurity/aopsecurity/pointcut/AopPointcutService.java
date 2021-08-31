package com.springsecurity.aopsecurity.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AopPointcutService {

    public void aopService(){
      log.debug("AopFirstService");
    }
}
