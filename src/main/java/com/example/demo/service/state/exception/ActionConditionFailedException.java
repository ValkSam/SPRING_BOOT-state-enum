package com.example.demo.service.state.exception;

/**
 * Created by ValkSam
 */
public class ActionConditionFailedException extends RuntimeException {
  public ActionConditionFailedException(String message) {
    super(message);
  }
}
