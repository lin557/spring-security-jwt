package com.lin.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 执行登录过程 可以跟数据库 关联
 */
public class SpringSecurityUtil {

    /**
     * 登录
     *
     * @param username    用户名
     * @param password    密码
     * @param authorities 权限
     */
    public static void login(String username, String password, List<String> authorities) {
        // 可以查下库，验证账号密码是否正确，再执行下面的代码
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        List<GrantedAuthority> authoritiesList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[]{}));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, authoritiesList);
        SecurityContextHolder.getContext().setAuthentication(token);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    public static String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    /**
     * 判断是否已登录
     *
     * @return
     */
    public static boolean isLogin() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

}
