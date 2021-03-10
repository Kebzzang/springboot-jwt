package com.keb.jwt.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keb.jwt.config.auth.PrincipalDetails;
import com.keb.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


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

            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨!!
            Authentication authentication=  //이 authentication에는 로그인한 정보가 담긴다.
                    authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal(); //다운캐스팅
            System.out.println(principalDetails.getUser().getUsername());
            //인증이 정상적으로 되어 authentication 객체가 세션영역에 저장 -> authentication 내 principal 객체를 가져와 출력이 되는 것 로그인이 되었다는 것


            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("================================");
        return null;
    }


    //1. 유저네임 패스워드 받아서
    //2. 정상인지 로그인 시도 함  authenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출됨
    //3. 그럼 loadUserByUsername() 함수 실행됨
    //4. PrincipalDetails 를 세션에 담고 (권한관리를 위해 세션에 담아야 함)
    //5. JWT 토큰을 만들어서 응답해주면 됨
}
