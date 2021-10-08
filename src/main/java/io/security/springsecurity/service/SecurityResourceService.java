package io.security.springsecurity.service;

import io.security.springsecurity.domain.entity.AccessIp;
import io.security.springsecurity.repository.AccessIpRepository;
import io.security.springsecurity.repository.ResourcesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;
    private final AccessIpRepository accessIpRepository;

    public ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> getResourceMap() {
        ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> result = new ConcurrentHashMap<>();

        resourcesRepository.findAllResources()
            .forEach(re -> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                });
                result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
            });

        return result;
    }

    public ConcurrentHashMap<String, List<ConfigAttribute>> getMethodResourceMap() {
        ConcurrentHashMap<String, List<ConfigAttribute>> result = new ConcurrentHashMap<>();

        resourcesRepository.findAllMethodResources()
            .forEach(re -> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                });
                result.put(re.getResourceName(), configAttributes);
            });

        return result;
    }

    public ConcurrentHashMap<String, List<ConfigAttribute>> getPointCutResourceMap() {
        ConcurrentHashMap<String, List<ConfigAttribute>> result = new ConcurrentHashMap<>();

        resourcesRepository.findAllPointcutResources()
            .forEach(re -> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                });
                result.put(re.getResourceName(), configAttributes);
            });

        return result;
    }

    public List<String> getIpList() {
        return accessIpRepository.findAll()
            .stream()
            .map(AccessIp::getIpAddress)
            .collect(Collectors.toList());
    }
}
