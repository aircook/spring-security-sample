package com.tistory.aircook.security.service;

import com.tistory.aircook.security.entity.UserEntity;
import com.tistory.aircook.security.model.CustomUserDetails;
import com.tistory.aircook.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("username is {}", username);

        //DB에서 조회
        UserEntity user = userRepository.findByUserid(username);

        if (ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("Could not found user" + username);
        }

        return new CustomUserDetails(user);
        //다음과 같이 UserDetails 생성할수도 있다.
//        return User.builder()
//                .username(user.getUserid())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
    }

}
