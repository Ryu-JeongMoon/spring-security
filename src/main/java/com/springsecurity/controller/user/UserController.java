package com.springsecurity.controller.user;


import com.springsecurity.domain.entity.Account;
import com.springsecurity.service.RoleService;
import com.springsecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/users")
    public String createUser() throws Exception {

        return "user/login/register";
    }

    @PostMapping(value = "/users")
    public String createUser(UserDto userDto) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(userDto, Account.class);
        userService.createUser(account);

        return "redirect:/";
    }
}
