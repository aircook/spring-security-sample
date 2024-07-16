# Spring Security

## Form 로그인 처리 흐름

1. UsernamePasswordAuthenticationFilter
   1. 클라이언트의 로그인 요청을 가로채고, 사용자명과 비밀번호를 추출하여 인증 토큰을 생성.
   2. 이 토큰을 AuthenticationManager에 전달하여 인증을 시도.
2. AuthenticationManager
   1. 여러 AuthenticationProvider 중 적절한 프로바이더를 사용하여 인증을 시도.
   2. 일반적으로 DaoAuthenticationProvider를 사용하여 사용자 정보를 조회하고 인증을 처리.
3. UserDetailsService
   1. AuthenticationManager로부터 사용자명을 받아 사용자 정보를 조회.
   2. loadUserByUsername 메소드를 통해 사용자명을 기반으로 사용자 정보를 데이터베이스에서 조회하고, UserDetails 객체로 반환.
4. UserRepository
   1. UserDetailsService가 사용자 정보를 조회할 때 사용.
   2. 데이터베이스와 상호작용하여 사용자 정보를 관리.
   3. Security가 제공하는 클래스는 아님, JPA, MyBatis등을 통해 구현.


## Customized 가능한 클래스

1. org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.java
   1. extends UsernamePasswordAuthenticationFilter
   2. override attemptAuthentication() --> request의 아이디, 패스워드를 이용하여 UsernamePasswordAuthenticationToken 생성, authenticationManager에게 인증요청
   3. override successfulAuthentication() --> 로그인 성공 처리
   4. override unsuccessfulAuthentication() --> 로그인 실패 처리
   5. http.addFilterAt(new CustomFilter(), UsernamePasswordAuthenticationFilter.class), SecurityFilterChain에서 구현된 필터 교체 설정
2. org/springframework/security/core/userdetails/UserDetailsService.java
3. org/springframework/security/core/userdetails/UserDetails.java