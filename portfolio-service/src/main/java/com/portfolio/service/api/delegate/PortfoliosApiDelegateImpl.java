package com.portfolio.service.api.delegate;

import com.portfolio.service.api.PortfoliosApiDelegate;
import com.portfolio.service.api.model.CreatePortfolioRequest;
import com.portfolio.service.api.model.PortfolioResponse;
import com.portfolio.service.api.model.PortfolioValuationResponse;
import com.portfolio.service.api.model.UpdatePortfolioRequest;
import com.portfolio.service.mapper.PortfolioMapper;
import com.portfolio.service.persistence.model.PortfolioEntity;
import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.service.PortfolioService;
import com.portfolio.service.service.PortfolioValuationService;
import com.portfolio.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Service
public class PortfoliosApiDelegateImpl implements PortfoliosApiDelegate {

  private final PortfolioService portfolioService;
  private final UserService userService;
  private final PortfolioValuationService portfolioValuationService;

  @Override
  public ResponseEntity<PortfolioResponse> createPortfolio(
      CreatePortfolioRequest createPortfolioRequest) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";
    String portfolioName = createPortfolioRequest.getName();

    PortfolioEntity portfolioEntity =
        portfolioService.createPortfolio(testExternalAuthId, portfolioName);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(portfolioEntity.getId())
            .toUri();

    PortfolioResponse portfolioResponse = PortfolioMapper.mapToResponse(portfolioEntity);

    return ResponseEntity.created(location).body(portfolioResponse);
  }

  @Override
  public ResponseEntity<PortfolioResponse> getPortfolio(Long portfolioId) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);
    PortfolioEntity portfolioEntity =
        portfolioService.getPortfolio(portfolioId, userEntity.getId());

    PortfolioResponse portfolioResponse = PortfolioMapper.mapToResponse(portfolioEntity);

    return ResponseEntity.ok(portfolioResponse);
  }

  @Override
  public ResponseEntity<List<PortfolioResponse>> getPortfolios() {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);
    List<PortfolioEntity> portfolioEntityList = portfolioService.getPortfolios(userEntity.getId());

    List<PortfolioResponse> portfolioResponseList =
        portfolioEntityList.stream().map(PortfolioMapper::mapToResponse).toList();

    return ResponseEntity.ok(portfolioResponseList);
  }

  @Override
  public ResponseEntity<PortfolioResponse> updatePortfolio(
      Long portfolioId, UpdatePortfolioRequest updatePortfolioRequest) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";
    String portfolioNameForUpdate = updatePortfolioRequest.getName();

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);
    PortfolioEntity portfolioEntity =
        portfolioService.updatePortfolio(portfolioId, userEntity.getId(), portfolioNameForUpdate);

    PortfolioResponse portfolioResponse = PortfolioMapper.mapToResponse(portfolioEntity);

    return ResponseEntity.ok(portfolioResponse);
  }

  @Override
  public ResponseEntity<Void> deletePortfolio(Long portfolioId) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

    portfolioService.deletePortfolio(portfolioId, userEntity.getId());

    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PortfolioValuationResponse> getPortfolioValuation(Long portfolioId) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

    PortfolioValuationResponse portfolioValuationResponse = portfolioValuationService.getValuation(portfolioId, userEntity.getId());

    return ResponseEntity.ok(portfolioValuationResponse);
  }
}
