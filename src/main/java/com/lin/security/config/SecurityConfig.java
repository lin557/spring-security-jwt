package com.lin.security.config;

import com.lin.security.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
// 开启角色注解 启用 JSR-250 安全控制注解
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
//@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

//    @Autowired
//    private final AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtRequestFilter authenticationJwtTokenFilter;
//    @Bean
//    public JwtRequestFilter authenticationJwtTokenFilter() {
//        return new JwtRequestFilter();
//    }

    /**
     * 认证管理器，登录的时候参数会传给 authenticationManager
     *
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 由于spring boot3.0废弃了extends WebSecurityConfigurerAdapter 的方式，所以这里采用添加@Bean新方式
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 关闭csrf, 默认会开启CSRF处理，判断请求是否携带了token，如果没有就拒绝访问。并且，在请求为(GET|HEAD|TRACE|OPTIONS)时，则不会开启
                .csrf()
                .disable()
                .authorizeHttpRequests()
                // 配置路径是否需要认证 放行登录接口 /api/** 放行所有 放行的，不会经过过滤器 authenticationJwtTokenFilter
                .requestMatchers("/api/auth/**")
                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                // 不通过Session获取SecurityContext
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .authenticationProvider(authenticationProvider)
                .authenticationManager(authenticationManager(authenticationConfiguration))
                //此处为添加jwt过滤
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        http.headers().frameOptions().disable();
        return http.build();
    }

    /**
     *跨域资源配置
     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource()
//    {
//        final CorsConfiguration configuration = new CorsConfiguration();
//
//        //此处发现如果不加入自己的项目地址，会被拦截。
//        configuration.setAllowedOriginPatterns(List.of("http://localhost:8083"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
//        configuration.setAllowCredentials(true);
//
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }

}
