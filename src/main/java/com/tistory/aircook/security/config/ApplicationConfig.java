package com.tistory.aircook.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

//순환참조해결
//https://velog.io/@platinouss/Spring-Circular-References-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0
@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); //deprecated
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
