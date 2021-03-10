package com.keb.jwt.controller;

import com.keb.jwt.model.User;
import com.keb.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class RestApiController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }
    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }
    @PostMapping("join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료!";
    }
    @GetMapping("/api/v1/user") //->매니저, 어드민
    public String user(){
        return "user";
    }
    @GetMapping("/api/v1/manager") //->매니저
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin") //->어드민
    public String admin(){
        return "admimn";
    }
}
