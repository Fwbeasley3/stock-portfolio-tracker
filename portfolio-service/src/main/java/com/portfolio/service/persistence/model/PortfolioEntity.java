package com.portfolio.service.persistence.model;

import com.portfolio.service.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "portfolio")
@Getter
@NoArgsConstructor
public class PortfolioEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity userEntity;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    public PortfolioEntity(UserEntity userEntity, String name){
        this.userEntity = Objects.requireNonNull(userEntity, "Owner cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");

    }
}
