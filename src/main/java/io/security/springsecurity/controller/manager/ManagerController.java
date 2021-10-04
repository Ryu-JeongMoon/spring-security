package io.security.springsecurity.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ManagerController {

    @GetMapping(value="/messages")
    public String mypage() {
        return "manager/manager";
    }

    @ResponseBody
    @GetMapping(value = "/api/messages")
    public String apiMessage() {
        return "messages ok";
    }
}
