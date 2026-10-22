package com.portfolio.service.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "portfolio")
@Getter
@NoArgsConstructor
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity userEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public PortfolioEntity(UserEntity userEntity, String name){
        this.userEntity = Objects.requireNonNull(userEntity, "Owner cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.createdAt = Instant.now();
    }
}
