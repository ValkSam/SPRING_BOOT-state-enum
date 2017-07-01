package com.example.demo.service.state.order;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ValkSam on 18.02.2017.
 */
public interface OrderStatus {

  default Set<OrderStatus> getAvailableNextStatesSet(Map<OrderActionEnum, OrderStatus> schemaMap) {
    Set<OrderStatus> availableNextStates =  schemaMap.values().stream().collect(Collectors.toSet());
    assert(availableNextStates.size()==schemaMap.values().size());
    return availableNextStates;
  }

  default Optional<OrderStatus> nextState(Map<OrderActionEnum, OrderStatus> schemaMap, OrderActionEnum action) {
    return Optional.ofNullable(schemaMap.get(action));
  }

  OrderStatus nextState(OrderActionEnum action);

  default Boolean availableForAction(Map<OrderActionEnum, OrderStatus> schemaMap, OrderActionEnum action){
    return schemaMap.get(action) != null;
  }

  Boolean availableForAction(OrderActionEnum action);

  void initSchema(Map<OrderActionEnum, OrderStatus> schemaMap);


  Integer getCode();

}
