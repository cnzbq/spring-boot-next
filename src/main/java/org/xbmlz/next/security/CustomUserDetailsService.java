package org.xbmlz.next.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xbmlz.next.domain.Role;
import org.xbmlz.next.domain.User;
import org.xbmlz.next.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author chenxc
 * @date 2023/04/21
 * @description Spring Security 用户认证
 */
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.UserDetails
     * @throws UsernameNotFoundException 用户名未找到异常
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("Authenticating {}", username);
        return userRepository.findByUsername(username)
                .map(user -> createSpringSecurityUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the " +
                        "database"));
    }

    /**
     * 创建Spring Security用户
     *
     * @param username 用户名
     * @param user     用户
     * @return org.springframework.security.core.userdetails.User
     * @throws UserNotActivatedException 用户未激活异常
     */
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String username, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + username + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
                .getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);
    }
}
