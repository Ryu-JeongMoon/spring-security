package io.security.springsecurity.service;

import io.security.springsecurity.domain.dto.UserDto;
import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.domain.entity.Role;
import io.security.springsecurity.repository.RoleRepository;
import io.security.springsecurity.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(Account account) {

        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setUserRoles(roles);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userRepository.save(account);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {

        Account account = userRepository.findById(id).orElse(new Account());
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(account, UserDto.class);

        List<String> roles = account.getUserRoles()
            .stream()
            .map(role -> role.getRoleName())
            .collect(Collectors.toList());

        userDto.setRoles(roles);
        return userDto;
    }

    @Transactional(readOnly = true)
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
