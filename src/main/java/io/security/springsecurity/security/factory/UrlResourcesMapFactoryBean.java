package io.security.springsecurity.security.factory;

import io.security.springsecurity.service.SecurityResourceService;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlResourcesMapFactoryBean implements FactoryBean<ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private SecurityResourceService securityResourceService;
    private ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap = new ConcurrentHashMap<>();

    @Override
    public ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (!resourceMap.isEmpty()) {
            resourceMap = securityResourceService.getResourceMap();
        }
        return resourceMap;
    }

    @Override
    public Class<?> getObjectType() {
        return ConcurrentHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
