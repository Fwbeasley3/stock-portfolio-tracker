package com.portfolio.service.exception;

public class HoldingAlreadyExistsException extends RuntimeException {
  public HoldingAlreadyExistsException(String message) {
    super(message);
  }
}
