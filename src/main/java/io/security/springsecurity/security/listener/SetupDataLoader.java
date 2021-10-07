package io.security.springsecurity.security.listener;

import io.security.springsecurity.domain.entity.AccessIp;
import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.domain.entity.Resources;
import io.security.springsecurity.domain.entity.Role;
import io.security.springsecurity.domain.entity.RoleHierarchy;
import io.security.springsecurity.repository.AccessIpRepository;
import io.security.springsecurity.repository.ResourcesRepository;
import io.security.springsecurity.repository.RoleHierarchyRepository;
import io.security.springsecurity.repository.RoleRepository;
import io.security.springsecurity.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static AtomicInteger count = new AtomicInteger(0);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessIpRepository accessIpRepository;
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
        setupAccessIpData();

        alreadySetup = true;
    }


    private void setupSecurityResources() {

        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);

        createResourceIfNotFound("/admin/**", "", roles, "url");
        createUserIfNotFound("admin", "pass", "admin@gmail.com", 10, roles);

        Set<Role> roles1 = new HashSet<>();
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        roles1.add(managerRole);

        createResourceIfNotFound("io.security.springsecurity.aopsecurity.method.AopMethodService.methodTest",
            "", roles1, "method");
        createResourceIfNotFound("io.security.springsecurity.aopsecurity.method.AopMethodService.innerCallMethodTest",
            "", roles1, "method");
        createResourceIfNotFound("execution(* io.security.springsecurity.aopsecurity.pointcut.*Service.*(..))",
            "", roles1, "pointcut");

        createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);
        createRoleHierarchyIfNotFound(managerRole, adminRole);

        Set<Role> roles3 = new HashSet<>();
        Role childRole1 = createRoleIfNotFound("ROLE_USER", "회원");
        roles3.add(childRole1);

        createResourceIfNotFound("/users/**", "", roles3, "url");
        createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);
        createRoleHierarchyIfNotFound(childRole1, managerRole);

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                .roleName(roleName)
                .roleDesc(roleDesc)
                .build();
        }

        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {
        Account account = userRepository.findByUsername(userName)
            .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        if (account == null) {
            account = Account.builder()
                .username(userName)
                .email(email)
                .age(age)
                .password(passwordEncoder.encode(password))
                .userRoles(roleSet)
                .build();
        }

        return userRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                .resourceName(resourceName)
                .roleSet(roleSet)
                .httpMethod(httpMethod)
                .resourceType(resourceType)
                .orderNum(count.incrementAndGet())
                .build();
        }
        return resourcesRepository.save(resources);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {
        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                .childName(parentRole.getRoleName())
                .build();
        }

        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());

        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                .childName(childRole.getRoleName())
                .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }

    private void setupAccessIpData() {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress("127.0.0.1");

        if (byIpAddress == null) {
            AccessIp accessIp = AccessIp.builder()
                .ipAddress("127.0.0.1")
                .build();
            accessIpRepository.save(accessIp);
        }
    }
}