package com.example.demo.service.state.exception;

/**
 * Created by ValkSam
 */
public class UnsupportedStatusForActionException extends RuntimeException {
  public UnsupportedStatusForActionException(String message) {
    super(message);
  }
}
