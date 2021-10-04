package io.security.springsecurity.service;

import io.security.springsecurity.domain.entity.Resources;
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

    public ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> getResourceMap() {
        ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> result = new ConcurrentHashMap<>();

        resourcesRepository.findAllResources()
            .forEach(re -> {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                re.getRoleSet().forEach(role -> {
                    configAttributes.add(new SecurityConfig(role.getRoleName()));
                    result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
                });
            });

        return result;
    }
}