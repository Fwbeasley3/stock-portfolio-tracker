package com.portfolio.service.service;

import com.portfolio.service.exception.PortfolioNotFoundException;
import com.portfolio.service.persistence.model.PortfolioEntity;
import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.persistence.model.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserService userService;

    @Transactional()
    public PortfolioEntity createPortfolio(String externalAuthId, String portfolioName) {

        //Retrieve the user that wants to create the portfolio
        UserEntity userEntity = userService.findOrCreateUser(externalAuthId);

        //Create the portfolio
        PortfolioEntity portfolioEntity = new PortfolioEntity(userEntity, portfolioName);

        return portfolioRepository.save(portfolioEntity);
    }

    @Transactional(readOnly = true)
    public PortfolioEntity getPortfolio(Long portfolioId, Long userId) {

        return portfolioRepository.findByIdAndUserEntity_Id(portfolioId, userId).orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));
    }

    @Transactional(readOnly = true)
    public List<PortfolioEntity> getPortfolios(Long userId) {

        return portfolioRepository.findByUserEntity_Id(userId);
    }
}
