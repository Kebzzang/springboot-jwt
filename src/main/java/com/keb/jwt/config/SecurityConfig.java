package com.keb.jwt.config;

import com.keb.jwt.config.jwt.JwtAuthenticationFilter;
import com.keb.jwt.config.jwt.JwtAuthorizationFilter;
import com.keb.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 x 스테이트리스
        .and()
        .addFilter(corsFilter) //@CrossOrigin(인증x) , 시큐리티 필터에 등록 인증ㅇ
        .formLogin().disable() //폼 태그로 로그인하는 것 안쓰겠다!
        .httpBasic().disable() //기본적인 http 로그인 방식 안쓰겠다  배리어 방식 쓰겠다.
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) //AuthenticationManager를 던져줘야 함
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
        .authorizeRequests()
        .antMatchers("/api/v1/user")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll();

    }
}
