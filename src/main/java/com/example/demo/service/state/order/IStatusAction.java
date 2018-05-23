package com.example.demo.service.state.order;

import com.example.demo.service.state.order.example.ExampleActionEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ValkSam
 */
public interface IStatusAction {
  String name();

  List<Predicate<ExampleActionEnum.ActionParamVO>> getPredicates();

  default boolean isVerifiable() {
    return !getPredicates().isEmpty();
  }

  @Builder
  @Getter
  class ActionParamVO {
    private String someParam = null;
    private List<String> somePermissionList;
  }

}
