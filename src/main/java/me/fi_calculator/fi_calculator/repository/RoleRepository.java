package me.fi_calculator.fi_calculator.repository;

import me.fi_calculator.fi_calculator.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByCode(String code);
}
