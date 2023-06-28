package com.venable.next.repository;

import com.venable.next.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author chenxc
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    String USERS_BY_USERNAME_CACHE = "usersByUsername";

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Cacheable(cacheNames = USERS_BY_USERNAME_CACHE)
    Optional<User> findByUsername(String username);
}
