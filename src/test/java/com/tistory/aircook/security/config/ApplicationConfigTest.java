package com.tistory.aircook.security.config;

import com.tistory.aircook.security.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ApplicationConfigTest {

    @Test
    public void test() {

        String password = "password";

        //PasswordEncoder defaultEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new StandardPasswordEncoder());
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        //SHA-256 encode
        DelegatingPasswordEncoder delegatingPasswordEncoder1 = new DelegatingPasswordEncoder("SHA-256", encoders);
        System.out.println(delegatingPasswordEncoder1.encode(password));

        //sha256 encode
        DelegatingPasswordEncoder delegatingPasswordEncoder2 = new DelegatingPasswordEncoder("sha256", encoders);
        System.out.println(delegatingPasswordEncoder2.encode(password));

        //bcrypt encode
        DelegatingPasswordEncoder delegatingPasswordEncoder3 = new DelegatingPasswordEncoder("bcrypt", encoders);
        System.out.println(delegatingPasswordEncoder3.encode(password));

        //delegatingPasswordEncoder1 로 패스워드 검증
        assertThat(delegatingPasswordEncoder1.matches(password, "{SHA-256}{hKqdAUl+sC79E0w+p/NldRtvEXqYx83BCnw562ezPfE=}078104340a21a5e6739da6d5b36547ba14cce60330ffe373e526fea0d6b351d7")).isTrue();
        assertThat(delegatingPasswordEncoder1.matches(password, "{sha256}afc3611e5984f21d7ea32f6c075f1a1d976bac782d48d6a3cb894a55d1ab4bb7acf3a0f58531d21d")).isTrue();
        assertThat(delegatingPasswordEncoder1.matches(password, "{bcrypt}$2a$10$ikgNiYI8VwP9s.6awIX0w.llzDlLm2Jt/yHE6oImetZ.MRRQ.StsO")).isTrue();

        //delegatingPasswordEncoder2 로 패스워드 검증
        assertThat(delegatingPasswordEncoder2.matches(password, "{SHA-256}{hKqdAUl+sC79E0w+p/NldRtvEXqYx83BCnw562ezPfE=}078104340a21a5e6739da6d5b36547ba14cce60330ffe373e526fea0d6b351d7")).isTrue();
        assertThat(delegatingPasswordEncoder2.matches(password, "{sha256}afc3611e5984f21d7ea32f6c075f1a1d976bac782d48d6a3cb894a55d1ab4bb7acf3a0f58531d21d")).isTrue();
        assertThat(delegatingPasswordEncoder2.matches(password, "{bcrypt}$2a$10$ikgNiYI8VwP9s.6awIX0w.llzDlLm2Jt/yHE6oImetZ.MRRQ.StsO")).isTrue();

        //delegatingPasswordEncoder3 로 패스워드 검증
        assertThat(delegatingPasswordEncoder3.matches(password, "{SHA-256}{hKqdAUl+sC79E0w+p/NldRtvEXqYx83BCnw562ezPfE=}078104340a21a5e6739da6d5b36547ba14cce60330ffe373e526fea0d6b351d7")).isTrue();
        assertThat(delegatingPasswordEncoder3.matches(password, "{sha256}afc3611e5984f21d7ea32f6c075f1a1d976bac782d48d6a3cb894a55d1ab4bb7acf3a0f58531d21d")).isTrue();
        assertThat(delegatingPasswordEncoder3.matches(password, "{bcrypt}$2a$10$ikgNiYI8VwP9s.6awIX0w.llzDlLm2Jt/yHE6oImetZ.MRRQ.StsO")).isTrue();





    }


}