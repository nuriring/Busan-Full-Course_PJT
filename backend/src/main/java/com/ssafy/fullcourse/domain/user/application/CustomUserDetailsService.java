package com.ssafy.fullcourse.domain.user.application;

import com.ssafy.fullcourse.domain.user.entity.User;
import com.ssafy.fullcourse.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component("userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        // DB에서 유저정보와 권한정보를 가져온다
        return userRepository.findByEmail(email)
                .map(user -> createUser(user))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {

        // 유저가 활성화상태라면, userName, Password, 권한정보를 기반으로 User객체 리턴
//        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//                .collect(Collectors.toList());
//        log.info("grantedAuthorities : " + grantedAuthorities);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        log.info("grantedAuthority : " + grantedAuthority);

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getSocialId(),
//                grantedAuthorities
                Collections.singleton(grantedAuthority)
                );
    }
}
