package io.security.springsecurity.controller.login;

import io.security.springsecurity.domain.entity.Account;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login(
        @RequestParam(required = false, defaultValue = "false") String error,
        @RequestParam(required = false, defaultValue = "no exception") String exception, Model model) {

        model.addAllAttributes(Map.of("error", error, "exception", exception));
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = getAuthenticationOfCurrentUser();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }

    @GetMapping("/denied")
    public String accessDeniedPage(@RequestParam(required = false) String exception, Model model) {
        Authentication authentication = getAuthenticationOfCurrentUser();
        Account account = (Account) authentication.getPrincipal();
        model.addAllAttributes(Map.of("username", account.getUsername(), "exception", exception));
        return "user/login/denied";
    }

    private Authentication getAuthenticationOfCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

/*
model.addAttribute() 두번을 한번으로 줄이기 위해 model.addAllAttributes(Map.of(k1, v1, k2, v2)) 로 넘기는 경우
view 단에서 param.k1, param.k2로 값을 받아와야 하는데 param 자체가 required=false 이므로 null 일 수 있다
둘다 null 로 넘어오는 경우에 Error resolving template [java.lang.NullPointerException] 에러 뜸

Map.of()를 사용하고 싶은 경우에는 defaultValue 를 줘서 th:if 통과하지 못 하도록 하면 됨
즉 error = false 아니더라도 통과하지 못 하게만 하면 되니까 숫자 0 넣어도 되고 등등 뭔지 알지

view 단에서 ${param.username} 으로 하면 안 나오고.. 그냥 ${username} 으로 하면 cannot resolve 'username' 뜨면서 값은 나온다!?
다른 건 @RequestParam 으로 받느냐 안 받느냐가 전부인데 뭐 때문에 안 뜨는걸까?
Map.of() 에다가 넣으면 뺄 때 param.~~ 로 쓰는게 아닌감?
 */