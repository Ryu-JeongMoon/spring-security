package io.security.springsecurity.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    @GetMapping(value="/manager")
    public String mypage() {
        return "manager/manager";
    }
}
