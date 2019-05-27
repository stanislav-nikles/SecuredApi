package com.hankuper.securedapi.repositories;

import com.hankuper.securedapi.entities.Role;
import com.hankuper.securedapi.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
