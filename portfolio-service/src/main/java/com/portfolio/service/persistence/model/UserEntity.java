package com.portfolio.service.persistence.model;

import com.portfolio.service.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor
public class UserEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "external_auth_id", nullable = false, unique = true)
  private String externalAuthId;

  public UserEntity(String externalAuthId) {
    this.externalAuthId =
        Objects.requireNonNull(externalAuthId, "External authentication ID cannot be null");
  }
}
