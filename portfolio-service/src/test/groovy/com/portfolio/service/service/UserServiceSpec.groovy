package com.portfolio.service.service

import com.portfolio.service.persistence.model.UserEntity
import com.portfolio.service.persistence.model.repository.UserRepository
import spock.lang.Specification

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock(UserRepository)
    UserService userService = new UserService(userRepository)

    def"Find a user if they exist"() {
        given: "There is a user persisted in the database"

        String externalAuthId = "dummyId123"
        UserEntity existingUser = new UserEntity(externalAuthId)

        when: "The user service looks up the user"
        UserEntity result = userService.findOrCreateUser(externalAuthId)

        then: "Verify our interactions that our method was called"
        1 * userRepository.findByExternalAuthId(externalAuthId) >> Optional.of(existingUser)
        0 * userRepository.save(existingUser)
        result == existingUser
    }

    def"Create a user if they do not exist"() {
        given: "There is no user found by the repository"

        String externalAuthId = "dummyId123"

        when: "The user service looks up the user"
        userService.findOrCreateUser(externalAuthId)

        then: "Verify our retrieval did not return a user and that the save was called"
        1 * userRepository.findByExternalAuthId(externalAuthId) >> Optional.empty()
        1 * userRepository.save({it.externalAuthId == externalAuthId}) >> {UserEntity user -> user}

    }
}
