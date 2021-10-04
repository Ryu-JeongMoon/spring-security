package io.security.springsecurity.aopsecurity.liveaop;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AopLiveMethodService {

    public void liveAopService(){
      log.debug("LiveAopFirstService");
    }
}
