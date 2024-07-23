package com.tistory.aircook.security.service;

import com.tistory.aircook.security.entity.UserEntity;
import com.tistory.aircook.security.model.LoginUserDetails;
import com.tistory.aircook.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


//둘다 구현하면 다음과 같은 메시지가 나온다.
//Global AuthenticationManager configured with AuthenticationProvider bean with name customAuthenticationProvider
//Global AuthenticationManager configured with an AuthenticationProvider bean.
//UserDetailsService beans will not be used for username/password login. Consider removing the AuthenticationProvider bean.
//Alternatively, consider using the UserDetailsService in a manually instantiated DaoAuthenticationProvider.
//@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("username is {}", username);

        //DB에서 조회
        UserEntity user = userRepository.findByUserid(username);

        if (ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("Could not found user" + username);
        }

        return new LoginUserDetails(user);
        //다음과 같이 UserDetails 생성할수도 있다.
//        return User.builder()
//                .username(user.getUserid())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
    }

}
