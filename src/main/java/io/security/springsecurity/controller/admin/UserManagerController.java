package io.security.springsecurity.controller.admin;


import io.security.springsecurity.domain.dto.UserDto;
import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.domain.entity.Role;
import io.security.springsecurity.service.RoleService;
import io.security.springsecurity.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping(value = "/admin/users")
    public String getUsers(Model model) {
        List<Account> accounts = userService.getUsers();
        model.addAttribute("users", accounts);
        return "admin/user/list";
    }

    @PostMapping(value = "/admin/users")
    public String createUser(UserDto userDto) {

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(userDto, Account.class);
        userService.createUser(account);

        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/users/{id}")
    public String getUser(@PathVariable(value = "id") Long id, Model model) {
        UserDto userDto = userService.getUser(id);
        List<Role> roleList = roleService.getRoles();

        model.addAttribute("act", (id > 0) ? "modify" : "add");
        model.addAttribute("user", userDto);
        model.addAttribute("roleList", roleList);

        return "admin/user/detail";
    }

    @GetMapping(value = "/admin/users/delete/{id}")
    public String removeUser(@PathVariable(value = "id") Long id, Model model) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
