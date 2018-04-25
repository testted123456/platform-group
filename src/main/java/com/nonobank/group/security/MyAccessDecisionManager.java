package com.nonobank.group.security;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.entity.db.RoleUrlPath;
import com.nonobank.group.repository.RoleUrlPathRepository;
import com.nonobank.group.util.IpAdrressUtil;
import org.apache.http.HttpException;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tangrubei on 2018/3/28.
 */
@Component
@Scope("prototype")
public class MyAccessDecisionManager
        implements org.springframework.security.access.AccessDecisionManager {

    private static Map<String, String> urlMap;


    public static final String NO_LOGIN = "no login";


    private static final String ANONYMOUS_USER = "anonymousUser";


    private static final String ROLE_ADMIN = "Admin";

    private static final String SYSTEM = "GROUP";


    @Value("${ignore.urlPath}")
    String urlPathIgnore;


    @Value("${ignore.ip}")
    String ipIgnore;




    @Autowired
    RoleUrlPathRepository roleUrlPathRepository;


    public void initUrlMap() {

        List<RoleUrlPath> roleUrlPaths = roleUrlPathRepository.findBySystemEqualsAndOptstatusNot(SYSTEM, (short) 2);
        if (roleUrlPaths == null || roleUrlPaths.size() == 0) {
            return ;
        }
        urlMap = new HashMap<>();
        roleUrlPaths.forEach(roleUrlPath -> {
            urlMap.put(roleUrlPath.getUrlPath(), roleUrlPath.getRoleName());
        });


    }





    public boolean checkIgnore(String value, String ignoreConf) {
        if (ignoreConf == null || ignoreConf.length() == 0) {
            return false;
        }
        String[] igonres = ignoreConf.split(",");
        for (String ingore : igonres) {
            if (value.equals(ingore) || value.endsWith(ingore)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否匿名用户
     *
     * @param authentication
     * @return
     */
    public static boolean isAnonymous(Authentication authentication) {
        if (authentication == null) {
            return true;
        } else {
            return authentication.getPrincipal().toString().equals(ANONYMOUS_USER);

        }
    }


    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
    	
    	if(((FilterInvocation) object).getRequest().getMethod().equals("OPTIONS")){
        	return;
        }

//        判断ip是否需要忽略
        String ip = IpAdrressUtil.getIpAdrress(((FilterInvocation) object).getRequest());
        if (checkIgnore(ip, ipIgnore)) {
            return;
        }

//        判断url是否需要忽略
        String url = ((FilterInvocation) object).getRequestUrl();
        if (url.indexOf("?") != -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (checkIgnore(url, urlPathIgnore)) {
            return;
        }

//      匿名用户即为非法访问
        if (isAnonymous(authentication)) {
            throw new AccessDeniedException(NO_LOGIN);
        }

//      获取urlmap
        if (urlMap == null || urlMap.keySet().size() == 0) {
            throw new AccessDeniedException("");
        }

//        判断是否有相应的角色
        String needRole = (String) urlMap.get(url);
        
        if(null == needRole){
        	return;
        }
        
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            if (ga.getAuthority().equals(ROLE_ADMIN) || needRole.trim().equals(ga.getAuthority().trim()) || needRole.trim().contains(ga.getAuthority().trim())) {
                return;
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
