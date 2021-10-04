package io.security.springsecurity.service;

import io.security.springsecurity.security.aop.CustomMethodSecurityInterceptor;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

@Service
@RequiredArgsConstructor
public class MethodSecurityService {

    private final MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;
    private final AnnotationConfigServletWebServerApplicationContext applicationContext;
    private final CustomMethodSecurityInterceptor methodSecurityInterceptor;

    public void addMethodSecured(String className, String roleName) throws Exception {

        int lastDotIndex = className.lastIndexOf(".");
        String methodName = className.substring(lastDotIndex + 1);
        String typeName = className.substring(0, lastDotIndex);
        Class<?> type = ClassUtils.resolveClassName(typeName, ClassUtils.getDefaultClassLoader());
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(type.getDeclaredConstructor().newInstance());
        proxyFactory.addAdvice(methodSecurityInterceptor);
        Object proxy = proxyFactory.getProxy();

        List<ConfigAttribute> attr = Arrays.asList(new SecurityConfig(roleName));
        mapBasedMethodSecurityMetadataSource.addSecureMethod(type, methodName, attr);

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getBeanFactory();
        registry.destroySingleton(beanName);
        registry.registerSingleton(beanName, proxy);

    }

    public void removeMethodSecured(String className) throws Exception {

        int lastDotIndex = className.lastIndexOf(".");
        String typeName = className.substring(0, lastDotIndex);
        Class<?> type = ClassUtils.resolveClassName(typeName, ClassUtils.getDefaultClassLoader());
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        Object newInstance = type.getDeclaredConstructor().newInstance();

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getBeanFactory();
        Object singleton = registry.getSingleton(beanName);
        registry.destroySingleton(beanName);
        registry.registerSingleton(beanName, newInstance);

    }
}
