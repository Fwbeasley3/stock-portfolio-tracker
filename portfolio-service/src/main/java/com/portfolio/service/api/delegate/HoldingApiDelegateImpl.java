package com.portfolio.service.api.delegate;

import com.portfolio.service.api.HoldingsApiDelegate;
import com.portfolio.service.api.model.HoldingResponse;
import com.portfolio.service.mapper.HoldingMapper;
import com.portfolio.service.persistence.model.HoldingEntity;
import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.service.HoldingService;
import com.portfolio.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class HoldingApiDelegateImpl implements HoldingsApiDelegate {

  private final HoldingService holdingService;
  private final UserService userService;

  @Override
  public ResponseEntity<HoldingResponse> getHolding(Long portfolioId, Long holdingId) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

    HoldingEntity holdingEntity = holdingService.getHolding(portfolioId, holdingId, userEntity.getId());

    HoldingResponse holdingResponse = HoldingMapper.mapToResponse(holdingEntity);
    return ResponseEntity.ok(holdingResponse);
  }

  @Override
  public ResponseEntity<List<HoldingResponse>> getHoldings(Long portfolioId) {

    // TODO Replace development identity with authenticated JWT subject
    String testExternalAuthId = "dev-user-123";

    UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);
    List<HoldingEntity> holdingEntityList = holdingService.getHoldings(portfolioId, userEntity.getId());

    List<HoldingResponse> holdingResponseList =
        holdingEntityList.stream().map(HoldingMapper::mapToResponse).toList();

    return ResponseEntity.ok(holdingResponseList);
  }
}
