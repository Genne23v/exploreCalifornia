package com.example.ec.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ec.domain.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
}
