package io.security.springsecurity.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class FormAccessDeniedHandler implements AccessDeniedHandler {

    private final String errorPage = "/denied";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

        String deniedUrl = String.format("%s?exception=%s", errorPage, accessDeniedException.getMessage());
        response.sendRedirect(deniedUrl);
    }
}
