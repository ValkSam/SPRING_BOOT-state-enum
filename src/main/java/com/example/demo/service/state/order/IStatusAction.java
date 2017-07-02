package com.example.demo.service.state.order;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public interface IStatusAction {
  String name();

  List<Predicate<OrderActionEnum.ActionParamVO>> getPredicates();

  default boolean isVerifiable() {
    return !getPredicates().isEmpty();
  }

}
