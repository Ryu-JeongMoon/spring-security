package io.security.springsecurity.security.aop;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMethodSecurityInterceptor extends AbstractSecurityInterceptor implements MethodInterceptor {

    private final MethodSecurityMetadataSource securityMetadataSource;

    public Class<?> getSecureObjectClass() {
        return MethodInvocation.class;
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        InterceptorStatusToken token = super.beforeInvocation(mi);

        Object result;
        try {
            result = mi.proceed();
        } finally {
            super.finallyInvocation(token);
        }
        return super.afterInvocation(token, result);
    }

    public MethodSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
