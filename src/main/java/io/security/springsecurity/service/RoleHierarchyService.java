package io.security.springsecurity.service;

import io.security.springsecurity.domain.entity.RoleHierarchy;
import io.security.springsecurity.repository.RoleHierarchyRepository;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    public String findAllHierarchy() {

        Iterator<RoleHierarchy> it = roleHierarchyRepository.findAll().iterator();
        StringBuffer roles = new StringBuffer();

        while (it.hasNext()) {
            RoleHierarchy model = it.next();
            if (StringUtils.hasText(model.getParentName().toString())) {
                roles.append(model.getParentName().getChildName()).append(" > ").append(model.getChildName()).append("\n");
            }
        }

        return roles.toString();
    }
}
