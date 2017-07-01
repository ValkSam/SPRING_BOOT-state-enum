package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.UnsupportedStatusForActionException;
import com.example.demo.service.state.exception.UnsupportedStatusNameException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ValkSam
 */
public enum OrderStatusEnum implements IStatus {
  CREATED(1) {
    @Override
    public void initSchema(Map<IStatusAction, IStatus> schemaMap) {
      schemaMap.put(OrderActionEnum.ACCEPT, SELF_ACCEPTED);
      schemaMap.put(OrderActionEnum.SAVE, ACTIVE);
      schemaMap.put(OrderActionEnum.CREATE_MAIN_SPLITTED, ACTIVE);
      schemaMap.put(OrderActionEnum.SPLIT, SPLITTED);
      schemaMap.put(OrderActionEnum.REJECT, REJECTED);
      schemaMap.put(OrderActionEnum.CREATE_REST_SPLITTED, REJECTED);
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
      schemaMap.put(OrderActionEnum.HOLD_FOR_ACCEPTANCE, IN_ACCEPTANCE);
      schemaMap.put(OrderActionEnum.SPLIT, SELF_ACCEPTED);
      schemaMap.put(OrderActionEnum.REVOKE, REVOKED);
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
      schemaMap.put(OrderActionEnum.CREATE_MAIN_SPLITTED, IN_ACCEPTANCE);
      schemaMap.put(OrderActionEnum.CREATE_REST_SPLITTED, ACTIVE);
      schemaMap.put(OrderActionEnum.ACCEPT, ACCEPTED);
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

  public static OrderStatusEnum convert(int id) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.code == id)
        .findAny()
        .orElseThrow(() -> new UnsupportedStatusForActionException(String.valueOf(id)));
  }

  public static OrderStatusEnum convert(String name) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.name().equals(name))
        .findAny()
        .orElseThrow(() -> new UnsupportedStatusNameException(name));
  }

}
