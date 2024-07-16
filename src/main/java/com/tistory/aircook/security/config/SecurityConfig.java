package com.tistory.aircook.security.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * security의 기본설정 아이디는 user, 비밀번호는 로그에서 보여줌
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    //빈 선언만 해도 기본 http://localhost:8080/login 페이지가 404가 나온다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer ->
                customizer.disable());

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
        //http.formLogin(customizer -> customizer.disable());
        //SecurityFilterChain을 정의하면 Spring Security의 기본 보안 구성이 재정의됩니다.
        // 기본 보안 구성에는 기본 로그인 페이지에 대한 설정이 포함되어 있으므로,
        // 사용자가 SecurityFilterChain을 정의하면서 기본 보안 구성을 제공하지 않으면 기본 로그인 페이지도 더 이상 제공되지 않습니다.
        //http.formLogin(withDefaults());
        http.formLogin(customizer -> customizer
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/login/success", true));

        //httpBasic 비활성화
        http.httpBasic(customizer -> customizer.disable());


        //deprecatec된 authorizeRequests()에서 동작하던 AuthenticationProvider가 동작하지 않는다.
        http.authorizeHttpRequests(customizer ->
                customizer
                        .requestMatchers(securityProperties.getExclude().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
        );

        //BasicAuthenticationFilter 전에 WebSecurityFilter 추가
        //filter ignoring 설절을 위해 bean으로 연결하지 않음. new SessionTokenFilter()!!
        //https://bitgadak.tistory.com/10
        //http.addFilterBefore(new SessionTokenFilter(securityProperties.getExclude()), BasicAuthenticationFilter.class);

//        http.sessionManagement(customizer ->
//                customizer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/")
//                        .maximumSessions(1)
//                        .expiredUrl("/"));

        return http.build();
    }


    /**
     * 기본로그인페이지에서 사용할 사용자 계정정보를 인메모리에 지정
     * 지정하면 Using generated security password 사라진다.
     * 비밀번호의 {noop} 의미는 암호화 없음
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user1").password("{noop}1234").roles("user").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); //deprecated
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
