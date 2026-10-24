package com.portfolio.service.service;

import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.persistence.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserEntity findOrCreateUser(String externalAuthId) {

    Optional<UserEntity> userEntityOpt = userRepository.findByExternalAuthId(externalAuthId);

    // check if user is present and return the user
    if (userEntityOpt.isPresent()) {
      return userEntityOpt.get();
    }

    // otherwise create and save a user
    UserEntity userEntity = new UserEntity(externalAuthId);

    return userRepository.save(userEntity);
  }
}
