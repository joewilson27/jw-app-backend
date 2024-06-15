package jw.apps.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jw.apps.backend.entity.ERole;
import jw.apps.backend.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  
  Optional<Role> findByName(ERole name);
  
}
