package com.portfolio.service.persistence.repository

import com.portfolio.service.persistence.model.UserEntity
import com.portfolio.service.persistence.model.repository.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositorySpec extends Specification {

    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:18")

    static {
        postgres.start()
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl)
        registry.add("spring.datasource.username", postgres::getUsername)
        registry.add("spring.datasource.password", postgres::getPassword)
    }

    @Autowired UserRepository userRepository
    @Autowired EntityManager entityManager

    def"Persist a user entity and retrieve by id"(){
        given: "Create a User entity"

        UserEntity userEntity = new UserEntity("dummyId123")

        when: "The User is saved"
        UserEntity savedUser= userRepository.save(userEntity)

        entityManager.flush()
        entityManager.clear()

        Optional<UserEntity> retrievedUser =
                userRepository.findById(savedUser.id)

        then:"Verify the user was persisted with correct values"
        savedUser.id != null
        retrievedUser.isPresent()
        retrievedUser.get().externalAuthId == userEntity.externalAuthId
    }

    def"Persist a user entity and retrieve by externalAuthId"(){
        given: "Create a User entity"

        UserEntity userEntity = new UserEntity("dummyId123")

        when: "The User is saved"
        UserEntity savedUser= userRepository.save(userEntity)

        entityManager.flush()
        entityManager.clear()

        Optional<UserEntity> retrievedUser =
                userRepository.findByExternalAuthId(savedUser.externalAuthId)

        then:"Verify the user was persisted with correct values"
        savedUser.id != null
        retrievedUser.isPresent()
        retrievedUser.get().externalAuthId == userEntity.externalAuthId
    }

    def"Return empty when externalAuthId doesn't match the existing user"(){
        given: "Create a User entity"

        UserEntity userEntity = new UserEntity("dummyId123")

        when: "The User is saved"
        userRepository.save(userEntity)

        entityManager.flush()
        entityManager.clear()

        Optional<UserEntity> retrievedUser =
                userRepository.findByExternalAuthId("nonexistingId")

        then:"Verify no user matches the requested externalAuthId"
        retrievedUser.isEmpty()
    }
}
