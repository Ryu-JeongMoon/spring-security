package io.security.springsecurity.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        SavedRequest cacheRequest = requestCache.getRequest(request, response);
        if (cacheRequest != null) {
            String redirectUrl = cacheRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}

/*
if - else 없이 아래와 같이 하니까 Cannot call sendRedirect() after the response has been committed 뜸!
redirectStrategy가 바로 보내버리는 건줄 알았는데 아닌가봄;;
if (cacheRequest != null) {
    String redirectUrl = cacheRequest.getRedirectUrl();
    redirectStrategy.sendRedirect(request, response, redirectUrl);
}
redirectStrategy.sendRedirect(request, response, "/");
 */