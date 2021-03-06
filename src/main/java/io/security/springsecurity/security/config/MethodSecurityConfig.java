package io.security.springsecurity.security.config;

import io.security.springsecurity.security.aop.CustomMethodSecurityInterceptor;
import io.security.springsecurity.security.enums.SecurityMethodType;
import io.security.springsecurity.security.factory.MethodResourcesMapFactoryBean;
import io.security.springsecurity.security.processor.ProtectPointcutPostProcessor;
import io.security.springsecurity.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
//@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private SecurityResourceService securityResourceService;
//    private final CustomMethodSecurityInterceptor customMethodSecurityInterceptor;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }

    @Bean
    public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean() {
        MethodResourcesMapFactoryBean methodResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
        methodResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        methodResourcesMapFactoryBean.setResourceType(SecurityMethodType.METHOD.getValue());
        return methodResourcesMapFactoryBean;
    }

//    @Bean
    //@Profile("pointcut")
    /*BeanPostProcessor protectPointcutPostProcessor() throws Exception {

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();

        Class<?> clazz = Class.forName("org.springframework.security.config.method.ProtectPointcutPostProcessor");
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(MapBasedMethodSecurityMetadataSource.class);
        declaredConstructor.setAccessible(true);
        Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
        Method setPointcutMap = instance.getClass().getMethod("setPointcutMap", Map.class);
        setPointcutMap.setAccessible(true);
        setPointcutMap.invoke(instance, pointcutResourcesMapFactoryBean().getObject());

        return (BeanPostProcessor)instance;
    }*/

    /**
     * ????????????????????? ?????? ???????????? ????????? ?????? ????????? ?????? ????????? ???????????? ????????? ?????? ????????? ???????????? ???????????? ?????? ?????? AspectJ ????????????????????? Fix ?????? ?????? ????????? ??????????????? ?????? ?????? ????????? ??????????????? ??????
     * ??????????????? ???
     */
    @Bean
    @Profile("pointcut")
    public ProtectPointcutPostProcessor protectPointcutPostProcessor() {

        ProtectPointcutPostProcessor protectPointcutPostProcessor = new ProtectPointcutPostProcessor(
            mapBasedMethodSecurityMetadataSource());
        protectPointcutPostProcessor.setPointcutMap(pointcutResourcesMapFactoryBean().getObject());

        return protectPointcutPostProcessor;
    }

    @Bean
    @Profile("pointcut")
    public MethodResourcesMapFactoryBean pointcutResourcesMapFactoryBean() {

        MethodResourcesMapFactoryBean pointcutResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
        pointcutResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        pointcutResourcesMapFactoryBean.setResourceType(SecurityMethodType.POINTCUT.getValue());
        return pointcutResourcesMapFactoryBean;
    }

    @Bean
    public CustomMethodSecurityInterceptor customMethodSecurityInterceptor(
        MapBasedMethodSecurityMetadataSource methodSecurityMetadataSource) {
        CustomMethodSecurityInterceptor customMethodSecurityInterceptor = new CustomMethodSecurityInterceptor(
            methodSecurityMetadataSource);
        customMethodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        customMethodSecurityInterceptor.setAfterInvocationManager(afterInvocationManager());
        RunAsManager runAsManager = runAsManager();

        if (runAsManager != null) {
            customMethodSecurityInterceptor.setRunAsManager(runAsManager);
        }

        return customMethodSecurityInterceptor;
    }
}