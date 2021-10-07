package io.security.springsecurity.security.voter;

import io.security.springsecurity.service.SecurityResourceService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        boolean isGranted = securityResourceService.getIpList()
            .stream()
            .anyMatch(ip -> ip.equals(remoteAddress));

        if (!isGranted) {
            throw new AccessDeniedException("Invalid Access");
        }

        return ACCESS_ABSTAIN;
    }
}

/*
ABSTAIN 을 넘겨줘야 다음 인증 과정으로 넘어간다
GRANTED 주는 경우엔 다음 인증 과정 전부 스킵
Voter Strategy -> AffirmativeBased 이기 때문!
 */