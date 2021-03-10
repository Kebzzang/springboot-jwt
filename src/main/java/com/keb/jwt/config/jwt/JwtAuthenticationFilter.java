package com.keb.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keb.jwt.config.auth.PrincipalDetails;
import com.keb.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;


// /login 요청해서 유저네임이랑 패스워드를 전송(post)
// 이 필터가 동작을 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;


    //login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도 중!!");

        try { //이 스트림 안에, 이 바이트 안에 유저네임과 패스워드가 담겨있다 !!
            /*BufferedReader br=request.getReader();

            String input=null;
            while((input=br.readLine()) !=null){
                System.out.println(input);
            }*/
            //유저, 패스워드 제이슨 데이터를 받아서 출력해보자
            ObjectMapper om=new ObjectMapper();
            User user=om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            //토큰을 만들자 (form 로그인하면 자동이지만..여기선 직접해야함..)
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨!! 정상이면 authentication이 리턴됨
            //디비에 있는 유저네임, 패스워드와 일치한다.
            Authentication authentication=  //이 authentication에는 로그인한 정보가 담긴다.
                    authenticationManager.authenticate(authenticationToken);
            // >로그인되었다 !
            PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal(); //다운캐스팅
            System.out.println("로그인 완료!!: "+principalDetails.getUser().getUsername());
            // authentication 객체가 세션영역에 저장 -> authentication 내 principal 객체를 가져와 출력이 되는 것 로그인이 되었다는 것
            //리턴 이유는 권한 관리를 시큐리티가 대신 해주시 때문에 편리해서...
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없는데 단지 권한 처리 때문에 세션에 넣어준당
            //JWT 토큰 만들기

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("================================");
        return null;
    }
    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다
    //JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication 이 실행됨: 인증이 완료되었다는 뜻 ");
        PrincipalDetails principalDetails=(PrincipalDetails) authResult.getPrincipal(); //다운캐스팅

        //HS256방식 (RSA 방식이 아님)
        String jwtToken= JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME)) //토큰 유효시간 정하기 -> 만료되면 다시 만들어주면 됨 여기선 10분!
                .withClaim("id", principalDetails.getUser().getId()) //비공개 클레임 내가 넣고 싶은 키밸류값 넣으면 됨
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET)); //서버만 알고 있는 시크릿 키!

//        super.successfulAuthentication(request, response, chain, authResult);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken); //헤더에 담겨 사용자에게 응답됨
    }
    //1. 유저네임 패스워드 받아서
    //2. 정상인지 로그인 시도 함  authenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출됨
    //3. 그럼 loadUserByUsername() 함수 실행됨
    //4. PrincipalDetails 를 세션에 담고 (권한관리를 위해 세션에 담아야 함)
    //5. JWT 토큰을 만들어서 응답해주면 됨
}
