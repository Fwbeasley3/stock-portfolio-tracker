package com.portfolio.service.exception;

public class PortfolioNotFoundException extends RuntimeException {
  public PortfolioNotFoundException(String message) {
    super(message);
  }
}
