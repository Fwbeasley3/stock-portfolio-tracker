package com.portfolio.service.api.delegate;

import com.portfolio.service.api.HoldingsApiDelegate;
import com.portfolio.service.api.model.CreateHoldingRequest;
import com.portfolio.service.api.model.HoldingResponse;
import com.portfolio.service.mapper.HoldingMapper;
import com.portfolio.service.persistence.model.HoldingEntity;
import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.service.HoldingService;
import com.portfolio.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@AllArgsConstructor
@Service
public class HoldingApiDelegateImpl implements HoldingsApiDelegate {

    private final HoldingService holdingService;
    private final UserService userService;

    @Override
    public ResponseEntity<HoldingResponse> createHolding(Long portfolioId, CreateHoldingRequest createHoldingRequest) {

        // TODO Replace development identity with authenticated JWT subject
        String testExternalAuthId = "dev-user-123";
        String symbol = createHoldingRequest.getSymbol();
        BigDecimal quantity = createHoldingRequest.getQuantity();
        BigDecimal averageCost = createHoldingRequest.getAverageCost();

        UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

        HoldingEntity holdingEntity = holdingService.createHolding(portfolioId, userEntity.getId(), symbol, quantity, averageCost);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(holdingEntity.getId()).toUri();

        HoldingResponse holdingResponse = HoldingMapper.mapToResponse(holdingEntity);

        return ResponseEntity.created(location).body(holdingResponse);
    }
}
