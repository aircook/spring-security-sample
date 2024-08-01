package com.tistory.aircook.security.config;


import com.tistory.aircook.security.service.LoginUserDetailsService;
import com.tistory.aircook.security.service.UserService;
import com.tistory.aircook.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.*;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

/**
 * security의 기본설정 아이디는 user, 비밀번호는 로그에서 보여줌
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    private final SecurityProperties securityProperties;

    private final UserService userService;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    private final CustomAuthorizationManager customAuthorizationManager;

    private final CustomOAuth2UserService customOAuth2UserService;

    //AuthenticationManager Bean 등록, AuthenticationConfiguration Bean은 Spring이 생성함
    //UserDetailsService를 구현하면 다음 메시지가 보인다.
    //Global AuthenticationManager configured with UserDetailsService bean with name customUserDetailsService
    //@Bean
    //public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    //    return configuration.getAuthenticationManager();
    //}


    //인증관리자(Manager) 빈, 아래 fiterChain 메소드에 전달됨, 인증공급자(Provider)가 정의되어 있음
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("memuser")
//                .password(passwordEncoder().encode("pass"))
//                .roles("USER");
        return authenticationManagerBuilder.build();
    }


    //빈 선언만 해도 기본 http://localhost:8080/login 페이지가 404가 나온다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        //csrf 비활성화
        http.csrf(customizer -> customizer.disable());

        http.cors(customizer ->
                customizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOriginPatterns(Collections.singletonList("*"));
                        //config.setAllowedOrigins(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
        );

        //formLogin 비활성화
        http.formLogin(customizer -> customizer.disable());

        //SecurityFilterChain을 정의하면 Spring Security의 기본 보안 구성이 재정의됩니다.
        // 기본 보안 구성에는 기본 로그인 페이지에 대한 설정이 포함되어 있으므로,
        // 사용자가 SecurityFilterChain을 정의하면서 기본 보안 구성을 제공하지 않으면 기본 로그인 페이지도 더 이상 제공되지 않습니다.
        //http.formLogin(withDefaults());
        http.formLogin(customizer -> customizer
                // 로그인페이지 지정, customizing이 필요할때
                .loginPage("/login-form")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/api/v1/login")
                .defaultSuccessUrl("/login/success", true)
                //POST로 정의되어야 한다.
                .failureForwardUrl("/login/failure")
        );

        //httpBasic 비활성화
        http.httpBasic(customizer -> customizer.disable());

        //deprecatec된 authorizeRequests()에서 동작하던 AuthenticationProvider가 동작하지 않는다.
        http.authorizeHttpRequests(customizer ->
                customizer
                        .requestMatchers(securityProperties.getExclude().toArray(new String[0])).permitAll()
                        .requestMatchers("/access/**").access(customAuthorizationManager)
                        .anyRequest().authenticated()
        );

        //JWTFilter 등록
        //http.addFilterBefore(new TokenFilter(jwtUtil), LoginFilter.class);

        //filter ignoring 설정을 위해 bean으로 연결하지 않음. new ()!!
        //https://bitgadak.tistory.com/10
        //필터 추가 CustomAuthenticationFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        //UsernamePasswordAuthenticationFilter재구현하고 UserDetailsService 사용할때 테스트 완료했었음
        //아래와 authenticationManager이 다르다. UserDetailsService 조차도 재구현했음으로 사용안함
        //http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //이건 설정하나 안하나 뭔 변화가 있지?
        //http.authenticationManager(authenticationManager);
        //CsrfFilter 다음에 필터 등록, 인증관리자 전달, 인증관리자는 인증공급자(CustomAuthenticationProvider와 연결됨)

//        http.addFilter(new SecurityContextPersistenceFilter());

        http.addFilterAfter(new CustomAuthenticationFilter(authenticationManager, jwtUtil, delegatingSecurityContextRepository()), SecurityContextHolderFilter.class);

        //JWT 인증을 위한 세션 설정
        //http.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //세션 사용 설정
//        http.sessionManagement(customizer ->
//                customizer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/")
//                        .maximumSessions(1)
//                        .expiredUrl("/"));

        http.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.securityContext((securityContextCustomizer) -> {
            securityContextCustomizer.securityContextRepository(delegatingSecurityContextRepository());
            securityContextCustomizer.requireExplicitSave(true);
        });

        http.oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
                .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
                        .userService(customOAuth2UserService))
                .successHandler(new CustomAuthenticationSuccessHandler()) // CustomAuthenticationFilter에서 사용할려고 만든건데 사용함, JSON데이터 리턴
        );

        return http.build();
    }

    /**
     * 기본로그인페이지에서 사용할 사용자 계정정보를 인메모리에 지정
     * 지정하면 Using generated security password 사라진다.
     * 비밀번호의 {noop} 의미는 암호화 없음
     *
     * @return
     * @see LoginUserDetailsService
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user1").password("{noop}1234").roles("user").build());
//        return manager;
//    }
    @Bean
    public DelegatingSecurityContextRepository delegatingSecurityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

}
