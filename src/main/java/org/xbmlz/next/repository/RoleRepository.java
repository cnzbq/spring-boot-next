package org.xbmlz.next.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xbmlz.next.domain.Role;

/**
 * @author chenxc
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
