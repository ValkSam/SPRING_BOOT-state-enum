package com.example.demo.service.state.order;

import com.example.demo.service.state.order.example.ExampleActionEnum;
import com.example.demo.service.state.order.example.actionparam.ActionParam;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public interface IStatusAction<T> {
  String name();

  List<Predicate<T>> getPredicates();

  default boolean isVerifiable() {
    return !getPredicates().isEmpty();
  }

}
