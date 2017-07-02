package com.example.demo.service.state.order;

import lombok.Builder;
import lombok.Getter;

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

  @Builder
  @Getter
  public static class ActionParamVO {
    private String currentUserStatus = null;
    private List<String> currentUserPermissionList;
  }

}
