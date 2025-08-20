package me.fi_calculator.fi_calculator.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles",
        indexes = @Index(name = "idx_roles_code", columnList = "code", unique = true))
@Setter
@Getter
public class RoleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @Column(nullable = false, length = 128)
    private String name;
}
