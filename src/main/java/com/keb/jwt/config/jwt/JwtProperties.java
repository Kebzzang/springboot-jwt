package com.keb.jwt.config.jwt;

public interface JwtProperties {
    String SECRET="kebzzang";
    int EXPIRATION_TIME=60000*10;
    String TOKEN_PREFIX="Bearer ";
    String HEADER_STRING="Authorization";
}
