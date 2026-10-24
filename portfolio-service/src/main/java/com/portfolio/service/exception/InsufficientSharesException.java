package com.portfolio.service.exception;

public class InsufficientSharesException extends RuntimeException {
  public InsufficientSharesException(String message) {
    super(message);
  }
}
