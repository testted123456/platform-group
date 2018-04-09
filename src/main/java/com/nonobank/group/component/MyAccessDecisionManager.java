package com.nonobank.group.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tangrubei on 2018/3/28.
 */
@Component
public class MyAccessDecisionManager
        implements AccessDecisionManager {

    private static Map urlMap;

    static {
        urlMap = new HashMap();
        urlMap.put("/index", "ROLE_Admin");
        urlMap.put("/login", "permitAll");

    }


    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if(authentication.isAuthenticated()){

        }

        if (configAttributes == null) {
            return;
        }

        String url = ((FilterInvocation) object).getRequestUrl();
        if (url.indexOf("?") != -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        String needRole = (String) urlMap.get(url);
        if ("permitAll".equals(needRole)) {
            return;
        } else {
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {

        return true;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;

    }


}
