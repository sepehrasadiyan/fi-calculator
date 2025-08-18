package me.fi_calculator.fi_calculator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"),
        indexes = {
                @Index(name = "idx_users_id", columnList = "id"),
                @Index(name = "idx_users_email", columnList = "email")
        }
)
public class UserEntity implements UserDetails, Persistable<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 190)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
