package com.venable.next.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.venable.next.domain.Role;

/**
 * @author chenxc
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
