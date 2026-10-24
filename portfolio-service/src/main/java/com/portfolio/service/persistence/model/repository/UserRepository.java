package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByExternalAuthId(String externalAuthId);
}
