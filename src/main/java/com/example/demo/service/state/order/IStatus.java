package com.example.demo.service.state.order;

import com.example.demo.service.state.exception.ActionConditionFailedException;
import com.example.demo.service.state.exception.ActionParamsNeededException;
import com.example.demo.service.state.exception.UnsupportedStatusForActionException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by ValkSam
 */
public interface IStatus {

  void initSchema(Map<IStatusAction, IStatus> schemaMap);

  Integer getCode();

  String name();

  Map<IStatusAction, IStatus> getSchemaMap();

  static IStatus getBeginState(Class<? extends IStatus> statusClass) {
    Set<IStatus> allNodesSet = collectAllSchemaMapNodesSet(statusClass);
    List<IStatus> candidateList = Arrays.stream(OrderStatusEnum.class.getEnumConstants())
        .filter(e -> !allNodesSet.contains(e))
        .collect(Collectors.toList());
    if (candidateList.size() == 0) {
      throw new AssertionError("begin state not found");
    }
    if (candidateList.size() > 1) {
      throw new AssertionError("more than single begin state found: " + candidateList);
    }
    return candidateList.get(0);
  }

  static Set<IStatus> getEndStatesSet(Class<? extends IStatus> statusClass) {
    return Arrays.stream(statusClass.getEnumConstants())
        .filter(e -> e.getSchemaMap().isEmpty())
        .collect(Collectors.toSet());
  }

  static Set<IStatus> getMiddleStatesSet(Class<? extends IStatus> statusClass) {
    IStatus beginState = getBeginState(statusClass);
    return Arrays.stream(statusClass.getEnumConstants())
        .filter(e -> !e.getSchemaMap().isEmpty())
        .filter(e -> e != beginState)
        .collect(Collectors.toSet());
  }

  static Set<IStatus> collectAllSchemaMapNodesSet(Class<? extends IStatus> statusClass) {
    Set<IStatus> result = new HashSet<>();
    Arrays.stream(statusClass.getEnumConstants())
        .forEach(e -> result.addAll(e.getSchemaMap().values()));
    return result;
  }

  default IStatus nextState(IStatusAction action) {
    if (action.isVerifiable()) {
      throw new ActionParamsNeededException(action.name());
    }
    return nextState(getSchemaMap(), action)
        .orElseThrow(() -> new UnsupportedStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
  }

  default IStatus nextState(IStatusAction action, OrderActionEnum.ActionParamVO actionParamVO) {
    List<Predicate<OrderActionEnum.ActionParamVO>> failedPredicates = action.getPredicates().stream()
        .filter(e -> !e.test(actionParamVO))
        .collect(Collectors.toList());
    if (!failedPredicates.isEmpty()) {
      throw new ActionConditionFailedException(failedPredicates.stream().map(e->e.getClass().getSimpleName()).collect(Collectors.joining(";")));
    }
    return nextState(getSchemaMap(), action)
        .orElseThrow(() -> new UnsupportedStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
  }

  default Optional<IStatus> nextState(Map<IStatusAction, IStatus> schemaMap, IStatusAction action) {
    return Optional.ofNullable(schemaMap.get(action));
  }

  default Boolean availableForAction(IStatusAction action) {
    return getSchemaMap().get(action) != null;
  }

  static Set<IStatus> getAvailableForAction(Class<? extends IStatus> statusClass, IStatusAction action) {
    return Arrays.stream(statusClass.getEnumConstants())
        .filter(e -> e.availableForAction(action))
        .collect(Collectors.toSet());
  }

  static Set<IStatus> getAvailableForAction(Class<? extends IStatus> statusClass, Set<IStatusAction> action) {
    return Arrays.stream(statusClass.getEnumConstants())
        .filter(e -> action.stream().filter(a -> e.availableForAction(a)).findFirst().isPresent())
        .collect(Collectors.toSet());
  }


}
