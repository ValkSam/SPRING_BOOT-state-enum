package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.UnsupportedOrderStatusForActionException;
import com.example.demo.service.state.exception.UnsupportedOrderStatusNameException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ValkSam
 */
public enum OrderStatusEnum implements OrderStatus {
  CREATED(1) {
    @Override
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
      schemaMap.put(OrderActionEnum.ACCEPT, SELF_ACCEPTED);
      schemaMap.put(OrderActionEnum.SAVE, ACTIVE);
      schemaMap.put(OrderActionEnum.SPLIT, SPLITTED);
      schemaMap.put(OrderActionEnum.REJECT, REJECTED);
    }
  },
  SELF_ACCEPTED(2) {
    @Override
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
    }
  },
  ACTIVE(3) {
    @Override
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
      schemaMap.put(OrderActionEnum.HOLD_FOR_ACCEPTANCE, IN_ACCEPTANCE);
      schemaMap.put(OrderActionEnum.SPLIT, SELF_ACCEPTED);
      schemaMap.put(OrderActionEnum.REVOKE, REVOKED);
    }
  },
  REJECTED(4) {
    @Override
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
    }
  },
  IN_ACCEPTANCE(5) {
    @Override
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
      schemaMap.put(OrderActionEnum.CREATE_MAIN_SPLITTED, IN_ACCEPTANCE);
      schemaMap.put(OrderActionEnum.CREATE_REST_SPLITTED, ACTIVE);
      schemaMap.put(OrderActionEnum.ACCEPT, ACCEPTED);
    }
  },
  SPLITTED(6) {
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
    }
  },
  ACCEPTED(7) {
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
    }
  },
  REVOKED(8) {
    public void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap) {
    }
  };


  final private Map<OrderActionEnum, OrderStatus> schemaMap = new HashMap<>();

  @Override
  public OrderStatus nextState(OrderActionEnum action) {
    return nextState(schemaMap, action)
        .orElseThrow(() -> new UnsupportedOrderStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
  }

  @Override
  public Boolean availableForAction(OrderActionEnum action) {
    return availableForAction(schemaMap, action);
  }

  static {
    for (OrderStatusEnum status : OrderStatusEnum.class.getEnumConstants()) {
      status.initSchema(status.schemaMap);
    }
    /*check schemaMap*/
    getBeginState();
  }

  public static List<OrderStatus> getAvailableForActionStatusesList(OrderActionEnum action) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.availableForAction(action))
        .collect(Collectors.toList());
  }

  public static List<OrderStatus> getAvailableForActionStatusesList(List<OrderActionEnum> action) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> action.stream().filter(e::availableForAction).findFirst().isPresent())
        .collect(Collectors.toList());
  }

  /**/

  public static OrderStatusEnum convert(int id) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.code == id)
        .findAny()
        .orElseThrow(() -> new UnsupportedOrderStatusForActionException(String.valueOf(id)));
  }

  public static OrderStatusEnum convert(String name) {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.name().equals(name))
        .findAny()
        .orElseThrow(() -> new UnsupportedOrderStatusNameException(name));
  }



  public static OrderStatus getBeginState() {
    Set<OrderStatus> allNodesSet = collectAllSchemaMapNodesSet();
    List<OrderStatus> candidateList = Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> !allNodesSet.contains(e))
        .collect(Collectors.toList());
    if (candidateList.size() == 0) {
      System.out.println("begin state not found");
      throw new AssertionError();
    }
    ;
    if (candidateList.size() > 1) {
      System.out.println("more than single begin state found: " + candidateList);
      throw new AssertionError();
    }
    ;
    return candidateList.get(0);
  }

  private static Set<OrderStatus> collectAllSchemaMapNodesSet() {
    Set<OrderStatus> result = new HashSet<>();
    Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .forEach(e -> result.addAll(e.schemaMap.values()));
    return result;
  }

  public static Set<OrderStatus> getEndStatesSet() {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> e.schemaMap.isEmpty())
        .collect(Collectors.toSet());
  }

  public static Set<OrderStatus> getMiddleStatesSet() {
    return Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> !e.schemaMap.isEmpty())
        .collect(Collectors.toSet());
  }

  private Integer code;

  OrderStatusEnum(Integer code) {
    this.code = code;
  }

  @Override
  public Integer getCode() {
    return code;
  }

}
