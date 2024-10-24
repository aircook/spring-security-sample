package com.tistory.aircook.security.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ApplicationConfigTest {

    @Test
    @DisplayName("DelegatingPasswordEncoder Test")
    public void test() {

        log.debug("테스트 시작..");

        String password = "password";
        log.debug(password);
        //PasswordEncoder defaultEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new StandardPasswordEncoder());
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        //SHA-256 encode
        DelegatingPasswordEncoder delegatingPasswordEncoder1 = new DelegatingPasswordEncoder("SHA-256", encoders);
        String encodedPassword1 = delegatingPasswordEncoder1.encode(password);
        log.debug(encodedPassword1);

        //sha256 encode
        DelegatingPasswordEncoder delegatingPasswordEncoder2 = new DelegatingPasswordEncoder("sha256", encoders);
        String encodedPassword2 = delegatingPasswordEncoder2.encode(password);
        log.debug(encodedPassword2);

        //bcrypt encode
        DelegatingPasswordEncoder delegatingPasswordEncoder3 = new DelegatingPasswordEncoder("bcrypt", encoders);
        String encodedPassword3 = delegatingPasswordEncoder3.encode(password);
        log.debug(encodedPassword3);

        //delegatingPasswordEncoder1 로 패스워드 검증
        assertThat(delegatingPasswordEncoder1.matches(password, encodedPassword1)).isTrue();
        assertThat(delegatingPasswordEncoder1.matches(password, encodedPassword2)).isTrue();
        assertThat(delegatingPasswordEncoder1.matches(password, encodedPassword3)).isTrue();

        //delegatingPasswordEncoder2 로 패스워드 검증
        assertThat(delegatingPasswordEncoder2.matches(password, encodedPassword1)).isTrue();
        assertThat(delegatingPasswordEncoder2.matches(password, encodedPassword2)).isTrue();
        assertThat(delegatingPasswordEncoder2.matches(password, encodedPassword3)).isTrue();

        //delegatingPasswordEncoder3 로 패스워드 검증
        assertThat(delegatingPasswordEncoder3.matches(password, encodedPassword1)).isTrue();
        assertThat(delegatingPasswordEncoder3.matches(password, encodedPassword2)).isTrue();
        assertThat(delegatingPasswordEncoder3.matches(password, encodedPassword3)).isTrue();

    }

}