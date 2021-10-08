package io.security.springsecurity.security.factory;

import io.security.springsecurity.service.SecurityResourceService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

@Log4j2
public class MethodResourcesMapFactoryBean implements FactoryBean<ConcurrentHashMap<String, List<ConfigAttribute>>> {

    private SecurityResourceService securityResourceService;
    private String resourceType;
    private ConcurrentHashMap<String, List<ConfigAttribute>> resourcesMap;

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    public void init() {
        if ("method".equals(resourceType)) {
            resourcesMap = securityResourceService.getMethodResourceMap();
        } else if ("pointcut".equals(resourceType)) {
            resourcesMap = securityResourceService.getPointCutResourceMap();
        } else {
            log.error("resourceType must be 'method' or 'pointcut'");
        }
    }

    public ConcurrentHashMap<String, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    @SuppressWarnings("rawtypes")
    public Class<ConcurrentHashMap> getObjectType() {
        return ConcurrentHashMap.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
