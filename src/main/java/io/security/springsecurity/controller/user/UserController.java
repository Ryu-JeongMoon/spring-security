package io.security.springsecurity.controller.user;

import io.security.springsecurity.domain.dto.AccountDto;
import io.security.springsecurity.domain.entity.Account;
import io.security.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/my-page")
    public String myPage() {
        return "user/mypage";
    }

    @GetMapping("/accounts")
    public String createUserForm() {
        return "user/register";
    }

    @PostMapping("/accounts")
    public String createUser(AccountDto accountDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userService.createUser(account);
        return "redirect:/";
    }
}
