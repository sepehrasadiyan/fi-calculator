package me.fi_calculator.fi_calculator.repository;

import me.fi_calculator.fi_calculator.domain.RoleEntity;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
}
