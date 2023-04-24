package org.xbmlz.next.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xbmlz.next.domain.User;

import java.util.Optional;

/**
 * @author chenxc
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);
}
