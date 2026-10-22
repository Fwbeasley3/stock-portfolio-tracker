package com.portfolio.service.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_auth_id", nullable = false, unique = true)
    private String externalAuthId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public UserEntity(String externalAuthId) {
        this.externalAuthId = Objects.requireNonNull(externalAuthId, "External authentication ID cannot be null");
        this.createdAt = Instant.now();
    }
}
