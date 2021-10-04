//package io.security.springsecurity.aopsecurity;
//
//import io.security.springsecurity.aopsecurity.liveaop.AopLiveMethodService;
//import io.security.springsecurity.aopsecurity.method.AopMethodService;
//import io.security.springsecurity.aopsecurity.pointcut.AopPointcutService;
//import io.security.springsecurity.security.aop.CustomMethodSecurityInterceptor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.ClassUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class AopSecurityController {
//
//    private final AopMethodService aopMethodService;
//    private final AopPointcutService aopPointcutService;
//    private final AopLiveMethodService aopLiveMethodService;
//    private final MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;
//    private final AnnotationConfigServletWebServerApplicationContext applicationContext;
//    private final CustomMethodSecurityInterceptor methodSecurityInterceptor;
//
//    @GetMapping("/method")
//    public String methodTest(){
//        aopMethodService.methodTest();
//        return "method";
//    }
//
//    @GetMapping("/method2")
//    public String methodTest2(){
//        log.debug(aopMethodService.getClass().getSimpleName());
//        aopMethodService.methodTest2(aopMethodService);
//        return "method2";
//    }
//
//    @GetMapping("/method3")
//    public String methodTest3(){
//        aopMethodService.methodTest3();
//        return "method3";
//    }
//
//    @GetMapping("/aop1")
//    public String aopFirstService(){
//        aopPointcutService.aopService();
//        return "aop1";
//    }
//
//    @GetMapping("/liveaop")
//    public String liveAopService(){
//        aopLiveMethodService.liveAopService();
//        return "aop/liveaop";
//    }
//
//    @GetMapping("/addAop")
//    public void addPointcut(String fullName, String roleName) throws Exception {
//
//        String expression = "execution(* io.security.corespringsecurity.aopsecurity.liveaop.*Service.*(..))";
//        List<ConfigAttribute> attr = Arrays.asList(new SecurityConfig("ROLE_MANAGER"));
//        Map<String, List<ConfigAttribute>> pointcutMap = new LinkedHashMap<>();
//        pointcutMap.put(expression,attr);
////        protectPoitcutPostProcessor.setPointcutMap(pointcutMap);
//
//    }
//}
