## springboot-jwt

### jwt server build
![image](https://user-images.githubusercontent.com/44967760/110609760-e2039b80-81d0-11eb-9455-22d04579e006.png)


![image](https://user-images.githubusercontent.com/44967760/110609932-18411b00-81d1-11eb-8c33-d42fb4a4cce6.png)


![image](https://user-images.githubusercontent.com/44967760/110610248-635b2e00-81d1-11eb-954e-1001c8a11880.png)
<hr/>

**ROLE_USER, ROLE_ADMIN, ROLE_MANAGER 모두 접근 가능한 /api/v1/user**

![image](https://user-images.githubusercontent.com/44967760/110610555-b2a15e80-81d1-11eb-8c82-dbfe17775868.png)
<hr/>

**ROLE_USER로 접근 불가능한 /api/v1/manager ->403 forbidden**

![image](https://user-images.githubusercontent.com/44967760/110610835-f72cfa00-81d1-11eb-9051-caa9fd35b551.png)
<hr/>

**ROLE_ADMIN, ROLE_MANAGER 두 개의 롤을 갖고 있는 tester 계정으로 로그인 뒤 받아온 토큰으로 /api/v1/manager에 접속 요청**

![image](https://user-images.githubusercontent.com/44967760/110611105-3e1aef80-81d2-11eb-8dfa-585682b7d7b1.png)
<hr/>

**tester 계정으로 모든 유저 정보 출력 요청**

![image](https://user-images.githubusercontent.com/44967760/110611485-9baf3c00-81d2-11eb-8e9a-5433b02ea3b5.png)

