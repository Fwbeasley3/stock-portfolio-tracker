package com.portfolio.service.persistence.repository

import com.portfolio.service.persistence.model.PortfolioEntity
import com.portfolio.service.persistence.model.UserEntity
import com.portfolio.service.persistence.model.repository.PortfolioRepository
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
class PortfolioSpec extends Specification {

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

    @Autowired
    UserRepository userRepository
    @Autowired
    PortfolioRepository portfolioRepository
    @Autowired
    EntityManager entityManager


    def "Find a portfolio that belongs to the correct user"() {
        given: "There is a portfolio persisted in the database for a user"

        String externalAuthId1 = "dummyId123"

        String portfolioName = "Retirement"
        UserEntity userEntityOwner = new UserEntity(externalAuthId1)

        //Save the Owner
        userRepository.save(userEntityOwner)

        PortfolioEntity portfolioEntity = new PortfolioEntity(userEntityOwner, portfolioName)

        when: "The portfolio is retrieved by the portfolio id and correct user"
        PortfolioEntity savedPortfolio = portfolioRepository.save(portfolioEntity)

        entityManager.flush()
        entityManager.clear()

        Optional<PortfolioEntity> retrievedPortfolio =
                portfolioRepository.findByIdAndUserEntity_Id(savedPortfolio.id, userEntityOwner.id)

        then: "Verify the portfolio was persisted with correct values"
        savedPortfolio.id != null
        retrievedPortfolio.isPresent()
        retrievedPortfolio.get().userEntity.id == userEntityOwner.id
        retrievedPortfolio.get().name == portfolioName
    }

    def "Find a portfolio that belongs to the wrong user and returns empty"() {
        given: "There is a portfolio persisted in the database for a user"

        String externalAuthId1 = "dummyId123"
        String externalAuthId2 = "dummyId456"
        String portfolioName = "Retirement"
        UserEntity userEntityOwner = new UserEntity(externalAuthId1)
        UserEntity userEntityNonOwner = new UserEntity(externalAuthId2)

        //Save the Owner and non Owner
        userRepository.save(userEntityOwner)
        userRepository.save(userEntityNonOwner)

        PortfolioEntity portfolioEntity = new PortfolioEntity(userEntityOwner, portfolioName)

        when: "The method returns empty given the retrieval for the portfolio belongs to a different user"
        PortfolioEntity savedPortfolio = portfolioRepository.save(portfolioEntity)

        entityManager.flush()
        entityManager.clear()

        Optional<PortfolioEntity> retrievedPortfolio =
                portfolioRepository.findByIdAndUserEntity_Id(savedPortfolio.id, userEntityNonOwner.id)

        then: "Verify the result is empty"
        savedPortfolio.id != null
        retrievedPortfolio.isEmpty()
    }

    def "Find all portfolio that belongs to the correct user"() {
        given: "There are portfolios persisted in the database for a user"

        String externalAuthId1 = "dummyId123"
        String externalAuthId2 = "dummyId456"

        String portfolioName1 = "Retirement"
        String portfolioName2 = "Investment"
        String portfolioName3 = "Roth IRA"

        UserEntity userEntityOwner = new UserEntity(externalAuthId1)
        UserEntity userEntityOtherOwner = new UserEntity(externalAuthId2)

        //Save the Owner
        userRepository.save(userEntityOwner)
        userRepository.save(userEntityOtherOwner)

        PortfolioEntity portfolioEntity1 = new PortfolioEntity(userEntityOwner, portfolioName1)
        PortfolioEntity portfolioEntity2 = new PortfolioEntity(userEntityOwner, portfolioName2)
        PortfolioEntity portfolioEntity3 = new PortfolioEntity(userEntityOwner, portfolioName3)
        // Create other portfolio with other use to ensure we only pull our selected user's portfolios
        PortfolioEntity portfolioEntity4 = new PortfolioEntity(userEntityOtherOwner, portfolioName1)

        PortfolioEntity savedPortfolio1 = portfolioRepository.save(portfolioEntity1)
        PortfolioEntity savedPortfolio2 = portfolioRepository.save(portfolioEntity2)
        PortfolioEntity savedPortfolio3 = portfolioRepository.save(portfolioEntity3)
        PortfolioEntity savedPortfolio4 = portfolioRepository.save(portfolioEntity4)

        entityManager.flush()
        entityManager.clear()

        when: "The portfolios are retrieved by the user ID"

        List<PortfolioEntity> retrievedPortfolios =
                portfolioRepository.findByUserEntity_Id(userEntityOwner.id)

        then: "Verify that all portfolios were retrieved for the user"
        savedPortfolio1.id != null
        savedPortfolio2.id != null
        savedPortfolio3.id != null
        savedPortfolio4.id != null
        retrievedPortfolios.size() == 3
        retrievedPortfolios*.name as Set == [portfolioName1, portfolioName2, portfolioName3] as Set
        retrievedPortfolios.every { it.userEntity.id == userEntityOwner.id }
    }
}
