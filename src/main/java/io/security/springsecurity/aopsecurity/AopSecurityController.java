package io.security.springsecurity.aopsecurity;

import io.security.springsecurity.aopsecurity.liveaop.AopLiveMethodService;
import io.security.springsecurity.aopsecurity.method.AopMethodService;
import io.security.springsecurity.aopsecurity.pointcut.AopPointcutService;
import io.security.springsecurity.domain.dto.AccountDto;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AopSecurityController {

    private final AopMethodService aopMethodService;
    private final AopPointcutService aopPointcutService;
    private final AopLiveMethodService aopLiveMethodService;
//    private final MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;
//    private final AnnotationConfigServletWebServerApplicationContext applicationContext;
//    private final CustomMethodSecurityInterceptor methodSecurityInterceptor;

    @GetMapping("/pre-authorize")
    @PreAuthorize("hasRole('ROLE_USER') and #account.username == principal.username")
    public String preAuthorize(AccountDto account, Model model, Principal principal) {
        model.addAttribute("method", "success @PreAuthorize");
        return "aop/method";
    }

    @GetMapping("/method-secured")
    public String methodSecured(Model model) {
        aopMethodService.methodTest();
        model.addAttribute("method", "Success MethodSecured");
        return "aop/method";
    }

    @GetMapping("/method")
    public String methodTest() {
        aopMethodService.methodTest();
        return "method";
    }

    @GetMapping("/method2")
    public String methodTest2() {
        log.debug(aopMethodService.getClass().getSimpleName());
        aopMethodService.methodTest2(aopMethodService);
        return "method2";
    }

    @GetMapping("/method3")
    public String methodTest3() {
        aopMethodService.methodTest3();
        return "method3";
    }

    @GetMapping("/aop1")
    public String aopFirstService() {
        aopPointcutService.aopService();
        return "aop1";
    }

    @GetMapping("/liveaop")
    public String liveAopService() {
        aopLiveMethodService.liveAopService();
        return "aop/liveaop";
    }

    @GetMapping("/addAop")
    public void addPointcut(String fullName, String roleName) throws Exception {

        String expression = "execution(* io.security.corespringsecurity.aopsecurity.liveaop.*Service.*(..))";
        List<ConfigAttribute> attr = List.of(new SecurityConfig("ROLE_MANAGER"));
        Map<String, List<ConfigAttribute>> pointcutMap = new LinkedHashMap<>();
        pointcutMap.put(expression, attr);
//        protectPoitcutPostProcessor.setPointcutMap(pointcutMap);

    }
}
