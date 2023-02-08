package com.lin.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// 开启角色注解 启用 JSR-250 安全控制注解
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    /**
     * 由于spring boot3.0废弃了extends WebSecurityConfigurerAdapter 的方式，所以这里采用添加@Bean新方式
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize-> {
                    try {
                        authorize
                                // 放行登录接口
                                .requestMatchers("/api/auth/login").permitAll()
                                // 放行资源目录
                                .requestMatchers("/static/**", "/resources/**").permitAll()
                                // 其余的都需要权限校验
                                .anyRequest().authenticated()
                                // 防跨站请求伪造
                                .and().csrf(csrf -> csrf.disable());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        ).build();
    }

}
