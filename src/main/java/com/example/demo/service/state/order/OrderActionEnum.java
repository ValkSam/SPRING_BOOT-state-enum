package com.example.demo.service.state.order;

/**
 * Created by ValkSam
 */
public enum OrderActionEnum implements IStatusAction{
  ACCEPT,
  SAVE,
  REJECT,
  REVOKE,
  SPLIT,
  CREATE_MAIN_SPLITTED,
  CREATE_REST_SPLITTED,
  HOLD_FOR_ACCEPTANCE;
}
