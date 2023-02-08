package com.lin.security.filter;

import com.lin.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    /**
     * 从 Authorization 标头中，提取令牌
     *
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(BEARER.length());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 密钥
            String secret = "secret";
            String jwt = parseJwt(request);
            if (jwt != null && JwtUtils.verifyToken(jwt, secret)) {
                // 这部份信息 实际可以从 这里读取 但服务重启丢失 最好存到 其他内存库中 如 redis mongodb
                // 用户信息 request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
                String sessionId = JwtUtils.getAudience(jwt);
                List<String> authorities=new ArrayList<>();
                authorities.add("YourController:YourMethod");
                // 上面是示例 最好从库中读取
                List<GrantedAuthority> authoritiesList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[]{}));
                // 如果令牌存在(存在逻辑自己编写)，则加载令牌 权限等信息可以从 redis/sql/mongodb 等库中读取 防止服务重启 数据丢失
                UserDetails userDetails = User.builder().username(sessionId).password("empty").authorities(authoritiesList).build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(sessionId, sessionId, authoritiesList);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

                System.out.println("认证成功" + sessionId);
            }
        } catch (Exception e) {
            log.error("无法设置用户认证:{}", e);
        }

        filterChain.doFilter(request, response);
    }
}
