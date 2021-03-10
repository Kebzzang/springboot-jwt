package com.keb.jwt.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.keb.jwt.config.auth.PrincipalDetails;
import com.keb.jwt.model.User;
import com.keb.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//시큐리티가 필터를 가지고 있는데, 그 필터 중에 BasicAuthenticationFilter 가 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때. 위 필터를 무조건 타게 되어있음.
//만약에 권한이나 인증이 필요한 주소가 아니라면, 이 필터를 통과하지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository=userRepository;

    }
    //인증, 권한 필요한 주소 요청ㅇ시 이 필터를 통과함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청!!!");
        String jwtHeader=request.getHeader("Authorization");
        System.out.println("jwtHeader: "+jwtHeader);//

        //header가 비었는지, 베어러로 시작하지 않는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return ;
        }
        //이제 JWT 토큰을 검증해서 정상적인 사용자인지 확인해야 함
        String jwtToken=request.getHeader("Authorization").replace("Bearer ", "");
        String username= JWT.require(Algorithm.HMAC256("kebzzang")).build().verify(jwtToken).getClaim("username").asString();
        //서명이 제대로 됐다는 것 -> 유저네임이 제대로 들어왔다는 것 널이 아니라는 것
        if(username != null){
            User userEntity=userRepository.findByUsername(username); //레퍼지토리에 있으면?
            PrincipalDetails principalDetails=new PrincipalDetails(userEntity);

            //JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어줌
            Authentication authentication=
                    new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
            //강제로 시큐리티의 세션에 접근해 authentication 객체를 저장장
           SecurityContextHolder.getContext().setAuthentication(authentication);
           chain.doFilter(request, response);
        }
    }
}
