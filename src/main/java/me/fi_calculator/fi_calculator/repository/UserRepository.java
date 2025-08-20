package me.fi_calculator.fi_calculator.repository;

import me.fi_calculator.fi_calculator.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    // Run from N+1 :)
    @Query("""
        select u from UserEntity u
        left join fetch u.roles
        where lower(u.email) = lower(:email)
    """)
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);
}
