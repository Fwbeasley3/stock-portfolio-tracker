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
}
