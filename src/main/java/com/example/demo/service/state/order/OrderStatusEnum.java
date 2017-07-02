package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.UnsupportedStatusForActionException;
import com.example.demo.service.state.exception.UnsupportedStatusNameException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.service.state.order.OrderActionEnum.*;

/**
 * Created by ValkSam
 */
public enum OrderStatusEnum implements IStatus {
  CREATED(1) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
      schemaMap.put(ACCEPT, SELF_ACCEPTED);
      schemaMap.put(SAVE, ACTIVE);
      schemaMap.put(CREATE_MAIN_SPLITTED, ACTIVE);
      schemaMap.put(SPLIT, SPLITTED);
      schemaMap.put(REJECT, REJECTED);
      schemaMap.put(CREATE_REST_SPLITTED, REJECTED);
    }
  },
  SELF_ACCEPTED(2) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
    }
  },
  ACTIVE(3) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
      schemaMap.put(HOLD_FOR_ACCEPTANCE, IN_ACCEPTANCE);
      schemaMap.put(SPLIT, SELF_ACCEPTED);
      schemaMap.put(REVOKE, REVOKED);
    }
  },
  REJECTED(4) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
    }
  },
  IN_ACCEPTANCE(5) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
      schemaMap.put(CREATE_MAIN_SPLITTED, IN_ACCEPTANCE);
      schemaMap.put(CREATE_REST_SPLITTED, ACTIVE);
      schemaMap.put(ACCEPT, ACCEPTED);
    }
  },
  SPLITTED(6) {
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
    }
  },
  ACCEPTED(7) {
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
    }
  },
  REVOKED(8) {
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
    }
  };

  private final Integer code;

  private final Map<IStatusAction, IStatus> schemaMap = new HashMap<>();

  OrderStatusEnum(Integer code) {
    this.code = code;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public Map<IStatusAction, IStatus> getSchemaMap() {
    return schemaMap;
  }

  static {
    for (IStatus status : OrderStatusEnum.class.getEnumConstants()) {
      status.initSchema(status.getSchemaMap());
    }
    /*check schemaMap*/
    IStatus.getBeginState(OrderStatusEnum.class);
  }


}
