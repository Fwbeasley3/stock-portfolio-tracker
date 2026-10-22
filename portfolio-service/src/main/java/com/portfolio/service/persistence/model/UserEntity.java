package com.portfolio.service.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "externalAuthId", nullable = false, unique = true)
    private String externalAuthId;

    @Column(name = "createdAt", nullable = false)
    private Instant createdAt;
}
