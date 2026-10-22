package com.portfolio.service.service

import com.portfolio.service.persistence.model.PortfolioEntity
import com.portfolio.service.persistence.model.UserEntity
import com.portfolio.service.persistence.model.repository.PortfolioRepository
import spock.lang.Specification

class PortfolioServiceSpec extends Specification {

    PortfolioRepository portfolioRepository = Mock(PortfolioRepository)
    UserService userService = Mock(UserService)
    PortfolioService portfolioService = new PortfolioService(portfolioRepository, userService)

    def "Create portfolio for a user"() {
        given: "A user"

        String externalAuthId = "dummyId123"
        String portfolioName = "Retirement"
        UserEntity userEntity = new UserEntity(externalAuthId)

        when: "The user want to create a portfolio"
        PortfolioEntity result = portfolioService.createPortfolio(externalAuthId, portfolioName)

        then: "Verify our interactions and that the portfolio was created with the correct user"
        1 * userService.findOrCreateUser(externalAuthId) >> userEntity
        1 * portfolioRepository.save({it.name == portfolioName && it.userEntity == userEntity}) >> {PortfolioEntity portfolio -> portfolio}
        result.name == portfolioName
        result.userEntity == userEntity
    }

    def "Retrieve a portfolio for a user"() {
        given: "A user has a portfolio"

        String externalAuthId = "dummyId123"
        String portfolioName = "Retirement"
        UserEntity userEntity = new UserEntity(externalAuthId)
        userEntity.id = 1L
        PortfolioEntity portfolioEntity = new PortfolioEntity(userEntity, portfolioName)
        portfolioEntity.id = 1L

        when: "The user want to retrieve a single portfolio they have"
        PortfolioEntity result = portfolioService.getPortfolio(portfolioEntity.id, userEntity.id)

        then: "Verify our interactions and that the portfolio was retrieved for the correct user"
        1 * portfolioRepository.findByIdAndUserEntity_Id(portfolioEntity.id, userEntity.id) >> Optional.of(portfolioEntity)
        result.name == portfolioName
        result.userEntity == userEntity
    }

    def "Retrieve all portfolios for a user"() {
        given: "A user has multiple portfolioa"

        String externalAuthId = "dummyId123"
        String portfolioName1 = "Retirement"
        String portfolioName2 = "Investment"
        String portfolioName3 = "Roth IRA"

        UserEntity userEntity = new UserEntity(externalAuthId)
        userEntity.id = 1L
        PortfolioEntity portfolioEntity1 = new PortfolioEntity(userEntity, portfolioName1)
        portfolioEntity1.id = 1L
        PortfolioEntity portfolioEntity2 = new PortfolioEntity(userEntity, portfolioName2)
        portfolioEntity2.id = 2L
        PortfolioEntity portfolioEntity3 = new PortfolioEntity(userEntity, portfolioName3)
        portfolioEntity3.id = 3L

        when: "The user want to retrieve multiple portfolios they have"
        List<PortfolioEntity> result = portfolioService.getPortfolios(userEntity.id)

        then: "Verify our interactions and that the portfolios were retrieved for the correct user"
        1 * portfolioRepository.findByUserEntity_Id(userEntity.id) >> [portfolioEntity1, portfolioEntity2, portfolioEntity3]
        result.size() == 3

        and: "The correct portfolios are returned"
        result*.name as Set == [portfolioName1, portfolioName2, portfolioName3] as Set

        and: "Every portfolio belongs to the user"
        result.every() {it.userEntity.id == userEntity.id}
    }

    def "Retrieve all portfolios for a user returns empty list"() {
        given: "A user has multiple portfolioa"

        String externalAuthId = "dummyId123"

        UserEntity userEntity = new UserEntity(externalAuthId)
        userEntity.id = 1L

        when: "The user want to retrieve multiple portfolios they have"
        List<PortfolioEntity> result = portfolioService.getPortfolios(userEntity.id)

        then: "The repository returns an empty list"
        1 * portfolioRepository.findByUserEntity_Id(userEntity.id) >> []
        and :"The service returns an empty list"
        result.isEmpty()
    }
}
